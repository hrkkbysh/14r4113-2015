package controller.debugger;

/**
 * @author 14r4113 on 2016/01/08.
 */
import java.net.URL;
import java.util.BitSet;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import org.controlsfx.control.spreadsheet.*;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import static uicomponent.GraphicCreator.createEffectIcon;

public class VisL1SceneController implements Initializable,DebugControllable{

	public SpreadsheetView traceTargetSheet;
	@FXML
	private GridPane memGrid;

	@FXML
	private GridPane regHeader;

	@FXML
	private AnchorPane otherPane;

	@FXML
	private SpreadsheetView memSheet;

	@FXML
	private SpreadsheetView regSheet;

	@FXML
	private SpreadsheetView frSheet;

	@FXML
	private TabPane watchPane;

	@FXML
	private HBox memHeader;

	@FXML
	private Tab inTb;

	@FXML
	private TextArea inTa;

	@FXML
	private Tab outTb;

	@FXML
	private AnchorPane outPn;

	@FXML
	private Tab traceTb;

	@FXML
	private AnchorPane tPn;

	@FXML
	private Tab lblTb;

	@FXML
	private AnchorPane lblPn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert memGrid != null : "fx:id=\"memGrid\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert regHeader != null : "fx:id=\"regHeader\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert regSheet != null : "fx:id=\"regSheet\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert frSheet != null : "fx:id=\"frSheet\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert memSheet!= null : "fx:id=\"memSheet\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert watchPane != null : "fx:id=\"watchPane\" was not injected: check your FXML file 'VisL1Scene.fxml'.";
		assert inTb != null : "fx:id=\"inTb\" was not injected: check your FXML file 'test.fxml'.";
		assert inTa != null : "fx:id=\"inTa\" was not injected: check your FXML file 'test.fxml'.";
		assert outTb != null : "fx:id=\"outTb\" was not injected: check your FXML file 'test.fxml'.";
		assert outPn != null : "fx:id=\"outPn\" was not injected: check your FXML file 'test.fxml'.";
		assert traceTb != null : "fx:id=\"traceTb\" was not injected: check your FXML file 'test.fxml'.";
		assert tPn != null : "fx:id=\"tPn\" was not injected: check your FXML file 'test.fxml'.";
		assert lblTb != null : "fx:id=\"lblTb\" was not injected: check your FXML file 'test.fxml'.";
		assert lblPn != null : "fx:id=\"lblPn\" was not injected: check your FXML file 'test.fxml'.";

		Label l1 = new Label("Memory");
		l1.setPadding(new Insets(2.5,10.0,0.0,10.0));

		JFXComboBox<Label> jfxCombo = new JFXComboBox<>();
		jfxCombo.getItems().addAll(new Label("#1000"),new Label("#3000"),new Label("#5000"),
				new Label("#7000"),new Label("#9000"),new Label("#B000"),
				new Label("#D000"),new Label("#F000"));

		jfxCombo.setEditable(true);
		jfxCombo.setPromptText("Select Label or Location");
		jfxCombo.setPrefWidth(180.0);
		Glyph g = createEffectIcon(FontAwesome.Glyph.TABLE).size(16.0);
		g.setPadding(new Insets(0.0,0.0,10.0,0.0));
		memHeader.getChildren().addAll(g,l1,jfxCombo);

		HBox hBox2 = new HBox();
		hBox2.getChildren().add(createEffectIcon(FontAwesome.Glyph.TABLE).size(16.0));
		Label l2 = new Label("Register , I/O.");
		l2.setPadding(new Insets(0.0,10.0,0.0,10.0));
		hBox2.getChildren().add(l2);
		regHeader.add(hBox2, 0, 0);

		int rowCount = 65536;
		int columnCount = 1;
		GridBase grid = new GridBase(rowCount, columnCount);
		grid.getColumnHeaders().add("Memory");
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
		memSheet.setRowHeaderWidth(60);
		memSheet.getColumns().get(0).setPrefWidth(memSheet.getWidth()-75.0);
		memSheet.widthProperty().addListener(e->{
			memSheet.getColumns().get(0).setPrefWidth(memSheet.getWidth()-75.0);
		});

		jfxCombo.getC3DEditor().setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				/*String text = "Key Typed: " + ke.getCharacter();*/
				if(event.getCharacter().equals("\r")){
					//grid.getRows().get(Integer.parseInt(jfxCombo.getC3DEditor().getText()));
					memSheet.scrollToRow(Integer.parseInt(jfxCombo.getC3DEditor().getText()));
				}
			}
		});
		rowCount = 10;
		columnCount = 2;
		GridBase grid2 = new GridBase(rowCount, columnCount);
		ObservableList<ObservableList<SpreadsheetCell>> rows2 = FXCollections.observableArrayList();
		for (int row = 0; row < grid2.getRowCount(); ++row) {
			final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
			grid2.getRowHeaders().add(regName[row]);
			for (int column = 0; column < grid.getColumnCount(); ++column) {
				SpreadsheetCell c = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, "");
				SpreadsheetCellEditor s = SpreadsheetCellType.OBJECT.createEditor(regSheet);
				//mo.bindModel(row, c.itemProperty(), MachineObserver.Comp.REG);
				//mo.bindRegister(row, c.itemProperty());
				list.add(c);
			}
			rows2.add(list);
		}
		grid2.setRows(rows2);
		regSheet.setRowHeaderWidth(60);
		regSheet.getColumns().get(0).setPrefWidth(regSheet.getWidth()-62.0);
		regSheet.widthProperty().addListener(e->{
			regSheet.getColumns().get(0).setPrefWidth(regSheet.getWidth()-62.0);
		});
		regSheet.setGrid(grid2);

		rowCount = 1;
		columnCount = 3;
		GridBase grid3 = new GridBase(rowCount, columnCount);
		ObservableList<ObservableList<SpreadsheetCell>> rows3 = FXCollections.observableArrayList();
		for(int i = 0;i<3;i++){
			grid3.getColumnHeaders().add(frName[i]);
		}
		for (int row = 0; row < grid3.getRowCount(); ++row) {
			grid3.getRowHeaders().add("FR");
			final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
			for (int column = 0; column < grid3.getColumnCount(); ++column) {
				SpreadsheetCell c = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, "?");
				//mo.bindModel(row, c.itemProperty(), MachineObserver.Comp.REG);
				//mo.bindRegister(row, c.itemProperty());
				list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,""));
				//SpreadsheetCellType.STRING.acceptDrop();
			}
			rows3.add(list);
		}
		//grid3.setLocked(true);
		grid3.setRows(rows3);
		frSheet.setRowHeaderWidth(60);
		for(int i=0;i<3;i++){
			frSheet.getColumns().get(i).setPrefWidth((frSheet.getWidth()-65.0)/3);
		}

		frSheet.getColumns().get(0).setResizable(false);
		frSheet.getColumns().get(1).setResizable(false);
		frSheet.getColumns().get(2).setResizable(false);
		grid3.setLocked(true);
		frSheet.widthProperty().addListener(e->{
			frSheet.getColumns().get(0).setPrefWidth((frSheet.getWidth()-65.0)/3);
		});
		frSheet.widthProperty().addListener(e->{
			frSheet.getColumns().get(1).setPrefWidth((frSheet.getWidth()-65.0)/3);
		});
		frSheet.widthProperty().addListener(e->{
			frSheet.getColumns().get(2).setPrefWidth((frSheet.getWidth()-62.0)/3);
		});
		frSheet.setGrid(grid3);
		watchPane.setCursor(Cursor.CLOSED_HAND);

		grid.setResizableRows(new BitSet());
		grid2.setResizableRows(new BitSet());
		grid3.setResizableRows(new BitSet());

		rowCount = 3;
		columnCount = 2;
		GridBase grid4 = new GridBase(rowCount, columnCount);
		ObservableList<ObservableList<SpreadsheetCell>> rows4 = FXCollections.observableArrayList();
		for (int row = 0; row < grid4.getRowCount(); ++row) {
			final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
			for (int column = 0; column < grid4.getColumnCount(); ++column) {
				//SpreadsheetCell c = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, "?");
				//mo.bindModel(row, c.itemProperty(), MachineObserver.Comp.REG);
				//mo.bindRegister(row, c.itemProperty());
				list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,""));
			}
			rows4.add(list);
		}
		grid4.setRows(rows4);
		traceTargetSheet.setGrid(grid4);

		l1.setOnDragDetected(event -> {
			System.out.println("l1 drag detected!");
			// drag was detected, start drag-and-drop gesture
		/*	//TablePosition selected = memSheet.getSelectionModel().getFocusedCell();
			if(selected !=null){
			}*/
		});
		/*memSheet.getSelectionModel().getSelectedCells().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				TablePosition selected = memSheet.getSelectionModel().getFocusedCell();
				Object item = memSheet.getGrid().getRows().get(selected.getRow()).get(selected.getColumn()).getItem();
				System.out.println(i++ +"clicked:"+item.toString());
			}
		});*/
		/*これ！*/
		memSheet.setOnMouseDragEntered(event1 -> {
			System.out.print("mem mouse drag entered detected!");
			ObservableList<TablePosition> selectedItems = memSheet.getSelectionModel().getSelectedCells();
			for(TablePosition selectedItem:selectedItems) {
				Object item = memSheet.getGrid().getRows().get(selectedItem.getRow()).get(selectedItem.getColumn()).getItem();
				if (item != null) {
					System.out.println("clicked:" + item.toString());
					System.out.print(", row:" + selectedItem.getRow());
					System.out.println(", column:" + selectedItem.getColumn());
				}
			}
		});
		//memSheet.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
/*

		memSheet.setOnDragDetected(event1 ->  System.out.println("mem drag detected!"));
		memSheet.setOnDragDone(event1 ->  System.out.println("mem drag done detected!"));
		memSheet.setOnDragDropped(event1 ->  System.out.println("mem drag dropped detected!"));
		memSheet.setOnDragEntered(event1 ->  System.out.println("mem drag entered detected!"));
		memSheet.setOnDragExited(event1 ->  System.out.println("mem drag exited detected!"));
		memSheet.setOnDragOver(event1 ->  System.out.println("mem drag over detected!"));
		memSheet.setOnMouseClicked(event1 ->  System.out.println("mem mouse clicked detected!"));
		memSheet.setOnMouseDragEntered(event1 -> System.out.println("mem mouse drag entered detected!"));
		memSheet.setOnMouseDragExited(event1 ->  System.out.println("mem mouse drag exited detected!"));
		memSheet.setOnMouseDragged(event1 -> System.out.println("mem mouse drag detected!"));
		memSheet.setOnMouseDragOver(event1 -> System.out.println("mem mouse drag over detected!"));
		memSheet.setOnMouseDragReleased(event1 -> System.out.println("mem mouse drag released detected!"));
		memSheet.setOnMouseEntered(event1 -> System.out.println("mem mouse entered detected!"));
		memSheet.setOnMouseExited(event1 -> System.out.println("mem mouse exited detected!"));
		memSheet.setOnMouseMoved(event1 -> System.out.println("mem mouse moved detected!"));
		memSheet.setOnMousePressed(event1 -> System.out.println("mem mouse pressed detected!"));
		memSheet.setOnMouseReleased(event1 -> System.out.println("mem mouse released detected!"));
*/
		/*memSheet.setOnDragDetected(event -> {
			System.out.println("mem drag detected!");
			// drag was detected, start drag-and-drop gesture
			TablePosition selected = memSheet.getSelectionModel().getFocusedCell();
			if(selected !=null){
				Dragboard db = memSheet.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				Object item = memSheet.getGrid().getRows().get(selected.getRow()).get(selected.getColumn()).getItem();
				content.putString(item.toString());
				db.setContent(content);
				event.consume();
			}
		});*/
/*
		traceTargetSheet.setOnDragOver(event -> {
			// data is dragged over the target
			Dragboard db = event.getDragboard();
			if (event.getDragboard().hasString()){
				event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			}
			event.consume();
		});

		traceTargetSheet.setOnDragExited(Event::consume);
		traceTargetSheet.setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if (db.hasString()) {
				String text = db.getString();
				Grid grid1 = traceTargetSheet.getGrid();
				final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
				list.add(SpreadsheetCellType.STRING.createCell(grid1.getRowCount(), grid1.getColumnCount(), 1, 1,text));
				grid1.getRows().add(list);
				traceTargetSheet.setGrid(grid1);
				success = true;
			}
			event.setDropCompleted(success);
			event.consume();
		});*/

/*		memSheet.setOnDragDone(event -> {
*//* the drag-and-drop gesture ended *//*
			System.out.println("onDragDone");
*//* if the data was successfully moved, clear it *//*
			if (event.getTransferMode() == TransferMode.MOVE) {
				source.setText("");
			}
			event.consume();
		});*/
	}
static int i=0;
	private final String[] regName = {"PR","SP","GR0","GR1","GR2","GR3","GR4","GR5","GR6","GR7",};

	private final String[] frName = {"OF","SF","ZF"};

	@Override
	public void setViewModel(CommonViewModel cvm) {

	}
}

