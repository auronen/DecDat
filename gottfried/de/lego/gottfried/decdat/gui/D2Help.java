package de.lego.gottfried.decdat.gui;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.lego.gottfried.decdat.MainForm;
import de.lego.gottfried.util.SimpleIO;

public class D2Help {
	public static void Open(String name) {
		String fileName = "Help_" + name + ".txt";
		byte[] content = SimpleIO.ReadAllBytes("res/" + fileName);

		if(content == null) {
			MainForm.Err("Konnte die gesuchte Hilfedatei nicht laden.");
			return;
		}

		try {
			File out = new File(fileName);
			FileOutputStream os = new FileOutputStream(out);
			os.write(content);
			os.close();
			Desktop.getDesktop().open(out);
		} catch(FileNotFoundException e) {
			MainForm.Err("Konnte die Hilfedatei nicht exportieren.");
		} catch(IOException e) {
			MainForm.Err("Konnte die Hilfedatei nicht anzeigen.");
		}

	}
}
