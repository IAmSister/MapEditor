package tiled.mapeditor.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.prefs.Preferences;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tiled.mapeditor.MapEditor;
import tiled.mapeditor.widget.IntegerSpinner;
import tiled.util.TiledConfiguration;

public class SplitMapDialog extends JDialog implements ActionListener {
	
	// ��ʼֵ
    private final Preferences prefs = TiledConfiguration.node("dialog/splitmap");
	private MapEditor editor;

    // ����
    private static final String DIALOG_TITLE = "�ָ��ͼ";
    private static final String WIDTH_LABEL = "ͼƬ���";
    private static final String HEIGHT_LABEL = "ͼƬ�߶�";
    private static final String QUALITY_LABEL = "ͼƬ����(1~100)";
    private static final String OK_BUTTON = "ȷ��";
    private static final String CANCEL_BUTTON = "ȡ��";
    
    // �ؼ�
    private IntegerSpinner imageWidth;
    private IntegerSpinner imageHeight;
    private IntegerSpinner jpegQuality;


    public SplitMapDialog(MapEditor editor){
    	super(editor.getAppFrame(), DIALOG_TITLE, true);
    	
    	this.editor = editor;
    	
    	this.init();
    	
    	super.pack();
    	super.setResizable(false);
    	super.setLocationRelativeTo(editor.getAppFrame());
    	
    	super.setVisible(true);
    }
    
    public void init(){
    	// �����������ó�ʼ����
        int defaultImageWidth = prefs.getInt("imageWidth", 200);
        int defaultImageHeight = prefs.getInt("imageHeight", 150);
        int defaultQuality = prefs.getInt("imageQuality", 80);
        
        imageWidth = new IntegerSpinner(defaultImageWidth, 1);
        imageHeight = new IntegerSpinner(defaultImageHeight, 1);
        jpegQuality = new IntegerSpinner(defaultQuality, 1);
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout( new GridLayout(3, 2));
        inputPanel.add(new JLabel(WIDTH_LABEL));
        inputPanel.add(imageWidth);
        inputPanel.add(new JLabel(HEIGHT_LABEL));
        inputPanel.add(imageHeight);
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
	        	splitImage(imageWidth.intValue(), imageHeight.intValue(), jpegQuality.intValue());
	        	// �ָ�ɹ��ű�������
	        	prefs.putInt("imageWidth", imageWidth.intValue());
	        	prefs.putInt("imageHeight", imageHeight.intValue());
	        	prefs.putInt("imageQuality", jpegQuality.intValue());
	        }
		}
		catch(Exception e1){
			e1.printStackTrace();
		}
		super.dispose();
	}
	
	private void splitImage(int width, int height, int quality){
		// ѡ��Ŀ¼����ͼƬ�ָ����image,x,y,jpg����
		// �ߴ�̶�Ϊ200*150
        String startLocation = TiledConfiguration.node("recent").get("file0", "");
//        String startLocation = prefs.get("splitdir", "");

        JFileChooser chooser = new JFileChooser(startLocation);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.showOpenDialog(this.getParent());
		File dir = chooser.getSelectedFile();
		if(dir != null){
			dir.mkdirs();
//			prefs.put("splitdir", dir.getPath());
//        	String path = ch.getSelectedFile().getAbsolutePath();
            TiledConfiguration.node("recent").put("file0", chooser.getSelectedFile().getAbsolutePath());
			Image img = editor.getMapView().getBackgroundImage();
			
			BufferedImage bufferedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
			Graphics2D g2d = bufferedImg.createGraphics();
			int imgWidth = img.getWidth(null);
			int imgHeight = img.getHeight(null);
			for(int row=0; row<=imgHeight/height; row++){
				for(int col=0; col<=imgWidth/width; col++){
					g2d.setColor(Color.BLACK);
					g2d.fillRect(0, 0, width, height);
					
					int dx = imgWidth - width*col;
					int dy = imgHeight - height*row;
					if(dx != 0 && dy != 0){
						if(dx >= width){
							if(dy >= height){
								// ����
								g2d.drawImage(img, 0, 0, width, height, 
										col*width, row*height, (col+1)*width, (row+1)*height, null);
							}
							else{
								// ����
								g2d.drawImage(img, 0, 0, width, dy, 
										col*width, row*height, (col+1)*width, row*height+dy, null);
								
							}
						}
						else{
							if(dy >= height){
								// ����
								g2d.drawImage(img, 0, 0, dx, height, 
										col*width, row*height, col*width+dx, (row+1)*height, null);
							}
							else{
								// ����
								g2d.drawImage(img, 0, 0, dx, dy, 
										col*width, row*height, col*width+dx, row*height+dy, null);
							}
						}
						File file = new File(dir, "map_"+col+"_"+row+".jpg");
						if(file.exists()){
							file.delete();
						}
						try {
							// �򵥵ı���
//							ImageIO.write(bufferedImg, "jpg", file);
							
							// ��������JPEG�����ı���
							ImageWriter writer = null;
							Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
							if(iter.hasNext()){
								writer = iter.next();
							}
							
							if(writer != null){
								ImageOutputStream ios = ImageIO.createImageOutputStream(file);
								writer.setOutput(ios);
								ImageWriteParam iwparam = writer.getDefaultWriteParam();
								iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
								iwparam.setCompressionQuality(quality/100.0f);
								
								writer.write(null, new IIOImage(bufferedImg, null, null), iwparam);
								
								ios.flush();
								writer.dispose();
								ios.close();
							}
							
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
						
					}
					
				}
			}
			
			
			g2d.dispose();
		}
		
	}

}
