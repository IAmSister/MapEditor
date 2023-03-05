package tiled.mapeditor.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import tiled.mapeditor.MapEditor;
import tiled.mapeditor.dialogs.AddObjectLayerDialog;
import tiled.mapeditor.widget.LayerEditorPanel;

public class AddObjectLayerAction extends AbstractAction {

	private MapEditor editor;
	private LayerEditorPanel panel;
	
	public AddObjectLayerAction(MapEditor editor, LayerEditorPanel panel) {
		this.editor = editor;
		this.panel = panel;
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		AddObjectLayerDialog dl = new AddObjectLayerDialog(this.editor, panel);
		dl.setVisible(true);
	}
}
