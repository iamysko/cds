/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class TableUtils.
 */
public class TableUtils {

	private static Logger log = LoggerFactory.getLogger(TableUtils.class);

	/**
	 * Creates the image from data.
	 *
	 * @param data        the data
	 * @param columnNames the column names
	 * @return the buffered image
	 */
	public static BufferedImage createImageFromData(final String[][] data, final String[] columnNames) {

		log.info("[Image Generation] Generating image for data {}", Arrays.toString(columnNames));

		final int maxWidths[] = new int[columnNames.length];
		Arrays.fill(maxWidths, 0);
		final TableModel model = new DefaultTableModel(data, columnNames);
		final JTable table = new JTable(model);
		final JTableHeader header = table.getTableHeader();

		for (int i = 0; i < columnNames.length; i++) {
			if (maxWidths[i] < columnNames[i].length()) {
				maxWidths[i] = columnNames[i].length();
			}
		}

		for (final String[] dataObject : data) {
			if (maxWidths.length == dataObject.length) {
				for (int i = 0; i < dataObject.length; i++) {
					if (maxWidths[i] < dataObject[i].length()) {
						maxWidths[i] = dataObject[i].length();
					}
				}
			} else {
				log.warn(
						"[Image Generation] Possible problem with image generation. maxWidths size: {} / dataObject size: {}",
						maxWidths.length, dataObject.length);
			}
		}

		log.info("[Image Generation] Max widths: {}", Arrays.toString(maxWidths));

		for (int i = 0; i < columnNames.length; i++) {
			table.getColumnModel().getColumn(i).setMinWidth(maxWidths[i]);
			table.getColumnModel().getColumn(i).setMaxWidth(maxWidths[i]);
			table.getColumnModel().getColumn(i).setPreferredWidth(maxWidths[i]);
			header.getColumnModel().getColumn(i).setMinWidth(maxWidths[i]);
			header.getColumnModel().getColumn(i).setMaxWidth(maxWidths[i]);
			header.getColumnModel().getColumn(i).setPreferredWidth(maxWidths[i]);
		}

		int totalWidth = 0;
		for (final int i : maxWidths) {
			totalWidth += i;
		}
		final int totalHeight = data.length * 10;
		table.setSize(totalWidth, totalHeight);
		header.setSize(totalWidth, 10);

		final BufferedImage tableImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2D = tableImage.createGraphics();
		header.paint(g2D);
		g2D.translate(0, header.getHeight());
		table.paint(g2D);
		g2D.dispose();

		log.info("[Image Generation] Returning image of dimensions {} x {}", totalWidth, totalHeight);

		return tableImage;

	}

}
