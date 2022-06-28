package de.lego.gottfried.decdat.token;

import java.util.HashMap;

public enum TokenEnum {
	BINOP(null, -1),
	UNOP(null, -2),
	CALL(null, -3),
	ASSIGN(null, -4),
	PUSH(null, -5),
	JUMP(null, -6),
	RETURN(null, -7),

	zPAR_OP_PLUS("add", 0, BINOP, "+", 6),
	zPAR_OP_MINUS("sub", 1, BINOP, "-", 6),
	zPAR_OP_MUL("mul", 2, BINOP, "*", 5),
	zPAR_OP_DIV("div", 3, BINOP, "/", 5),
	zPAR_OP_MOD("mod", 4, BINOP, "%", 5),
	zPAR_OP_OR("or", 5, BINOP, "|", 12),
	zPAR_OP_AND("and", 6, BINOP, "&", 10),
	zPAR_OP_LOWER("lwr", 7, BINOP, "<", 8),
	zPAR_OP_HIGHER("hgh", 8, BINOP, ">", 8),
	zPAR_OP_IS("is", 9, ASSIGN, "=", 16),
	zPAR_OP_LOG_OR("lor", 11, BINOP, "||", 14),
	zPAR_OP_LOG_AND("land", 12, BINOP, "&&", 13),
	zPAR_OP_SHIFTL("shl", 13, BINOP, "<<", 7),
	zPAR_OP_SHIFTR("shr", 14, BINOP, ">>", 7),
	zPAR_OP_LOWER_EQ("leq", 15, BINOP, "<=", 8),
	zPAR_OP_EQUAL("eq", 16, BINOP, "==", 9),
	zPAR_OP_NOTEQUAL("neq", 17, BINOP, "!=", 9),
	zPAR_OP_HIGHER_EQ("heq", 18, BINOP, ">=", 8),
	zPAR_OP_ISPLUS("iadd", 19, ASSIGN, "+=", 16),
	zPAR_OP_ISMINUS("isub", 20, ASSIGN, "-=", 16),
	zPAR_OP_ISMUL("imul", 21, ASSIGN, "*=", 16),
	zPAR_OP_ISDIV("idiv", 22, ASSIGN, "/=", 16),
	zPAR_OP_UN_PLUS("uplus", 30, UNOP, "+", 3),
	zPAR_OP_UN_MINUS("uminus", 31, UNOP, "-", 3),
	zPAR_OP_UN_NOT("not", 32, UNOP, "!", 3),
	zPAR_OP_UN_NEG("neg", 33, UNOP, "~", 3),
	zPAR_TOK_BRACKETON(null, 40),
	zPAR_TOK_BRACKETOFF(null, 41),
	zPAR_TOK_SEMIKOLON(null, 42),
	zPAR_TOK_KOMMA(null, 43),
	zPAR_TOK_SCHWEIF(null, 44),
	zPAR_TOK_NONE(null, 45),
	zPAR_TOK_FLOAT(null, 51),
	zPAR_TOK_VAR(null, 52),
	zPAR_TOK_OPERATOR(null, 53),
	zPAR_TOK_RET("retn", 60, RETURN),
	zPAR_TOK_CALL("call", 61, CALL, true),
	zPAR_TOK_CALLEXTERN("callx", 62, CALL, true),
	zPAR_TOK_POPINT("popi", 63),
	zPAR_TOK_PUSHINT("pushi", 64, PUSH, true),
	zPAR_TOK_PUSHVAR("pushv", 65, PUSH, true),
	zPAR_TOK_PUSHSTR("pushs", 66, PUSH),
	zPAR_TOK_PUSHINST("pushin", 67, PUSH, true),
	zPAR_TOK_PUSHINDEX("pushid", 68, PUSH),
	zPAR_TOK_POPVAR("popv", 69),
	zPAR_TOK_ASSIGNSTR("astr", 70, ASSIGN, "=", 16),
	zPAR_TOK_ASSIGNSTRP("astrp", 71, ASSIGN, "=", 16),
	zPAR_TOK_ASSIGNFUNC("afnc", 72, ASSIGN, "=", 16),
	zPAR_TOK_ASSIGNFLOAT("aflt", 73, ASSIGN, "=", 16),
	zPAR_TOK_ASSIGNINST("ains", 74, ASSIGN, "=", 16),
	zPAR_TOK_JUMP("jmp", 75, JUMP, true),
	zPAR_TOK_JUMPF("jmpf", 76, JUMP, true),
	zPAR_TOK_SETINSTANCE("sinst", 80, ASSIGN, true),
	zPAR_TOK_SKIP(null, 90),
	zPAR_TOK_LABEL(null, 91),
	zPAR_TOK_FUNC(null, 92),
	zPAR_TOK_FUNCEND(null, 93),
	zPAR_TOK_CLASS(null, 94),
	zPAR_TOK_CLASSEND(null, 95),
	zPAR_TOK_INSTANCE(null, 96),
	zPAR_TOK_INSTANCEEND(null, 97),
	zPAR_TOK_NEWSTRING(null, 98),
	zPAR_TOK_FLAGARRAY(null, 180),
	zPAR_TOK_PUSH_ARRAYVAR("pusha", 245, PUSH, true, true);

	public int			Value	= -1;
	public int			Precedence	= 0;
	public boolean		Parameter;
	public boolean		Flag;
	public TokenEnum	Type	= null;
	public String		Operator;
	public String		Name;

	public boolean isOp() {
		return Type == BINOP || Type == UNOP;
	}

	private void Init(String name, int id, TokenEnum type, boolean param, boolean flag) {
		Value = id;
		Parameter = param;
		Flag = flag;
		Type = type;
		Name = name;
		if(Name != null)
			while(Name.length() < 9)
				Name += ' ';
	}

	private TokenEnum() {}

	private TokenEnum(String name, int id) {
		Init(name, id, null, false, false);
	}

	private TokenEnum(String name, int id, TokenEnum type) {
		Init(name, id, type, false, false);
	}

	private TokenEnum(String name, int id, TokenEnum type, String operator, int prec) {
		Operator = operator;
		Precedence = prec;
		Init(name, id, type, false, false);
	}

	private TokenEnum(String name, int id, TokenEnum type, boolean param) {
		Init(name, id, type, param, false);
	}

	private TokenEnum(String name, int id, TokenEnum type, boolean param, boolean flag) {
		Init(name, id, type, param, flag);
	}

	private static HashMap<Integer, TokenEnum>	map	= new HashMap<Integer, TokenEnum>();

	public static TokenEnum getByValue(int value) {
		return map.get(value);
	}

	static {
		for(TokenEnum t : values())
			map.put(t.Value, t);
	}

}
