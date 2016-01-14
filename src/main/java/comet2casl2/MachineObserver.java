package comet2casl2;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author 14r4113 on 2016/01/14.
 */
public class MachineObserver {

	static AddressSpace as;
	static Register reg;
	static SymbolTable symTbl;
	public static ErrorTable errorTable;
	public static Comet2InstructionTable instTable;

	MachineObserver(int loadAdr,int spAdr){
		as = new AddressSpace(loadAdr,spAdr);
		reg = new Register();
		symTbl = new SymbolTable();
		reg.setSp(spAdr);
	}
	public boolean bindModel(int loc, ObjectProperty<Object> item, Comp comp){
		return comp.bindData(loc,item);
	}
	public boolean bindLoadModel(int loc, SimpleStringProperty item){
		return false;
	}

	public enum Comp{
		MEM(){
			@Override
			boolean bindData(int loc, ObjectProperty<Object> item) {
				item.bind(as.readA(loc).contentProperty());
				return false;
			}
		},REG{
			@Override
			boolean bindData(int id, ObjectProperty<Object> item) {
				item.bind(reg.getRegister(id).contentProperty());
				return false;
			}
		},LOAD{//FIXME
			@Override
			boolean bindData(int loc, ObjectProperty<Object> item) {
				item.bind(as.readA(loc).contentProperty());
				return false;
			}
		},FR{//FIXME
			@Override
			boolean bindData(int id, ObjectProperty<Object> item) {
				item.bind(reg.getRegister(id).contentProperty());
				return false;
			}
		};
		abstract boolean bindData(int loc, ObjectProperty<Object> item);
	}
}
