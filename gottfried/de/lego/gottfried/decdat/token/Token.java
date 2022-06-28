package de.lego.gottfried.decdat.token;


import java.util.ArrayList;

import de.lego.gottfried.decdat.MainForm;
import de.lego.gottfried.decdat.dat.Dat;

public class Token {
	public TokenEnum	op;
	public int			stackPtr;

	private static int	currPos;
	public static Token[] DoStack(int start, int end) {
		ArrayList<Token> a = new ArrayList<Token>();
		
		Dat d = MainForm.theDat;

		for(int offs = start; offs < end; ++offs) {
			currPos = offs;
			TokenEnum t = TokenEnum.getByValue(d.getStackByte(offs));
			Token tok;
			if(t.Parameter) {
				int p = d.getStackInt(offs + 1);
				switch(t) {
					case zPAR_TOK_CALL:
						tok = new TokenCall(t, p);
						break;
					case zPAR_TOK_PUSH_ARRAYVAR:
						tok = new TokenArray(t, p, d.getStackByte(offs + 5));
						offs++;
						break;
					case zPAR_TOK_CALLEXTERN:
					case zPAR_TOK_PUSHVAR:
					case zPAR_TOK_PUSHINST:
					case zPAR_TOK_SETINSTANCE:
						tok = new TokenSymbolParam(t, p);
						break;
					default:
						tok = new TokenIntParam(t, p);
				}
				offs += 4;
			}
			else
				tok = new Token(t);

			a.add(tok);
		}
		
		return a.toArray(new Token[a.size()]);
	}

	protected Token(TokenEnum tok) {
		stackPtr = currPos;
		op = tok;
	}
}
