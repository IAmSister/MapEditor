package tiled.mapeditor.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import tiled.mapeditor.MapEditor;

public class ElementChooseDialog extends JDialog implements ActionListener {

	private MapEditor editor;
	
	public ElementChooseDialog(MapEditor editor) {
		this.editor = editor;
		this.init();
	}
	
	public void init() {
		// read local xls file to load all elements
		
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
