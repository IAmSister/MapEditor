package tiled.mapeditor.actions;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import tiled.mapeditor.MapEditor;
import tiled.mapeditor.Resources;
import tiled.util.TiledConfiguration;

public class SetBackAction extends AbstractFileAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String OPEN_ERROR_TITLE = Resources.getString("dialog.saveas.error.title");

    public SetBackAction(MapEditor editor, SaveAction saveAction) {
        super(editor, saveAction,
        		"设置背景",
        		"设置地图背景图片"
        );

//        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
    }

    @Override
	protected void doPerformAction() {
		if(editor.getCurrentMap() == null){
			JOptionPane.showMessageDialog(editor.getAppFrame(), "请先创建地图");
			return;
		}
    	// 打开对话框，选择图片文件
        //Start at the location of the most recently loaded map file
        String startLocation = TiledConfiguration.node("recent").get("file0", "");

        JFileChooser ch = new JFileChooser(startLocation);

/*            JOptionPane.showMessageDialog(editor.getAppFrame(),
                    "Error while loading plugins: " + e.getLocalizedMessage(),
                    OPEN_ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
*/

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

        int ret = ch.showOpenDialog(editor.getAppFrame());
        if (ret == JFileChooser.APPROVE_OPTION) {
        	String path = ch.getSelectedFile().getParentFile().getAbsolutePath();
            TiledConfiguration.node("recent").put("file0", path);
        	// 加载图片
            try {
				Image img = ImageIO.read(ch.getSelectedFile());
				editor.setBackground(img);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(editor.getAppFrame(),
                "Error while loading plugins: " + e.getLocalizedMessage(),
                OPEN_ERROR_TITLE,
                JOptionPane.ERROR_MESSAGE);
        		e.printStackTrace();

			}
            
        }
    	
    	
    	// 加载图片

	}

}
