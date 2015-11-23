package controller;

/**
 * Sample Skeleton for 'HistoryScene.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class HistoryViewController extends BorderPane implements Initializable{


	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="exeCountColumn"
	private TableColumn<?, ?> exeCountColumn; // Value injected by FXMLLoader

	@FXML // fx:id="traceTableView"
	private TableView<?> traceTableView; // Value injected by FXMLLoader

	@FXML // fx:id="searchField"
	private TextField searchField; // Value injected by FXMLLoader

	@FXML // fx:id="tBranchDescription"
	private TableColumn<?, ?> tBranchDescription; // Value injected by FXMLLoader

	@FXML // fx:id="pAddressColumn"
	private TableColumn<?, ?> pAddressColumn; // Value injected by FXMLLoader

	@FXML // fx:id="pInstColumn"
	private TableColumn<?, ?> pInstColumn; // Value injected by FXMLLoader

	@FXML // fx:id="symbolListBox"
	private ChoiceBox<?> symbolListBox; // Value injected by FXMLLoader

	@FXML // fx:id="profileTableView"
	private TableView<?> profileTableView; // Value injected by FXMLLoader

	@FXML // fx:id="tExecutedAddressColumn"
	private TableColumn<?, ?> tExecutedAddressColumn; // Value injected by FXMLLoader

	@FXML // fx:id="tNoColumn"
	private TableColumn<?, ?> tNoColumn; // Value injected by FXMLLoader

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert exeCountColumn != null : "fx:id=\"exeCountColumn\" was not injected: check your FXML file 'HistoryScene.fxml'.";
		assert traceTableView != null : "fx:id=\"traceTableView\" was not injected: check your FXML file 'HistoryScene.fxml'.";
		assert searchField != null : "fx:id=\"searchField\" was not injected: check your FXML file 'HistoryScene.fxml'.";
		assert tBranchDescription != null : "fx:id=\"tBranchDescription\" was not injected: check your FXML file 'HistoryScene.fxml'.";
		assert pAddressColumn != null : "fx:id=\"pAddressColumn\" was not injected: check your FXML file 'HistoryScene.fxml'.";
		assert pInstColumn != null : "fx:id=\"pInstColumn\" was not injected: check your FXML file 'HistoryScene.fxml'.";
		assert symbolListBox != null : "fx:id=\"symbolListBox\" was not injected: check your FXML file 'HistoryScene.fxml'.";
		assert profileTableView != null : "fx:id=\"profileTableView\" was not injected: check your FXML file 'HistoryScene.fxml'.";
		assert tExecutedAddressColumn != null : "fx:id=\"tExecutedAddressColumn\" was not injected: check your FXML file 'HistoryScene.fxml'.";
		assert tNoColumn != null : "fx:id=\"tNoColumn\" was not injected: check your FXML file 'HistoryScene.fxml'.";
	}
}


