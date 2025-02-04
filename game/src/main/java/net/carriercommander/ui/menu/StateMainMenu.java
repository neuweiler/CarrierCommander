package net.carriercommander.ui.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jme3.app.Application;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.CallMethodAction;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.ListBox;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.style.ElementId;
import net.carriercommander.Constants;
import net.carriercommander.network.client.StateNetworkClient;
import net.carriercommander.network.host.StateNetworkHost;
import net.carriercommander.network.model.config.GameConfig;
import net.carriercommander.network.model.config.GameType;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.hud.widgets.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateMainMenu extends WindowState {
	private static final Logger logger = LoggerFactory.getLogger(StateMainMenu.class);
	private List<GameConfig> gameConfigs;

	private ListBox listGameConfigs, listGameTypes;
	private TextField humanPlayers, aiPlayers, teams, port, minimumPlayers;

	@Override
	protected void initialize(Application app) {
		gameConfigs = loadConfigs("/GameConfigs");

		window = new Window();

		Container topRow = window.addChild(new Container());
		Container middleRow = window.addChild(new Container());
		Container bottomRow = window.addChild(new Container());

		topRow.addChild(new Label("Carrier Commander", new ElementId("window.title.label")));

		Container gameSelectionPanel = middleRow.addChild(new Container(), 0);

		Container worldPanel = gameSelectionPanel.addChild(new Container());
		worldPanel.addChild(new Label("Select World", new ElementId("window.header.label")));
		listGameConfigs = worldPanel.addChild(new ListBox());
		listGameConfigs.setVisibleItems(4);
		listGameConfigs.setCellRenderer(new GameConfigCellRenderer());
		gameConfigs.forEach(config -> listGameConfigs.getModel().add(config));
		listGameConfigs.getSelectionModel().setSelection(0);
		listGameConfigs.addCommands(ListBox.ListAction.Click, new Command<ListBox>() {
			@Override
			public void execute(ListBox source) {
				listGameTypes.getModel().clear();
				((GameConfig)source.getSelectedItem()).getGameTypes().forEach(gameType -> listGameTypes.getModel().add(gameType));
				listGameTypes.getSelectionModel().setSelection(0);
			}
		});

		Container gamePanel = gameSelectionPanel.addChild(new Container());
		gamePanel.addChild(new Label("Select Game Type", new ElementId("window.header.label")));
		listGameTypes = gamePanel.addChild(new ListBox());
		listGameTypes.setVisibleItems(3);
		listGameTypes.setCellRenderer(new GameTypeCellRenderer());
		gameConfigs.get(0).getGameTypes().forEach(type -> listGameTypes.getModel().add(type));
		listGameTypes.getSelectionModel().setSelection(0);

		Container gameParamsPanel = middleRow.addChild(new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.None, FillMode.Last)), 1);
		gameParamsPanel.addChild(new Label("Options", new ElementId("window.header.label")));
		gameParamsPanel.addChild(new Label("Human players:"));
		humanPlayers = new TextField("1");
		gameParamsPanel.addChild(humanPlayers, 1);
		gameParamsPanel.addChild(new Label("AI players:"));
		aiPlayers = new TextField("1");
		gameParamsPanel.addChild(aiPlayers, 1);
		gameParamsPanel.addChild(new Label("Teams:"));
		teams = new TextField("0");
		gameParamsPanel.addChild(teams, 1);
		gameParamsPanel.addChild(new Label("Port:"));
		port = new TextField(String.valueOf(Constants.DEFAULT_PORT));
		gameParamsPanel.addChild(port, 1);
		gameParamsPanel.addChild(new Label("Wait for players:"));
		minimumPlayers = new TextField("0");
		gameParamsPanel.addChild(minimumPlayers, 1);

		bottomRow.addChild(new ActionButton(new CallMethodAction("Start Game", this, "start")), 0);
		bottomRow.addChild(new ActionButton(new CallMethodAction("Exit", app, "stop")), 2);
		bottomRow.addChild(new ActionButton(new CallMethodAction("Connect to Host", this, "connect")), 1);

		scaleAndPosition(app.getCamera(), .2f, .6f, Constants.MENU_MAGNIFICATION);
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		GuiGlobals.getInstance().requestFocus(window);

		if (Constants.AUTOSTART) {
//			action();
		}
	}

	protected void start() {
		logger.info("human players: {}, ai players: {}, teams. {}, port: {}, min players: {}",
				humanPlayers.getText(), aiPlayers.getText(), teams.getText(),
				port.getText(), minimumPlayers.getText());

		startGame((GameConfig) listGameConfigs.getSelectedItem(), (GameType) listGameTypes.getSelectedItem());
	}

	protected void connect() {
		getStateManager().getState(StateNetworkMenu.class).setEnabled(true);
		setEnabled(false);
	}

	public void startGame(GameConfig gameConfig, GameType gameType) {
		int portNumber = Integer.parseInt(port.getText());
		getStateManager().attach(new StateNetworkHost(gameConfig, gameType, portNumber));
		getStateManager().attach(new StateNetworkClient("localhost", portNumber));
//		getStateManager().attach(new StateLoadGame(new Vector3f(300, 0, 1700)));
		setEnabled(false);
	}

	private List<GameConfig> loadConfigs(String resourcePath) {
		List<GameConfig> gameConfigs = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			URI uri = getClass().getResource(resourcePath).toURI();

			Map<String, String> env = new HashMap<>();
			env.put("create", "true");
			FileSystem zipFs = FileSystems.newFileSystem(uri, env);

			Path dirPath = Paths.get(uri);
			Files.list(dirPath)
					.forEach(path -> {
						logger.info("loading game config '{}'", path);
						try {
							GameConfig config = mapper.readValue(path.toUri().toURL(), GameConfig.class);
							gameConfigs.add(config);
						} catch (IOException e) {
							e.printStackTrace();
							throw new RuntimeException("unable to parse config file", e);
						}
					});

			zipFs.close();
		} catch (IOException | URISyntaxException e) {
			logger.error("unable to load game configs", e);
			throw new RuntimeException("invalid configs");
		}
		return gameConfigs;
	}
}
