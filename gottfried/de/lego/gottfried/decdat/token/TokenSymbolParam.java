package de.lego.gottfried.decdat.token;

import de.lego.gottfried.decdat.MainForm;
import de.lego.gottfried.decdat.dat.DatSymbol;

public class TokenSymbolParam extends Token {
	public DatSymbol	sym;

	protected TokenSymbolParam(TokenEnum tok, int param) {
		super(tok);
		sym = MainForm.theDat.Symbols[param];
	}

	protected TokenSymbolParam(TokenEnum tok) {
		super(tok);
	}
}
