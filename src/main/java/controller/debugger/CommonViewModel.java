package controller.debugger;

import com.jfoenix.controls.JFXComboBox;
import comet2casl2.MachineObserver;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import java.util.HashMap;

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
	private ChoiceBox<String> aryMenuButton;
	enum Ary{
		BIN("2進数"),OCT("8進数"),SIGN_DEC("符号無10進数"),NO_SIGN_DEC("10進数"),HEX("16進数"),CHAR("文字(JIS X 0201)");
		private final String text;
		private static HashMap<String,Ary> map = new HashMap<>();
		Ary(String text){
			this.text = text;
		}
		public String getText(){return text;}
		public static Ary toAry(String text){
			return map.get(text);
		}
		static{
			for(Ary a: Ary.values())
				map.put(a.text,a);
		}
	}

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

	public void bindAry(ChoiceBox<String> aryMenuButton) {
		this.aryMenuButton = aryMenuButton;
		ObservableList<String> items = aryMenuButton.getItems();
		for(Ary a: Ary.values()){
			items.add(a.getText());
		}
		aryMenuButton.getSelectionModel().selectFirst();
		aryMenuButton.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {System.out.println(Ary.toAry(newValue));});
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
