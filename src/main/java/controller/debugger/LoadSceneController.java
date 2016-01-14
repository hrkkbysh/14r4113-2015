package controller.debugger;


import java.net.URL;
import java.util.ResourceBundle;

import comet2casl2.MachineObserver;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.controlsfx.glyphfont.FontAwesome;

import static controller.GraphicCreator.*;
/**
 * @author 14r4113 on 2016/01/08.
 */
public class LoadSceneController implements Initializable{


	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TableColumn<?, ?> relocContentColumn;

	@FXML
	private Label startAdrLbl;

	@FXML
	private TableView<?> relocTable;

	@FXML
	private TableColumn<?, ?> memAdressColumn;

	@FXML
	private Label fileLbl;

	@FXML
	private Label memBox;

	@FXML
	private Label loadBox;

	@FXML
	private Label sizeLbl;

	@FXML
	private TableColumn<?, ?> relocAddressColumn;

	@FXML
	private TableView<?> memTable;

	@FXML
	private TableColumn<?, ?> memContentColumn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert relocContentColumn != null : "fx:id=\"relocContentColumn\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert startAdrLbl != null : "fx:id=\"startAdrLbl\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert relocTable != null : "fx:id=\"relocTable\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert memAdressColumn != null : "fx:id=\"memAdressColumn\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert fileLbl != null : "fx:id=\"fileLbl\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert memBox != null : "fx:id=\"memBox\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert loadBox != null : "fx:id=\"loadBox\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert sizeLbl != null : "fx:id=\"sizeLbl\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert relocAddressColumn != null : "fx:id=\"relocAddressColumn\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert memTable != null : "fx:id=\"memTable\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert memContentColumn != null : "fx:id=\"memContentColumn\" was not injected: check your FXML file 'LoadScene.fxml'.";

		loadBox.setGraphic(createEffectIcon(FontAwesome.Glyph.FILE).size(24.0));
		memBox.setGraphic(createEffectIcon(FontAwesome.Glyph.TABLE).size(24.0));
		loadBox.setContentDisplay(ContentDisplay.LEFT);
		memBox.setContentDisplay(ContentDisplay.LEFT);
	}

	void setSimMode(MachineObserver b){

	}
}
