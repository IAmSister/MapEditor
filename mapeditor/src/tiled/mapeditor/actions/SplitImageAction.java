package tiled.mapeditor.actions;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import tiled.mapeditor.MapEditor;
import tiled.mapeditor.dialogs.NewMapDialog;
import tiled.mapeditor.dialogs.SplitMapDialog;
import tiled.util.TiledConfiguration;

public class SplitImageAction extends AbstractAction {

	private MapEditor editor;
	private final static int CELL_WIDTH = 200;
	private final static int CELL_HEIGHT = 150;
	
	public SplitImageAction(MapEditor editor){
		this.editor = editor;
		this.putValue(AbstractAction.NAME, "分割图片");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "将地图分割为小图片");
	}
	
	public void actionPerformed(ActionEvent e) {
		if(editor.getCurrentMap() == null){
			JOptionPane.showMessageDialog(editor.getAppFrame(), "请先创建地图");
			return;
		}
		if(editor.getMapView().getBackgroundImage() == null){
			JOptionPane.showMessageDialog(editor.getAppFrame(), "请先设置背景");
			return;
		}
//    	NewMapDialog nmd = new NewMapDialog((JFrame)editor.getAppFrame());
		new SplitMapDialog(editor);
//		dialog.setVisible(true);

/*		// 选择目录，将图片分割，按照image_x_y,jpg保存
		// 尺寸固定为200*150
        String startLocation = TiledConfiguration.node("recent").get("splitdir", "");

        JFileChooser chooser = new JFileChooser(startLocation);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.showOpenDialog(editor.getAppFrame());
		File dir = chooser.getSelectedFile();
		if(dir != null){
			TiledConfiguration.node("recent").put("splitdir", dir.getPath());
			Image img = editor.getMapView().getBackgroundImage();
			
			BufferedImage bufferedImg = new BufferedImage(CELL_WIDTH, CELL_HEIGHT, BufferedImage.TYPE_INT_BGR);
			Graphics2D g2d = bufferedImg.createGraphics();
			int imgWidth = img.getWidth(null);
			int imgHeight = img.getHeight(null);
			for(int row=0; row<=imgHeight/CELL_HEIGHT; row++){
				for(int col=0; col<=imgWidth/CELL_WIDTH; col++){
					g2d.setColor(Color.BLACK);
					g2d.fillRect(0, 0, CELL_WIDTH, CELL_HEIGHT);
					
					int dx = imgWidth - CELL_WIDTH*col;
					int dy = imgHeight - CELL_HEIGHT*row;
					if(dx != 0 && dy != 0){
						if(dx >= CELL_WIDTH){
							if(dy >= CELL_HEIGHT){
								// 左上
								g2d.drawImage(img, 0, 0, CELL_WIDTH, CELL_HEIGHT, 
										col*CELL_WIDTH, row*CELL_HEIGHT, (col+1)*CELL_WIDTH, (row+1)*CELL_HEIGHT, null);
							}
							else{
								// 左下
								g2d.drawImage(img, 0, 0, CELL_WIDTH, dy, 
										col*CELL_WIDTH, row*CELL_HEIGHT, (col+1)*CELL_WIDTH, row*CELL_HEIGHT+dy, null);
								
							}
						}
						else{
							if(dy >= CELL_HEIGHT){
								// 右上
								g2d.drawImage(img, 0, 0, dx, CELL_HEIGHT, 
										col*CELL_WIDTH, row*CELL_HEIGHT, col*CELL_WIDTH+dx, (row+1)*CELL_HEIGHT, null);
							}
							else{
								// 右下
								g2d.drawImage(img, 0, 0, dx, dy, 
										col*CELL_WIDTH, row*CELL_HEIGHT, col*CELL_WIDTH+dx, row*CELL_HEIGHT+dy, null);
							}
						}
						File file = new File(dir, "map_"+col+"_"+row+".jpg");
						try {
							ImageIO.write(bufferedImg, "jpg", file);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
						
					}
					
				}
			}
			
			
			g2d.dispose();
		}
*/			
		
	}

}
