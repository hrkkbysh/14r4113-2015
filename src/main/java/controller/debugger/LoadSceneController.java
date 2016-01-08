package controller.debugger;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
	private TableColumn<?, ?> memAdressColumn;

	@FXML
	private TableView<?> relocTable;

	@FXML
	private Label startLabel;

	@FXML
	private TableView<?> memTable;

	@FXML
	private TableColumn<?, ?> memContentColumn;

	@FXML
	private TableColumn<?, ?> relocAddressColumn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert relocContentColumn != null : "fx:id=\"relocContentColumn\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert memAdressColumn != null : "fx:id=\"memAdressColumn\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert relocTable != null : "fx:id=\"relocTable\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert startLabel != null : "fx:id=\"startLabel\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert memTable != null : "fx:id=\"memTable\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert memContentColumn != null : "fx:id=\"memContentColumn\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert relocAddressColumn != null : "fx:id=\"relocAddressColumn\" was not injected: check your FXML file 'LoadScene.fxml'.";

	}
}
