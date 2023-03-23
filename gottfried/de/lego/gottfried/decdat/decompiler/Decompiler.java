package de.lego.gottfried.decdat.decompiler;

import static de.lego.gottfried.decdat.dat.DatSymbol.Flag.Classvar;
import static de.lego.gottfried.decdat.dat.DatSymbol.Flag.Const;
import static de.lego.gottfried.decdat.dat.DatSymbol.Flag.External;
import static de.lego.gottfried.decdat.dat.DatSymbol.Type.Class;
import static de.lego.gottfried.decdat.dat.DatSymbol.Type.Float;
import static de.lego.gottfried.decdat.dat.DatSymbol.Type.Func;
import static de.lego.gottfried.decdat.dat.DatSymbol.Type.Instance;
import static de.lego.gottfried.decdat.dat.DatSymbol.Type.Int;
import static de.lego.gottfried.decdat.dat.DatSymbol.Type.Prototype;
import static de.lego.gottfried.decdat.dat.DatSymbol.Type.String;
import static de.lego.gottfried.decdat.dat.DatSymbol.Type.VariableArgument;
import de.lego.gottfried.decdat.MainForm;
import de.lego.gottfried.decdat.dat.DatSymbol;

public abstract class Decompiler {
	public static Decompiler get(DatSymbol sym) {
		switch(sym.type()) {
			case Class:
				return new DecompilerClass(sym);
			case Func:
				if(sym.hasFlags(Const))
					return new DecompilerFunction(sym);
			case Int:
			case String:
			case Float:
			case VariableArgument:
				if(sym.hasFlags(Classvar))
					return new DecompilerVariable(sym);
				else if(sym.hasFlags(Const))
					return new DecompilerConstant(sym);
				return new DecompilerVariable(sym);
			case Instance:
				return new DecompilerInstance(sym);
			case Prototype:
				return new DecompilerPrototype(sym);
		}

		MainForm.LogErr("cannot decompile symbol " + sym.name);
		return null;
	}

	public static Decompiler getOP(DatSymbol sym) {
		switch(sym.type()) {
			case Func:
				if(!sym.hasFlags(Const) || sym.hasFlags(External))
					break;
			case Instance:
			case Prototype:
				return new DecompilerTokens(sym);
		}
		return null;
	}

	protected DatSymbol		symbol;
	protected StringBuilder	sb	= new StringBuilder();

	protected Decompiler(DatSymbol sym) {
		symbol = sym;
	}

	public abstract String toString();

	protected void add(String t) {
		sb.append(t);
	}

	protected void add(char t) {
		sb.append(t);
	}

	protected void add(int t) {
		sb.append(t);
	}

	protected void remove(int i) {
		sb.setLength(sb.length() - i);
	}

	protected void nl() {
		sb.append(System.getProperty("line.separator"));
	}

	protected void removenl() {
		remove(System.getProperty("line.separator").length());
	}

	protected void addBytecode(boolean ret) {

		int start = (int)symbol.content[0] + 6 * symbol.ele();
		if(symbol.isInstance() && symbol.hasPrototype())
			start += 5; // call

		int end = symbol.findFunctionEnd() - 1;

		DatSymbol[] lv = symbol.getLocalVars();
		if(lv.length > 0) {
			for(DatSymbol s : lv) {
				add("    ");
				add(get(s).toString());
			}

			nl();
		}

		for(String s : new Function(start, end, ret).getLines()) {
			add("    ");
			add(s);
			nl();
		}
	}
}
