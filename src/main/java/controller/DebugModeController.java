package controller;

import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

import controller.debugger.VisL1SceneController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.validation.ValidationSupport;
import uicomponent.SideNode;

import static controller.GraphicCreator.*;

public class DebugModeController extends BorderPane implements Initializable,Threadable, Controllable<EditModeScene> {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button showDISButton;

	@FXML
	private MenuItem backMI;

	@FXML
	private MenuItem breakLabelMI;

	@FXML
	private Button goToHomeButton;

	@FXML
	private MenuItem varWindowMI;

	@FXML
	private ChoiceBox<String> aryMenuButton;

	@FXML
	private MenuItem stopMI;

	@FXML
	private Button runButton;

	@FXML
	private MenuItem stepInMI;

	@FXML
	private Button showLogSButton;

	@FXML
	private MenuItem watchWindowMI;

	@FXML
	private MenuItem stepOverMI;

	@FXML
	private MenuItem breakSubMI;

	@FXML
	private MenuItem runtoCurMI;

	@FXML
	private BorderPane root;

	@FXML
	private MenuItem traceVarWindowMI;

	@FXML
	private Button backButton;

	@FXML
	private Button showLoadSButton;

	@FXML
	private MenuItem runMI;

	@FXML
	private Button stepInButton;

	@FXML
	private Button pauseButton;

	@FXML
	private Button stepOverButton;

	@FXML
	private Button stepOutButton;

	@FXML
	private Button runtoCurButton;

	@FXML
	private MenuItem stepOutMI;

	@FXML
	private MenuItem pauseMI;

	@FXML
	private MenuItem resumeMI;

	@FXML
	private Button stopButton;

	@FXML
	private StackPane debugSceneContainer;

	@FXML
	private Button showBPButton;

	@FXML
	private Button showCL1SButton;

	@FXML
	private Button showCL2SButton;

	@FXML
	private Menu windowMenu;

	@FXML
	private Button evExButton;

	enum Ary{
		BIN("2進数"),OCT("8進数"),SIGN_DEC("符号無10進数"),NO_SIGN_DEC("10進数"),HEX("16進数"),CHAR("文字(JIS X 0201)");
		private final String text;
		private static HashMap<String,Ary> map = new HashMap<>();
		Ary(String text){
			this.text = text;
		}
		public String getText(){return text;}
		public static Ary toAry(String text){
			return map.get(text);
		}
		static{
			for(Ary a: Ary.values())
				map.put(a.text,a);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert showDISButton != null : "fx:id=\"showDISButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert backMI != null : "fx:id=\"backMI\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert breakLabelMI != null : "fx:id=\"breakLabelMI\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert goToHomeButton != null : "fx:id=\"goToHomeButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert varWindowMI != null : "fx:id=\"varWindowMI\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert aryMenuButton != null : "fx:id=\"aryMenuButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert stopMI != null : "fx:id=\"stopMI\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert runButton != null : "fx:id=\"runButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert stepInMI != null : "fx:id=\"stepInMI\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert showLogSButton != null : "fx:id=\"showLogSButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert watchWindowMI != null : "fx:id=\"watchWindowMI\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert stepOverMI != null : "fx:id=\"stepOverMI\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert breakSubMI != null : "fx:id=\"breakSubMI\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert runtoCurMI != null : "fx:id=\"runtoCurMI\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert traceVarWindowMI != null : "fx:id=\"traceVarWindowMI\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert showLoadSButton != null : "fx:id=\"showLoadSButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert runMI != null : "fx:id=\"runMI\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert stepInButton != null : "fx:id=\"stepInButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert pauseButton != null : "fx:id=\"pauseButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert stepOverButton != null : "fx:id=\"stepOverButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert stepOutButton != null : "fx:id=\"stepOutButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert runtoCurButton != null : "fx:id=\"runtoCurButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert stepOutMI != null : "fx:id=\"stepOutMI\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert pauseMI != null : "fx:id=\"pauseMI\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert resumeMI != null : "fx:id=\"resumeMI\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert stopButton != null : "fx:id=\"stopButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert debugSceneContainer != null : "fx:id=\"debugSceneContainer\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert showBPButton != null : "fx:id=\"showBPButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert showCL1SButton != null : "fx:id=\"showCL1SButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert showCL2SButton != null : "fx:id=\"showCL2SButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert windowMenu != null : "fx:id=\"windowMenu\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		assert evExButton != null : "fx:id=\"evExButton\" was not injected: check your FXML file 'DebuggerScene.fxml'.";
		aryMenuButton.getItems().addAll("2進数", "8進数", "符号無10進数","10進数","16進数","文字(JIS X 0201)");
		aryMenuButton.getSelectionModel().selectFirst();
		aryMenuButton.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {showAryView(Ary.toAry(newValue));});

		statusBar = new StatusBar();
		root.setBottom(statusBar);
		Button rightB2 = new Button("", createEffectIcon(FontAwesome.Glyph.INFO));
		statusBar.getRightItems().addAll(new Separator(Orientation.VERTICAL),rightB2);
		/*errView = new SideNode(" Information View",editorPane);
		rightB2.setOnAction(e -> {
					if (editorPane.isShowDetailNode()) {
						editorPane.setShowDetailNode(false);
					} else {
						editorPane.setShowDetailNode(true);
					}
				}
		);*/
		statusBar.textProperty().set("機械語直接投入モード画面");
		root.setBottom(statusBar);
	}

	private void showAryView(Ary ary) {
		System.out.println(ary.getText());
	}

	private ScreensController<EditModeScene> scEMC;
	private NodeController<DebugModeScene> scDMC;
	private EditModeController caec;
	private ExecutorService service;
	private Map<DebugModeScene, FXMLLoader> fxmlLoaders = new EnumMap<>(DebugModeScene.class);
	private StatusBar statusBar;
	ValidationSupport validationSupport = new ValidationSupport();
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
		showCL1SButton.setGraphic(createEffectIcon(FontAwesome.Glyph.COLUMNS));
		showCL2SButton.setGraphic(createEffectIcon(FontAwesome.Glyph.LAPTOP));
		showDISButton.setGraphic(createEffectIcon(FontAwesome.Glyph.LIST_ALT));
		showLogSButton.setGraphic(createEffectIcon(FontAwesome.Glyph.HISTORY));
		showLoadSButton.setGraphic(createEffectIcon(FontAwesome.Glyph.HDD_ALT));
		goToHomeButton.setGraphic(createEffectIcon(FontAwesome.Glyph.HOME));

		stopButton.setGraphic(createEffectIcon(FontAwesome.Glyph.STOP));
		pauseButton.setGraphic(createEffectIcon(FontAwesome.Glyph.PAUSE));
		runButton.setGraphic(createEffectIcon(FontAwesome.Glyph.PLAY));
		backButton.setGraphic(createEffectIcon(FontAwesome.Glyph.UNDO));
		stepOverButton.setGraphic(createEffectIcon(FontAwesome.Glyph.ARROW_RIGHT));
		stepInButton.setGraphic(createEffectIcon(FontAwesome.Glyph.SIGN_IN));
		stepOutButton.setGraphic(createEffectIcon(FontAwesome.Glyph.SIGN_OUT));
		runtoCurButton.setGraphic(createEffectIcon(FontAwesome.Glyph.FAST_FORWARD));

		showBPButton.setGraphic(createEffectIcon(FontAwesome.Glyph.CIRCLE).color(Color.RED));
		evExButton.setGraphic(createEffectIcon(FontAwesome.Glyph.LIST_ALT));

		stopMI.setGraphic(createEffectIcon(FontAwesome.Glyph.STOP));
		pauseMI.setGraphic(createEffectIcon(FontAwesome.Glyph.PAUSE));
		runMI.setGraphic(createEffectIcon(FontAwesome.Glyph.PLAY));
		backMI.setGraphic(createEffectIcon(FontAwesome.Glyph.UNDO));
		stepOverMI.setGraphic(createEffectIcon(FontAwesome.Glyph.ARROW_RIGHT));
		stepInMI.setGraphic(createEffectIcon(FontAwesome.Glyph.SIGN_IN));
		stepOutMI.setGraphic(createEffectIcon(FontAwesome.Glyph.SIGN_OUT));
		runtoCurMI.setGraphic(createEffectIcon(FontAwesome.Glyph.FAST_FORWARD));

		breakLabelMI.setGraphic(createEffectIcon(FontAwesome.Glyph.FLAG));
		breakSubMI.setGraphic(createEffectIcon(FontAwesome.Glyph.FLAG_ALT));

		varWindowMI.setGraphic(createEffectIcon(FontAwesome.Glyph.CALENDAR_ALT).color(Color.BLUE));
		traceVarWindowMI.setGraphic(createEffectIcon(FontAwesome.Glyph.CALENDAR_ALT).color(Color.RED));
		watchWindowMI.setGraphic(createEffectIcon(FontAwesome.Glyph.EYEDROPPER).color(Color.YELLOW));

		showCL1SButton.setOnAction(e->scDMC.setScreen(DebugModeScene.VL1));
		showCL2SButton.setOnAction(e->scDMC.setScreen(DebugModeScene.VL2));
		showDISButton.setOnAction(e->scDMC.setScreen(DebugModeScene.SETTING));
		showLogSButton.setOnAction(e->scDMC.setScreen(DebugModeScene.LOG));
		showLoadSButton.setOnAction(e->scDMC.setScreen(DebugModeScene.LOAD));
		goToHomeButton.setOnAction(e->gotoHomeAction());

		stopButton.setOnAction(e->{
			VisL1SceneController c = fxmlLoaders.get(DebugModeScene.VL1).getController();
			System.out.println("stop button clicked!");
		});
	}
	int i = 0;
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
		c.setRootAndInit(this);
		//c.setEditMode();
		statusBar.textProperty().set("機械語直接投入モード画面");
	}
	public void setSimulateMode(){
		statusBar.textProperty().set("デバッグ画面");

	}
	void gotoHomeAction() {
		scEMC.setScreen(EditModeScene.ROOT);
	}
}