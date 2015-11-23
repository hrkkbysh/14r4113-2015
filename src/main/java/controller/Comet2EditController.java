package controller;

/**
 * Sample Skeleton for 'Comet2EditScene.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import jfxtras.scene.menu.CornerMenu;

public class Comet2EditController extends BorderPane implements Initializable,Threadable,ModeTogglable,Controllable<SimSceneType>{

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="gr3Field"
    private TextField gr3Field; // Value injected by FXMLLoader

    @FXML // fx:id="setDataVisualMenuItem"
    private MenuItem setDataVisualMenuItem; // Value injected by FXMLLoader

    @FXML // fx:id="gr5Label"
    private Label gr5Label; // Value injected by FXMLLoader

    @FXML // fx:id="spField"
    private TextField spField; // Value injected by FXMLLoader

    @FXML // fx:id="prLabel"
    private Label prLabel; // Value injected by FXMLLoader

    @FXML // fx:id="saveMenu"
    private MenuItem saveMenu; // Value injected by FXMLLoader

    @FXML // fx:id="redoButton"
    private Button redoButton; // Value injected by FXMLLoader

    @FXML // fx:id="codingAssistMenuItem"
    private MenuItem codingAssistMenuItem; // Value injected by FXMLLoader

    @FXML // fx:id="leftStatus1"
    private Label leftStatus1; // Value injected by FXMLLoader

    @FXML // fx:id="leftStatus2"
    private Label leftStatus2; // Value injected by FXMLLoader

    @FXML // fx:id="leftStatus3"
    private Label leftStatus3; // Value injected by FXMLLoader
    
    
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
    
    @FXML // fx:id="setDebuggerMenuItem"
    private MenuItem setDebuggerMenuItem; // Value injected by FXMLLoader

    @FXML // fx:id="frLabel"
    private Label frLabel; // Value injected by FXMLLoader

    @FXML // fx:id="gr6Label"
    private Label gr6Label; // Value injected by FXMLLoader

    @FXML // fx:id="copyButton"
    private Button copyButton; // Value injected by FXMLLoader

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

    @FXML // fx:id="xRefMenuItem"
    private MenuItem xRefMenuItem; // Value injected by FXMLLoader

    @FXML // fx:id="gr4Field"
    private TextField gr4Field; // Value injected by FXMLLoader


    @FXML // fx:id="selectAllMenu"
    private MenuItem selectAllMenu; // Value injected by FXMLLoader

    @FXML // fx:id="gr1Field"
    private TextField gr1Field; // Value injected by FXMLLoader

    @FXML // fx:id="xRefButton"
    private Button xRefButton; // Value injected by FXMLLoader

    @FXML // fx:id="undoButton"
    private Button undoButton; // Value injected by FXMLLoader

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

    @FXML // fx:id="modeToolTip"
    private Tooltip modeToolTip; // Value injected by FXMLLoader

    @FXML // fx:id="openButton"
    private Button openButton; // Value injected by FXMLLoader

    @FXML // fx:id="prField"
    private TextField prField; // Value injected by FXMLLoader

    @FXML // fx:id="contentColumn"
    private TableColumn<?, ?> contentColumn; // Value injected by FXMLLoader

    @FXML // fx:id="saveButton"
    private Button saveButton; // Value injected by FXMLLoader

    @FXML // fx:id="gr6Field"
    private TextField gr6Field; // Value injected by FXMLLoader

    @FXML // fx:id="undoMenu"
    private MenuItem undoMenu; // Value injected by FXMLLoader

    @FXML // fx:id="casl2EditRoot"
    private BorderPane casl2EditRoot; // Value injected by FXMLLoader

    @FXML // fx:id="pasteButton"
    private Button pasteButton; // Value injected by FXMLLoader

    @FXML // fx:id="newMenu"
    private MenuItem newMenu; // Value injected by FXMLLoader

    @FXML // fx:id="gr5Field"
    private TextField gr5Field; // Value injected by FXMLLoader

    @FXML // fx:id="reductionFontSizeButton"
    private Button reductionFontSizeButton; // Value injected by FXMLLoader

    @FXML // fx:id="gr2Field"
    private TextField gr2Field; // Value injected by FXMLLoader

    @FXML // fx:id="spLabel"
    private Label spLabel; // Value injected by FXMLLoader

    @FXML // fx:id="modeMenuPane"
    private StackPane modeMenuPane; // Value injected by FXMLLoader

    @FXML // fx:id="cutMenu"
    private MenuItem cutMenu; // Value injected by FXMLLoader

    @FXML // fx:id="gr1Label"
    private Label gr1Label; // Value injected by FXMLLoader

    @FXML // fx:id="newButton"
    private Button newButton; // Value injected by FXMLLoader

    @FXML // fx:id="gr4Label"
    private Label gr4Label; // Value injected by FXMLLoader

    @FXML // fx:id="memIndexListPane"
    private Pane memIndexListPane; // Value injected by FXMLLoader

    @FXML // fx:id="redoMenu"
    private MenuItem redoMenu; // Value injected by FXMLLoader

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
    void checkCASL2CodeState(ActionEvent event) {

    }

    @FXML
    void xRefAction(ActionEvent event) {

    }

    @FXML
    void checkEditorState(ActionEvent event) {

    }

    @FXML
    void codingAssistAction(ActionEvent event) {

    }

    @FXML
    void setDebuggerAction(ActionEvent event) {

    }

    @FXML
    void setDataVisualAction(ActionEvent event) {

    }
    
    @FXML
    void helpAction(ActionEvent event) {

    }

    @FXML // fx:id="selectAryImage"
    private ImageView selectAryImage; // Value injected by FXMLLoader

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
    
    @FXML // fx:id="splitAryMenuButton"
    private SplitMenuButton splitAryMenuButton; // Value injected by FXMLLoader
    
    @FXML
    void transBinaryAction(ActionEvent event) {

    }

    @FXML
    void transOctalAction(ActionEvent event) {

    }

    @FXML
    void transSignDecimalAction(ActionEvent event) {

    }
    @FXML
    void transNoSignDecimalAction(ActionEvent event) {

    }

    @FXML
    void transHexAction(ActionEvent event) {

    }

    @FXML
    void transCharAction(ActionEvent event) {

    }

    
    @FXML // fx:id="invokedComet2Button"
    private Button invokedComet2Button; // Value injected by FXMLLoader
    @FXML // fx:id="invokedComet2MenuItem"
    private MenuItem invokedComet2MenuItem; // Value injected by FXMLLoader
    
    @FXML
    void invokedComet2Action(ActionEvent event) {
    	sc.setScreen(SimSceneType.VISUALIZATION);
    }
    
    @FXML // fx:id="modeIcon"
    private ImageView modeIcon; // Value injected by FXMLLoader
	
	@FXML // fx:id="normalModeIcon"
	private ImageView normalModeIcon; // Value injected by FXMLLoader
	@FXML // fx:id="extensionModeIcon"
	private ImageView extensionModeIcon; // Value injected by FXMLLoader
    
    @FXML // fx:id="transitionCasl2EditMenuItem"
    private MenuItem transitionCasl2EditMenuItem; // Value injected by FXMLLoader
    @FXML // fx:id="transitionCasl2EditExtensionMenuItem"
    private MenuItem transitionCasl2EditExtensionMenuItem;// Value injected by FXMLLoader
    @FXML // fx:id="transitionComet2EditExtensionMenuItem"
    private MenuItem transitionComet2EditExtensionMenuItem; // Value injected by FXMLLoader
    @FXML // fx:id="transitionComet2EditMenuItem"
    private MenuItem transitionComet2EditMenuItem; // Value injected by FXMLLoader

    @FXML
    void transitionCasl2EditAction(ActionEvent event) {
    	cec.setAssemblerMode(AssemblerMode.NORMALCASL2);
    	sc.setScreen(SimSceneType.CASL2_EDIT);
    }

    @FXML
    void transitionCasl2EditExtensionAction(ActionEvent event) {
    	cec.setAssemblerMode(AssemblerMode.EXTENSIONCASL2);
    	sc.setScreen(SimSceneType.CASL2_EDIT);
    }
   
    @FXML
    void transitionComet2EditAction(ActionEvent event) {
    	asmMode = AssemblerMode.NORMALCASL2;
    	setNormalMode();
    }
    
    @FXML
    void transitionComet2EditExtensionAction(ActionEvent event) {
    	asmMode = AssemblerMode.EXTENSIONCASL2;
    	setExtensionMode();
    }

    @FXML
    void showModeMenuAction(ActionEvent event) {
    	if(modeMenu.shownProperty().get()){ modeMenu.hide();}
    	else { modeMenu.show();}
    }
    
    private void setModeMenu() {
		modeMenu = new CornerMenu(CornerMenu.Location.BOTTOM_LEFT, this.modeMenuPane, true);		
		modeMenu.getItems().addAll(transitionCasl2EditMenuItem,transitionCasl2EditExtensionMenuItem,transitionComet2EditExtensionMenuItem);
		modeMenu.hide();


		switch(asmMode){
		case NORMALCASL2: setNormalMode();
		break;
		case EXTENSIONCASL2: setExtensionMode();
		break;
		default: setNormalMode();
		}
	}
    private void setNormalMode() {
		modeIcon.setImage(normalModeIcon.getImage());
		transitionComet2EditMenuItem.setVisible(false);
		modeToolTip.setText(asmMode.getTooltip());
	}
	private void setExtensionMode() {
		modeIcon.setImage(extensionModeIcon.getImage());
		transitionComet2EditExtensionMenuItem.setVisible(false);
		modeToolTip.setText(asmMode.getTooltip());
	}

    @Override
	public void initialize(URL location, ResourceBundle resources) {
        assert gr3Field != null : "fx:id=\"gr3Field\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert setDataVisualMenuItem != null : "fx:id=\"setDataVisualMenuItem\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert gr5Label != null : "fx:id=\"gr5Label\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert octalImageView != null : "fx:id=\"octalImageView\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert invokedComet2MenuItem != null : "fx:id=\"invokedComet2MenuItem\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert spField != null : "fx:id=\"spField\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert prLabel != null : "fx:id=\"prLabel\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert saveMenu != null : "fx:id=\"saveMenu\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert redoButton != null : "fx:id=\"redoButton\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert invokedComet2Button != null : "fx:id=\"invokedComet2Button\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert codingAssistMenuItem != null : "fx:id=\"codingAssistMenuItem\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert leftStatus3 != null : "fx:id=\"leftStatus3\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert leftStatus1 != null : "fx:id=\"leftStatus1\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert leftStatus2 != null : "fx:id=\"leftStatus2\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert quitMenu != null : "fx:id=\"quitMenu\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert memoryTable != null : "fx:id=\"memoryTable\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert copyMenu != null : "fx:id=\"copyMenu\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert addressColumn != null : "fx:id=\"addressColumn\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert openMenu != null : "fx:id=\"openMenu\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert selectAryImage != null : "fx:id=\"selectAryImage\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert binaryImageView != null : "fx:id=\"binaryImageView\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert charImageView != null : "fx:id=\"charImageView\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert transitionComet2EditExtensionMenuItem != null : "fx:id=\"transitionComet2EditExtensionMenuItem\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert transitionComet2EditMenuItem != null : "fx:id=\"transitionComet2EditMenuItem\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert setDebuggerMenuItem != null : "fx:id=\"setDebuggerMenuItem\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert frLabel != null : "fx:id=\"frLabel\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert gr6Label != null : "fx:id=\"gr6Label\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert copyButton != null : "fx:id=\"copyButton\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert helpMenu != null : "fx:id=\"helpMenu\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert zoomFontSizeSlider != null : "fx:id=\"zoomFontSizeSlider\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert gr0Label != null : "fx:id=\"gr0Label\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert gr7Field != null : "fx:id=\"gr7Field\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert hexImageView != null : "fx:id=\"hexImageView\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert gr3Label != null : "fx:id=\"gr3Label\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert xRefMenuItem != null : "fx:id=\"xRefMenuItem\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert gr4Field != null : "fx:id=\"gr4Field\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert modeIcon != null : "fx:id=\"modeIcon\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
		assert extensionModeIcon != null : "fx:id=\"extensionModeIcon\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert selectAllMenu != null : "fx:id=\"selectAllMenu\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert gr1Field != null : "fx:id=\"gr1Field\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert xRefButton != null : "fx:id=\"xRefButton\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert undoButton != null : "fx:id=\"undoButton\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert decimalImageView != null : "fx:id=\"decimalImageView\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert openRecentMenu != null : "fx:id=\"openRecentMenu\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert frField != null : "fx:id=\"frField\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert gr2Label != null : "fx:id=\"gr2Label\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert gr0Field != null : "fx:id=\"gr0Field\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert pasteMenu != null : "fx:id=\"pasteMenu\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert expansionFontSizeButton != null : "fx:id=\"expansionFontSizeButton\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert splitAryMenuButton != null : "fx:id=\"splitAryMenuButton\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert modeToolTip != null : "fx:id=\"modeToolTip\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert openButton != null : "fx:id=\"openButton\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert prField != null : "fx:id=\"prField\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert contentColumn != null : "fx:id=\"contentColumn\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert transitionCasl2EditExtensionMenuItem != null : "fx:id=\"transitionCasl2ExtensionMenuItem\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert gr6Field != null : "fx:id=\"gr6Field\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert undoMenu != null : "fx:id=\"undoMenu\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert casl2EditRoot != null : "fx:id=\"casl2EditRoot\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert pasteButton != null : "fx:id=\"pasteButton\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert transitionCasl2EditMenuItem != null : "fx:id=\"transitionCasl2EditMenuItem\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert newMenu != null : "fx:id=\"newMenu\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert gr5Field != null : "fx:id=\"gr5Field\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert reductionFontSizeButton != null : "fx:id=\"reductionFontSizeButton\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert gr2Field != null : "fx:id=\"gr2Field\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert spLabel != null : "fx:id=\"spLabel\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert modeMenuPane != null : "fx:id=\"modeMenuPane\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert cutMenu != null : "fx:id=\"cutMenu\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert gr1Label != null : "fx:id=\"gr1Label\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert newButton != null : "fx:id=\"newButton\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert gr4Label != null : "fx:id=\"gr4Label\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert memIndexListPane != null : "fx:id=\"memIndexListPane\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert redoMenu != null : "fx:id=\"redoMenu\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert gr7Label != null : "fx:id=\"gr7Label\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";
        assert saveAsMenu != null : "fx:id=\"saveAsMenu\" was not injected: check your FXML file 'Comet2EditScene.fxml'.";

    }

  //骨格の自動生成はここまで

  		private VisualizationController vc;
  		private Casl2EditController cec;
  		private ScreensController<SimSceneType> sc;
  		private ExecutorService service;
  		private CornerMenu modeMenu;
  		private AssemblerMode asmMode;

  		public void setVC(VisualizationController vc) {
  			this.vc = vc;
  		}
  		public void setCEC(Casl2EditController cec){
  			this.cec = cec;
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
			setModeMenu();
		}
}
