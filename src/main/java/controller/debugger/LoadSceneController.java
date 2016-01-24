package controller.debugger;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.controlsfx.glyphfont.FontAwesome;

import static controller.GraphicCreator.*;
/**
 * @author 14r4113 on 2016/01/08.
 */
public class LoadSceneController implements Initializable,DebugControllable{

	@FXML
	public VBox medBox;
	@FXML
	public Button loadBut;

	@FXML
	private Label startAdrLbl;
	@FXML
	private Label fileLbl;

	@FXML
	private Label memBox;

	@FXML
	private Label loadBox;

	@FXML
	private Label sizeLbl;

	@FXML
	private SpreadsheetView loadSheet;

	@FXML
	private SpreadsheetView memSheet;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert startAdrLbl != null : "fx:id=\"startAdrLbl\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert fileLbl != null : "fx:id=\"fileLbl\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert memBox != null : "fx:id=\"memBox\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert loadBox != null : "fx:id=\"loadBox\" was not injected: check your FXML file 'LoadScene.fxml'.";
		assert sizeLbl != null : "fx:id=\"sizeLbl\" was not injected: check your FXML file 'LoadScene.fxml'.";
		loadBox.setGraphic(createEffectIcon(FontAwesome.Glyph.FILE).size(24.0));
		memBox.setGraphic(createEffectIcon(FontAwesome.Glyph.TABLE).size(24.0));
		loadBox.setContentDisplay(ContentDisplay.LEFT);
		memBox.setContentDisplay(ContentDisplay.LEFT);
		int rowCount = 1;
		int columnCount = 1;
		GridBase grid = new GridBase(rowCount, columnCount);
		//grid.getColumnHeaders().add("Memory");
		ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
		for (int row = 0; row < grid.getRowCount(); ++row) {
			final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
			for (int column = 0; column < grid.getColumnCount(); ++column) {
				SpreadsheetCell c = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, "");
				grid.getRowHeaders().add(Integer.toString(row));
				//これ！
				//mo.bindModel(row, c.itemProperty(),MachineObserver.Comp.MEM);
				list.add(c);
			}
			rows.add(list);
		}
		grid.setRows(rows);
		memSheet.setGrid(grid);

		rowCount = 1;
		columnCount = 1;
		GridBase grid1 = new GridBase(rowCount, columnCount);
		ObservableList<ObservableList<SpreadsheetCell>> rows1 = FXCollections.observableArrayList();
		for (int row = 0; row < grid1.getRowCount(); ++row) {
			final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
			for (int column = 0; column < grid1.getColumnCount(); ++column) {
				SpreadsheetCell c = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, "");
				grid1.getRowHeaders().add(Integer.toString(row));
				//これ！
				//mo.bindModel(row, c.itemProperty(),MachineObserver.Comp.MEM);
				list.add(c);
			}
			rows1.add(list);
		}
		grid1.setRows(rows1);
		loadSheet.setGrid(grid1);

		memSheet.setRowHeaderWidth(60);
		memSheet.getColumns().get(0).setPrefWidth(memSheet.getWidth()-75.0);
		memSheet.widthProperty().addListener(e->{
			memSheet.getColumns().get(0).setPrefWidth(memSheet.getWidth()-75.0);
		});
		loadSheet.setRowHeaderWidth(120);
		loadSheet.getColumns().get(0).setPrefWidth(loadSheet.getWidth()-122.0);
		loadSheet.widthProperty().addListener(e->{
			loadSheet.getColumns().get(0).setPrefWidth(loadSheet.getWidth()-122.0);
		});

	}

	@Override
	public void setViewModel(CommonViewModel cvm) {

	}
}
