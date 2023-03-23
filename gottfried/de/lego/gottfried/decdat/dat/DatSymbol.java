package de.lego.gottfried.decdat.dat;

import java.util.ArrayList;
import java.util.Collections;

import de.lego.gottfried.decdat.decompiler.Decompiler;

public class DatSymbol {
	public static int cID = 0;

	public Dat thedat;
	public int id;
	public int b_hasName;
	public String name;
	public String nameLo;
	public String nameGl;
	public int offset;
	public int bitfield;

	public int filenr;
	public int line;
	public int line_anz;
	public int pos_beg;
	public int pos_anz;

	public int parent;
	public String parentS;

	public Object[] content;

	public boolean isLocal;

	public static final int bitfield_ele = ((1 << 12) - 1) << 0;
	public static final int bitfield_type = ((1 << 4) - 1) << 12;
	public static final int bitfield_flags = ((1 << 6) - 1) << 16;

	public final class Type {
		public static final int Void = 0 << 12,
				Float = 1 << 12,
				Int = 2 << 12,
				String = 3 << 12,
				Class = 4 << 12,
				Func = 5 << 12,
				Prototype = 6 << 12,
				Instance = 7 << 12,
				VariableArgument = 8 << 12;
	}

	public final class Flag {
		public static final int Const = 1 << 16,
				Return = 2 << 16,
				Classvar = 4 << 16,
				External = 8 << 16,
				Merged = 16 << 16;
	}

	public DatSymbol(String n) {
		name = n;
	}

	public DatSymbol(Dat dat, DatStream s) {
		thedat = dat;

		id = cID++;
		b_hasName = s.ReadInt();
		name = s.ReadString();
		nameLo = name.toLowerCase();
		offset = s.ReadInt();
		bitfield = s.ReadInt();
		filenr = s.ReadInt();
		filenr = filenr & 0x7FFFF; // 19 bits
		line = s.ReadInt();
		line = line & 0x7FFFF; // 19 bits
		line_anz = s.ReadInt();
		line_anz = line_anz & 0x7FFFF; // 19 bits
		pos_beg = s.ReadInt();
		pos_beg = pos_beg & 0xFFFFFF; // 24 bits
		pos_anz = s.ReadInt();
		pos_anz = pos_anz & 0xFFFFFF; // 24 bits

		isLocal = name.contains(".");

		if ((flags() & Flag.Classvar) == 0) {
			int type = type();
			if (type == Type.Func || type == Type.Class || type == Type.Prototype)
				content = new Object[1];
			else
				content = new Object[ele()];

			if (content.length == 0 && type == Type.Instance)
				content = new Object[1];

			for (int i = 0; i < content.length; ++i)
				switch (type) {
					case Type.String:
					case Type.VariableArgument:
						content[i] = s.ReadString();
						break;
					case Type.Float:
						content[i] = s.ReadFloat();
						break;
					default:
						content[i] = s.ReadInt();
						break;
				}
		}

		parent = s.ReadInt();
	}

	public String localName() {
		if (nameGl == null)
			return nameGl = (isLocal ? name.split("\\.")[1] : name);
		return nameGl;
	}

	public boolean isRegular() {
		return !isLocal && name.charAt(0) != 0xFF;
	}

	public boolean hasFunction() {
		return (hasFlags(Flag.Const) && (type() == Type.Func)) || type() == Type.Instance || type() == Type.Prototype;
	}

	public int ele() {
		return bitfield & bitfield_ele;
	}

	public void eleSet(int v) {
		bitfield = (bitfield & ~bitfield_ele) | v;
	}

	public int type() {
		return bitfield & bitfield_type;
	}

	public void setType(int v) {
		bitfield = (bitfield & ~bitfield_type) | v;
	}

	public int flags() {
		return bitfield & bitfield_flags;
	}

	public void setFlags(int v) {
		bitfield = (bitfield & ~bitfield_flags) | v;
	}

	public boolean hasFlags(int v) {
		return (flags() & v) > 0;
	}

	public void addFlags(int v) {
		bitfield |= v & bitfield_flags;
	}

	public void removeFlags(int v) {
		bitfield &= ~(v & bitfield_flags);
	}

	public int sizeOf() {
		int t = type();
		switch (t) {
			case Type.Float:
			case Type.Int:
			case Type.Func:
				return 4;
			case Type.String:
			case Type.VariableArgument:
				return 20;
		}
		return offset;
	}

	public ArrayList<DatSymbol> children() {
		ArrayList<DatSymbol> a = new ArrayList<DatSymbol>();
		for(int i = id + 1, m = id + 1 + ele(); i < m; ++i)
			a.add(thedat.Symbols[i]);
		return a;
	}

	public int findFunctionEnd() {
		return thedat.functionOffsets.get(Collections.binarySearch(thedat.functionOffsets, (Integer) content[0]) + 1);
	}

	public boolean hasPrototype() {
		return thedat.Symbols[parent].type() == Type.Prototype;
	}

	public boolean isInstance() {
		return type() == Type.Instance;
	}

	public DatSymbol[] getParameters() {
		ArrayList<DatSymbol> a = children();
		return a.toArray(new DatSymbol[a.size()]);
	}

	public DatSymbol[] getLocalVars() {
		ArrayList<DatSymbol> a = new ArrayList<DatSymbol>();
		int cid = id + ele() + 1;
		if (cid >= thedat.Symbols.length)
			return new DatSymbol[] {};
		DatSymbol curr;
		while ((curr = thedat.Symbols[cid++]).name.startsWith(name + '.'))
			a.add(curr);
		return a.toArray(new DatSymbol[a.size()]);
	}

	private String typeIntToString(int type) {
		switch (type) {
			case Type.Void:
				return "void";
			case Type.Int:
				return "int";
			case Type.String:
				return "string";
			case Type.Float:
				return "float";
			case Type.Prototype:
			case Type.Instance:
				if (parent == -1)
					return "__class";
				return thedat.Symbols[parent].name;
			case Type.Func:
				return "func";
			case Type.Class:
				return "class";
			case Type.VariableArgument:
				return "va";
		}
		return "?";
	}

	public String flagsToString() {
		StringBuilder sb = new StringBuilder();
		int flags = flags();
		if ((flags & Flag.Const) > 0)
			sb.append("const, ");
		if ((flags & Flag.Return) > 0)
			sb.append("return, ");
		if ((flags & Flag.Classvar) > 0)
			sb.append("classvar, ");
		if ((flags & Flag.External) > 0)
			sb.append("external, ");
		if ((flags & Flag.Merged) > 0)
			sb.append("merged  ");
		if (sb.length() > 2)
			sb.setLength(sb.length() - 2);
		return sb.toString();
	}

	public String contentToString() {
		if (content == null || content.length == 0)
			return "";
		StringBuilder res = new StringBuilder();
		for (Object o : content)
			res.append(o).append(", ");
		res.setLength(res.length() - 2);
		return res.toString();
	}

	public String typeToString() {
		return typeIntToString(type());
	}

	public String getReturn() {
		return typeIntToString(offset << 12);
	}

	public boolean hasReturn() {
		return hasFlags(Flag.Return);
	}

	public String toString() {
		return Decompiler.get(this).toString();
	}

	public String getTypeString() {
		switch (type()) {
			case Type.Instance:
				return "instance(" + typeToString() + ')';
			case Type.Prototype:
				return "prototype(" + typeToString() + ')';
			case Type.Func:
				if (!hasFlags(Flag.Const))
					break;
				return "func " + getReturn();
			case Type.Class:
				return typeToString();
		}
		return (hasFlags(Flag.Const) ? "const " : "var ") + typeToString();
	}
}
