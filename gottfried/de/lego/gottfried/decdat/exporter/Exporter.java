package de.lego.gottfried.decdat.exporter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import java.nio.charset.Charset;
import java.util.Objects;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import de.lego.gottfried.decdat.MainForm;
import de.lego.gottfried.decdat.dat.DatSymbol;
import de.lego.gottfried.decdat.decompiler.Decompiler;
import de.lego.gottfried.util.Pair;

public class Exporter {
	private static boolean jobDone;
	private static Integer symbolsMax = 0;
	private static Integer symbolsCount = 0;
	private static boolean toFile(Collection<DatSymbol> syms, File file) {
		FileOutputStream fos;
		long startTime = System.nanoTime();
		try {
			file.getParentFile().mkdirs();
			fos = new FileOutputStream(file);
			symbolsMax = syms.size();
			symbolsCount = 0;
			jobDone = false;

			JOptionPane pane = new JOptionPane("Exporting...", JOptionPane.INFORMATION_MESSAGE);
			JDialog dialog = pane.createDialog(MainForm.frmDecdat, "Information");
			JLabel label = new JLabel("0 / " + symbolsMax, JLabel.CENTER);
			JProgressBar bar = new JProgressBar(0, symbolsMax);
			pane.setOptions(new Object[]{});
			pane.add(label, 1);
			pane.add(bar, 2);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			SwingWorker<Void, Void> sw = new SwingWorker<Void,Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					for(DatSymbol sym : syms) {
						symbolsCount++;
						label.setText(symbolsCount + " / " + symbolsMax);
						bar.setValue(symbolsCount);

						if (sym.name.charAt(0) == '�' || sym.name.charAt(0) == '˙' || sym.name.charAt(0) == 'ÿ' || sym.name.charAt(0) == 'я') {
							if (sym.name.equalsIgnoreCase("�INSTANCE_HELP") || sym.name.equalsIgnoreCase("˙INSTANCE_HELP") || sym.name.equalsIgnoreCase("ÿINSTANCE_HELP") || sym.name.equalsIgnoreCase("яINSTANCE_HELP"))
								fos.write(("// " + Objects.requireNonNull(Decompiler.get(sym)).toString() + System.getProperty("line.separator")).getBytes(Charset.forName(MainForm.encoding)));
						}
						else
							fos.write((Objects.requireNonNull(Decompiler.get(sym)).toString() + System.getProperty("line.separator")).getBytes(Charset.forName(MainForm.encoding)));
					}
					MainForm.Log("Processed " + symbolsCount + " out of " + symbolsMax + " symbols");
					jobDone = true;
					return null;
				}

				@Override
				protected void done() {
					dialog.dispose();
				}
			};

			sw.execute();
			dialog.setVisible(true);
			fos.close();
		} catch(FileNotFoundException e) {
			MainForm.LogErr("file '" + file + "' not found.");
			MainForm.Err("File '" + file + "' not found.");
			return false;
		} catch(IOException e) {
			MainForm.LogErr("error while writing to the file '" + file + "'");
			MainForm.Err("Error while writing to the file '" + file + "'");
			e.printStackTrace();
			return false;
		}
		long finishTime = System.nanoTime();
		long elapsedTime = (finishTime - startTime)/1000000;
		MainForm.Log("elapsed execution time: " + elapsedTime + "ms");
		return jobDone;
	}

	public static boolean ToFile(Collection<DatSymbol> syms, File file) {
		MainForm.Log("start exporting multiple symbols");
		MainForm.Indent(2);

		boolean ret = toFile(syms, file);

		MainForm.Indent(-2);
		return ret;
	}

	private static boolean TokenstoFile(Collection<DatSymbol> syms, File file) {
		FileOutputStream fos;
		long startTime = System.nanoTime();
		try {
			file.getParentFile().mkdirs();
			fos = new FileOutputStream(file);
			symbolsMax = syms.size();
			symbolsCount = 0;
			jobDone = false;

			JOptionPane pane = new JOptionPane("Exporting...", JOptionPane.INFORMATION_MESSAGE);
			JDialog dialog = pane.createDialog(MainForm.frmDecdat, "Information");
			JLabel label = new JLabel("0 / " + symbolsMax, JLabel.CENTER);
			JProgressBar bar = new JProgressBar(0, symbolsMax);
			pane.setOptions(new Object[]{});
			pane.add(label, 1);
			pane.add(bar, 2);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			SwingWorker<Void, Void> sw = new SwingWorker<Void,Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					for(DatSymbol sym : syms) {
						symbolsCount++;
						label.setText(symbolsCount + " / " + symbolsMax);
						bar.setValue(symbolsCount);

							/* */
							Decompiler p = Decompiler.getOP(sym);
							if (p == null) {
							} else {
								fos.write(sym.name.getBytes());
								fos.write('\n');
								fos.write(
									(p.toString()).getBytes());
							}
							/* */
					}
					MainForm.Log("Processed " + symbolsCount + " out of " + symbolsMax + " symbols");
					jobDone = true;
					return null;
				}

				@Override
				protected void done() {
					dialog.dispose();
				}
			};

			sw.execute();
			dialog.setVisible(true);
			fos.close();
		} catch(FileNotFoundException e) {
			MainForm.LogErr("file '" + file + "' not found.");
			MainForm.Err("File '" + file + "' not found.");
			return false;
		} catch(IOException e) {
			MainForm.LogErr("error while writing to the file '" + file + "'");
			MainForm.Err("Error while writing to the file '" + file + "'");
			e.printStackTrace();
			return false;
		}
		long finishTime = System.nanoTime();
		long elapsedTime = (finishTime - startTime)/1000000;
		MainForm.Log("elapsed execution time: " + elapsedTime + "ms");
		return jobDone;
	}

	public static boolean TokensToFile(Collection<DatSymbol> syms, File file) {
		MainForm.Log("start exporting tokens");
		MainForm.Indent(2);

		boolean ret = TokenstoFile(syms, file);

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
						MainForm.Err("Cannot export to lower or equal target-id at line " + cLN);
						return false;
					}
					if(cID >= MainForm.theDat.Symbols.length) {
						MainForm.LogErr("id is too high! at line " + cLN);
						MainForm.Err("Id is too high! at line " + cLN);
						return false;
					}
				}
				lastID = cID;
				parsed.add(Pair.getIS(cID, (tok.length >= 2 ? tok[1] : null)));
			} catch(Exception e) {
				MainForm.LogErr("cannot parse token '" + tok[0] + "' as number at line " + cLN);
				MainForm.Err("Cannot parse token '" + tok[0] + "' as number at line " + cLN);
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
