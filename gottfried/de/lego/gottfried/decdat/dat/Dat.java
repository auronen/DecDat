package de.lego.gottfried.decdat.dat;


import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import de.lego.gottfried.decdat.MainForm;

public class Dat {
	public DatStream				Stream;
	public String					Path;
	public int						Version;
	public DatSymbol				Symbols[];
	public LinkedList<DatSymbol>	SymbolsC;
	public LinkedList<DatSymbol>	SymbolsRegular;
	public Map<Integer, DatSymbol>	IdSymbolPairs;
	public int						sortedTable[];
	public DatSymbol				sortedSymbolTable[];
	public byte						stack[];
	public ArrayList<DatSymbol>		functionSymbols;
	public ArrayList<Integer>		functionOffsets;
	public boolean					fileOk;
	public Map<String, Map<Integer, String>> tokenSubstitutionsMap;

	public Dat(String path) {
		DatSymbol.cID = 0;
		Path = path;

		fileOk = false;
		byte b[];
		try {
			File file = new File(Path);
			FileInputStream fis = new FileInputStream(file);
			b = new byte[(int)file.length()];
			fis.read(b);
			fis.close();
		} catch(Exception e) {
			MainForm.LogErr(e.getMessage());
			e.printStackTrace();
			return;
		}

		Stream = new DatStream(b);

		MainForm.Log("load new dat");
		MainForm.Indent(2);

		ReadHead();
		ReadSortedTable();
		ReadSymbols();
		ReadStack();

		MainForm.Log("valid dat loaded");
		MainForm.Indent(-2);
		Stream = null;

		initFuncOffs();
		fixNames();
		fillSortedSymbolTable();
		fixExternals();

		fileOk = true;
	}

	private void ReadHead() {
		Version = Stream.ReadByte();
		MainForm.Log("read '" + Path + "' of version " + Version);
	}

	private void ReadSortedTable() {
		MainForm.Log("read sorted symboltable");
		sortedTable = new int[Stream.ReadInt()];
		for(int i = 0; i < sortedTable.length; ++i)
			sortedTable[i] = Stream.ReadInt();
		MainForm.Log(sortedTable.length + " symbols to read");
	}

	private void ReadSymbols() {
		MainForm.Log("reading symbols");
		Symbols = new DatSymbol[sortedTable.length];
		SymbolsC = new LinkedList<DatSymbol>();
		SymbolsRegular = new LinkedList<DatSymbol>();
		IdSymbolPairs = new HashMap<Integer, DatSymbol>();

		tokenSubstitutionsMap = new HashMap<String, Map<Integer, String>>();
		for(Map.Entry<String, String> set : MainForm.tokenPrefixes.entrySet())
			tokenSubstitutionsMap.put(set.getKey(), new HashMap<Integer, String>());

		for(int i = 0; i < Symbols.length; ++i) {
			Symbols[i] = new DatSymbol(this, Stream);
			SymbolsC.add(Symbols[i]);
			IdSymbolPairs.put(Symbols[i].id, Symbols[i]);
			if(Symbols[i].name.length() != 0 && !Symbols[i].isLocal && Symbols[i].name.charAt(0) != 0xFF)
				SymbolsRegular.add(Symbols[i]);

			for(Map.Entry<String, String> set : MainForm.tokenPrefixes.entrySet())
				if(Symbols[i].name.toLowerCase().startsWith(set.getValue())) {
					int val = (Integer)Symbols[i].content[0];
					if(tokenSubstitutionsMap.get(set.getKey()).get(val) == null)
						tokenSubstitutionsMap.get(set.getKey()).put(val, Symbols[i].name.toLowerCase());
				}
		}
	}
	
	private void ReadStack() {
		int i;
		MainForm.Log("read datastack of length " + (i = Stream.ReadInt()));
		stack = Stream.ReadBytes(i);
	}

	private void fillSortedSymbolTable() {
		sortedSymbolTable = Symbols.clone();
		Arrays.sort(sortedSymbolTable, new Comparator<DatSymbol>() {
			@Override
			public int compare(DatSymbol o1, DatSymbol o2) {
				return o1.name.compareTo(o2.name);
			}
		});
		for(int i = 0; i < Symbols.length; ++i)
			sortedTable[i] = sortedSymbolTable[i].id;
	}

	public int getSymbolIDByName(String name) {
		int id;
		if((id = Arrays.binarySearch(sortedSymbolTable, new DatSymbol(fixName(name)), new Comparator<DatSymbol>() {
			@Override
			public int compare(DatSymbol o1, DatSymbol o2) {
				return o1.name.compareTo(o2.name);
			}
		})) < 0)
			return -1;
		return sortedTable[id];
	}

	public DatSymbol getSymbolByName(String name) {
		int id;
		if((id = getSymbolIDByName(fixName(name))) >= 0)
			return Symbols[id];
		return new DatSymbol("<invalid>");
	}

	public int getStackByte(int offs) {
		int i;
		return (i = stack[offs]) < 0 ? i + 256 : i;
	}

	public int getStackInt(int offs) {
		return getStackByte(offs) | (getStackByte(offs + 1) << 8) | (getStackByte(offs + 2) << 16) | (getStackByte(offs + 3) << 24);
	}

	public DatSymbol getSymbolByStackOffset(int offs) {
		return functionSymbols.get(Collections.binarySearch(functionOffsets, offs));
	}

	public LinkedList<DatSymbol> getAllSymbolsByName(String regex) {
		regex = regex.toLowerCase();
		LinkedList<DatSymbol> l = new LinkedList<DatSymbol>();
		for(DatSymbol s : Symbols)
			if(s.name.matches(regex) || s.name.contains(regex))
				l.add(s);
		return l;
	}

	public LinkedList<DatSymbol> getAllSingleSymbolsByName(String regex) {
		regex = regex.toLowerCase();
		LinkedList<DatSymbol> l = new LinkedList<DatSymbol>();
		for(DatSymbol s : Symbols)
			if(!s.isLocal && s.name.matches(regex))
				l.add(s);
		return l;
	}

	public LinkedList<DatSymbol> getAllSymbolIDs(String regex) {
		regex = regex.toLowerCase();
		LinkedList<DatSymbol> l = new LinkedList<DatSymbol>();
		for(DatSymbol s : Symbols)
			if(String.valueOf(s.id).matches(regex))
				l.add(s);
		return l;
	}

	public LinkedList<DatSymbol> getAllSymbolsByType(String regex) {
		regex = regex.toLowerCase();
		LinkedList<DatSymbol> l = new LinkedList<DatSymbol>();
		for(DatSymbol s : Symbols)
			if(s.getTypeString().matches(regex) || s.getTypeString().contains(regex))
				l.add(s);
		return l;
	}

	private void initFuncOffs() {
		if(functionOffsets == null) {
			functionOffsets = new ArrayList<Integer>();
			functionSymbols = new ArrayList<DatSymbol>();
			for(DatSymbol s : Symbols)
				if(s.hasFunction()) {
					functionOffsets.add((Integer)s.content[0]);
					functionSymbols.add(s);
				}
			functionOffsets.add(stack.length);
			Collections.sort(functionOffsets);
			Collections.sort(functionSymbols, new Comparator<DatSymbol>() {
				@Override
				public int compare(DatSymbol o1, DatSymbol o2) {
					return (Integer)o1.content[0] - (Integer)o2.content[0];
				}
			});
		}
	}

	private void fixExternals() {
		getSymbolByName("CREATEINVITEM.PAR1").setType(DatSymbol.Type.Instance);
		getSymbolByName("CREATEINVITEMS.PAR1").setType(DatSymbol.Type.Instance);
		getSymbolByName("HLP_GETNPC.PAR0").setType(DatSymbol.Type.Instance);
		getSymbolByName("NPC_REMOVEINVITEMS.PAR1").setType(DatSymbol.Type.Instance);
		getSymbolByName("NPC_HASITEMS.PAR1").setType(DatSymbol.Type.Instance);
	}

	private String fixName(String name) {
		return name.toLowerCase();
	}

	private void fixNames() {
		for(DatSymbol s : Symbols)
			s.name = fixName(s.name);
	}

	public static void sortSymbolsByName(List<DatSymbol> syms) {
		Collections.sort(syms, new Comparator<DatSymbol>() {
			@Override
			public int compare(DatSymbol arg0, DatSymbol arg1) {
				return arg0.name.compareTo(arg1.name);
			}
		});
	}

	public static void sortSymbolsByType(List<DatSymbol> syms) {
		Collections.sort(syms, new Comparator<DatSymbol>() {
			@Override
			public int compare(DatSymbol arg0, DatSymbol arg1) {
				return arg0.getTypeString().compareTo(arg1.getTypeString());
			}
		});
	}

	public static void sortSymbolsByID(List<DatSymbol> syms) {
		Collections.sort(syms, new Comparator<DatSymbol>() {
			@Override
			public int compare(DatSymbol arg0, DatSymbol arg1) {
				return arg0.id - arg1.id;
			}
		});
	}
}
