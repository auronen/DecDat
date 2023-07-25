package de.lego.gottfried.decdat.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class TokenFileFilter extends FileFilter {
	public static TokenFileFilter	inst	= new TokenFileFilter();

	@Override
	public boolean accept(File f) {
		return f.isDirectory() || f.getAbsolutePath().toLowerCase().endsWith(".tok");
	}

	@Override
	public String getDescription() {
		return "Daedalus Token file (*.tok)";
	}
}
