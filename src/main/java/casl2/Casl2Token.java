package casl2;


import assembler.Token;

public class Casl2Token implements Token{
	private Casl2Symbol symbol;
	private final String token;
	private final int line;
	public Casl2Token(Casl2Symbol symbol, String token,int line) {
		this.symbol = symbol;
		this.token = token;
		this.line = line;
	}
	/* (非 Javadoc)
	 * @see hosei.wadalab.compiler.casl2.Token#getSymbol()
	 */
	@Override
	public Casl2Symbol getSymbol() {
		return symbol;
	}
	/* (非 Javadoc)
	 * @see hosei.wadalab.compiler.casl2.Token#setSymbol(hosei.wadalab.compiler.casl2.Casl2Symbol)
	 */
	@Override
	public void setSymbol(Casl2Symbol cs){
		this.symbol =cs;
	}
	/* (非 Javadoc)
	 * @see hosei.wadalab.compiler.casl2.Token#getToken()
	 */
	@Override
	public String getToken() {
		return token;
	}
	/* (非 Javadoc)
	 * @see hosei.wadalab.compiler.casl2.Token#getLine()
	 */
	@Override
	public int getLine() {
		return line;
	}

	public static Token newInstance(Token origin){
		Token copy = new Casl2Token(origin.getSymbol(),origin.getToken(),origin.getLine());
		return copy;
	}
}
