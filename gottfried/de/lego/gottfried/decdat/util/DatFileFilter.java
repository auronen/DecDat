package de.lego.gottfried.decdat.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class DatFileFilter extends FileFilter {
	public static DatFileFilter	inst	= new DatFileFilter();

	@Override
	public boolean accept(File f) {
		return f.isDirectory() || f.getAbsolutePath().toLowerCase().endsWith(".dat");
	}

	@Override
	public String getDescription() {
		return "Kompilierte Daedalus-Scripte (*.dat)";
	}
}
