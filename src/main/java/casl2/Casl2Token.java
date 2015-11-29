package casl2;

import assembler.Token;

/**
 * Created by Haruki on 2015/11/29.
 */
public class Casl2Token implements Token{
    private Casl2Symbol symbol;
    private String token;

    public Casl2Token(Casl2Symbol symbol,String token) {
        this.symbol = symbol;
        this.token = token;
    }

    @Override
    public Casl2Symbol getSymbol() {
        return symbol;
    }

    @Override
    public String getToken() {
        return token;
    }
}
