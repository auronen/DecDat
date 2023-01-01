package de.lego.gottfried.decdat.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class OUFileFilter extends FileFilter {
	public static OUFileFilter	inst	= new OUFileFilter();

	@Override
	public boolean accept(File f) {
		return f.isDirectory() || f.getAbsolutePath().toLowerCase().endsWith(".bin");
	}

	@Override
	public String getDescription() {
		return "Binary Output Units (*.bin)";
	}
}