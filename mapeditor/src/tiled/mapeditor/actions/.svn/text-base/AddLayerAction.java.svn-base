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

import tiled.mapeditor.Resources;
import tiled.mapeditor.MapEditor;
import tiled.core.Map;

/**
 * Adds a layer to the current map and selects it.
 *
 * @version $Id: AddLayerAction.java,v 1.1 2007/10/17 07:56:37 gulei Exp $
 */
public class AddLayerAction extends AbstractLayerAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AddLayerAction(MapEditor editor) {
        super(editor,
              Resources.getString("action.layer.add.name"),
              Resources.getString("action.layer.add.tooltip"),
              Resources.getIcon("gnome-new.png"));
    }

    protected void doPerformAction() {
        Map currentMap = editor.getCurrentMap();
        currentMap.addLayer();
        editor.setCurrentLayer(currentMap.getTotalLayers() - 1);
    }
}
