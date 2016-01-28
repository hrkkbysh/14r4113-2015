package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;

import static uicomponent.GraphicCreator.*;

public class RootController implements Initializable, Controllable<EditModeScene> {
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button objButton;

	@FXML
	private Button asmButton;

	@Override // This method is called by the FXMLLoader when initialization is complete
	public void initialize(URL location, ResourceBundle resources) {
		assert objButton != null : "fx:id=\"objButton\" was not injected: check your FXML file 'RootScene.fxml'.";
		assert asmButton != null : "fx:id=\"asmButton\" was not injected: check your FXML file 'RootScene.fxml'.";

		asmButton.setText("アセンブリ言語プログラミング");
		objButton.setText("機械語プログラミング");

		asmButton.setGraphic(createEffectIcon(FontAwesome.Glyph.FILE_TEXT_ALT).size(150.0));
		objButton.setGraphic(createEffectIcon(FontAwesome.Glyph.LAPTOP).size(150.0));
		asmButton.setOnAction(e->gotoCasl2EditMode());
		objButton.setOnAction(e->gotoComet2EditMode());

	}

	private ScreensController<EditModeScene> screenPage;
	private Stage stage;
	private DebugModeController coec;

	@Override
	public void setScreenParent(ScreensController<EditModeScene> screenPage) {
		this.screenPage = screenPage;
	}


	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setCOEC(DebugModeController COEC) {
		this.coec = COEC;
	}

	void gotoCasl2EditMode() {
		screenPage.setScreen(EditModeScene.CASL2_EDIT);
	}

	void gotoComet2EditMode() {
		coec.setEditMode();
		screenPage.setScreen(EditModeScene.DEBUG);
	}
}
