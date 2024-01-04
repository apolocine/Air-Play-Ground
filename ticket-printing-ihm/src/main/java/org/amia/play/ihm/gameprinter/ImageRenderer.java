package org.amia.play.ihm.gameprinter;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import org.amia.playground.utils.ImageUtil;

import java.awt.*;

public class ImageRenderer extends DefaultTableCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof ImageIcon) {
            JLabel label = new JLabel();
             //byte[] data =  (byte[]) value;new ImageIcon(data)
            ImageIcon icon = ImageUtil.resizeImageIcon((ImageIcon) value, 40, 40);
            label.setIcon(icon);
            return label;
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
