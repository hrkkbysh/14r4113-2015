package controller.debugger;

import com.jfoenix.controls.JFXComboBox;
import comet2casl2.MachineObserver;
import javafx.scene.control.Menu;
import javafx.scene.control.TabPane;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

/**
 * Created by WadaLabS on 2016/01/21.
 */
public class CommonViewModel {
    private SpreadsheetView memSheet,regSheet,frSheet,loadSheet,traceSheet,profileSheet,traceTarget,labelSheet;
    private JFXComboBox<String> locSelecter,loadLocSelecter;
    private final String[] regName = {"PR","SP","GR0","GR1","GR2","GR3","GR4","GR5","GR6","GR7",};
    private final String[] frName = {"OF","SF","ZF"};
    private Menu windowMenu;
    private TabPane watchPane;
    private MachineObserver mo;

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
    public void breakLabelAction(){}
    public void breakSubAction(){}
    public void varWindonAction(){}
    public void traceVarAction(){}
    public void watchWindowAction(){}

}
