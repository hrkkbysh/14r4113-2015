package controller.debugger;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
/**
 * @author 14r4113 on 2016/01/08.
 */
public class LogSceneController implements Initializable{

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TabPane logPane;

	@FXML
	private TableView<?> traceTableView;

	@FXML
	private TableView<?> profileTableView1;

	@FXML
	private TableColumn<?, ?> pAddressColumn1;

	@FXML
	private TableColumn<?, ?> exeCountColumn1;

	@FXML
	private TableColumn<?, ?> pInstColumn1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert logPane != null : "fx:id=\"logPane\" was not injected: check your FXML file 'LogScene.fxml'.";
		assert traceTableView != null : "fx:id=\"traceTableView\" was not injected: check your FXML file 'LogScene.fxml'.";
		assert profileTableView1 != null : "fx:id=\"profileTableView1\" was not injected: check your FXML file 'LogScene.fxml'.";
		assert pAddressColumn1 != null : "fx:id=\"pAddressColumn1\" was not injected: check your FXML file 'LogScene.fxml'.";
		assert exeCountColumn1 != null : "fx:id=\"exeCountColumn1\" was not injected: check your FXML file 'LogScene.fxml'.";
		assert pInstColumn1 != null : "fx:id=\"pInstColumn1\" was not injected: check your FXML file 'LogScene.fxml'.";
	}
}

