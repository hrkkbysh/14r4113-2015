package controller.debugger;

/**
 * @author 14r4113 on 2016/01/08.
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
public class VisL1SceneController implements Initializable{

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TableColumn<?, ?> addressCol;

	@FXML
	private TableColumn<?, ?> contentCol;

	@FXML
	private Label gr2Label;

	@FXML
	private TextField prF;

	@FXML
	private Label gr5Label;

	@FXML
	private TextField spF;

	@FXML
	private TextField gr6F;

	@FXML
	private Label prLabel;

	@FXML
	private TextField gr4F;

	@FXML
	private TextField ofF;

	@FXML
	private TextField gr2F;

	@FXML
	private TextField gr1F;

	@FXML
	private TextField sfF;

	@FXML
	private WebView ioField;

	@FXML
	private TextField zfF;

	@FXML
	private TableView<?> memoryTable;

	@FXML
	private Label gr6Label;

	@FXML
	private TextField gr7F;

	@FXML
	private Label gr0Label;

	@FXML
	private TextField gr5F;

	@FXML
	private TextField gr3F;

	@FXML
	private Label gr3Label;

	@FXML
	private Label spLabel;

	@FXML
	private TextField gr0F;

	@FXML
	private Label gr1Label;

	@FXML
	private GridPane memGrid;

	@FXML
	private Label gr4Label;

	@FXML
	private Label gr7Label;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert addressCol != null : "fx:id=\"addressCol\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert contentCol != null : "fx:id=\"contentCol\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert gr2Label != null : "fx:id=\"gr2Label\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert prF != null : "fx:id=\"prF\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert gr5Label != null : "fx:id=\"gr5Label\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert spF != null : "fx:id=\"spF\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert gr6F != null : "fx:id=\"gr6F\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert prLabel != null : "fx:id=\"prLabel\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert gr4F != null : "fx:id=\"gr4F\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert ofF != null : "fx:id=\"ofF\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert gr2F != null : "fx:id=\"gr2F\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert gr1F != null : "fx:id=\"gr1F\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert sfF != null : "fx:id=\"sfF\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert ioField != null : "fx:id=\"ioField\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert zfF != null : "fx:id=\"zfF\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert memoryTable != null : "fx:id=\"memoryTable\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert gr6Label != null : "fx:id=\"gr6Label\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert gr7F != null : "fx:id=\"gr7F\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert gr0Label != null : "fx:id=\"gr0Label\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert gr5F != null : "fx:id=\"gr5F\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert gr3F != null : "fx:id=\"gr3F\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert gr3Label != null : "fx:id=\"gr3Label\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert spLabel != null : "fx:id=\"spLabel\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert gr0F != null : "fx:id=\"gr0F\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert gr1Label != null : "fx:id=\"gr1Label\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert memGrid != null : "fx:id=\"memGrid\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert gr4Label != null : "fx:id=\"gr4Label\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert gr7Label != null : "fx:id=\"gr7Label\" was not injected: check your FXML file 'VisL1Scene.fxml'.";


	}
}

