package tiled.mapeditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import tiled.mapeditor.MapEditor;
import tiled.mapeditor.dialogs.ResizeDialog;
import tiled.mapeditor.dialogs.ResizeDialog2;

public class ResizeAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MapEditor editor;

	public ResizeAction(MapEditor e){
		this.editor = e;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
//       new ResizeDialog2(editor);
		if(editor.getCurrentMap() == null){
			JOptionPane.showMessageDialog(editor.getAppFrame(), "请先创建地图");
			return;
		}
       ResizeDialog rd = new ResizeDialog((JFrame)editor.getAppFrame(), editor);
       rd.setVisible(true);
	}

}
