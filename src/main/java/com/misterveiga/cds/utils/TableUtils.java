/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

/**
 * The Class TableUtils.
 */
public class TableUtils {

	/**
	 * Creates the image from data.
	 *
	 * @param data        the data
	 * @param columnNames the column names
	 * @return the buffered image
	 */
	public static BufferedImage createImageFromData(final String[][] data, final String[] columnNames) {

		final TableModel model = new DefaultTableModel(data, columnNames);

		final JTable table = new JTable(model);

		table.doLayout();

		final JTableHeader header = table.getTableHeader();
		final int totalWidth = header.getWidth() + table.getWidth();
		final int totalHeight = header.getHeight() + table.getHeight();

		final BufferedImage tableImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2D = tableImage.createGraphics();
		header.paint(g2D);
		g2D.translate(0, header.getHeight());
		table.paint(g2D);

		return tableImage;

	}

}
