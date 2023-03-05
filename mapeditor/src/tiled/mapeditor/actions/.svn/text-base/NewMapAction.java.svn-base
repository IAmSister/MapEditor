/*
 *  Tiled Map Editor, (c) 2004-2006
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  Adam Turk <aturk@biggeruniverse.com>
 *  Bjorn Lindeijer <b.lindeijer@xs4all.nl>
 */

package tiled.mapeditor.actions;

import javax.swing.JFrame;
import javax.swing.KeyStroke;

import tiled.core.Map;
import tiled.mapeditor.MapEditor;
//import tiled.mapeditor.Resources;
import tiled.mapeditor.dialogs.NewMapDialog;

/**
 * Creates a new map.
 *
 * @version $Id: NewMapAction.java,v 1.1 2007/10/17 07:56:37 gulei Exp $
 */
public class NewMapAction extends AbstractFileAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NewMapAction(MapEditor editor, SaveAction saveAction) {
        super(editor, saveAction,
              "新建...",//Resources.getString("action.map.new.name"),
              "创建新地图");//Resources.getString("action.map.new.tooltip"));

        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
    }

    protected void doPerformAction() {
    	NewMapDialog nmd = new NewMapDialog((JFrame)editor.getAppFrame());
        Map newMap = nmd.create();
        if (newMap != null) {
            editor.setCurrentMap(newMap);
        }
    }
}
