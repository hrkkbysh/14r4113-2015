package casl2;


import assembler.Symbol;

public enum Casl2Symbol implements Symbol {
	MACHINEINST,MACROINST,ASSEMBLERINST,LABEL,
	REGISTER,EOL,
	NUM_CONST,STR_CONST,EQUAL,
	COMMA,EOF,ERROR;
}