package assembler;

import casl2.Casl2Symbol;

public interface Token {

	Casl2Symbol getSymbol();

	void setSymbol(Casl2Symbol cs);

	String getToken();

	int getLine();

}