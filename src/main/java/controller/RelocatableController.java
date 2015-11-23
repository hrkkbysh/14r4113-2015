package controller;

/**
 * Sample Skeleton for 'RelocatableScene.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


public class RelocatableController extends BorderPane implements Initializable,Threadable,ModeTogglable,Controllable<SimSceneType>{

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="relocContentColumn"
	private TableColumn<?, ?> relocContentColumn; // Value injected by FXMLLoader

	@FXML // fx:id="gotoCasl2EditButton"
	private Button gotoCasl2EditButton; // Value injected by FXMLLoader

	@FXML // fx:id="octalImageView"
	private ImageView octalImageView; // Value injected by FXMLLoader

	@FXML // fx:id="expansionFontSizeButton"
	private Button expansionFontSizeButton; // Value injected by FXMLLoader

	@FXML // fx:id="splitAryMenuButton"
	private SplitMenuButton splitAryMenuButton; // Value injected by FXMLLoader

	@FXML // fx:id="memListPane"
	private Pane memListPane; // Value injected by FXMLLoader

	@FXML // fx:id="gotoComet2EditButton"
	private Button gotoComet2EditButton; // Value injected by FXMLLoader

	@FXML // fx:id="startLabel"
	private Label startLabel; // Value injected by FXMLLoader

	@FXML // fx:id="invokedComet2Button"
	private Button invokedComet2Button; // Value injected by FXMLLoader

	@FXML // fx:id="leftStatus3"
	private Label leftStatus3; // Value injected by FXMLLoader

	@FXML // fx:id="endLabel"
	private Label endLabel; // Value injected by FXMLLoader

	@FXML // fx:id="leftStatus1"
	private Label leftStatus1; // Value injected by FXMLLoader

	@FXML // fx:id="leftStatus2"
	private Label leftStatus2; // Value injected by FXMLLoader

	@FXML // fx:id="casl2EditRoot"
	private BorderPane casl2EditRoot; // Value injected by FXMLLoader

	@FXML // fx:id="relocTable"
	private TableView<?> relocTable; // Value injected by FXMLLoader

	@FXML // fx:id="selectAryImage"
	private ImageView selectAryImage; // Value injected by FXMLLoader

	@FXML // fx:id="binaryImageView"
	private ImageView binaryImageView; // Value injected by FXMLLoader

	@FXML // fx:id="charImageView"
	private ImageView charImageView; // Value injected by FXMLLoader

	@FXML // fx:id="zoomFontSizeSlider"
	private Slider zoomFontSizeSlider; // Value injected by FXMLLoader

	@FXML // fx:id="memContentColumn"
	private TableColumn<?, ?> memContentColumn; // Value injected by FXMLLoader

	@FXML // fx:id="relocAddressColumn"
	private TableColumn<?, ?> relocAddressColumn; // Value injected by FXMLLoader

	@FXML // fx:id="hexImageView"
	private ImageView hexImageView; // Value injected by FXMLLoader

	@FXML // fx:id="reductionFontSizeButton"
	private Button reductionFontSizeButton; // Value injected by FXMLLoader

	@FXML // fx:id="memAdressColumn"
	private TableColumn<?, ?> memAdressColumn; // Value injected by FXMLLoader

	@FXML // fx:id="sizeLabel"
	private Label sizeLabel; // Value injected by FXMLLoader

	@FXML // fx:id="memTable"
	private TableView<?> memTable; // Value injected by FXMLLoader

	@FXML // fx:id="decimalImageView"
	private ImageView decimalImageView; // Value injected by FXMLLoader

	@FXML
	void reductionFontSizeAction(ActionEvent event) {

	}

	@FXML
	void expansionFontSizeAction(ActionEvent event) {

	}

	@FXML
	void loadAction(ActionEvent event) {

	}

	@FXML
	void transBinaryAction(ActionEvent event) {

	}

	@FXML
	void transOctalAction(ActionEvent event) {

	}

	@FXML
	void transDecimalAction(ActionEvent event) {

	}

	@FXML
	void transHexAction(ActionEvent event) {

	}

	@FXML
	void transCharAction(ActionEvent event) {

	}

	@FXML
	void gotoCasl2EditAction(ActionEvent event) {
		sc.setScreen(SimSceneType.CASL2_EDIT);
	}

	@FXML
	void gotoComet2EditAction(ActionEvent event) {
		sc.setScreen(SimSceneType.COMET2_EDIT);
	}

	@FXML
	void invokedComet2Action(ActionEvent event) {
		sc.setScreen(SimSceneType.VISUALIZATION);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert relocContentColumn != null : "fx:id=\"relocContentColumn\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert gotoCasl2EditButton != null : "fx:id=\"gotoCasl2EditButton\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert octalImageView != null : "fx:id=\"octalImageView\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert expansionFontSizeButton != null : "fx:id=\"expansionFontSizeButton\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert splitAryMenuButton != null : "fx:id=\"splitAryMenuButton\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert memListPane != null : "fx:id=\"memListPane\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert gotoComet2EditButton != null : "fx:id=\"gotoComet2EditButton\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert startLabel != null : "fx:id=\"startLabel\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert invokedComet2Button != null : "fx:id=\"invokedComet2Button\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert leftStatus3 != null : "fx:id=\"leftStatus3\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert endLabel != null : "fx:id=\"endLabel\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert leftStatus1 != null : "fx:id=\"leftStatus1\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert leftStatus2 != null : "fx:id=\"leftStatus2\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert casl2EditRoot != null : "fx:id=\"casl2EditRoot\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert relocTable != null : "fx:id=\"relocTable\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert selectAryImage != null : "fx:id=\"selectAryImage\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert binaryImageView != null : "fx:id=\"binaryImageView\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert charImageView != null : "fx:id=\"charImageView\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert zoomFontSizeSlider != null : "fx:id=\"zoomFontSizeSlider\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert memContentColumn != null : "fx:id=\"memContentColumn\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert relocAddressColumn != null : "fx:id=\"relocAddressColumn\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert hexImageView != null : "fx:id=\"hexImageView\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert reductionFontSizeButton != null : "fx:id=\"reductionFontSizeButton\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert memAdressColumn != null : "fx:id=\"memAdressColumn\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert sizeLabel != null : "fx:id=\"sizeLabel\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert memTable != null : "fx:id=\"memTable\" was not injected: check your FXML file 'RelocatableScene.fxml'.";
		assert decimalImageView != null : "fx:id=\"decimalImageView\" was not injected: check your FXML file 'RelocatableScene.fxml'.";

	}

	private ScreensController<SimSceneType> sc;
	private ExecutorService service;
	private Casl2EditController caec;
	private VisualizationController vc;
	private AssemblerMode asmMode;

	public void setEC(Casl2EditController caec) {
		this.caec = caec;
	}

	public void setVC(VisualizationController vc) {
		this.vc = vc;
	}

	@Override
	public void setExecutorService(ExecutorService service) {
		this.service = service;
	}

	@Override
	public void setScreenParent(ScreensController<SimSceneType> sc) {
		this.sc = sc;
	}

	@Override
	public void setAssemblerMode(AssemblerMode asmMode) {
		this.asmMode = asmMode;
	}

}
