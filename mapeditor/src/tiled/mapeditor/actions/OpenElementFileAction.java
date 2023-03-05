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
		this.putValue(AbstractAction.NAME, "��Ԫ���ļ�");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "����������Ԫ�ص�xls�ļ�");
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(editor.getAppFrame());
		File file = chooser.getSelectedFile();
		if(file != null){
			// ���ļ�
			try {
				this.editor.readAllObjects(file);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
