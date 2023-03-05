package tiled.mapeditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import tiled.mapeditor.MapEditor;
import tiled.mapeditor.dialogs.ScaleDialog;

@SuppressWarnings("serial")
public class ScaleAction extends AbstractAction {
	private MapEditor editor;

	public ScaleAction(MapEditor editor){
		this.editor = editor;
		this.putValue(AbstractAction.NAME, "缩放地图");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "将地图可视区域另存为缩略图");
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(editor.getCurrentMap() == null){
			JOptionPane.showMessageDialog(editor.getAppFrame(), "请先创建地图");
			return;
		}
		if(editor.getMapView().getBackgroundImage() == null){
			JOptionPane.showMessageDialog(editor.getAppFrame(), "请先设置背景");
			return;
		}
//    	NewMapDialog nmd = new NewMapDialog((JFrame)editor.getAppFrame());
		new ScaleDialog(editor);
	}

}
