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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import tiled.mapeditor.MapEditor;
import tiled.mapeditor.util.ObjectLayerInfo;
import tiled.mapeditor.widget.LayerEditorPanel;
import tiled.mapeditor.widget.VerticalStaticJPanel;

public class AddObjectLayerDialog extends JDialog implements ActionListener{

	private JButton okButton, bCancel;
	private MapEditor editor;
	private JTextField text;
	private JTextArea des;
	private JComboBox combo;
	
	private LayerEditorPanel panel;
	
    private static final String SHAPE_TAB = "形状";//Resources.getString("dialog.brush.tab.shape");
    private static final String CUSTOM_TAB = "自定制";//Resources.getString("dialog.brush.tab.custom");
	
    public AddObjectLayerDialog(MapEditor editor, LayerEditorPanel panel) {
    	this.editor = editor;
    	this.panel = panel;
    	init();
    }
    
	private JPanel createShapePanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
		textPanel.add(new JLabel("物品层名称"));
		this.text = new JTextField("default", 20);
		textPanel.add(text);
		
		JPanel desPanel = new JPanel();
		desPanel.setLayout(new BoxLayout(desPanel, BoxLayout.X_AXIS));
		desPanel.add(new JLabel("物品层描述"));
		this.des = new JTextArea();
		desPanel.add(des);
		
		JPanel typePanel = new JPanel();
		typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.X_AXIS));
		typePanel.add(new JLabel("活动范围"));
		String[] types = {"全局", "部分"};
		this.combo = new JComboBox(types); 
		typePanel.add(combo);
		
		panel.add(textPanel);
		panel.add(Box.createRigidArea(new Dimension(10, 10)));
		panel.add(desPanel);
		panel.add(Box.createRigidArea(new Dimension(10, 10)));
		panel.add(typePanel);
		
		return panel;
	}
	
	public void init() {
		this.setTitle("Set Object Name and Type:");
		this.setSize(300, 200);
/*
		JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.addTab(SHAPE_TAB, createShapePanel());
        tabs.addTab(CUSTOM_TAB, new JPanel());
        tabs.setEnabledAt(1, false);
*/		
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
        Object source = e.getSource();

        if (source == okButton) {
        	String name = this.text.getText();
        	String des = this.des.getText();
        	int type = this.combo.getSelectedIndex();
	
        	if (!this.editor.getMap().containsKey(name)) {
        	
	        	ObjectLayerInfo objectLayer = new ObjectLayerInfo(name, des, type);
	        	this.editor.getMap().put(name, objectLayer);
	        	
	        	System.out.println("map size = " + this.editor.getMap().size());
	
	    		Vector vcol = new Vector(); // 列名
	    		Vector vrow = new Vector(); // 内容
	    		// 用一个for循环来模拟数据库的内容
	    		vcol.addElement("物品层名称");
	    		vcol.addElement("物品层描述");
	    		
	    		int layerNum = this.editor.getMap().size();
	    		Object[] names = this.editor.getMap().keySet().toArray();
	    		for (int row = 0; row < layerNum; row++) {
	    			Vector vr1 = new Vector();
	    			vr1.addElement(names[row]);
	    			vr1.addElement(((ObjectLayerInfo)(this.editor.getMap().get(names[row]))).getDes());
	    			
	    			vrow.addElement(vr1);
	    		}

	    		((DefaultTableModel)this.panel.getTable().getModel()).setDataVector(vrow, vcol);
	    		this.panel.getTable().repaint();
	        	
	        	this.editor.getBelowPanel().addTab(name);
	        	System.out.println("1");
	        	this.editor.getCurrentMap().addLayer();
	        	int mapLayerNum = this.editor.getCurrentMap().getLayerVector().size();
	        	System.out.println("mapLayerNum = " + mapLayerNum);
	        	this.editor.getCurrentMap().getLayer(mapLayerNum-1).setName(name);
        	}
        	
            dispose();
        }
        else if (source == bCancel) {
            dispose();
        }
	}
}
