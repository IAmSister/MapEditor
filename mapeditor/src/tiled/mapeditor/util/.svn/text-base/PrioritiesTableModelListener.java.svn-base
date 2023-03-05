package tiled.mapeditor.util;

import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import tiled.mapeditor.MapEditor;

public class PrioritiesTableModelListener implements TableModelListener {
	
	MapEditor editor;
	
	public PrioritiesTableModelListener(MapEditor editor) {
		this.editor = editor;
	}
	
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		TableModel model = (TableModel)e.getSource();
		int fr = e.getFirstRow();
		int lr = e.getLastRow();
		int c = e.getColumn();
/*		
		System.out.println("table changed: type= " + e.getType());
		
		System.out.println("INSERT = " + e.INSERT);
		System.out.println("DELETE = " + e.DELETE);
		System.out.println("UPDATE = " + e.UPDATE);
		
		System.out.println("col = " + c);
		System.out.println("fr = " + fr);
		System.out.println("lr = " + lr);
*/		
		if(c == 1 && fr >= 0) {
			String n = (String)model.getValueAt(fr, c);
			System.out.println("change: (" + fr + ", " + c + ") = " + n);
			
			// change layer object data structure
			JTable table = this.editor.getBelowPanel().getCurrentLeftTable();
			int selectedID = table.getSelectedRow();
			String elementName = (String)table.getValueAt(selectedID, 0);
			String layerName = this.editor.getBelowPanel().getCurrentLayerName();
			
			HashMap priorities = (HashMap)((ObjectLayerInfo)this.editor.getMap().get(layerName)).getObjects().get(elementName);
			
			String priorityName = (String)model.getValueAt(fr, 0);
			priorities.remove(priorityName);
			priorities.put(priorityName, n);
			
			System.out.println("new priority = " + (HashMap)((ObjectLayerInfo)this.editor.getMap().get(layerName)).getObjects().get(elementName));
		}
	}

}
