package tiled.mapeditor.dialogs;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tiled.core.Map;
import tiled.mapeditor.MapEditor;
import tiled.mapeditor.widget.IntegerSpinner;

public class ResizeDialog2 extends JDialog implements ActionListener {

	private MapEditor editor;
	
    private static final String DIALOG_TITLE = "分割地图";
    private static final String WIDTH_LABEL = "地图宽度";
    private static final String HEIGHT_LABEL = "地图高度";
    private static final String OK_BUTTON = "确定";
    private static final String CANCEL_BUTTON = "取消";

    // 控件
    private IntegerSpinner imageWidth;
    private IntegerSpinner imageHeight;

    public ResizeDialog2(MapEditor editor){
    	super(editor.getAppFrame(), DIALOG_TITLE, true);

		this.editor = editor;
		
		this.init();
		
    	super.pack();
    	super.setResizable(false);
    	super.setLocationRelativeTo(editor.getAppFrame());
    	
    	super.setVisible(true);
		
	}

    public void init(){
    	if(editor.getCurrentMap() == null){
    		return;
    	}
    	// 根据配置设置初始参数
    	Map currentMap = editor.getCurrentMap();
        int defaultImageWidth = currentMap.getWidth();
        int defaultImageHeight = currentMap.getHeight();
        
        imageWidth = new IntegerSpinner(defaultImageWidth, 1);
        imageHeight = new IntegerSpinner(defaultImageHeight, 1);
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout( new GridLayout(3, 2));
        inputPanel.add(new JLabel(WIDTH_LABEL));
        inputPanel.add(imageWidth);
        inputPanel.add(new JLabel(HEIGHT_LABEL));
        inputPanel.add(imageHeight);

        JButton buttonOk = new JButton(OK_BUTTON);
        buttonOk.addActionListener(this);
        JButton buttonCancel = new JButton(CANCEL_BUTTON);
        buttonCancel.addActionListener(this);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(buttonOk);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(buttonCancel);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(Box.createGlue());
        mainPanel.add(buttonPanel);
        
        getContentPane().add(mainPanel);
        getRootPane().setDefaultButton(buttonOk);

  	
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		try{
	        if (e.getActionCommand().equals(OK_BUTTON)) {
	        	Map currentMap = editor.getCurrentMap();
	        	currentMap.resize(imageWidth.intValue(), imageHeight.intValue(), 0, 0);
	        	// TODO::修改currentMap的行列
	        }
		}
		catch(Exception e1){
			e1.printStackTrace();
		}
		super.dispose();
	}

}
