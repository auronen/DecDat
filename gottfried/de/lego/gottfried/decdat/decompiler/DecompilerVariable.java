package de.lego.gottfried.decdat.decompiler;

import de.lego.gottfried.decdat.dat.DatSymbol;

public class DecompilerVariable extends Decompiler {
	protected DecompilerVariable(DatSymbol sym) {
		super(sym);
	}

	@Override
	public String toString() {
		add("var ");
		add(symbol.typeToString());
		add(" ");
		add(symbol.localName());

		if(symbol.ele() > 1) {
			add("[");
			add(symbol.ele());
			add("]");
		}

		add(";");
		nl();

		return sb.toString();
	}
}
