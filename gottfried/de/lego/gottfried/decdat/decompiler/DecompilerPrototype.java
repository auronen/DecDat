package de.lego.gottfried.decdat.decompiler;

import de.lego.gottfried.decdat.dat.DatSymbol;

public class DecompilerPrototype extends Decompiler {
	protected DecompilerPrototype(DatSymbol sym) {
		super(sym);
	}

	@Override
	public String toString() {
		add("prototype ");
		add(symbol.name);
		add("(");
		add(symbol.typeToString());
		add(") {");

		nl();

		addBytecode(false);

		add("};");
		nl();

		return sb.toString();
	}
}
