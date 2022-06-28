package de.lego.gottfried.decdat.token;

public class TokenIntParam extends Token {
	public int	param;

	public TokenIntParam(TokenEnum tok, int par) {
		super(tok);
		param = par;
	}
}
