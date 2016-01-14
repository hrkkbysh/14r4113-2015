package controller.debugger;

/**
 * @author 14r4113 on 2016/01/08.
 */

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import comet2casl2.MachineObserver;
import controller.DebugModeController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

public class VisL1SceneController implements Initializable{

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private GridPane regGrid;

	@FXML
	private GridPane memGrid;

	@FXML
	private GridPane memHeader;

	@FXML
	private GridPane regHeader;

	@FXML
	private AnchorPane regPane;

	@FXML
	private SpreadsheetView memSheet;

	@FXML
	private SpreadsheetView regSheet;

	private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert regGrid != null : "fx:id=\"regGrid\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert memGrid != null : "fx:id=\"memGrid\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert memHeader != null : "fx:id=\"memHeader\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert regHeader != null : "fx:id=\"regHeader\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert regPane != null : "fx:id=\"regPane\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		HBox hBox1 = new HBox();
		hBox1.getChildren().add(fontAwesome.create(FontAwesome.Glyph.TABLE).size(16.0));
		Label l1 = new Label("Memory");
		l1.setPadding(new Insets(0.0,10.0,0.0,10.0));
		hBox1.getChildren().add(l1);
		memGrid.add(hBox1, 0, 0);

		JFXComboBox<Label> jfxCombo = new JFXComboBox<>();
		jfxCombo.getItems().add(new Label("#1000"));
		jfxCombo.getItems().add(new Label("#2000"));
		jfxCombo.getItems().add(new Label("#3000"));
		jfxCombo.getItems().add(new Label("#4000"));

		jfxCombo.setEditable(true);
		jfxCombo.setPromptText("Select Label");
		hBox1.getChildren().add(jfxCombo);
		//memGrid.add(new VBox(jfxCombo),1,0);

		HBox hBox2 = new HBox();
		hBox2.getChildren().add(fontAwesome.create(FontAwesome.Glyph.TABLE).size(16.0));
		Label l2 = new Label("Register");
		l2.setPadding(new Insets(0.0,10.0,0.0,10.0));
		hBox2.getChildren().add(l2);
		regGrid.add(hBox2, 0, 0);

		int rowCount = 65535;
		int columnCount = 1;
		GridBase grid = new GridBase(rowCount, columnCount);
		grid.getRowHeaders().add("Memory");
		ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
		for (int row = 0; row < grid.getRowCount(); ++row) {
			final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
			for (int column = 0; column < grid.getColumnCount(); ++column) {
				SpreadsheetCell c = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, "?");
				//これ！
				mo.bindModel(row, c.itemProperty(),MachineObserver.Comp.MEM);
				list.add(c);
			}
			rows.add(list);
		}
		grid.setRows(rows);
		memSheet.setGrid(grid);
		memSheet.setRowHeaderWidth(60);
		memSheet.setShowColumnHeader(false);
		memSheet.getColumns().get(0).setPrefWidth(memSheet.getWidth()-70.0);
		memSheet.widthProperty().addListener(e->{
			memSheet.getColumns().get(0).setPrefWidth(memSheet.getWidth()-70.0);
		});
		rowCount = 10;
		columnCount = 2;
		GridBase grid2 = new GridBase(rowCount, columnCount);
		ObservableList<ObservableList<SpreadsheetCell>> rows2 = FXCollections.observableArrayList();
		for (String aRegName : regName) {
			grid2.getRowHeaders().add(aRegName);
		}

		for (int row = 0; row < grid2.getRowCount(); ++row) {
			final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
			for (int column = 0; column < grid.getColumnCount(); ++column) {
				SpreadsheetCell c = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, "?");
				mo.bindModel(row, c.itemProperty(), MachineObserver.Comp.REG);
				//mo.bindRegister(row, c.itemProperty());
				list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,""));
			}
			rows2.add(list);
		}

		grid2.setRows(rows2);
		regSheet.setGrid(grid2);
		regSheet.setShowColumnHeader(false);
		regSheet.setRowHeaderWidth(60);
		regSheet.setShowColumnHeader(false);
		regSheet.getColumns().get(0).setPrefWidth(regSheet.getWidth()-70.0);
		regSheet.widthProperty().addListener(e->{
			regSheet.getColumns().get(0).setPrefWidth(regSheet.getWidth()-70.0);
		});
	}

	private final String[] regName = {"PR","SP","GR0","GR1","GR2","GR3","GR4","GR5","GR6","GR7",};

	private MachineObserver mo;
	public void setRootAndInit(DebugModeController rootAndInit) {
		this.dmc = rootAndInit;
	}
	DebugModeController dmc;

	public void initSimState(){
		//mo.setMemInfo(grid.getRowHeaders());
	}
}

