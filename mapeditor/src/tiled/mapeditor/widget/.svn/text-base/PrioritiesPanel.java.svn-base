package tiled.mapeditor.widget;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import tiled.core.MapLayer;
import tiled.mapeditor.MapEditor;
import tiled.mapeditor.dialogs.AddElementDialog;
import tiled.mapeditor.dialogs.DelObjectLayerDialog;
import tiled.mapeditor.util.ElementPrioritiesCellEditor;
import tiled.mapeditor.util.ObjectLayerInfo;
import tiled.mapeditor.util.PrioritiesTableModelListener;
//import tiled.mapeditor.util.SpinnerEditor;

public class PrioritiesPanel extends JScrollPane implements ActionListener, MouseListener {
	
	MapEditor md;
	JTabbedPane tabs;
	int selectedTabID;
	HashMap leftTableMap;
	HashMap rightTableMap;
	
	public PrioritiesPanel(MapEditor editor) {
		
		super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.setBorder(null);
		
		this.md = editor;
		this.tabs = new JTabbedPane(JTabbedPane.TOP);
		this.tabs.setEnabled(true);
		this.selectedTabID = -1;
		this.leftTableMap = new HashMap();
		this.rightTableMap = new HashMap();

		this.getViewport().add(tabs);

		this.md.setBelowPanel(this);
	}

	protected JPanel createPanel(String layerName) {
		JPanel panel = new JPanel();		
		panel.setLayout(new GridLayout(1, 2));

/*		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 1));

		buttonPanel.add(this.addButton);
		buttonPanel.add(this.delButton);
		panel.add(buttonPanel);
*/		
		JScrollPane pane1 = new JScrollPane();	// elements in current map layer, with two buttons, add/del element
		panel.add(pane1);
		JScrollPane right = new JScrollPane();	// current element's priorities
		panel.add(right);
		
		// ---------------
		// left table
		// ---------------
		// get mapeditor.objects
		HashMap objects = ((ObjectLayerInfo)this.md.getMap().get(layerName)).getObjects();
		int objectNum = objects.size();
		Object[] arr = objects.keySet().toArray();
		
		Vector vcol = new Vector(); // 列名
		Vector vrow = new Vector(); // 内容
		// 用一个for循环来模拟数据库的内容
		vcol.addElement("Object Name");
		for (int row = 0; row < objectNum; row++)
		{
			Vector vr1 = new Vector();
			vr1.addElement(arr[row]);
			vrow.addElement(vr1);
		}

		JTable jTable = new JTable(vrow, vcol);
		// 滚动条设置左右滚
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		// 在滚动条中放入表
		pane1.getViewport().add(jTable, null);
		jTable.addMouseListener(this);
		this.leftTableMap.put(layerName, jTable);

		// ---------------
		// right table
		// ---------------
		vcol = new Vector(); // 列名
		vrow = new Vector(); // 内容
		// 用一个for循环来模拟数据库的内容
		vcol.addElement("Name");
		vcol.addElement("Value");

		JTable jTable1 = new JTable(vrow, vcol);
		// 滚动条设置左右滚
		jTable.setAutoResizeMode(jTable1.AUTO_RESIZE_LAST_COLUMN);
		// 在滚动条中放入表
		right.getViewport().add(jTable1, null);
		jTable1.getModel().addTableModelListener(new PrioritiesTableModelListener(this.md));
		
//		TableColumn col = jTable1.getColumn("Value");
//		System.out.println("============" + col.getIdentifier().toString());
//		System.out.println(col.toString());
	
//		jTable1.setDefaultEditor(col.getClass(), new SpinnerEditor());
//		col.setCellEditor(new SpinnerEditor());
//		jTable1.getColumn("Value").setCellEditor(new ElementPrioritiesCellEditor(layerName));
		this.rightTableMap.put(layerName, jTable1);
		
		return panel;
	}
	
	public void addTab(String layerName) {
		System.out.println("in add Tab !!!!!!!");
		JPanel panel = createPanel(layerName);
		this.tabs.add(layerName, panel);
		this.tabs.repaint();
		this.tabs.addChangeListener(new TabChangeListener());
		System.out.println("this.tabs.getTabCount() = " + this.tabs.getTabCount());
		
		if(this.tabs.getTabCount() == 1) {
			this.selectedTabID = this.tabs.getSelectedIndex();
			
			System.out.println("this.selectedTabID = " + this.selectedTabID);
			
			if(this.md.getRoadEditing() == false) {
	        	String name = this.tabs.getTitleAt(this.selectedTabID);
	        	int layerIndex = md.getCurrentMap().getLayerIndex(name);
	        	md.setCurrentLayer(layerIndex);
				
				Vector allLayers = md.getCurrentMap().getLayerVector();
				((MapLayer)(allLayers.get(0))).setVisible(false);
				for (int i=1; i<allLayers.size(); i++) {
					if (i == layerIndex) 
						((MapLayer)(allLayers.get(i))).setVisible(true);
					else 
						((MapLayer)(allLayers.get(i))).setVisible(false);
				}
			}
		}
		System.out.println("current layer = " + md.getCurrentLayer().getName());
		updateUI();
	}
	
	public void delTab(String layerName) {
		
		int tabID = this.tabs.indexOfTab(layerName);
		System.out.println("tabID = " + tabID);
		this.leftTableMap.remove(layerName);
		this.rightTableMap.remove(layerName);
		this.tabs.removeTabAt(tabID);
		updateUI();
		
		this.selectedTabID = this.tabs.getSelectedIndex();
		
		if(this.md.getRoadEditing() == false) {
        	String name = this.tabs.getTitleAt(this.selectedTabID);
        	int layerIndex = md.getCurrentMap().getLayerIndex(name);
        	md.setCurrentLayer(layerIndex);
			
			Vector allLayers = md.getCurrentMap().getLayerVector();
			((MapLayer)(allLayers.get(0))).setVisible(false);
			for (int i=1; i<allLayers.size(); i++) {
				if (i == layerIndex) 
					((MapLayer)(allLayers.get(i))).setVisible(true);
				else 
					((MapLayer)(allLayers.get(i))).setVisible(false);
			}
		}

	}
	
	public void removeAllTab() {
		this.leftTableMap.clear();
		this.rightTableMap.clear();
		this.tabs.removeAll();
	}
	
	class TabChangeListener implements ChangeListener {
        public void stateChanged(ChangeEvent e){   
        	        	
        	JTabbedPane tabbedPane = (JTabbedPane)e.getSource();   
        	int selectedIndex = tabbedPane.getSelectedIndex();
        	if(selectedIndex < 0) {
        		return;
        	}
        	
        	String layerName = tabbedPane.getTitleAt(selectedIndex);

        	if (md.getRoadEditing() == false) {
	        	int layerIndex = md.getCurrentMap().getLayerIndex(layerName);
				md.setCurrentLayer(layerIndex);
				
				Vector allLayers = md.getCurrentMap().getLayerVector();
				((MapLayer)(allLayers.get(0))).setVisible(false);
				for (int i=1; i<allLayers.size(); i++) {
					if (i == layerIndex) {
						((MapLayer)(allLayers.get(i))).setVisible(true);
					}
					else { 
						((MapLayer)(allLayers.get(i))).setVisible(false);
					}
				}
        	}        	
        	
        	if (selectedIndex != PrioritiesPanel.this.selectedTabID) {

        		JPanel selected = (JPanel)tabbedPane.getComponentAt(selectedIndex);
        		
				HashMap objects = ((ObjectLayerInfo)PrioritiesPanel.this.md.getMap().get(layerName)).getObjects();
				int objectNum = objects.size();
				Object[] arr = objects.keySet().toArray();
			
				Vector vcol = new Vector(); // 列名
				Vector vrow = new Vector(); // 内容
				// 用一个for循环来模拟数据库的内容
				vcol.addElement("Object Name");
				
				for (int row = 0; row < objectNum; row++)
				{
					Vector vr1 = new Vector();
					vr1.addElement(arr[row]);
					vrow.addElement(vr1);
				}

				try {
					((DefaultTableModel)((JTable)PrioritiesPanel.this.leftTableMap.get(layerName)).getModel()).setDataVector(vrow, vcol);
					selected.repaint();
				}catch(Exception e1) {
					e1.printStackTrace();
				}
        	}
        	PrioritiesPanel.this.selectedTabID = selectedIndex;
        }
	}
	
	public void updateTable1(boolean addFlag, String elementName, String oldName) {
	
		String layerName = this.tabs.getTitleAt(selectedTabID);
		
		if (addFlag == true) {	// add an element
			
			// data structure change -- add element
			HashMap priorities = (HashMap)this.md.getAllObjects().get(oldName);
			HashMap newPriorities = (HashMap)priorities.clone();
			newPriorities.remove("Name");
			System.out.println("elementname = " + elementName);
			System.out.println("oldName = " + oldName);
			System.out.println("priorities = " + newPriorities.toString());
			((ObjectLayerInfo)this.md.getMap().get(layerName)).getObjects().put(elementName, newPriorities);
			
		}else {					// del an element
			
			((ObjectLayerInfo)this.md.getMap().get(layerName)).getObjects().remove(elementName);	
			
			// set right table to null
			Vector vcol = new Vector(); // 列名
			Vector vrow = new Vector(); // 内容
			// 用一个for循环来模拟数据库的内容
			vcol.addElement("Name");
			vcol.addElement("Value");
			((DefaultTableModel)((JTable)this.rightTableMap.get(layerName)).getModel()).setDataVector(vrow, vcol);
		}
		
		// new table model
		HashMap objects = ((ObjectLayerInfo)this.md.getMap().get(layerName)).getObjects();
		int objectNum = objects.size();
		Object[] arr = objects.keySet().toArray();
		Vector vcol = new Vector(); // 列名
		Vector vrow = new Vector(); // 内容
		// 用一个for循环来模拟数据库的内容
		vcol.addElement("Object Name");
		
		for (int row = 0; row < objectNum; row++)
		{
			Vector vr1 = new Vector();
			vr1.addElement(arr[row]);
			vrow.addElement(vr1);
		}
		
		System.out.println("vcol = " + vcol.toString());
		System.out.println("vrow = " + vrow.toString());

		try {
			JTable table = (JTable)this.leftTableMap.get(layerName);
			if (table == null) {
				System.out.println("table null");
			}
			((DefaultTableModel)table.getModel()).setDataVector(vrow, vcol);
//			table.setModel(df);
			this.tabs.repaint();
		}catch(Exception e1) {
			System.out.println(e1.toString());
			e1.printStackTrace();
		}
	}
	
	/* no use now */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
        Object source = e.getSource();
	}
	
	public void mouseClicked(MouseEvent e) {
		
		String layerName = this.tabs.getTitleAt(selectedTabID);
		JTable leftTable = (JTable)this.leftTableMap.get(layerName);
		
		int selectedID = leftTable.getSelectedRow();
    	System.out.println("selectedID = " + selectedID);
    	if (selectedID < 0) {
    		return;
    	}
    	
    	String elementName = (String)leftTable.getValueAt(selectedID, 0);
    	
    	HashMap priorities = (HashMap)((ObjectLayerInfo)this.md.getMap().get(layerName)).getObjects().get(elementName);
    	
    	// display priorities in right table
    	JTable rightTable = (JTable)this.rightTableMap.get(layerName);
		Object[] arr = priorities.keySet().toArray();
		Vector vcol = new Vector(); // 列名
		Vector vrow = new Vector(); // 内容
		// 用一个for循环来模拟数据库的内容
		vcol.addElement("Name");
		vcol.addElement("Value");
		
		for (int row = 0; row < arr.length; row++)
		{
			Vector vr1 = new Vector();
			vr1.addElement(arr[row]);
			vr1.addElement(priorities.get(arr[row]));
			vrow.addElement(vr1);
		}
//		DefaultTableModel df = new DefaultTableModel(vrow, vcol);
//		rightTable.setModel(df);
		((DefaultTableModel)rightTable.getModel()).setDataVector(vrow, vcol);
	}
	
	public void updateTable2(String layerName, String elementName) {
		
    	HashMap priorities = (HashMap)((ObjectLayerInfo)this.md.getMap().get(layerName)).getObjects().get(elementName);
    	
    	// display priorities in right table
    	JTable rightTable = (JTable)this.rightTableMap.get(layerName);
		Object[] arr = priorities.keySet().toArray();
		Vector vcol = new Vector(); // 列名
		Vector vrow = new Vector(); // 内容
		// 用一个for循环来模拟数据库的内容
		vcol.addElement("Name");
		vcol.addElement("Value");
		
		for (int row = 0; row < arr.length; row++)
		{
			Vector vr1 = new Vector();
			vr1.addElement(arr[row]);
			vr1.addElement(priorities.get(arr[row]));
			vrow.addElement(vr1);
		}
		((DefaultTableModel)rightTable.getModel()).setDataVector(vrow, vcol);		
	}
	
	public void mouseEntered(MouseEvent e) {
		
	}
	
	public void mouseExited(MouseEvent e) {
		
	}
	
	public void mousePressed(MouseEvent e) {
		
	}
	
	public void mouseReleased(MouseEvent e) {
		
	}
	
	public String getCurrentLayerName() {
		if (this.selectedTabID < 0) {
			return "";
		} else {
			String layerName = this.tabs.getTitleAt(this.selectedTabID);
			return layerName;
		}
	}
	
	public JTable getCurrentLeftTable() {
		if (this.selectedTabID < 0) {
			return null;
		} else {
			String layerName = this.tabs.getTitleAt(this.selectedTabID);
			return (JTable)this.leftTableMap.get(layerName);
		}
	}
	
	public String getCurrentObjectName() {
		String layerName = this.tabs.getTitleAt(selectedTabID);
		JTable leftTable = (JTable)this.leftTableMap.get(layerName);
		
		int selectedID = leftTable.getSelectedRow();
    	System.out.println("selectedID = " + selectedID);
    	if (selectedID < 0) {
    		return "";
    	}
    	
    	return (String)leftTable.getValueAt(selectedID, 0);
	}
}
