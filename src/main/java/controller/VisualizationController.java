package controller;

/**
 * Sample Skeleton for 'VisualizationScene.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

import casl2.AsmMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import jfxtras.scene.menu.CornerMenu;

public class VisualizationController extends BorderPane implements Initializable,Threadable,ModeTogglable,Controllable<SimSceneType>{

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="gr3Field"
	private TextField gr3Field; // Value injected by FXMLLoader

	@FXML // fx:id="outTraceViewMenuItem"
	private MenuItem outTraceViewMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="setDataVisualMenuItem"
	private MenuItem setDataVisualMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="gr5Label"
	private Label gr5Label; // Value injected by FXMLLoader

	@FXML // fx:id="spField"
	private TextField spField; // Value injected by FXMLLoader

	@FXML // fx:id="runButton"
	private Button runButton; // Value injected by FXMLLoader

	@FXML // fx:id="prLabel"
	private Label prLabel; // Value injected by FXMLLoader

	@FXML // fx:id="saveMenu"
	private MenuItem saveMenu; // Value injected by FXMLLoader

	@FXML // fx:id="BacktoStateMenuItem"
	private MenuItem backToExStateMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="leftStatus3"
	private Label leftStatus3; // Value injected by FXMLLoader

	@FXML // fx:id="leftStatus1"
	private Label leftStatus1; // Value injected by FXMLLoader

	@FXML // fx:id="leftStatus2"
	private Label leftStatus2; // Value injected by FXMLLoader

	@FXML // fx:id="quitMenu"
	private MenuItem quitMenu; // Value injected by FXMLLoader

	@FXML // fx:id="memoryTable"
	private TableView<?> memoryTable; // Value injected by FXMLLoader

	@FXML // fx:id="copyMenu"
	private MenuItem copyMenu; // Value injected by FXMLLoader

	@FXML // fx:id="addressColumn"
	private TableColumn<?, ?> addressColumn; // Value injected by FXMLLoader

	@FXML // fx:id="openMenu"
	private MenuItem openMenu; // Value injected by FXMLLoader

	@FXML // fx:id="pauseButton"
	private Button pauseButton; // Value injected by FXMLLoader	
	
	@FXML // fx:id="frLabel"
	private Label frLabel; // Value injected by FXMLLoader

	@FXML // fx:id="gr6Label"
	private Label gr6Label; // Value injected by FXMLLoader

	@FXML // fx:id="helpMenu"
	private MenuItem helpMenu; // Value injected by FXMLLoader

	@FXML // fx:id="zoomFontSizeSlider"
	private Slider zoomFontSizeSlider; // Value injected by FXMLLoader

	@FXML // fx:id="gr0Label"
	private Label gr0Label; // Value injected by FXMLLoader

	@FXML // fx:id="gr7Field"
	private TextField gr7Field; // Value injected by FXMLLoader

	@FXML // fx:id="gr3Label"
	private Label gr3Label; // Value injected by FXMLLoader

	@FXML // fx:id="instProfileMenuItem"
	private MenuItem instProfileMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="reloadMenuItem"
	private MenuItem reloadMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="gr4Field"
	private TextField gr4Field; // Value injected by FXMLLoader

	@FXML // fx:id="modeIcon"
	private ImageView modeIcon; // Value injected by FXMLLoader

	@FXML // fx:id="selectAllMenu"
	private MenuItem selectAllMenu; // Value injected by FXMLLoader

	@FXML // fx:id="setStopStatementMenuItem"
	private MenuItem setStopStatementMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="gr1Field"
	private TextField gr1Field; // Value injected by FXMLLoader


	@FXML // fx:id="openRecentMenu"
	private Menu openRecentMenu; // Value injected by FXMLLoader

	@FXML // fx:id="frField"
	private TextField frField; // Value injected by FXMLLoader

	@FXML // fx:id="gr2Label"
	private Label gr2Label; // Value injected by FXMLLoader

	@FXML // fx:id="gr0Field"
	private TextField gr0Field; // Value injected by FXMLLoader

	@FXML // fx:id="pasteMenu"
	private MenuItem pasteMenu; // Value injected by FXMLLoader

	@FXML // fx:id="expansionFontSizeButton"
	private Button expansionFontSizeButton; // Value injected by FXMLLoader

	@FXML // fx:id="editorPane"
	private AnchorPane editorPane; // Value injected by FXMLLoader

	@FXML // fx:id="modeToolTip"
	private Tooltip modeToolTip; // Value injected by FXMLLoader

	@FXML // fx:id="backToExStateButton"
	private Button backToExStateButton; // Value injected by FXMLLoader

	@FXML // fx:id="prField"
	private TextField prField; // Value injected by FXMLLoader

	@FXML // fx:id="contentColumn"
	private TableColumn<?, ?> contentColumn; // Value injected by FXMLLoader

	@FXML // fx:id="stepRunMenuItem"
	private MenuItem stepRunMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="gr6Field"
	private TextField gr6Field; // Value injected by FXMLLoader

	@FXML // fx:id="undoMenu"
	private MenuItem undoMenu; // Value injected by FXMLLoader

	@FXML // fx:id="setBPMenuItem"
	private MenuItem setBPMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="casl2EditRoot"
	private BorderPane casl2EditRoot; // Value injected by FXMLLoader

	
	@FXML // fx:id="setDetailMenuItem"
	private MenuItem setDetailMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="newMenu"
	private MenuItem newMenu; // Value injected by FXMLLoader

	@FXML // fx:id="gr5Field"
	private TextField gr5Field; // Value injected by FXMLLoader

	@FXML // fx:id="reductionFontSizeButton"
	private Button reductionFontSizeButton; // Value injected by FXMLLoader

	@FXML // fx:id="gr2Field"
	private TextField gr2Field; // Value injected by FXMLLoader

	@FXML // fx:id="setStopStatementButton"
	private Button setStopStatementButton; // Value injected by FXMLLoader

	@FXML // fx:id="spLabel"
	private Label spLabel; // Value injected by FXMLLoader

	@FXML // fx:id="stepRunButton"
	private Button stepRunButton; // Value injected by FXMLLoader

	@FXML // fx:id="modeMenuPane"
	private StackPane modeMenuPane; // Value injected by FXMLLoader

	@FXML // fx:id="outHistoryButton"
	private Button outHistoryButton; // Value injected by FXMLLoader

	@FXML // fx:id="cutMenu"
	private MenuItem cutMenu; // Value injected by FXMLLoader

	@FXML // fx:id="gr1Label"
	private Label gr1Label; // Value injected by FXMLLoader

	@FXML // fx:id="stopButton"
	private Button stopButton; // Value injected by FXMLLoader

	@FXML // fx:id="gr4Label"
	private Label gr4Label; // Value injected by FXMLLoader

	@FXML // fx:id="memIndexListPane"
	private Pane memIndexListPane; // Value injected by FXMLLoader

	@FXML // fx:id="redoMenu"
	private MenuItem redoMenu; // Value injected by FXMLLoader

	@FXML // fx:id="setBPButton"
	private Button setBPButton; // Value injected by FXMLLoader

	@FXML // fx:id="gr7Label"
	private Label gr7Label; // Value injected by FXMLLoader

	@FXML // fx:id="saveAsMenu"
	private MenuItem saveAsMenu; // Value injected by FXMLLoader

	@FXML
	void reductionFontSizeAction(ActionEvent event) {

	}

	@FXML
	void expansionFontSizeAction(ActionEvent event) {

	}

	@FXML
	void memorySortAction(ActionEvent event) {

	}

	@FXML
	void newAction(ActionEvent event) {

	}

	@FXML
	void openAction(ActionEvent event) {

	}

	@FXML
	void openRecentAction(ActionEvent event) {

	}

	@FXML
	void saveAction(ActionEvent event) {

	}

	@FXML
	void saveAsAction(ActionEvent event) {

	}

	@FXML
	void quitAction(ActionEvent event) {

	}

	@FXML
	void undoAction(ActionEvent event) {

	}

	@FXML
	void redoAction(ActionEvent event) {

	}

	@FXML
	void cutAction(ActionEvent event) {

	}

	@FXML
	void copyAction(ActionEvent event) {

	}

	@FXML
	void pasteAction(ActionEvent event) {

	}

	@FXML
	void selectAllAction(ActionEvent event) {

	}

	@FXML
	void invokedComet2ControllerAction(ActionEvent event) {

	}

	@FXML
	void checkCASL2RunState(ActionEvent event) {

	}

	@FXML
	void stepRunAction(ActionEvent event) {

	}

	@FXML
	void backToExStateAction(ActionEvent event) {

	}

	@FXML
	void outTraceViewAction(ActionEvent event) {

	}

	@FXML
	void instProfileAction(ActionEvent event) {

	}

	@FXML
	void setBPAction(ActionEvent event) {

	}

	@FXML
	void setStopStatementAction(ActionEvent event) {

	}

	@FXML
	void setDetailAction(ActionEvent event) {

	}

	@FXML
	void setDataVisualAction(ActionEvent event) {

	}

	@FXML
	void helpAction(ActionEvent event) {

	}

	@FXML
	void stopAction(ActionEvent event) {

	}

	@FXML
	void runAction(ActionEvent event) {

	}

	@FXML
	void pauseAction(ActionEvent event) {

	}

	@FXML
	void outHistoryAction(ActionEvent event) {

	}
	
	@FXML // fx:id="binaryImageView"
	private ImageView binaryImageView; // Value injected by FXMLLoader
	@FXML // fx:id="octalImageView"
	private ImageView octalImageView; // Value injected by FXMLLoader
	@FXML // fx:id="decimalImageView"
	private ImageView decimalImageView; // Value injected by FXMLLoader
	@FXML // fx:id="hexImageView"
	private ImageView hexImageView; // Value injected by FXMLLoader

	@FXML // fx:id="charImageView"
	private ImageView charImageView; // Value injected by FXMLLoader

	@FXML // fx:id="selectAryImage"
	private ImageView selectAryImage; // Value injected by FXMLLoader
	@FXML // fx:id="splitAryMenuButton"
	private SplitMenuButton splitAryMenuButton; // Value injected by FXMLLoader

	@FXML
	void transBinaryAction(ActionEvent event) {

	}

	@FXML
	void transOctalAction(ActionEvent event) {

	}

	@FXML
	void transNoSignDecimalAction(ActionEvent event) {

	}
	
	@FXML
	void transSignDecimalAction(ActionEvent event) {

	}

	@FXML
	void transHexAction(ActionEvent event) {

	}

	@FXML
	void transCharAction(ActionEvent event) {

	}

	@FXML // fx:id="invokedComet2MenuItem"
	private MenuItem invokedComet2MenuItem; // Value injected by FXMLLoader
	@FXML
	void reloadAction(ActionEvent event) {
		sc.setScreen(SimSceneType.RELOCATABLE);
	}
	
	//画面遷移
	@FXML
	private ImageView normalModeIcon;
	@FXML
	private ImageView extensionModeIcon;
	
	@FXML // fx:id="transitionCasl2EditMenuItem"
	private MenuItem transitionCasl2EditMenuItem; // Value injected by FXMLLoader
	
	@FXML // fx:id="transitionCasl2ExtensionMenuItem"
	private MenuItem transitionCasl2ExtensionMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="transitionComet2EditMenuItem"
	private MenuItem transitionComet2EditMenuItem; // Value injected by FXMLLoader
	
	@FXML // fx:id="transitionCComet2InnerL2MenuItem"
	private MenuItem transitionComet2InnerL2MenuItem; // Value injected by FXMLLoader
	
	@FXML
	void transitionCasl2EditAction(ActionEvent event) {
		asmMode = AsmMode.NORMAL;
		sc.setScreen(SimSceneType.CASL2_EDIT);
	}
	@FXML
	void transitionCasl2ExtensionAction(ActionEvent event) {
		asmMode = AsmMode.EXTEND;
		sc.setScreen(SimSceneType.CASL2_EDIT);
	}
	@FXML
	void transitionComet2EditAction(ActionEvent event) {
		sc.setScreen(SimSceneType.COMET2_EDIT);
	}
	@FXML
	void transitionComet2InnerL2Action(ActionEvent event) {
		sc.setScreen(SimSceneType.COMET_INNERL2);
	}
	@FXML
	void showModeMenuAction(ActionEvent event) {
    	if(modeMenu.shownProperty().get()){ modeMenu.hide();}
    	else { modeMenu.show();}
	}
	private void setModeMenu() {
		modeMenu = new CornerMenu(CornerMenu.Location.BOTTOM_LEFT, this.modeMenuPane, true);		
		modeMenu.getItems().addAll(transitionCasl2EditMenuItem,transitionCasl2ExtensionMenuItem,transitionComet2EditMenuItem,transitionComet2InnerL2MenuItem);
		modeMenu.hide();
		
		switch(asmMode){
		case NORMAL: setNormalMode();
		break;
		case EXTEND: setExtensionMode();
		break;
		default: setNormalMode();
		}
	}
	private void setNormalMode() {
		modeIcon.setImage(normalModeIcon.getImage());
		modeToolTip.setText(asmMode.getTooltip());
	}
	private void setExtensionMode() {
		modeIcon.setImage(extensionModeIcon.getImage());
		modeToolTip.setText(asmMode.getTooltip());
	}
	
	//コントローラー初期化
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert gr3Field != null : "fx:id=\"gr3Field\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert outTraceViewMenuItem != null : "fx:id=\"outTraceViewMenuItem\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert setDataVisualMenuItem != null : "fx:id=\"setDataVisualMenuItem\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert gr5Label != null : "fx:id=\"gr5Label\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert octalImageView != null : "fx:id=\"octalImageView\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert invokedComet2MenuItem != null : "fx:id=\"invokedComet2MenuItem\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert spField != null : "fx:id=\"spField\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert runButton != null : "fx:id=\"runButton\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert prLabel != null : "fx:id=\"prLabel\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert saveMenu != null : "fx:id=\"saveMenu\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert leftStatus3 != null : "fx:id=\"leftStatus3\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert leftStatus1 != null : "fx:id=\"leftStatus1\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert leftStatus2 != null : "fx:id=\"leftStatus2\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert quitMenu != null : "fx:id=\"quitMenu\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert memoryTable != null : "fx:id=\"memoryTable\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert copyMenu != null : "fx:id=\"copyMenu\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert addressColumn != null : "fx:id=\"addressColumn\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert openMenu != null : "fx:id=\"openMenu\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert selectAryImage != null : "fx:id=\"selectAryImage\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert binaryImageView != null : "fx:id=\"binaryImageView\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert pauseButton != null : "fx:id=\"pauseButton\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert charImageView != null : "fx:id=\"charImageView\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert transitionComet2EditMenuItem != null : "fx:id=\"transitionComet2EditMenuItem\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert frLabel != null : "fx:id=\"frLabel\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert gr6Label != null : "fx:id=\"gr6Label\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert helpMenu != null : "fx:id=\"helpMenu\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert zoomFontSizeSlider != null : "fx:id=\"zoomFontSizeSlider\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert gr0Label != null : "fx:id=\"gr0Label\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert gr7Field != null : "fx:id=\"gr7Field\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert hexImageView != null : "fx:id=\"hexImageView\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert gr3Label != null : "fx:id=\"gr3Label\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert instProfileMenuItem != null : "fx:id=\"instProfileMenuItem\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert reloadMenuItem != null : "fx:id=\"reloadMenuItem\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert gr4Field != null : "fx:id=\"gr4Field\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert modeIcon != null : "fx:id=\"modeIcon\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert selectAllMenu != null : "fx:id=\"selectAllMenu\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert setStopStatementMenuItem != null : "fx:id=\"setStopStatementMenuItem\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert gr1Field != null : "fx:id=\"gr1Field\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert decimalImageView != null : "fx:id=\"decimalImageView\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert openRecentMenu != null : "fx:id=\"openRecentMenu\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert frField != null : "fx:id=\"frField\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert gr2Label != null : "fx:id=\"gr2Label\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert gr0Field != null : "fx:id=\"gr0Field\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert pasteMenu != null : "fx:id=\"pasteMenu\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert expansionFontSizeButton != null : "fx:id=\"expansionFontSizeButton\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert editorPane != null : "fx:id=\"editorPane\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert splitAryMenuButton != null : "fx:id=\"splitAryMenuButton\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert modeToolTip != null : "fx:id=\"modeToolTip\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert backToExStateButton != null : "fx:id=\"backStateButton\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert prField != null : "fx:id=\"prField\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert contentColumn != null : "fx:id=\"contentColumn\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert transitionCasl2ExtensionMenuItem != null : "fx:id=\"transitionCasl2ExtensionMenuItem\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert stepRunMenuItem != null : "fx:id=\"stepRunMenuItem\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert transitionComet2EditMenuItem != null : "fx:id=\"transitionComet2EditMenuItem\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert transitionComet2InnerL2MenuItem != null : "fx:id=\"transitionComet2InnerL2MenuItem\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert gr6Field != null : "fx:id=\"gr6Field\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert undoMenu != null : "fx:id=\"undoMenu\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert setBPMenuItem != null : "fx:id=\"setBPMenuItem\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert casl2EditRoot != null : "fx:id=\"casl2EditRoot\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert transitionCasl2EditMenuItem != null : "fx:id=\"transitionCasl2EditMenuItem\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert setDetailMenuItem != null : "fx:id=\"setDetailMenuItem\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert newMenu != null : "fx:id=\"newMenu\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert gr5Field != null : "fx:id=\"gr5Field\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert reductionFontSizeButton != null : "fx:id=\"reductionFontSizeButton\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert gr2Field != null : "fx:id=\"gr2Field\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert setStopStatementButton != null : "fx:id=\"setStopStatementButton\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert spLabel != null : "fx:id=\"spLabel\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert stepRunButton != null : "fx:id=\"stepRunButton\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert modeMenuPane != null : "fx:id=\"modeMenuPane\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert outHistoryButton != null : "fx:id=\"outHistoryButton\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert cutMenu != null : "fx:id=\"cutMenu\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert gr1Label != null : "fx:id=\"gr1Label\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert stopButton != null : "fx:id=\"stopButton\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert gr4Label != null : "fx:id=\"gr4Label\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert memIndexListPane != null : "fx:id=\"memIndexListPane\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert redoMenu != null : "fx:id=\"redoMenu\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert setBPButton != null : "fx:id=\"setBPButton\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert gr7Label != null : "fx:id=\"gr7Label\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
		assert saveAsMenu != null : "fx:id=\"saveAsMenu\" was not injected: check your FXML file 'VisualizationScene.fxml'.";
	}


	//骨格の自動形成はここまで

	Casl2EditController caec;
	RelocatableController rc;
	ScreensController<SimSceneType> sc;
	private ExecutorService service;
	private CornerMenu modeMenu;
	private AsmMode asmMode;

	@Override
	public void setScreenParent(ScreensController<SimSceneType> screenPage) {
		this.sc = screenPage;

	}

	@Override
	public void setExecutorService(ExecutorService service) {
		this.service =service;
	}
	
	@Override
	public void setAssemblerMode(AsmMode asmMode) {
		this.asmMode = asmMode;
		setModeMenu();
	}

	public void setEC(Casl2EditController caec) {
		this.caec = caec;
	}

	public void setRC(RelocatableController rc) {
		this.rc = rc;
	}
}

