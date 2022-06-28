package de.lego.gottfried.decdat.token;

import de.lego.gottfried.decdat.MainForm;

public class TokenCall extends TokenSymbolParam {
	protected TokenCall(TokenEnum tok, int param) {
		super(tok);
		sym = MainForm.theDat.getSymbolByStackOffset(param);
	}
}
