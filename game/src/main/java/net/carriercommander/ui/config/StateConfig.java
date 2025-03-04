package net.carriercommander.ui.config;

import com.jme3.app.Application;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.*;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.core.VersionedList;
import net.carriercommander.Constants;
import net.carriercommander.StateViewFilter;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.controls.widgets.ToggleButton;
import net.carriercommander.ui.controls.widgets.ToggleGroup;
import net.carriercommander.ui.controls.widgets.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.stream.Collectors;

public class StateConfig extends WindowState {
	private static final Logger logger = LoggerFactory.getLogger(StateConfig.class);
	public static final String SETTINGS_TOKEN = "net/carriercommander";
	final AppSettings settings = new AppSettings(true);
	private Selector<DisplayMode> resolution;
	private Selector<Integer> bitDepth;
	private Selector<Integer> refreshRate;
	private Selector<Integer> numSamples;

	@Override
	protected void initialize(Application app) {
		try {
			settings.load(SETTINGS_TOKEN);
		} catch (BackingStoreException e) {
			logger.error("unable to load previous settings", e);
		}

		window = new Window();

		Container topRow = window.addChild(new Container());
		Container middleRow = window.addChild(new Container());
		Container bottomRow = window.addChild(new Container());

		middleRow.addChild(new Label("Select Resolution:"));
		resolution = middleRow.addChild(new Selector<>(getDistinctResolutions()), 1);
		resolution.setMaximumVisibleItems(5);
		resolution.getListBox().addClickCommands(source -> {
			DisplayMode displayMode = resolution.getSelectedItem();
			settings.setResolution(displayMode.getWidth(), displayMode.getHeight());
			updateSettings();
		});

		bitDepth = middleRow.addChild(new Selector<>(getDistinctBitDepth(resolution.getSelectedItem())), 2);
		bitDepth.getListBox().addClickCommands(source -> {
			settings.setBitsPerPixel(bitDepth.getSelectedItem());
			updateSettings();
		});


		refreshRate = middleRow.addChild(new Selector<>(getDistinctRefreshRates(resolution.getSelectedItem())), 3);
		refreshRate.setMaximumVisibleItems(5);
		refreshRate.getListBox().addClickCommands(source -> {
			settings.setFrequency(refreshRate.getSelectedItem());
			updateSettings();
		});

		middleRow.addChild(new Label("Anti Alias Samples:"));
		numSamples = middleRow.addChild(new Selector<>(new VersionedList<>(Arrays.asList(new Integer[]{0, 2, 4, 8, 16, 24, 32}))), 1);
		numSamples.getListBox().addClickCommands(source -> {
			settings.setSamples(numSamples.getSelectedItem());
			getStateManager().getState(StateViewFilter.class).setNumSamples(numSamples.getSelectedItem());
			updateSettings();
		});
		middleRow.addChild(createFilterCheckbox("Fast Approximate Anti-Aliasing (FXAA)", "FXAA"))
				.addClickCommands(source -> {
					boolean enabled = ((Checkbox) source).isChecked();
					settings.putBoolean("FXAA", enabled);
					getStateManager().getState(StateViewFilter.class).setFXAAEnabled(enabled);
				});

		middleRow.addChild(createSettingsCheckbox("Fullscreen", "Fullscreen"));
		middleRow.addChild(createSettingsCheckbox("Gamma Correction", "GammaCorrection"));
		middleRow.addChild(createSettingsCheckbox("V-Sync", "VSync"));

		middleRow.addChild(createFilterCheckbox("Water", "Water"))
				.addClickCommands(source -> {
					boolean enabled = ((Checkbox) source).isChecked();
					settings.putBoolean("Water", enabled);
					getStateManager().getState(StateViewFilter.class).setWaterEnabled(enabled);
				});

		middleRow.addChild(createFilterCheckbox("Fog", "Fog"))
				.addClickCommands(source -> {
					boolean enabled = ((Checkbox) source).isChecked();
					settings.putBoolean("Fog", enabled);
					getStateManager().getState(StateViewFilter.class).setFogEnabled(enabled);
				});

		middleRow.addChild(createFilterCheckbox("Bloom", "Bloom"))
				.addClickCommands(source -> {
					boolean enabled = ((Checkbox) source).isChecked();
					settings.putBoolean("Bloom", enabled);
					getStateManager().getState(StateViewFilter.class).setBloomEnabled(enabled);
				});

		middleRow.addChild(createFilterCheckbox("Light Scattering Filter", "LightScattering"))
				.addClickCommands(source -> {
					boolean enabled = ((Checkbox) source).isChecked();
					settings.putBoolean("LightScattering", enabled);
					getStateManager().getState(StateViewFilter.class).setLightScatteringEnabled(enabled);
				});

		middleRow.addChild(createFilterCheckbox("Translucent Bucket Filter", "TranslucentBucket"))
				.addClickCommands(source -> {
					boolean enabled = ((Checkbox) source).isChecked();
					settings.putBoolean("TranslucentBucket", enabled);
					getStateManager().getState(StateViewFilter.class).setTranslucentBucketEnabled(enabled);
				});



		middleRow.addChild(new Label("Scale:"));
		ToggleGroup toggleGroup = middleRow.addChild(new ToggleGroup(), 1);
		toggleGroup.addChild(new ToggleButton(new CallMethodAction("100%", this, "setScale100")), 1);
		toggleGroup.addChild(new ToggleButton(new CallMethodAction("200%", this, "setScale200")), 2);
		toggleGroup.addChild(new ToggleButton(new CallMethodAction("300%", this, "setScale300")), 3);

		scaleAndPosition(app.getCamera(), .6f, .5f, Constants.MENU_MAGNIFICATION);
		setScale(1);
		setSelectedValues();
		this.setEnabled(false);
	}

	private Checkbox createSettingsCheckbox(String label, String settingToken) {
		Checkbox checkbox = new Checkbox(label);
		checkbox.setChecked(settings.getBoolean(settingToken));
		checkbox.addClickCommands(source -> {
			settings.putBoolean(settingToken, ((Checkbox) source).isChecked());
			updateSettings();
		});
		return checkbox;
	}

	private Checkbox createFilterCheckbox(String label, String settingToken) {
		Checkbox checkbox = new Checkbox(label);
		checkbox.setChecked(settings.getBoolean(settingToken));
		return checkbox;
	}

	private void updateSettings() {
		getApplication().setSettings(settings);
		try {
			settings.save(SETTINGS_TOKEN);
		} catch (BackingStoreException e) {
			logger.error("unable to store settings", e);
		}
		getApplication().restart();

		//TODO re-position and re-scale all open windows
	}

	@Override
	protected void cleanup(Application app) {

	}

	protected void setScale100() {
		setScale(1);
	}

	protected void setScale200() {
		setScale(2);
	}

	protected void setScale300() {
		setScale(3);
	}

	protected void setScale(float scale) {
		float newScale = getStandardScale() * scale * Constants.MENU_MAGNIFICATION;
		window.setLocalScale(newScale);
		resolution.getListBox().setLocalScale(newScale);
		bitDepth.getListBox().setLocalScale(newScale);
		refreshRate.getListBox().setLocalScale(newScale);
	}

	private VersionedList<DisplayMode> getDistinctResolutions() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Map<String, DisplayMode> map = new HashMap<>();
		for (DisplayMode displayMode : gd.getDisplayModes()) {
			map.put(displayMode.getWidth() + "x" + displayMode.getHeight(), displayMode);
		}
		return new VersionedList<>(map
				.values()
				.stream()
				.sorted(Comparator.comparingInt(DisplayMode::getHeight))
				.sorted(Comparator.comparingInt(DisplayMode::getWidth))
				.collect(Collectors.toList()));

	}

	private VersionedList<Integer> getDistinctBitDepth(DisplayMode mode) {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		return new VersionedList<>(
				Arrays.stream(gd.getDisplayModes())
						.filter(m -> m.getHeight() == mode.getHeight() && m.getWidth() == mode.getWidth())
						.map(DisplayMode::getBitDepth)
						.distinct()
						.sorted()
						.toList()
		);
	}

	private VersionedList<Integer> getDistinctRefreshRates(DisplayMode mode) {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		return new VersionedList<>(
				Arrays.stream(gd.getDisplayModes())
						.filter(m -> m.getHeight() == mode.getHeight() && m.getWidth() == mode.getWidth() && m.getBitDepth() == mode.getBitDepth())
						.map(DisplayMode::getRefreshRate)
						.distinct()
						.sorted()
						.toList()
		);
	}

	private void setSelectedValues() {
		refreshRate.getModel().stream()
				.filter(refreshRate -> refreshRate == settings.getFrequency())
				.findFirst()
				.ifPresentOrElse(r -> refreshRate.setSelectedItem(r), () -> refreshRate.setSelectedItem(refreshRate.getModel().get(0)));

		bitDepth.getModel().stream()
				.filter(bitDepth -> bitDepth == settings.getDepthBits())
				.findFirst()
				.ifPresentOrElse(b -> bitDepth.setSelectedItem(b), () -> bitDepth.setSelectedItem(bitDepth.getModel().get(0)));

		resolution.getModel().stream()
				.filter(m -> m.getHeight() == settings.getHeight() && m.getWidth() == settings.getWidth())
				.findFirst()
				.ifPresentOrElse(displayMode -> resolution.setSelectedItem(displayMode), () -> resolution.setSelectedItem(resolution.getModel().get(0)));

	}
}
