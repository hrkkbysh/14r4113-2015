package controller.debugger;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

/**
 * @author 14r4113 on 2016/01/08.
 */
public class LogSceneController implements Initializable{

/*	@FXML
	public SpreadsheetView profileSheet;
	@FXML
	public SpreadsheetView traceSheet;*/
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TabPane logPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert logPane != null : "fx:id=\"logPane\" was not injected: check your FXML file 'LogScene.fxml'.";
		/*int rowCount = 1;
		int columnCount = 3;
		GridBase grid = new GridBase(rowCount, columnCount);
		grid.getColumnHeaders().addAll("行(番地)","命令","呼び出し回数");
		ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
		for (int row = 0; row < grid.getRowCount(); ++row) {
			final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
			for (int column = 0; column < grid.getColumnCount(); ++column) {
				SpreadsheetCell c = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, "");
				grid.getRowHeaders().add(Integer.toString(row));
				list.add(c);
			}
			rows.add(list);
		}
		grid.setRows(rows);
		profileSheet.setGrid(grid);
	*//*	profileSheet.resizeRowsToFitContent();*//*

		GridBase grid1 = new GridBase(rowCount, columnCount);
		grid1.getColumnHeaders().addAll("行(番地)","命令","分岐の説明");
		ObservableList<ObservableList<SpreadsheetCell>> rows1 = FXCollections.observableArrayList();
		for (int row = 0; row < grid1.getRowCount(); ++row) {
			final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
			for (int column = 0; column < grid1.getColumnCount(); ++column) {
				SpreadsheetCell c = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, "");
				grid1.getRowHeaders().add(Integer.toString(row));
				list.add(c);
			}
			rows1.add(list);
		}
		grid1.setRows(rows1);
		traceSheet.setGrid(grid1);
	*//*	traceSheet.resizeRowsToFitContent();*/
	}
}

