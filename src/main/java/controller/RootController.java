package controller;

import java.net.URL;
import java.util.ResourceBundle;

import casl2.AsmMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class RootController extends AnchorPane implements Initializable,ModeTogglable,Controllable<SimSceneType>{

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="root"
	private AnchorPane root; // Value injected by FXMLLoader

	@FXML
	void gotoCasl2EditMode(ActionEvent event) {
		cec.setAssemblerMode(AsmMode.NORMAL);
		pretrans();
		screenPage.setScreen(SimSceneType.CASL2_EDIT);
	}

	@FXML
	void gotoCasl2ExtensionEditMode(ActionEvent event){
		cec.setAssemblerMode(AsmMode.EXTEND);
		pretrans();
		screenPage.setScreen(SimSceneType.CASL2_EDIT);
	}

	@FXML
	void gotoComet2EditMode(ActionEvent event) {
		pretrans();
		screenPage.setScreen(SimSceneType.COMET2_EDIT);
	}

	@Override
	public void setScreenParent(ScreensController<SimSceneType> screenPage) {
		this.screenPage = screenPage;
	}

	@Override // This method is called by the FXMLLoader when initialization is complete
	public void initialize(URL location, ResourceBundle resources) {
		assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'Root.fxml'.";
	}

	private ScreensController<SimSceneType> screenPage;
	private AsmMode asmMode;
	private Stage stage;

	private Casl2EditController cec;

	@Override
	public void setAssemblerMode(AsmMode asmMode) {
		this.asmMode = asmMode;
	}
	public void setStage(Stage stage){
		this.stage = stage;
	}
	private void pretrans(){
		stage.setResizable(true);
	}
	public void setCEC(Casl2EditController cec){
		this.cec = cec;
	}

}
