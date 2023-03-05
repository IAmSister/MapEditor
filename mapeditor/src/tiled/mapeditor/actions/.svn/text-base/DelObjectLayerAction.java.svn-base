package tiled.mapeditor.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import tiled.mapeditor.MapEditor;
import tiled.mapeditor.dialogs.DelObjectLayerDialog;
import tiled.mapeditor.widget.LayerEditorPanel;

public class DelObjectLayerAction extends AbstractAction {

	private MapEditor editor;
	private LayerEditorPanel panel;
	
	public DelObjectLayerAction(MapEditor editor, LayerEditorPanel panel) {
		this.editor = editor;
		this.panel = panel;
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		DelObjectLayerDialog dl = new DelObjectLayerDialog(this.editor, this.panel);
		dl.setVisible(true);
	}

}
