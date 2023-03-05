package tiled.mapeditor.actions;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import tiled.mapeditor.MapEditor;
import tiled.util.TiledConfiguration;

public class SaveThumbnailAction extends AbstractFileAction {

    public SaveThumbnailAction(MapEditor editor, SaveAction saveAction) {
        super(editor, saveAction,
        		"��������ͼ",
        		"�����ͼ����������ͼ"
        );

//        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
    }
	@Override
	protected void doPerformAction() {
		
		if(editor.getCurrentMap() == null){
			JOptionPane.showMessageDialog(editor.getAppFrame(), "���ȴ�����ͼ");
			return;
		}
		
		if(editor.getMapView().getBackgroundImage() == null){
			JOptionPane.showMessageDialog(editor.getAppFrame(), "�������ñ���");
			return;
		}
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
        	// ����·��
        	String path = ch.getSelectedFile().getParentFile().getAbsolutePath();
            TiledConfiguration.node("recent").put("file0", path);
			Image img = editor.getMapView().getBackgroundImage();
			if(img != null){
				// ���ͼƬ�ߴ������ߴ��Ƿ�ƥ��
				int gridX = editor.getCurrentMap().getWidth();
				int gridY = editor.getCurrentMap().getHeight();
				int oldW = img.getWidth(null);
				int oldH = img.getHeight(null);
				Point coord = tile2RightBottom(gridX-1, gridY-1);
				if(coord.x > oldW || coord.y > oldH){
					JOptionPane.showMessageDialog(editor.getAppFrame(), "������ͼ��С����Ե�п�϶�����޸ĵ�ͼ�ߴ�");
					return;
				}
				oldW = coord.x;
				oldH = coord.y;

				int newW = 0;
				int newH = 0;
				
				float rateWH = oldW*1.0f/oldH;
				if(rateWH > 4.0/3.0){
					// ̫���ˣ�����ˮƽ����С
					newW = 400;
					newH = (int)(newW/rateWH);
				}
				else{
					newH = 300;
					newW = (int)(newH*rateWH);
				}
				
				
				// �����ţ�Ȼ�����
				
				// ����
				BufferedImage scaleImg = new BufferedImage(oldW, oldH, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = scaleImg.createGraphics();
				g2d.drawImage(img, 0, 0, oldW, oldH, 
											0, 0, oldW, oldH, null);
				// ����
//				float scale = newW*1.0f/coord.x;
//				AffineTransform tx = new AffineTransform();
//				tx.scale(scale, scale);
//				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				
//				scaleImg = op.filter(scaleImg, null);
				Image awtImage = scaleImg.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
				// ����
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
//					ImageOutputStream ios;
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
						ImageIO.write(bufferedImg, "jpg", file);

/*						ios = ImageIO.createImageOutputStream(file);
						writer.setOutput(ios);
						ImageWriteParam iwparam = writer.getDefaultWriteParam();
						iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
						iwparam.setCompressionQuality(100/100.0f);
						
						writer.write(null, new IIOImage(bufferedImg, null, null), iwparam);
						
						ios.flush();
						writer.dispose();
						ios.close();
*/					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
        	
        }

	}

	/**
	 * ����tile�����Ӧ��������е�����
	 * @param x	tile����������
	 * @param y	tile����������
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
	 * ���꣬���㵽���½Ǿ��е�����
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
