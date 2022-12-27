package de.lego.gottfried.decdat.exporter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import de.lego.gottfried.decdat.MainForm;
import de.lego.gottfried.decdat.dat.DatSymbol;
import de.lego.gottfried.decdat.decompiler.Decompiler;
import de.lego.gottfried.util.Pair;

public class Exporter {
	private static boolean toFile(Collection<DatSymbol> syms, File file) {
		FileOutputStream fos;
		try {
			file.getParentFile().mkdirs();

			fos = new FileOutputStream(file);
			for(DatSymbol sym : syms) {
				MainForm.Log("export symbol " + sym.name + "(:" + sym.id + ")");
				fos.write((Decompiler.get(sym).toString() + System.getProperty("line.separator")).getBytes(MainForm.encoding));
			}
			fos.close();
		} catch(FileNotFoundException e) {
			MainForm.LogErr("file '" + file + "' not found");
			MainForm.Err("Datei konnte nicht gefunden werden.");
			return false;
		} catch(IOException e) {
			MainForm.LogErr("error while writing to the file '" + file + "'");
			MainForm.Err("Fehler beim Beschreiben der Datei.\nGenauere Informationen sind im Logfile zu finden.");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean ToFile(Collection<DatSymbol> syms, File file) {
		MainForm.Log("start exporting multiple symbols");
		MainForm.Indent(2);

		boolean ret = toFile(syms, file);

		MainForm.Indent(-2);
		return ret;
	}

	public static boolean ToFile(DatSymbol sym, File file) {
		MainForm.Log("start exporting single symbol");
		MainForm.Indent(2);

		LinkedList<DatSymbol> l = new LinkedList<DatSymbol>();
		l.add(sym);
		boolean ret = toFile(l, file);

		MainForm.Indent(-2);
		return ret;
	}

	public static boolean ToFile(String exportDef, File file) {
		MainForm.Log("start exporting with exportdefiniton");
		MainForm.Indent(2);

		LinkedList<Pair<Integer, String>> parsed = new LinkedList<Pair<Integer, String>>();
		int lastID = 0, cLN = 0;

		String[] lines = exportDef.split("\\n");
		for(String line : lines) {
			++cLN;
			line = line.trim();
			if(line.length() == 0 || line.charAt(0) == ';')
				continue;
			String[] tok = line.split("[ \\t]+");

			if(tok.length <= 0) {
				MainForm.Log("skipping invalid line '" + line + "'");
				continue;
			}

			try {
				int cID;
				if(tok[0].trim().charAt(0) == '-') {
					cID = MainForm.theDat.Symbols.length - 1;
				}
				else {
					cID = Integer.valueOf(tok[0]);
					if(cID <= lastID) {
						MainForm.LogErr("cannot export to lower target-id at line " + cLN);
						MainForm.Err("Die Ziel-ID darf nicht niedriger oder gleich der vorherigen sein. (Zeile " + cLN + ")");
						return false;
					}
					if(cID >= MainForm.theDat.Symbols.length) {
						MainForm.LogErr("id is too high! at line " + cLN);
						MainForm.Err("Die Ziel-ID darf die Anzahl der Symbole nicht überschreiten. (Zeile " + cLN + ")");
						return false;
					}
				}
				lastID = cID;
				parsed.add(Pair.getIS(cID, (tok.length >= 2 ? tok[1] : null)));
			} catch(Exception e) {
				MainForm.LogErr("cannot parse token as number at line " + cLN);
				MainForm.Err("'" + tok[0] + "' kann nicht als Zahl geparst werden. (Zeile " + cLN + ")");
				return false;
			}

		}

		MainForm.Log("exportdefinition seems to be correct. letsa go.");

		lastID = 0;

		for(Pair<Integer, String> p : parsed) {
			if(p.right == null) {
				MainForm.Log("it is brave to skip symbols. i hope you know what you're doing");
				lastID = p.left;
				continue;
			}
			LinkedList<DatSymbol> syms = new LinkedList<DatSymbol>();
			for(int i = lastID + 1; i <= p.left; ++i)
				if(MainForm.theDat.Symbols[i].isRegular())
					syms.add(MainForm.theDat.Symbols[i]);
			if(!toFile(syms, new File(file.getAbsolutePath(), p.right)))
				return false;
			lastID = p.left;
		}

		MainForm.Indent(-2);
		return true;
	}
}
