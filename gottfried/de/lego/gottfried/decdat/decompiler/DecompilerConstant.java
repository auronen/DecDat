package de.lego.gottfried.decdat.decompiler;

import de.lego.gottfried.decdat.dat.DatSymbol;

public class DecompilerConstant extends Decompiler {
	protected DecompilerConstant(DatSymbol sym) {
		super(sym);
	}

	@Override
	public String toString() {
		add("const ");
		add(symbol.typeToString());
		add(" ");
		add(symbol.localName());

		if(symbol.ele() > 1) {
			add("[");
			add(symbol.ele());
			add("] = {");

			int sqrt = (int)Math.round(Math.sqrt(symbol.ele()));

			if(symbol.ele() < 16)
				sqrt = Integer.MAX_VALUE;

			for(int i = 0; i < symbol.ele(); ++i) {
				if(i % sqrt == 0) {
					nl();
					add("    ");
				}
				Object o = symbol.content[i];
				if(o instanceof String) {
					add("\"");
					add(o.toString());
					add("\"");
				}
				else
					add(o.toString());
				if(i < symbol.ele() - 1)
					add(", ");
			}
			nl();
			add("}");
		}
		else {
			add(" = ");
			Object o = symbol.content[0];
			if(o instanceof String) {
				add("\"");
				add(o.toString());
				add("\"");
			}
			else
				add(o.toString());
		}

		add(";");
		nl();

		return sb.toString();
	}
}
