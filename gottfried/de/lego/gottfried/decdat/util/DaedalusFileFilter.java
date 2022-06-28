package de.lego.gottfried.decdat.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class DaedalusFileFilter extends FileFilter {
	public static DaedalusFileFilter	inst	= new DaedalusFileFilter();

	@Override
	public boolean accept(File f) {
		return f.isDirectory() || f.getAbsolutePath().toLowerCase().endsWith(".d");
	}

	@Override
	public String getDescription() {
		return "Daedalus-Script (*.d)";
	}
}
