package controller;

import java.net.URL;
import java.util.ResourceBundle;

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
		asmMode = AssemblerMode.NORMALCASL2;
		pretrans();
		screenPage.setScreen(SimSceneType.CASL2_EDIT);
	}

	@FXML
	void gotoCasl2ExtensionEditMode(ActionEvent event){
		asmMode = AssemblerMode.EXTENSIONCASL2;
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
	private AssemblerMode asmMode;
	private Stage stage;

	@Override
	public void setAssemblerMode(AssemblerMode asmMode) {
		this.asmMode = asmMode;
	}
	public void setStage(Stage stage){
		this.stage = stage;
	}
	private void pretrans(){
		stage.setResizable(true);
	}
}
