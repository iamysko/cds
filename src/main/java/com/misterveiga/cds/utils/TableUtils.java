/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.utils;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
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

		final int maxWidths[] = new int[columnNames.length];
		Arrays.fill(maxWidths, 0);
		final TableModel model = new DefaultTableModel(data, columnNames);
		final JTable table = new JTable(model);

		for (int i = 0; i < columnNames.length; i++) {
			if (maxWidths[i] < columnNames[i].length()) {
				maxWidths[i] = columnNames[i].length();
			}
		}

		for (int i = 0; i < data.length; i++) {
			for (final int c = 0; c < data[i].length; i++) {
				if (maxWidths[c] < data[i][c].length()) {
					maxWidths[c] = data[i][c].length();
				}
			}
		}

		for (int i = 0; i < columnNames.length; i++) {
			table.getColumnModel().getColumn(i).setMinWidth(maxWidths[i]);
			table.getColumnModel().getColumn(i).setMaxWidth(maxWidths[i]);
			table.getColumnModel().getColumn(i).setPreferredWidth(maxWidths[i]);
		}

		final JFrame frame = new JFrame();
		frame.getContentPane().add(new JScrollPane(table), BorderLayout.PAGE_END);

		table.setSize(800, 580);

		final JTableHeader header = table.getTableHeader();
		header.setSize(800, 20);

		final int totalWidth = header.getWidth() + table.getWidth();
		final int totalHeight = header.getHeight() + table.getHeight();

		final BufferedImage tableImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2D = tableImage.createGraphics();
//		header.paint(g2D);
//		g2D.translate(0, header.getHeight());
//		table.paint(g2D);
		frame.paint(g2D);
		g2D.dispose();

		return tableImage;

	}

}
