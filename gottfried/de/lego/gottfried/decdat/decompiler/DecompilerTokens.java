package de.lego.gottfried.decdat.decompiler;

import de.lego.gottfried.decdat.dat.DatSymbol;
import de.lego.gottfried.decdat.token.Token;
import de.lego.gottfried.decdat.token.TokenArray;
import de.lego.gottfried.decdat.token.TokenEnum;
import de.lego.gottfried.decdat.token.TokenIntParam;
import de.lego.gottfried.decdat.token.TokenSymbolParam;

public class DecompilerTokens extends Decompiler {
	protected DecompilerTokens(DatSymbol sym) {
		super(sym);
	}

	@Override
	public String toString() {
		//ArrayIndexOutOfBoundsException for tokens fix ?
		if(symbol.content.length == 0)
			return sb.toString();

		Token[] toks = Token.DoStack((int)symbol.content[0], symbol.findFunctionEnd());

		for(Token t : toks) {
			String hex = Integer.toHexString(t.stackPtr).toUpperCase();
			while(hex.length() < 6)
				hex = '0' + hex;
			add(hex);
			add("  ");
			add(t.op.Name);
			if(t instanceof TokenIntParam) {
				TokenIntParam tip = (TokenIntParam)t;
				if(tip.op.Type == TokenEnum.JUMP)
					add(Integer.toHexString(tip.param).toUpperCase());
				else
					add(tip.param);
			}
			else if(t instanceof TokenSymbolParam) {
				TokenSymbolParam tsp = (TokenSymbolParam)t;
				add(tsp.sym.name);
				if(tsp.op == TokenEnum.zPAR_TOK_PUSH_ARRAYVAR) {
					add('[');
					add(((TokenArray)t).offset);
					add(']');
				}
				if(tsp.sym.hasFlags(DatSymbol.Flag.Const) && tsp.op.Type != TokenEnum.CALL) {
					add(", ");
					if(tsp.sym.type() == DatSymbol.Type.String)
						add('"');
					if(tsp.op == TokenEnum.zPAR_TOK_PUSH_ARRAYVAR)
						add(tsp.sym.content[((TokenArray)t).offset].toString());
					else
						add(tsp.sym.content[0].toString());
					if(tsp.sym.type() == DatSymbol.Type.String)
						add('"');
				}
			}
			nl();
		}

		return sb.toString();
	}
}
