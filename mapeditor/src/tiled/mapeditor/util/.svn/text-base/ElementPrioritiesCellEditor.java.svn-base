package tiled.mapeditor.util;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import java.util.EventListener;

public class ElementPrioritiesCellEditor extends AbstractCellEditor implements
		TableCellEditor, CellEditorListener {

	private JComponent component = new JTextField();
	private String layerName;
	
	public ElementPrioritiesCellEditor(String layerName) {
		this.layerName = layerName;
		System.out.println("new ElementPrioritiesCellEditor, layerName = " + layerName);
		this.addCellEditorListener(this);
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value, 
			boolean isSelected,	int row, int column) 
 {
		// TODO Auto-generated method stub
		
		System.out.println("value =" + value.toString());
		System.out.println("isSelected = " + isSelected);
		System.out.println("pos = (" + column + ", " + row + ")");
		
		if (isSelected) {
            // cell (and perhaps other cells) are selected
			
        }

		return component;
	}

	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		System.out.println("in getCellEditorValue() : " + ((JTextField)component).getText());
		return ((JTextField)component).getText();
	}

	public void editingCanceled(ChangeEvent e) {
		System.out.println("cancel");
	}
	
	public void editingStopped(ChangeEvent e) {
		System.out.println("stop");
	}
}
