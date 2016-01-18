package controller.debugger;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

/**
 * @author 14r4113 on 2016/01/18.
 */
public class CommonComponent {
    private static SpreadsheetView memSheet,regSheet,frSheet,loadSheet,traceSheet,profileSheet,traceTarget,labelSheet;
    private static JFXComboBox<String> locSelecter,loadLocSelecter;
    private Menu windowMenu;
    private TabPane watchPane;
    static{
        int rowCount = 65536;
        int columnCount = 1;
        GridBase grid = new GridBase(rowCount, columnCount);
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

        locSelecter.getC3DEditor().setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
				/*String text = "Key Typed: " + ke.getCharacter();
				if (ke.isAltDown()) {
					text += " , alt down";
				}
				if (ke.isControlDown()) {
					text += " , ctrl down";
				}
				if (ke.isMetaDown()) {
					text += " , meta down";
				}
				if (ke.isShiftDown()) {
					text += " , shift down";
				}
				if(ke.getCode().isWhitespaceKey()){
					text +=", enter down";
				}
				System.out.println(text);*/
                if(event.getCharacter().equals("\r")){
                    //grid.getRows().get(Integer.parseInt(locSelecter.getC3DEditor().getText()));
                    memSheet.scrollToRow(Integer.parseInt(locSelecter.getC3DEditor().getText()));
                }
            }
        });
    }
}
