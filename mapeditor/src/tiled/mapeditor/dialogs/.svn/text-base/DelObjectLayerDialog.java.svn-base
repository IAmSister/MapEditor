package tiled.mapeditor.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.table.DefaultTableModel;

import tiled.mapeditor.MapEditor;
import tiled.mapeditor.widget.LayerEditorPanel;
import tiled.mapeditor.widget.VerticalStaticJPanel;
import tiled.mapeditor.util.ObjectLayerInfo;

public class DelObjectLayerDialog extends JDialog implements ActionListener {

	private JButton okButton, bCancel;
	private JTable layers;
	private MapEditor editor;
	LayerEditorPanel panel;
	
	public DelObjectLayerDialog(MapEditor editor, LayerEditorPanel panel) {
		this.editor = editor;
		this.panel = panel;
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
			vcol.addElement("物品层名称");
			vcol.addElement("物品层描述");

			int layerNum = this.editor.getMap().size();
			System.out.println("del: " + this.editor.getMap().size());
			Object[] names = this.editor.getMap().keySet().toArray();
			System.out.println("names len = " + names.length);
			System.out.println("names = " + names[0].toString());
			
			for (int row = 0; row < layerNum; row++) {
				System.out.println("row = " + row);
				Vector vr1 = new Vector();
				vr1.addElement(names[row]);
				vr1.addElement(((ObjectLayerInfo)(this.editor.getMap().get(names[row]))).getDes());
				
				vrow.addElement(vr1);
			}
			System.out.println("vrow size = " + vrow.size());
			
			this.layers = new JTable(vrow, vcol);
			// 滚动条设置左右滚
//			layers.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			// 在滚动条中放入表
			p.getViewport().add(layers, null);

		} catch (Exception ex) {
			System.out.println("错误：" + ex);
		}
		
		return panel;
	}
	
	public void init() {
		this.setTitle("Select Layer and Press OK");
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
        	int selectedID = this.layers.getSelectedRow();
        	System.out.println("selectedID = " + selectedID);
        	if (selectedID < 0) {
        		dispose();
        	}
        	String layerName = (String)(this.layers.getValueAt(selectedID, 0));
        	System.out.println(layerName);
        	// delete from object map
        	if (this.editor.getMap().containsKey(layerName)) {
        		this.editor.getMap().remove(layerName);
        	}

    		Vector vcol = new Vector(); // 列名
    		Vector vrow = new Vector(); // 内容
    		// 用一个for循环来模拟数据库的内容
    		vcol.addElement("物品层名称");
    		vcol.addElement("物品层描述");
    		
    		int layerNum = this.editor.getMap().size();
    		Object[] names = this.editor.getMap().keySet().toArray();
    		for (int row = 0; row < layerNum; row++) {
    			System.out.println("row = " + row);
    			Vector vr1 = new Vector();
    			vr1.addElement(names[row]);
    			vr1.addElement(((ObjectLayerInfo)(this.editor.getMap().get(names[row]))).getDes());
    			
    			vrow.addElement(vr1);
    		}
//    		this.panel.getTable().setModel(new DefaultTableModel(vrow, vcol));
    		((DefaultTableModel)this.panel.getTable().getModel()).setDataVector(vrow, vcol);
        	this.panel.getTable().repaint();
        	
        	this.editor.getBelowPanel().delTab(layerName);
        	this.editor.getCurrentMap().delLayer(layerName);
        	
            dispose();
        }
        else if (source == bCancel) {
            dispose();
        }
	}

}
