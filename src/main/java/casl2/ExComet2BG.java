package casl2;

public class ExComet2BG extends Comet2BG{
	private MacroTable macroTable;

	public ExComet2BG(Comet2InstructionTable instTable, ExSymbolTable symbolTable, ExErrorTable errorTable,MacroTable macroTable) {
		super(instTable,symbolTable,errorTable);
		this.macroTable = macroTable;
	}
}