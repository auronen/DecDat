package de.lego.gottfried.decdat.decompiler;

import de.lego.gottfried.decdat.dat.DatSymbol;

public class DecompilerInstance extends Decompiler {
	protected DecompilerInstance(DatSymbol sym) {
		super(sym);
	}

	@Override
	public String toString() {
		if(!symbol.hasFlags(DatSymbol.Flag.Const))
			return new DecompilerVariable(symbol).toString();

		add("instance ");
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
