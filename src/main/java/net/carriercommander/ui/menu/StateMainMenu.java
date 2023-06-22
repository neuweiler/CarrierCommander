package net.carriercommander.ui.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.event.ConsumingMouseListener;
import com.simsilica.lemur.event.MouseEventControl;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StateMainMenu extends WindowState {
	private static final Logger logger = LoggerFactory.getLogger(StateMainMenu.class);
	private List<GameConfig> gameConfigs;

	private ListBox listWorld, listType;
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
		listWorld = worldPanel.addChild(new ListBox());
		listWorld.setVisibleItems(4);
		listWorld.setCellRenderer(new GameConfigCellRenderer());
		gameConfigs.forEach(config -> listWorld.getModel().add(config));
		listWorld.getSelectionModel().setSelection(0);
		listWorld.addCommands(ListBox.ListAction.Click, new Command<ListBox>() {
			@Override
			public void execute(ListBox source) {
				listType.getModel().clear();
				((GameConfig)source.getSelectedItem()).getGameTypes().forEach(gameType -> listType.getModel().add(gameType));
				listType.getSelectionModel().setSelection(0);
			}
		});

		Container gamePanel = gameSelectionPanel.addChild(new Container());
		gamePanel.addChild(new Label("Select Game Type", new ElementId("window.header.label")));
		listType = gamePanel.addChild(new ListBox());
		listType.setVisibleItems(3);
		listType.setCellRenderer(new GameTypeCellRenderer());
		gameConfigs.get(0).getGameTypes().forEach(type -> listType.getModel().add(type));
		listType.getSelectionModel().setSelection(0);

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

		startGame((GameConfig) listWorld.getSelectedItem(), (GameType) listType.getSelectedItem());
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
			Path dirPath = Paths.get(uri);
			Files.list(dirPath)
					.forEach(path -> {
						logger.info("loading game config '{}'", path);
						try {
							GameConfig config = mapper.readValue(path.toFile(), GameConfig.class);
							gameConfigs.add(config);
						} catch (IOException e) {
							e.printStackTrace();
							throw new RuntimeException("unable to parse config file", e);
						}
					});
		} catch (IOException | URISyntaxException e) {
			logger.error("unable to load game configs", e);
			throw new RuntimeException("invalid configs");
		}
		return gameConfigs;
	}
}
