package controller;

/**
 * @author 14r4113 on 2016/01/13.
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class BPSceneController implements Initializable{

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField bitCountTF;

	@FXML
	private Button inVisibleBut;

	@FXML
	private CheckBox lineCB;

	@FXML
	private AnchorPane root;

	@FXML
	private AnchorPane showDetNode;

	@FXML
	private TextField evExTF;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert bitCountTF != null : "fx:id=\"bitCountTF\" was not injected: check your FXML file 'SetBPScene.fxml'.";
		assert inVisibleBut != null : "fx:id=\"inVisibleBut\" was not injected: check your FXML file 'SetBPScene.fxml'.";
		assert lineCB != null : "fx:id=\"lineCB\" was not injected: check your FXML file 'SetBPScene.fxml'.";
		assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'SetBPScene.fxml'.";
		assert showDetNode != null : "fx:id=\"showDetNode\" was not injected: check your FXML file 'SetBPScene.fxml'.";
		assert evExTF != null : "fx:id=\"evExTF\" was not injected: check your FXML file 'SetBPScene.fxml'.";

	}
}
