package de.lego.gottfried.decdat.token;

public class TokenArray extends TokenSymbolParam {
	public int	offset;

	public TokenArray(TokenEnum tok, int param, int flag) {
		super(tok, param);
		offset = flag;
	}
}
