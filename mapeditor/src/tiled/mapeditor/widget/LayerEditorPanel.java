package tiled.mapeditor.widget;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import tiled.core.MapLayer;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.mapeditor.MapEditor;
import tiled.mapeditor.actions.AddObjectLayerAction;
import tiled.mapeditor.actions.DelObjectLayerAction;
import tiled.mapeditor.actions.ResizeAction;
import tiled.mapeditor.actions.SaveAction;
import tiled.mapeditor.actions.SaveMapFileAction;
import tiled.mapeditor.actions.SaveThumbnailAction;
import tiled.mapeditor.actions.ScaleAction;
import tiled.mapeditor.actions.SetBackAction;
import tiled.mapeditor.actions.SplitImageAction;
import tiled.mapeditor.dialogs.AddElementDialog;
import tiled.mapeditor.dialogs.DelElementDialog;
import tiled.mapeditor.util.ObjectLayerInfo;
import javax.swing.table.DefaultTableModel;
/**
 * 层编辑器
 * 暂时支持两个功能
 * 1. 设置背景图片，将背景图片按照指定尺寸另存
 * 2. 设置能走的点，和半透明路点
 * @author jackflit
 *
 */
public class LayerEditorPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JComboBox combo;
	private String[] items;
	private CardLayout layout;	
	private MapEditor md;
	
	private JCheckBox cb1;
	private JCheckBox cb2;
	private JCheckBox cb3;
	private Image background;
	private JTable layersTable;
	
	private JButton addLayer;
	private JButton delLayer;
	private JButton addElement;
	private JButton delElement;
	private JButton getArea;
	
	public LayerEditorPanel(MapEditor editor, SaveAction action){
		
		this.md = editor;
		items = new String[]{"地图背景", "地图路点", "物品", "地图前景"};
		combo = new JComboBox(items);
		combo.setSelectedIndex(0);
		
		this.setLayout(new BorderLayout());
		this.add(combo, BorderLayout.NORTH);
		
		final JPanel cards = new JPanel();
		this.add(cards, BorderLayout.CENTER);

		combo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Object item = combo.getSelectedItem();
				layout.show(cards, (String)item);
/*
				if (((String)item).equals("NPC")) {
					md.setCurrentLayer(1);
				}else if (((String)item).equals("地图路点")) {
					md.setCurrentLayer(0);
				}
*/
			}
		});

		layout = new CardLayout();
		cards.setLayout( layout);
		
		JPanel backPanel = new JPanel();
		backPanel.setLayout(new GridLayout(3, 2));
		JButton setbutton = new JButton("添加背景");
		setbutton.addActionListener(new SetBackAction(md, action));
		backPanel.add(setbutton);
		JButton splitButton = new JButton("分割背景");
		splitButton.addActionListener(new SplitImageAction(md));
		backPanel.add(splitButton);
		JButton resizeButton = new JButton("调整尺寸");
		resizeButton.addActionListener(new ResizeAction(md));
		backPanel.add(resizeButton);
		JButton thumbButton = new JButton("缩略图");
		thumbButton.addActionListener(new SaveThumbnailAction(md, action));
		backPanel.add(thumbButton);
		JButton scaleButton = new JButton("缩放地图");
		scaleButton.addActionListener(new ScaleAction(md));
		backPanel.add(scaleButton);
		
//		JButton saveButton = new JButton("保存地图文件");
//		saveButton.addActionListener(new SaveMapFileAction(editor));
//		backPanel.add(saveButton);
		cards.add(backPanel, items[0]);
		
		JPanel pathPanel = new JPanel();
		
		JRadioButton b1 = new JRadioButton("设置路径");
		b1.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// 设置当前画刷状态为绘制路点
				
			}
		});
		pathPanel.add(b1);
		
		JRadioButton b2 = new JRadioButton("设置半透明路径");
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		pathPanel.add(b2);
		ButtonGroup bg = new ButtonGroup();
		bg.add(b1);
		bg.add(b2);
		bg.setSelected(b1.getModel(), true);
		
		cards.add(pathPanel, items[1]);

		JPanel npcPanel = new JPanel();
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		npcPanel.setLayout(gridBag);
		
		this.background = md.getBackground();
		cb1 = new JCheckBox("Background", true);
		cb3 = new JCheckBox("Road Points", true);	// Layer 0
		JPanel checkPanel = new JPanel();
		checkPanel.add(cb1);
		checkPanel.add(cb3);

		c.gridx = 0;
		c.gridy = 0;
		gridBag.setConstraints(checkPanel, c);
		npcPanel.add(checkPanel);

		ActionListener listener = new 
			ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					if (cb1.isSelected()) {
						if (md.getBackground() != null ) {
							background = md.getBackground();
						}else if (background != null) {
							md.setBackground(background);
						}
					}else {
						if (md.getBackground() != null ) {
							background = md.getBackground();
						}
						md.setBackground(null);
					}

					if (cb3.isSelected()){
						md.setRoadEditing(true);
						md.getCurrentMap().getLayer(0).setVisible(true);
						md.setCurrentLayer(0);
						Vector allLayers = md.getCurrentMap().getLayerVector();
						for (int i=1; i<allLayers.size(); i++) {
							((MapLayer)(allLayers.get(i))).setVisible(false);
						}
						
						getArea.setEnabled(false);
					}else {
						md.setRoadEditing(false);
						md.getCurrentMap().getLayer(0).setVisible(false);
						String layerName = md.getBelowPanel().getCurrentLayerName();
						System.out.println("layerName = " + layerName);
						if (!"".equals(layerName)) {
							int layerIndex = md.getCurrentMap().getLayerIndex(layerName);
							md.setCurrentLayer(layerIndex);
							Vector allLayers = md.getCurrentMap().getLayerVector();
							for (int i=1; i<allLayers.size(); i++) {
								if (i == layerIndex) 
									((MapLayer)(allLayers.get(i))).setVisible(true);
								else 
									((MapLayer)(allLayers.get(i))).setVisible(false);
							}
						}
						
						getArea.setEnabled(true);
					}
				}
			};

		cb1.addActionListener(listener);
		cb3.addActionListener(listener);
		
		this.addLayer = new JButton("Add Layer");
		this.delLayer = new JButton("Del Layer");
		this.addElement = new JButton("Add Element");
		this.delElement = new JButton("Del Element");
		this.getArea = new JButton ("Get Area");
		this.getArea.setEnabled(false);

		addLayer.addActionListener(new AddObjectLayerAction(md, this));
		delLayer.addActionListener(new DelObjectLayerAction(md, this));
		addElement.addActionListener(this);
		delElement.addActionListener(this);
		getArea.addActionListener(this);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.0; // 当窗口放大时，长度不变
		c.weighty = 0.0;
		gridBag.setConstraints(addLayer, c);
		npcPanel.add(addLayer);

		c.gridx = 0;
		c.gridy = 2;
		gridBag.setConstraints(delLayer, c);
		npcPanel.add(delLayer);
		
		c.gridx = 0;
		c.gridy = 3;
		gridBag.setConstraints(addElement, c);
		npcPanel.add(addElement);

		c.gridx = 0;
		c.gridy = 4;
		gridBag.setConstraints(delElement, c);
		npcPanel.add(delElement);
		
		c.gridx = 0;
		c.gridy = 5;
		gridBag.setConstraints(getArea, c);
		npcPanel.add(getArea);	
		
		JScrollPane layersPane = new JScrollPane();
		
		Vector vcol = new Vector(); // 列名
		Vector vrow = new Vector(); // 内容
		// 用一个for循环来模拟数据库的内容
		vcol.addElement("物品层名称");
		vcol.addElement("物品层描述");
		
		int layerNum = this.md.getMap().size();
		Object[] names = this.md.getMap().keySet().toArray();
		for (int row = 0; row < layerNum; row++) {
			System.out.println("row = " + row);
			Vector vr1 = new Vector();
			vr1.addElement(names[row]);
			vr1.addElement(((ObjectLayerInfo)(this.md.getMap().get(names[row]))).getDes());
			
			vrow.addElement(vr1);
		}

		this.layersTable = new JTable(vrow, vcol);
		this.layersTable.setEnabled(false);
		layersPane.getViewport().add(this.layersTable, null);
		
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		c.gridheight = 5;
		c.weightx = 1.0; // 当窗口放大时，长度变化
		c.weighty = 1.0;
		gridBag.setConstraints(layersPane, c);
		npcPanel.add(layersPane);

		cards.add(npcPanel, items[2]);
		
		JPanel forePanel = new JPanel();
		forePanel.add(new JLabel("fore"), BorderLayout.NORTH);
		cards.add(forePanel, items[3]);
		
		this.md.setLayerEditorPanel(this);
	}
	
	public JTable getTable() {
		return this.layersTable;
	}
	
	public void updateTable() {
		Vector vcol = new Vector(); // 列名
		Vector vrow = new Vector(); // 内容
		// 用一个for循环来模拟数据库的内容
		vcol.addElement("物品层名称");
		vcol.addElement("物品层描述");
		
		int layerNum = this.md.getMap().size();
		Object[] names = this.md.getMap().keySet().toArray();
		for (int row = 0; row < layerNum; row++) {
			Vector vr1 = new Vector();
			vr1.addElement(names[row]);
			vr1.addElement(((ObjectLayerInfo)(this.md.getMap().get(names[row]))).getDes());
			
			vrow.addElement(vr1);
		}

		((DefaultTableModel)this.layersTable.getModel()).setDataVector(vrow, vcol);
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
        Object source = e.getSource();
        if (source == this.addElement) {
        	AddElementDialog dl = new AddElementDialog(this.md, this.md.getBelowPanel());
    		dl.setVisible(true);
        }else if(source == this.delElement) {
        	DelElementDialog dl = new DelElementDialog(this.md, this.md.getBelowPanel().getCurrentLayerName());
    		dl.setVisible(true);
        }else if(source == this.getArea) {
        	System.out.println("getArea action here!");
        	
        	MapLayer currentLayer = this.md.getCurrentLayer();
        	System.out.println("currentLayer = " + currentLayer.getName());
        	
//        	if(currentLayer instanceof TileLayer) { }
    		int layerWidth = ((TileLayer)currentLayer).getWidth();
    		int layerHeight = ((TileLayer)currentLayer).getHeight();
    		System.out.println(layerWidth + ", " + layerHeight);
    		byte[] all = new byte[(layerWidth+1)*layerHeight];
    		
    		for (int i=0; i<layerHeight; i++) {	
    			for (int j=0; j<layerWidth; j++) {
    				
    				Tile tile = ((TileLayer)currentLayer).getTileAt(j, i);
    				if(tile != null && "tile.area.normal".equals(tile.getName())) 
    					all[i*(layerWidth+1) + j] = (byte)'1';
    				else 
    					all[i*(layerWidth+1) + j] = (byte)'0';
    			}
    			all[i*(layerWidth+1) + layerWidth] = (byte)',';
    		}
    		
    		String area = new String(all);
    		
    		String layerName = this.md.getBelowPanel().getCurrentLayerName();
    		System.out.println(area);
    		System.out.println(layerName);
    		
    		if(!"".equals(layerName)) {
	    		HashMap objects = (HashMap)((ObjectLayerInfo)this.md.getMap().get(layerName)).getObjects();
	    		Object[] names = objects.keySet().toArray();
	    		for(int i=0; i<names.length; i++) {
		     		HashMap priorities = (HashMap)objects.get(names[i]);
		    		priorities.remove("Area");
		    		priorities.put("Area", area);  			
	    		}
	    		
	    		String objectName = this.md.getBelowPanel().getCurrentObjectName();
	    		System.out.println("current object = " + objectName);
	    		if(!"".equals(objectName)) {
		    		this.md.getBelowPanel().updateTable2(layerName, objectName);
	    		}
    		}
        }
	}
}
