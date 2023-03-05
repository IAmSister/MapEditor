package tiled.mapeditor.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import tiled.mapeditor.MapEditor;

public class OpenElementFileAction extends AbstractAction {

	MapEditor editor;
	
	public OpenElementFileAction(MapEditor editor) {
		this.editor = editor;
		this.putValue(AbstractAction.NAME, "打开元素文件");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "打开描述基本元素的xls文件");
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(editor.getAppFrame());
		File file = chooser.getSelectedFile();
		if(file != null){
			// 打开文件
			try {
				this.editor.readAllObjects(file);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
