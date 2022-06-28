package de.lego.gottfried.decdat.decompiler;

import de.lego.gottfried.decdat.dat.DatSymbol;

public class DecompilerFunction extends Decompiler {
	protected DecompilerFunction(DatSymbol sym) {
		super(sym);
	}

	@Override
	public String toString() {

		add("func ");
		add(symbol.getReturn());
		add(" ");
		add(symbol.name);
		add("(");

		if(symbol.ele() > 0) {
			for(DatSymbol s : symbol.getParameters()) {
				add(Decompiler.get(s).toString());
				removenl();
				remove(1);
				add(", ");
			}
			remove(2);
		}

		if(symbol.hasFlags(DatSymbol.Flag.External)) {
			add(");");
			nl();
			return "// " + sb.toString();
		}

		add(") {");
		nl();

		addBytecode(symbol.offset != 0);

		add("};");
		nl();

		return sb.toString();
	}
}
