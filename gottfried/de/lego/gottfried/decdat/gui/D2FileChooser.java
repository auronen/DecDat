package de.lego.gottfried.decdat.gui;


import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import de.lego.gottfried.decdat.MainForm;
import de.lego.gottfried.decdat.util.DaedalusFileFilter;
import de.lego.gottfried.decdat.util.OUFileFilter;


public class D2FileChooser extends JFileChooser {
	private static final long	serialVersionUID	= -4195183042028555305L;
	private static File			lastFile			= null;

	public static File get(FileFilter f) {
		D2FileChooser c = new D2FileChooser(f, f == null ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
		if(c.showOpenDialog(MainForm.frmDecdat) == JFileChooser.APPROVE_OPTION)
			return lastFile = c.getSelectedFile();
		return null;
	}

	private D2FileChooser(FileFilter f, int mode) {
		super();
		setMultiSelectionEnabled(false);
		setFileFilter(f);
		setFileSelectionMode(mode);

		if(lastFile != null)
			setSelectedFile(lastFile);
		else
			setCurrentDirectory(new File("."));

		if(f == null)
			setDialogTitle("Please choose a target directory...");
		else if(f instanceof DaedalusFileFilter)
			setDialogTitle("Please choose script file...");
		else if(f instanceof OUFileFilter)
			setDialogTitle("Please choose a OU.bin file...");
		else
			setDialogTitle("Please choose a DAT file...");
	}
}
