package controller.debugger;

import com.jfoenix.controls.JFXComboBox;
import comet2casl2.MachineObserver;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

public class CommonViewModel {
	private SpreadsheetView memSheet,regSheet,frSheet,loadSheet,loadMemSheet,traceSheet,profileSheet,traceTarget,labelSheet;
	private JFXComboBox<String> locSelecter,loadLocSelecter;
	private final String[] regName = {"PR","SP","GR0","GR1","GR2","GR3","GR4","GR5","GR6","GR7",};
	private final String[] frName = {"OF","SF","ZF"};
	private Menu windowMenu;
	private TabPane watchPane;
	private TextArea inTa,outTa;
	private MachineObserver mo;
	private BooleanProperty traceVar,varWindow,breakSub,breakLabel;
	public void stopAction(){}
	public void pauseAction(){}
	public void runAction(){}
	public void backAction(){}
	public void stepOverAction(){}
	public void stepInAction(){}
	public void stepOutAction(){}
	public void runtoCurAction(){}
	public void showBPAction(){}
	public void evExAction(){}
	public void watchWindowAction(){}

	private GridBase genGrid(int rowCount,int columnCount,MachineObserver.Bind bindType){
		GridBase grid = new GridBase(rowCount, columnCount);
		ObservableList<ObservableList<SpreadsheetCell>> rows2 = FXCollections.observableArrayList();
		for (int row = 0; row < grid.getRowCount(); ++row) {
			final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
			grid.getRowHeaders().add(regName[row]);
			for (int column = 0; column < grid.getColumnCount(); ++column) {
				SpreadsheetCell c = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, "");
				MachineObserver.bindModel(row, c.itemProperty(), bindType);
				list.add(c);
			}
			rows2.add(list);
		}
		grid.setRows(rows2);
		return grid;
	}

	public void bindTraceVar(BooleanProperty traceVar) {
		//this.traceVar.bind(traceVar);
	}

	public void bindVarWindow(BooleanProperty varWindow) {
		//this.varWindow.bind(varWindow);
	}

	public void bindBreakSub(BooleanProperty breakSub) {
		//this.breakSub.bind(breakSub);
	}

	public void bindBreakLabel(BooleanProperty breakLabel) {
		//this.breakLabel.bind(breakLabel);
	}

	public void setMemSheet(SpreadsheetView memSheet) {
		this.memSheet = memSheet;
	}

	public void setRegSheet(SpreadsheetView regSheet) {
		this.regSheet = regSheet;
	}

	public void setFrSheet(SpreadsheetView frSheet) {
		this.frSheet = frSheet;
	}

	public void setLoadSheet(SpreadsheetView loadSheet) {
		this.loadSheet = loadSheet;
	}

	public void setLoadMemSheet(SpreadsheetView loadMemSheet) {
		this.loadMemSheet = loadMemSheet;
	}

	public void setTraceSheet(SpreadsheetView traceSheet) {
		this.traceSheet = traceSheet;
	}

	public void setProfileSheet(SpreadsheetView profileSheet) {
		this.profileSheet = profileSheet;
	}

	public void setTraceTarget(SpreadsheetView traceTarget) {
		this.traceTarget = traceTarget;
	}

	public void setLabelSheet(SpreadsheetView labelSheet) {
		this.labelSheet = labelSheet;
	}

	public void setLocSelecter(JFXComboBox<String> locSelecter) {
		this.locSelecter = locSelecter;
	}

	public void setLoadLocSelecter(JFXComboBox<String> loadLocSelecter) {
		this.loadLocSelecter = loadLocSelecter;
	}

	public void setWindowMenu(Menu windowMenu) {
		this.windowMenu = windowMenu;
	}

	public void setWatchPane(TabPane watchPane) {
		this.watchPane = watchPane;
	}

	public void setInTa(TextArea inTa) {
		this.inTa = inTa;
	}

	public void setOutTa(TextArea outTa) {
		this.outTa = outTa;
	}

	public void setMo(MachineObserver mo) {
		this.mo = mo;
	}

	public void setSimulateMode() {
	}

	public void setEditMode() {

	}
}
