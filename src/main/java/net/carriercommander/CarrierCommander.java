/*
 * Copyright (c) 2015, Michael Neuweiler
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of CarrierCommander nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package net.carriercommander;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import net.carriercommander.network.Utils;
import net.carriercommander.ui.menu.StateMainMenu;
import net.carriercommander.ui.menu.StateNetworkMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;

/**
 * Carrier Commander Main Class
 *
 * @author Michael Neuweiler
 */
public class CarrierCommander extends SimpleApplication {
	private static final Logger logger = LoggerFactory.getLogger(CarrierCommander.class);

	private static final String SETTINGS_TOKEN = "net/carriercommander";

	public static void main(String... args) {
		Utils.initSerializers();
		CarrierCommander app = new CarrierCommander();
		AppSettings settings = new AppSettings(true);

		settings.setFrameRate(60);
		settings.setResolution(1280, 720);
		settings.setFullscreen(false);
		settings.setVSync(true);
//		settings.setSamples(8);
		settings.setGammaCorrection(true);

		try {
			settings.load(SETTINGS_TOKEN);
		} catch (BackingStoreException e) {
			logger.error("unable to load previous settings", e);
		}
		settings.setSettingsDialogImage("/Interface/splash-512.png");
		settings.setTitle("Carrier Commander");
		settings.setIcons(loadIcons());

		app.setShowSettings(!Constants.AUTOSTART);
		app.setSettings(settings);
		app.start();
	}

	private static BufferedImage[] loadIcons() {
		ArrayList<BufferedImage> icons = new ArrayList<>();
		try {
			icons.add(ImageIO.read(CarrierCommander.class.getResource("/Icons/icon-128.png")));
			icons.add(ImageIO.read(CarrierCommander.class.getResource("/Icons/icon-32.png")));
			icons.add(ImageIO.read(CarrierCommander.class.getResource("/Icons/icon-16.png")));
		} catch (Exception e) {
			logger.warn("Error loading globe icons", e);
		}
		return icons.toArray(new BufferedImage[0]);
	}

	public CarrierCommander() {
		super(
				new StateMainMenu()
				, new StateSpinningCarrier()
				//, new OptionPanelState()
				, new StateNetworkMenu()
		);
	}

	@Override
	public void simpleInitApp() {
		setDisplayFps(Constants.DEBUG);
		setDisplayStatView(Constants.DEBUG);
		createUI();
		activatePhysics();
	}

	private void createUI() {
		GuiGlobals.initialize(this);
		BaseStyles.loadStyleResources("Interface/styles.groovy");
		GuiGlobals.getInstance().getStyles().setDefaultStyle("cc");
	}

	private void activatePhysics() {
		BulletAppState physicsState = new BulletAppState();
		physicsState.setThreadingType(Constants.DEBUG ? BulletAppState.ThreadingType.SEQUENTIAL : BulletAppState.ThreadingType.PARALLEL);
//		physicsState.setBroadphaseType(PhysicsSpace.BroadphaseType.SIMPLE);

		physicsState.setDebugEnabled(Constants.DEBUG);
		if (Constants.DEBUG) {
			physicsState.setDebugAxisLength(1f);
			physicsState.setDebugAxisLineWidth(3f);
		}

		getStateManager().attach(physicsState);
		physicsState.getPhysicsSpace().setMaxSubSteps(0); // prevent stutter and sudden stops in forward motion
	}

}
