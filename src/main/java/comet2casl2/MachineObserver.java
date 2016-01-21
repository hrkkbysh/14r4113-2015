package comet2casl2;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author 14r4113 on 2016/01/14.
 */
public class MachineObserver {

	AddressSpace as;
	Register reg;
	SymbolTable symTbl;
	ErrorTable errorTable;
	Comet2InstructionTable instTable;

	public MachineObserver(int loadAdr,int spAdr){
		as = new AddressSpace(loadAdr,spAdr);
		reg = new Register();
		symTbl = new SymbolTable();
		reg.setSp(spAdr);
	}
	public boolean bindModel(int loc, ObjectProperty<Object> item, Bind comp){
		return comp.bindData(loc,item);
	}

	public abstract class Bind{
		public final Bind MEM = new Bind(){
			@Override
			boolean bindData(int loc, ObjectProperty<Object> item) {
				item.bind(as.readA(loc).contentProperty());
				return false;
			}
		};
		public final Bind REG = new Bind(){
			@Override
			boolean bindData(int id, ObjectProperty<Object> item) {
				item.bind(reg.getRegister(id).contentProperty());
				return false;
			}
		};
		public final Bind LOAD = new Bind(){//FIXME
			@Override
			boolean bindData(int loc, ObjectProperty<Object> item) {
				item.bind(as.readA(loc).contentProperty());
				return false;
			}
		};
		public final Bind FR = new Bind(){//FIXME
			@Override
			boolean bindData(int id, ObjectProperty<Object> item) {
				item.bind(reg.getRegister(id).contentProperty());
				return false;
			}
		};
		abstract boolean bindData(int loc, ObjectProperty<Object> item);
	}
}
