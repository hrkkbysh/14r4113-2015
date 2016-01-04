package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class RootController extends AnchorPane implements Initializable,Controllable<EditModeScene>{

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="root"
	private AnchorPane root; // Value injected by FXMLLoader
	private DebugModeController coec;

	@FXML
	void gotoCasl2EditMode(ActionEvent event) {
		preTrans();
		screenPage.setScreen(EditModeScene.CASL2_EDIT);
	}

	@FXML
	void gotoComet2EditMode(ActionEvent event) {
		preTrans();
		coec.setEditMode();
		screenPage.setScreen(EditModeScene.DEBUG);
	}

	@Override
	public void setScreenParent(ScreensController<EditModeScene> screenPage) {
		this.screenPage = screenPage;
	}

	@Override // This method is called by the FXMLLoader when initialization is complete
	public void initialize(URL location, ResourceBundle resources) {
		assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'Root.fxml'.";
	}

	private ScreensController<EditModeScene> screenPage;
	private Stage stage;

	private void preTrans(){
		stage.setResizable(true);
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setCOEC(DebugModeController COEC) {
		this.coec = COEC;
	}
}
