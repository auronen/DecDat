package de.lego.gottfried.decdat.decompiler;

import de.lego.gottfried.decdat.dat.DatSymbol;

public class DecompilerClass extends Decompiler {
	protected DecompilerClass(DatSymbol sym) {
		super(sym);
	}

	@Override
	public String toString() {
		add("class ");
		add(symbol.name);
		add(" {");
		nl();
		for(DatSymbol s : symbol.children()) {
			add("    ");
			add(get(s).toString());
		}
		add("};");
		nl();
		return sb.toString();
	}
}
