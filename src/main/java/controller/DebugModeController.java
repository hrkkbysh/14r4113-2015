package controller;

import java.net.URL;
import java.util.EnumMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

import controller.debugger.VisL1SceneController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DebugModeController extends BorderPane implements Initializable,Threadable, Controllable<EditModeScene> {

	@FXML
	private MenuItem runMenuItem;
	@FXML
	private MenuItem setDebuggerMenuItem;
	@FXML
	private ImageView signDecimalImageView;
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button showDISButton;

	@FXML
	private MenuItem outTraceViewMenuItem;

	@FXML
	private MenuItem setDataVisualMenuItem;

	@FXML
	private ImageView octalImageView;

	@FXML
	private MenuItem invokedComet2MenuItem;

	@FXML
	private Button runButton;

	@FXML
	private SplitMenuButton splitAryMenuButton;

	@FXML
	private Button showLogSButton;

	@FXML
	private BorderPane root;

	@FXML
	private Label leftStatus3;

	@FXML
	private MenuItem stepRunMenuItem;

	@FXML
	private Label leftStatus1;

	@FXML
	private Label leftStatus2;

	@FXML
	private MenuItem setBPMenuItem;

	@FXML
	private ImageView selectAryImage;

	@FXML
	private ImageView binaryImageView;

	@FXML
	private Button pauseButton;

	@FXML
	private ImageView charImageView;

	@FXML
	private MenuItem setDetailMenuItem;

	@FXML
	private Button backToExStateButton;

	@FXML
	private ImageView hexImageView;

	@FXML
	private Button setStopStatementButton;

	@FXML
	private Button stepRunButton;

	@FXML
	private MenuItem instProfileMenuItem;

	@FXML
	private MenuItem reloadMenuItem;

	@FXML
	private Button showLoadSButton;

	@FXML
	private Button stopButton;

	@FXML
	private StackPane debugSceneContainer;

	@FXML
	private MenuItem setStopStatementMenuItem;

	@FXML
	private Button showCL1SButton;

	@FXML
	private Button showCL2SButton;

	@FXML
	private MenuItem backToExStateMenuItem;

	@FXML
	private Menu settingMenu;

	@FXML
	private Button setBPButton;

	@FXML
	private ImageView decimalImageView;


	@FXML
	private Button gotoHomeButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert gotoHomeButton != null : "fx:id=\"gotoHomeButton\" was not injected: check your FXML file 'EditScene.fxml'.";
		assert showDISButton != null : "fx:id=\"showDISButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert outTraceViewMenuItem != null : "fx:id=\"outTraceViewMenuItem\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert setDataVisualMenuItem != null : "fx:id=\"setDataVisualMenuItem\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert octalImageView != null : "fx:id=\"octalImageView\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert invokedComet2MenuItem != null : "fx:id=\"invokedComet2MenuItem\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert runButton != null : "fx:id=\"runButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert splitAryMenuButton != null : "fx:id=\"splitAryMenuButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert showLogSButton != null : "fx:id=\"showLogSButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert leftStatus3 != null : "fx:id=\"leftStatus3\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert stepRunMenuItem != null : "fx:id=\"stepRunMenuItem\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert leftStatus1 != null : "fx:id=\"leftStatus1\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert leftStatus2 != null : "fx:id=\"leftStatus2\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert setBPMenuItem != null : "fx:id=\"setBPMenuItem\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert selectAryImage != null : "fx:id=\"selectAryImage\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert binaryImageView != null : "fx:id=\"binaryImageView\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert pauseButton != null : "fx:id=\"pauseButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert charImageView != null : "fx:id=\"charImageView\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert setDetailMenuItem != null : "fx:id=\"setDetailMenuItem\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert backToExStateButton != null : "fx:id=\"backToExStateButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert hexImageView != null : "fx:id=\"hexImageView\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert setStopStatementButton != null : "fx:id=\"setStopStatementButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert stepRunButton != null : "fx:id=\"stepRunButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert instProfileMenuItem != null : "fx:id=\"instProfileMenuItem\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert reloadMenuItem != null : "fx:id=\"reloadMenuItem\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert showLoadSButton != null : "fx:id=\"showLoadSButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert stopButton != null : "fx:id=\"stopButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert debugSceneContainer != null : "fx:id=\"debugSceneContainer\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert setStopStatementMenuItem != null : "fx:id=\"setStopStatementMenuItem\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert showCL1SButton != null : "fx:id=\"showCL1SButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert showCL2SButton != null : "fx:id=\"showCL2SButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert backToExStateMenuItem != null : "fx:id=\"backToExStateMenuItem\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert settingMenu != null : "fx:id=\"settingMenu\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert setBPButton != null : "fx:id=\"setBPButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert decimalImageView != null : "fx:id=\"decimalImageView\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
	}


	private ScreensController<EditModeScene> scEMC;
	private NodeController<DebugModeScene> scDMC;
	private EditModeController caec;
	private ExecutorService service;
	private Map<DebugModeScene, FXMLLoader> fxmlLoaders = new EnumMap<>(DebugModeScene.class);

	//FIXME
	@Override
	public void setExecutorService(ExecutorService service) {
		this.service = service;
		scDMC = new NodeController<>(DebugModeScene.class);
		for(DebugModeScene d:DebugModeScene.values()){
			fxmlLoaders.put(d, scDMC.loadScreen(d));
		}
		scDMC.setScreen(DebugModeScene.VL1);
		debugSceneContainer.getChildren().addAll(scDMC);

		stage.heightProperty().addListener(e -> {
			scDMC.prefHeightProperty().unbind();
			autosize();
			scDMC.prefHeightProperty().bind(stage.heightProperty());
		});
		stage.widthProperty().addListener(e -> {
			scDMC.prefWidthProperty().unbind();
			autosize();
			scDMC.prefWidthProperty().bind(stage.widthProperty());
		});
		showCL1SButton.setOnAction(e->scDMC.setScreen(DebugModeScene.VL1));
		showCL2SButton.setOnAction(e->scDMC.setScreen(DebugModeScene.VL2));
		showDISButton.setOnAction(e->scDMC.setScreen(DebugModeScene.SETTING));
		showLogSButton.setOnAction(e->scDMC.setScreen(DebugModeScene.LOG));
		showLoadSButton.setOnAction(e->scDMC.setScreen(DebugModeScene.LOAD));
//		initEditor();
//		fileTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//		leftStatus1.textProperty().bind(activeFilePath);
	}
	public void setDisableButs(boolean value){
		showCL2SButton.setDisable(value);
		showDISButton.setDisable(value);
		showLoadSButton.setDisable(value);
		showLogSButton.setDisable(value);
	}

	@Override
	public void setScreenParent(ScreensController<EditModeScene> sc) {
		this.scEMC = sc;
	}
	Stage stage;
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setCAEC(EditModeController CAEC) {
		this.caec = CAEC;
	}

	public void setEditMode() {
		scDMC.setScreen(DebugModeScene.VL1);
		setDisableButs(false);
		VisL1SceneController c = fxmlLoaders.get(DebugModeScene.VL1).getController();
		//c.setEditMode();
	}
	@FXML
	void gotoHomeAction(javafx.event.ActionEvent event) {
		scEMC.unbindS();
		scEMC.setPrefSize(400.0,200.0);
		scEMC.setScreen(EditModeScene.ROOT);
	}
}