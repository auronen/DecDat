package de.lego.gottfried.decdat.gui;

import javax.swing.table.DefaultTableModel;

public class D2TableModel extends DefaultTableModel {
	private static final long	serialVersionUID	= -8717985588881529798L;

	public D2TableModel(String[] cells) {
		super(new Object[][] {}, cells);
	}

	public D2TableModel(Object[][] rows, String[] cells) {
		super(rows, cells);
	}

	public boolean isCellEditable(int row, int cell) {
		return false;
	}
}
