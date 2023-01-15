package de.lego.gottfried.decdat.decompiler;

import static de.lego.gottfried.decdat.dat.DatSymbol.Type.Func;
import static de.lego.gottfried.decdat.dat.DatSymbol.Type.Instance;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.lego.gottfried.decdat.MainForm;
import de.lego.gottfried.decdat.dat.DatSymbol;
import de.lego.gottfried.decdat.token.Token;
import de.lego.gottfried.decdat.token.TokenArray;
import de.lego.gottfried.decdat.token.TokenEnum;
import de.lego.gottfried.decdat.token.TokenIntParam;
import de.lego.gottfried.decdat.token.TokenSymbolParam;
import de.lego.gottfried.util.Pair;

public class Function {
	private Token[] code;
	private Token currTok;
	private int index;
	private LinkedList<Pair<Integer, String>> lines = new LinkedList<Pair<Integer, String>>();

	private boolean retParam;

	private String decompilePush(int forceParam) {
		Token t = code[index];
		if (t instanceof TokenSymbolParam) {
			TokenSymbolParam ts = (TokenSymbolParam) t;
			String ret;
			if (ts.sym.hasFlags(DatSymbol.Flag.Classvar)) {
 
				// by bfyryNy - fix for "forward declaration of member fields"
				if (index == 0)
					return ts.sym.localName();

				t = code[--index];
				if (t.op == TokenEnum.zPAR_TOK_SETINSTANCE) {
					DatSymbol sym = ((TokenSymbolParam) t).sym;
					ret = sym.localName() + "." + ts.sym.localName();
				} else {
					ret = ts.sym.localName();
					++index;
				}
			} else if (ts.sym.name.charAt(0) == '�' || ts.sym.name.charAt(0) == '˙' || ts.sym.name.charAt(0) == 'ÿ' || ts.sym.name.charAt(0) == 'я') {
				if (ts.sym.name.equalsIgnoreCase("�INSTANCE_HELP") || ts.sym.name.equalsIgnoreCase("˙INSTANCE_HELP") || ts.sym.name.equalsIgnoreCase("ÿINSTANCE_HELP") || ts.sym.name.equalsIgnoreCase("яINSTANCE_HELP"))
					ret = "NULL";
				else {
					ret = '"' + (String) ts.sym.content[0] + '"';
					return ret;
				}
			} else
				ret = ts.sym.localName();

			int val;
			if(ts instanceof TokenArray)
				val = ((TokenArray) ts).offset;
			else
				val = 0;

			if(MainForm.theDat.tokenSubstitutionsMap.get(ts.sym.localName()) != null)
				if(MainForm.theDat.tokenSubstitutionsMap.get(ts.sym.localName()).get(val) != null)
					return ret + "[" + MainForm.theDat.tokenSubstitutionsMap.get(ts.sym.localName()).get(val) + "]" + " /*" + ts.sym.localName() + " " + val + "*/";

			if (ts instanceof TokenArray)
				ret += "[" + val + "]";

			return ret;
		}

		if (forceParam == Instance || forceParam == Func) {
			int i = ((TokenIntParam) t).param;
			if (i == -1) {
				return forceParam == Func ? "NOFUNC" : "-1";
			}
			return MainForm.theDat.Symbols[i].localName();
		} else if (forceParam == DatSymbol.Type.Float) {
			return "" + Float.intBitsToFloat(((TokenIntParam) t).param);
		}

		// Auronen
		int intParam = (Integer) ((TokenIntParam) t).param;

		if(intParam > 60) {
			DatSymbol s = MainForm.theDat.IdSymbolPairs.get(intParam);
			if(s != null && s.id == intParam && s.type() == Instance && !s.name.contains(".par"))
				return s.name + " /*" + ((Integer) ((TokenIntParam) t).param).toString() + "*/";
		}

		return ((Integer) ((TokenIntParam) t).param).toString();
	}

	private String decompileParameter(int forceParam) {
		Token t = code[index];
		switch (t.op.Type) {
			case PUSH:
				return decompilePush(forceParam);
			case CALL:
				return decompileCall();
			case BINOP:
			case UNOP:
				return decompileOperation(0, true);
			default:
				break;
		}
		return "<?>";
	}

	private String decompileCall() {
		TokenSymbolParam call = (TokenSymbolParam) code[index];

		DatSymbol params[] = call.sym.getParameters();

		String comment = ""; 
		String ret = ")";
		for (int i = params.length - 1; i >= 0; --i) {
			--index;
			String prm = decompileParameter(params[i].type());
			if (MainForm.ouLoaded && new String("ai_output").equals(call.sym.name) && i == 2)
			{
				comment = "; //" + MainForm.ouLib.getText(prm);
			}
			ret = prm + ret;
			if (i > 0)
				ret = ", " + ret;
		}

		return call.sym.localName() + "(" + ret + /*"; " +*/ comment;
	}

	private String decompileOperation(int parent, boolean first) {
		Token t = code[index];
		// Auronen: ignore the precedence and put brackets everywhere
		//          the result of the logical expression is correct,
		//          but it looks weird. 
		//          TODO: Has to be improved
		boolean brackets = true;//t.op.Precedence < parent;
		StringBuilder ret = new StringBuilder();

		if (brackets && !first)
			ret.append('(');

		if (t.op.Type == TokenEnum.UNOP) {
			--index;
			ret.append(t.op.Operator);
			if (code[index].op.isOp())
				ret.append(decompileOperation(t.op.Precedence, false));
			else
				ret.append(decompileParameter(0));
		} else {
			--index;
			if (t.op.Type == TokenEnum.BINOP && code[index].op.isOp())
				ret.append(decompileOperation(t.op.Precedence, false));
			else
				ret.append(decompileParameter(0));

			ret.append(' ');
			ret.append(t.op.Operator);
			ret.append(' ');

			int ptype = 0;
			if (t.op == TokenEnum.zPAR_TOK_ASSIGNFLOAT)
				ptype = DatSymbol.Type.Float;
			else if (t.op == TokenEnum.zPAR_TOK_ASSIGNFUNC)
				ptype = DatSymbol.Type.Func;
			else if (t.op == TokenEnum.zPAR_TOK_ASSIGNINST)
				ptype = DatSymbol.Type.Instance;

			--index;
			if (t.op.Type == TokenEnum.BINOP && code[index].op.isOp())
				ret.append(decompileOperation(t.op.Precedence, false));
			else
				ret.append(decompileParameter(ptype));
		}

		if (brackets && !first)
			ret.append(')');

		return ret.toString();
	}

	private String decompileReturn() {
		String ret = "return";
		if (retParam) {
			--index;
			ret += ' ' + decompileParameter(0);
		}
		return ret + ';';
	}

	private HashSet<Integer> endif = new HashSet<Integer>();
	private Stack<Integer> ifmark = new Stack<Integer>();
	private boolean lifon = false;

	private void decompileCondition() {
		int t = 1;
		int tpos = ((TokenIntParam) code[index]).param;
		int sub = 1;
		boolean nosem = false;
		Pair<Integer, String> p;

		if (code[index].op == TokenEnum.zPAR_TOK_JUMP) {
			ifmark.push(code[index].stackPtr);
			if (!endif.contains(tpos)) {
				endif.add(tpos);
				//  this is a dirty fix for the problem described here: https://github.com/auronen/DecDat/issues/2
				//  v
				if (false && lines.size() > 0 && (p = lines.get(0)).right.startsWith("if") && lifon) {
					sub = 0;
					p.right = "else " + p.right;
					return;
				} else
					add("else {");
			} else {
				return;
			}
		} else {
			if (!ifmark.isEmpty() && ifmark.peek() == (tpos - 5)) {
				nosem = true;
				tpos = ifmark.pop();
				lifon = false;
			} else
				lifon = true;
			--index;
			add("if (" + decompileParameter(0) + ") {");
		}

		if (lines.size() <= 1) {
			sub = 0;
		} else
			while ((p = lines.get(t++)).left < tpos) {
				p.right = "    " + p.right;
				if (t == lines.size()) {
					sub = 0;
					break;
				}
			}

		lines.add(t - sub, new Pair<Integer, String>(-0xbeef, "}" + (nosem ? "" : ';')));

		if (nosem)
			if ((p = lines.get(t)).right.startsWith("if")) {
				p.right = "else " + p.right;
				lifon = false;
			}
	}

	private void decompile(int start, int end, boolean returnParam) {
		retParam = returnParam;

		code = Token.DoStack(start, end);
		index = code.length - 1;

		while (index >= 0) {
			switch ((currTok = code[index]).op.Type) {
				case CALL:
					String line = decompileCall();
					if (line.contains(";"))
						add(line);
					else 
						add(line + ";");
					break;
				case RETURN:
					add(decompileReturn());
					break;
				case PUSH:
					add(decompilePush(0) + ";");
					break;
				case BINOP:
				case UNOP:
				case ASSIGN:
					if (MainForm.ouLoaded)
					{
						String ass = decompileOperation(0, true);
						Pattern p = Pattern.compile("\"([^\"]*)\"");
						Matcher m = p.matcher(ass);
						String id = "", comment = "";
						while (m.find()) {
							  id = m.group(1);
						}
						if (id.length() == 0) {
							if (ass.endsWith(";"))
								add(ass);
							else
								add(ass + ';');
							break;
						} 
						String text = MainForm.ouLib.getText(id);
						if (text.length() == 0) {
							if (ass.contains(";")) {
								comment = text;
							}
							comment = ";";
						}
						else
							comment = "; //" + text;
						add(ass + comment);
						break;
					}
					else
						add(decompileOperation(0, true) + ';');
					break;
				case JUMP:
					decompileCondition();
					break;
				default:
					MainForm.LogErr("unknown token?");
					break;
			}
			--index;
		}
	}

	private void add(String s) {
		lines.addFirst(new Pair<Integer, String>(currTok.stackPtr, s));
	}

	public Function(int start, int end) {
		decompile(start, end, false);
	}

	public Function(int start, int end, boolean ret) {
		decompile(start, end, ret);
	}

	public LinkedList<String> getLines() {
		LinkedList<String> s = new LinkedList<String>();
		for (Pair<Integer, String> p : lines)
			s.add(p.right);
		return s;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Pair<Integer, String> s : lines)
			sb.append(s.right).append(System.getProperty("line.separator"));
		return sb.toString();
	}
}
