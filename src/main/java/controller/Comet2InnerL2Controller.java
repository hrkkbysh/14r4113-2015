package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class Comet2InnerL2Controller extends BorderPane implements Initializable,Threadable,ModeTogglable,Controllable<SimSceneType>{

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
	    private MenuItem saveMenu;

	    @FXML
	    private Label leftStatus3;

	    @FXML
	    private Label leftStatus1;

	    @FXML
	    private Label leftStatus2;

	    @FXML
	    private MenuItem quitMenu;

	    @FXML
	    private MenuItem copyMenu;

	    @FXML
	    private MenuItem openMenu;

	    @FXML
	    private ImageView selectAryImage;

	    @FXML
	    private ImageView binaryImageView;

	    @FXML
	    private Button pauseButton;

	    @FXML
	    private ImageView charImageView;

	    @FXML
	    private MenuItem transitionComet2EditMenuItem;

	    @FXML
	    private MenuItem helpMenu;

	    @FXML
	    private Slider zoomFontSizeSlider;

	    @FXML
	    private ImageView hexImageView;

	    @FXML
	    private MenuItem instProfileMenuItem;

	    @FXML
	    private MenuItem reloadMenuItem;

	    @FXML
	    private ImageView modeIcon;

	    @FXML
	    private MenuItem selectAllMenu;

	    @FXML
	    private MenuItem setStopStatementMenuItem;

	    @FXML
	    private MenuItem backToExStateMenuItem;

	    @FXML
	    private ImageView decimalImageView;

	    @FXML
	    private Menu openRecentMenu;

	    @FXML
	    private MenuItem pasteMenu;

	    @FXML
	    private Button expansionFontSizeButton;

	    @FXML
	    private SplitMenuButton splitAryMenuButton;

	    @FXML
	    private Tooltip modeToolTip;

	    @FXML
	    private Button backStateButton;

	    @FXML
	    private MenuItem transitionCasl2ExtensionMenuItem;

	    @FXML
	    private MenuItem stepRunMenuItem;

	    @FXML
	    private ImageView decimalImageView1;

	    @FXML
	    private MenuItem undoMenu;

	    @FXML
	    private MenuItem setBPMenuItem;

	    @FXML
	    private BorderPane casl2EditRoot;

	    @FXML
	    private MenuItem transitionCasl2EditMenuItem;

	    @FXML
	    private MenuItem setDetailMenuItem;

	    @FXML
	    private MenuItem newMenu;

	    @FXML
	    private Button reductionFontSizeButton;

	    @FXML
	    private Button setStopStatementButton;

	    @FXML
	    private Button stepRunButton;

	    @FXML
	    private Button outHistoryButton;

	    @FXML
	    private MenuItem cutMenu;

	    @FXML
	    private Button stopButton;

	    @FXML
	    private MenuItem redoMenu;

	    @FXML
	    private Button setBPButton;

	    @FXML
	    private MenuItem saveAsMenu;

	    @FXML
	    void reductionFontSizeAction(ActionEvent event) {

	    }

	    @FXML
	    void expansionFontSizeAction(ActionEvent event) {

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
	    void reloadAction(ActionEvent event) {
	    	sc.setScreen(SimSceneType.RELOCATABLE);
	    }
	    @FXML
	    void invokedComet2Action(ActionEvent event) {
	    	sc.setScreen(SimSceneType.VISUALIZATION);
	    }

	    @FXML
	    void transitionCasl2EditAction(ActionEvent event) {
	    	asmMode = AssemblerMode.NORMALCASL2;
	    	sc.setScreen(SimSceneType.CASL2_EDIT);
	    }

	    @FXML
	    void transitionCasl2ExtensionAction(ActionEvent event) {
	    	asmMode = AssemblerMode.EXTENSIONCASL2;
	    	sc.setScreen(SimSceneType.CASL2_EDIT);
	    }

	    @FXML
	    void transitionComet2EditAction(ActionEvent event) {
	    	sc.setScreen(SimSceneType.COMET2_EDIT);
	    }

	    @FXML
	    void helpAction(ActionEvent event) {

	    }

	    @FXML
	    void backStateAction(ActionEvent event) {

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

	    @Override
		public void initialize(URL location, ResourceBundle resources) {
			// TODO 自動生成されたメソッド・スタブ
			
		}
    
	VisualizationController vc;
	ScreensController<SimSceneType> sc;
	private ExecutorService service;
	private AssemblerMode asmMode;

	@Override
	public void setScreenParent(ScreensController<SimSceneType> screenPage) {
		this.sc = screenPage;
	}
	
	@Override
	public void setExecutorService(ExecutorService service) {
		this.service =service;
	}
	
	@Override
	public void setAssemblerMode(AssemblerMode asmMode) {
		this.asmMode = asmMode;
	}
	public void setVC(VisualizationController vc){
		this.vc = vc;
	}

}
