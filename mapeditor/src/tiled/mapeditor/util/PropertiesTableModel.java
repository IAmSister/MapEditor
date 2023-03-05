/*
 *  Tiled Map Editor, (c) 2004-2006
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  Adam Turk <aturk@biggeruniverse.com>
 *  Bjorn Lindeijer <b.lindeijer@xs4all.nl>
 */

package tiled.mapeditor.util;

import java.util.Properties;
import javax.swing.table.AbstractTableModel;

//import tiled.mapeditor.Resources;

/**
 * @version $Id: PropertiesTableModel.java,v 1.1 2007/10/17 07:56:37 gulei Exp $
 */
public class PropertiesTableModel extends AbstractTableModel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Properties properties;
    
    private static final String[] columnNames = { "����",//Resources.getString("dialog.properties.column.name"), 
    												"ֵ"};//Resources.getString("dialog.properties.column.value") };

    public PropertiesTableModel() {
        properties = new Properties();
    }

    public int getRowCount() {
        return properties.size() + 1;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Returns wether the given position in the table is editable. Values can
     * only be edited when they have a name.
     */
    public boolean isCellEditable(int row, int col) {
        return col == 0 || col == 1 && getValueAt(row, 0) != null;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object[] array = properties.keySet().toArray();
        if (rowIndex >= 0 && rowIndex < properties.size()) {
            if (columnIndex == 0) {
                return array[rowIndex];
            } else if (columnIndex == 1) {
                return properties.get(array[rowIndex]);
            }
        }
        return null;
    }

    public void setValueAt(Object value, int row, int col) {
        if (row >= 0) {
            if (row >= properties.size() && col == 0) {
                if (((String)value).length() > 0) {
                    properties.setProperty((String)value, "");
                    fireTableDataChanged();
                }
            } else {
                if (col == 1) {
                    properties.setProperty(
                            (String)getValueAt(row, 0), (String)value);
                    fireTableCellUpdated(row, col);
                } else if (col == 0) {
                    String val = (String)getValueAt(row, 1);
                    if (getValueAt(row, col) != null) {
                        properties.remove(getValueAt(row, col));
                    }
                    if (((String)value).length() > 0) {
                        properties.setProperty((String)value, val);
                    }
                    fireTableDataChanged();
                }
            }
        }
    }

    public void remove(Object key) {
        properties.remove(key);
        fireTableDataChanged();
    }

    public void update(Properties props) {
        properties = props;
        fireTableDataChanged();
    }

    public Properties getProperties() {
        return properties;
    }
}
