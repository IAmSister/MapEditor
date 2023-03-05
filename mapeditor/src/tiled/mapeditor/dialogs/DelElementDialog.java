package tiled.mapeditor.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import tiled.mapeditor.MapEditor;
import tiled.mapeditor.util.ObjectLayerInfo;
import tiled.mapeditor.widget.VerticalStaticJPanel;

public class DelElementDialog extends JDialog implements ActionListener {

	private JButton okButton, bCancel;
	private JTable elements;
	private MapEditor editor;
	private String layerName;
	
	public DelElementDialog(MapEditor editor, String layerName) {
		this.editor = editor;
		this.layerName = layerName;
		this.init();
	}
	
	private JPanel createShapePanel() {
		
		JPanel panel = new JPanel();
		JScrollPane p = new JScrollPane();
		panel.add(p);
		
		try
		{
			Vector vcol = new Vector(); // 列名
			Vector vrow = new Vector(); // 内容
			// 用一个for循环来模拟数据库的内容
			vcol.addElement("元素名称");

			ObjectLayerInfo layerInfo = (ObjectLayerInfo)this.editor.getMap().get(layerName);
			HashMap elementMap = layerInfo.getObjects();
			int elementNum = elementMap.size();

			Object[] names = elementMap.keySet().toArray();
			for (int row = 0; row < elementNum; row++) {
				Vector vr1 = new Vector();
				vr1.addElement(names[row]);				
				vrow.addElement(vr1);
			}
			
			this.elements = new JTable(vrow, vcol);
			// 滚动条设置左右滚
			this.elements.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			// 在滚动条中放入表
			p.getViewport().add(elements, null);

		} catch (Exception ex) {
			System.out.println("错误：" + ex);
		}
		
		return panel;
	}
	
	public void init() {
		this.setTitle("Select Element and Press OK");
		this.setSize(500,500);
		
        /* BUTTONS PANEL */
        okButton = new JButton("OK");
        bCancel = new JButton("Cancel");
        okButton.addActionListener(this);
        bCancel.addActionListener(this);
        
        JPanel buttons = new VerticalStaticJPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(Box.createGlue());
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
        	System.out.println("element selectedID = " + selectedID);
        	if (selectedID < 0) {
        		dispose();
        	}
        	String elementName = (String)(this.elements.getValueAt(selectedID, 0));
        	System.out.println(elementName);
        	
        	// delete from object map
        	ObjectLayerInfo layerInfo = (ObjectLayerInfo)this.editor.getMap().get(this.layerName);
        	if (layerInfo.getObjects().containsKey(elementName)) {
        		layerInfo.getObjects().remove(elementName);
        	}
        	
        	// delete an element
        	this.editor.getBelowPanel().updateTable1(false, elementName, elementName);
        	
            dispose();
        }
        else if (source == bCancel) {
            dispose();
        }

	}

}
