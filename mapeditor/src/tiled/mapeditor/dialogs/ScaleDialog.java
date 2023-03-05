package tiled.mapeditor.dialogs;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.prefs.Preferences;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import tiled.mapeditor.MapEditor;
import tiled.mapeditor.widget.IntegerSpinner;
import tiled.util.TiledConfiguration;

public class ScaleDialog extends JDialog implements ActionListener {

	// 初始值
    private final Preferences prefs = TiledConfiguration.node("dialog/scalemap");
	private MapEditor editor;

    // 文字
    private static final String DIALOG_TITLE = "缩放地图";
    private static final String SCALE_LABEL = "缩放比例(1~100)";
    private static final String QUALITY_LABEL = "图片质量(1~100)";
    private static final String OK_BUTTON = "确定";
    private static final String CANCEL_BUTTON = "取消";
    
    // 控件
    private IntegerSpinner scaleRate;
    private IntegerSpinner jpegQuality;

    public ScaleDialog(MapEditor editor){
    	super(editor.getAppFrame(), DIALOG_TITLE, true);
    	
    	this.editor = editor;
    	
    	this.init();
    	
    	super.pack();
    	super.setResizable(false);
    	super.setLocationRelativeTo(editor.getAppFrame());
    	
    	super.setVisible(true);
    }
    
    public void init(){
    	// 根据配置设置初始参数
        int defaultScaleRate = prefs.getInt("scaleRate", 50);
        int defaultQuality = prefs.getInt("imageQuality", 80);
        
        scaleRate = new IntegerSpinner(defaultScaleRate, 1);
        jpegQuality = new IntegerSpinner(defaultQuality, 1);
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout( new GridLayout(2, 2));
        inputPanel.add(new JLabel(SCALE_LABEL));
        inputPanel.add(scaleRate);
        inputPanel.add(new JLabel(QUALITY_LABEL));
        inputPanel.add(jpegQuality);

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
	        	scaleMap(scaleRate.intValue(), jpegQuality.intValue());
//	        	splitImage(imageWidth.intValue(), imageHeight.intValue(), jpegQuality.intValue());
	        	// 分割成功才保存设置
	        	prefs.putInt("scaleRate", scaleRate.intValue());
	        	prefs.putInt("imageQuality", jpegQuality.intValue());
	        }
		}
		catch(Exception e1){
			e1.printStackTrace();
		}
		super.dispose();
	}
	
	/**
	 * 将当前地图可视区域调整为指定的比例，并按照指定的质量保存
	 * 
	 * @param scaleRate
	 * @param jpegQuality
	 */
	private void scaleMap(int scaleRate, int jpegQuality){
        String startLocation = TiledConfiguration.node("recent").get("file0", "");

        JFileChooser ch = new JFileChooser(startLocation);
        
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
            	if(file.isDirectory()){
            		return true;
            	}
            	if(file.getName().length() < 4){
            		return false;
            	}
            	String ext = file.getName().substring(file.getName().length()-3);
            	if(ext.equalsIgnoreCase("JPG") || ext.equalsIgnoreCase("PNG") ||ext.equalsIgnoreCase("GIF")){
            		return true;
            	}
            	return false;
            }

			@Override
			public String getDescription() {
				return "Image File(*.jpg; *.png; *.gif)";
			}
        };

        ch.addChoosableFileFilter(fileFilter);

        int ret = ch.showSaveDialog(editor.getAppFrame());
        if(ch.getSelectedFile() != null){
        	// 保存路径
        	String path = ch.getSelectedFile().getParentFile().getAbsolutePath();
            TiledConfiguration.node("recent").put("file0", path);
			Image img = editor.getMapView().getBackgroundImage();
			if(img != null){
				// 检查图片尺寸和网格尺寸是否匹配
				int gridX = editor.getCurrentMap().getWidth();
				int gridY = editor.getCurrentMap().getHeight();
				int oldW = img.getWidth(null);
				int oldH = img.getHeight(null);
				Point coord = tile2RightBottom(gridX-1, gridY-1);
				if(coord.x > oldW || coord.y > oldH){
					JOptionPane.showMessageDialog(editor.getAppFrame(), "背景地图过小，边缘有空隙，请修改地图尺寸");
					return;
				}
				// 剪裁用尺寸
				oldW = coord.x;
				oldH = coord.y;

				int newW = oldW*scaleRate/100;
				int newH = oldH*scaleRate/100;
				
				// 先剪裁，然后缩放
				
				// 剪裁
				BufferedImage scaleImg = new BufferedImage(oldW, oldH, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = scaleImg.createGraphics();
				g2d.drawImage(img, 0, 0, oldW, oldH, 
											0, 0, oldW, oldH, null);
				// 缩放
//				float scale = scaleRate/100f;
//				AffineTransform tx = new AffineTransform();
//				tx.scale(scale, scale);
//				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				
//				scaleImg = op.filter(scaleImg, null);
				Image awtImage = scaleImg.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);

				// 剪裁
				BufferedImage bufferedImg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
				g2d = bufferedImg.createGraphics();
				g2d.drawImage(awtImage, 0, 0, newW, newH, 
											0, 0, newW, newH, null);
				
				ImageWriter writer = null;
				Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
				if(iter.hasNext()){
					writer = iter.next();
				}
				
				if(writer != null){
					ImageOutputStream ios;
					try {
						File file = ch.getSelectedFile();
						String p = file.getAbsolutePath();
						if(p.lastIndexOf(".jpg") != p.length()-4){
							p = p+".jpg";
							file = new File(p);
						}
						if(file.exists()){
							file.delete();
						}
						ios = ImageIO.createImageOutputStream(file);
						writer.setOutput(ios);
						ImageWriteParam iwparam = writer.getDefaultWriteParam();
						iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
						iwparam.setCompressionQuality(jpegQuality/100.0f);
						
						writer.write(null, new IIOImage(bufferedImg, null, null), iwparam);
						
						ios.flush();
						writer.dispose();
						ios.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
        	
        }
		
	}
	/**
	 * 返回tile坐标对应的网格的中点坐标
	 * @param x	tile所在列坐标
	 * @param y	tile所在行坐标
	 * @return
	 */
	private static final int TILE_WIDTH = 64;
	private static final int TILE_HEIGHT = 32;
	private static final int HALF_TILE_WIDTH = 32;
	private static final int HALF_TILE_HEIGHT = 16;
	public static Point tile2ImageCoord(int x, int y){
		Point rt;
        if(y%2 == 0){
        	rt = new Point(x*TILE_WIDTH, y*HALF_TILE_HEIGHT);
        }
        else{
        	rt = new Point(x*TILE_WIDTH+HALF_TILE_WIDTH, y*HALF_TILE_HEIGHT);
        }
        return rt;
		
	}
	
	/**
	 * 坐标，计算到右下角举行点像素
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static Point tile2RightBottom(int x, int y){
		Point rt;
        if(y%2 == 0){
        	rt = new Point(x*TILE_WIDTH+HALF_TILE_WIDTH, y*HALF_TILE_HEIGHT);
        }
        else{
        	rt = new Point(x*TILE_WIDTH+HALF_TILE_WIDTH, y*HALF_TILE_HEIGHT);
        }
        return rt;
	}

}
