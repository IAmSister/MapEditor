package tiled.mapeditor.dialogs;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import tiled.mapeditor.MapEditor;
import tiled.mapeditor.util.ObjectLayerInfo;
import tiled.mapeditor.widget.PrioritiesPanel;
import tiled.mapeditor.widget.VerticalStaticJPanel;

public class AddElementDialog extends JDialog implements ActionListener, MouseListener {

	private JButton okButton, bCancel;
	private JTextField newName;
	private JTable elements;
	private JTable priorites;
	private MapEditor editor;
	private PrioritiesPanel parrent;
	
	public AddElementDialog(MapEditor editor, PrioritiesPanel parrent) {
		this.editor = editor;
		this.parrent = parrent;
		this.init();
	}
	
	private JPanel createShapePanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		JScrollPane p1 = new JScrollPane();
		JScrollPane p2 = new JScrollPane();
		panel.add(p1);
		panel.add(p2);
		
		try
		{
			int elementsNum = this.editor.getAllObjects().size();
			Object[] names = this.editor.getAllObjects().keySet().toArray();
			System.out.println("names len = " + names.length);

			// LEFT TABLE
			Vector vcol = new Vector(); // 列名
			Vector vrow = new Vector(); // 内容
			// 用一个for循环来模拟数据库的内容
			vcol.addElement("element name");
			
			for (int row = 0; row < elementsNum; row++) {
				System.out.println("row = " + row);
				Vector vr1 = new Vector();
				vr1.addElement(names[row]);				
				vrow.addElement(vr1);
			}
			
			this.elements = new JTable(vrow, vcol);
			this.elements.addMouseListener(this);
			
			p1.getViewport().add(elements, null);
			
			// RIGHT TABLE
			Vector vcol2 = new Vector(); // 列名
			Vector vrow2 = new Vector(); // 内容
			// 用一个for循环来模拟数据库的内容
			vcol2.addElement("name");
			vcol2.addElement("value");
			
			this.priorites = new JTable(vrow2, vcol2);
			this.priorites.setEnabled(false);
			p2.getViewport().add(priorites, null);

		} catch (Exception ex) {
			System.out.println("错误：" + ex);
		}
		
		return panel;
	}

	protected void init() {
		this.setTitle("add an element in current layer");
		this.setSize(500,500);
		
        /* BUTTONS PANEL */
        okButton = new JButton("OK");
        bCancel = new JButton("Cancel");
        okButton.addActionListener(this);
        bCancel.addActionListener(this);
        
        JLabel label = new JLabel("New Name: ");
        this.newName = new JTextField("");
        
        JPanel buttons = new VerticalStaticJPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(Box.createGlue());
        buttons.add(label);
        buttons.add(this.newName);
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));
        buttons.add(okButton);
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));
        buttons.add(bCancel);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.add(createShapePanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(buttons);
		
		getContentPane().add(mainPanel);
		getRootPane().setDefaultButton(okButton);
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
        Object source = e.getSource();

        if (source == okButton) {
        	int selectedID = this.elements.getSelectedRow();
//        	System.out.println("selectedID = " + selectedID);
        	if (selectedID < 0) {
        		dispose();
        	}
        	
        	String oldName = (String)(this.elements.getValueAt(selectedID, 0));
        	String elementName = this.newName.getText(); 
        	
        	System.out.println(elementName);
			
        	// tell parent
        	this.parrent.updateTable1(true, elementName, oldName);
        	
        	dispose();
        }
        else if (source == bCancel) {
        	dispose();
        }
	}

	public void mouseClicked(MouseEvent e) {
		int selectedID = this.elements.getSelectedRow();
//    	System.out.println("selectedID = " + selectedID);
    	if (selectedID < 0) {
    		dispose();
    	}
    	
    	String elementName = (String)(this.elements.getValueAt(selectedID, 0));
    	System.out.println(elementName);
    	
    	this.newName.setText(elementName);
    	
    	// display all priorities in table 2
    	HashMap priorities = (HashMap)this.editor.getAllObjects().get(elementName);
    	System.out.println("priorities = " + priorities.toString());
    	Object[] arr = priorities.keySet().toArray();
    	
		Vector vcol2 = new Vector(); // 列名
		Vector vrow2 = new Vector(); // 内容
		// 用一个for循环来模拟数据库的内容
		vcol2.addElement("name");
		vcol2.addElement("value");
		
		for (int i=0; i<arr.length; i++) {
			Vector tmp = new Vector();
			tmp.addElement(arr[i]);
			tmp.addElement(priorities.get(arr[i]));
			vrow2.addElement(tmp);
		}
		
//		DefaultTableModel model = new DefaultTableModel(vrow2, vcol2);
//		this.priorites.setModel(model);
		((DefaultTableModel)this.priorites.getModel()).setDataVector(vrow2, vcol2);
		this.priorites.repaint();
    	
	}
	
	public void mouseEntered(MouseEvent e) {
		
	}
	
	public void mouseExited(MouseEvent e) {
		
	}
	
	public void mousePressed(MouseEvent e) {
		
	}
	
	public void mouseReleased(MouseEvent e) {
		
	}
}
