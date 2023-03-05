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

import javax.swing.JOptionPane;

import tiled.mapeditor.MapEditor;
import tiled.mapeditor.Resources;
import tiled.util.TileMergeHelper;
import tiled.core.TileLayer;
import tiled.core.Map;

/**
 * Merges all layers of the map. Optionally it will create a new tileset with
 * merged tiles.
 *
 * @version $Id: MergeAllLayersAction.java,v 1.1 2007/10/17 07:56:37 gulei Exp $
 */
public class MergeAllLayersAction extends AbstractLayerAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MergeAllLayersAction(MapEditor editor) {
        super(editor,
              Resources.getString("action.layer.mergeall.name"),
              Resources.getString("action.layer.mergeall.tooltip"));
    }

    protected void doPerformAction() {
        Map map = editor.getCurrentMap();

        if (JOptionPane.showConfirmDialog(editor.getAppFrame(),
                                          "Do you wish to merge tile images, and create a new tile set?",
                                          "Merge Tiles?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION ) {
            TileMergeHelper tmh = new TileMergeHelper(map);
            int len = map.getTotalLayers();
            //TODO: Add a dialog option: "Yes, visible only"
            TileLayer newLayer = tmh.merge(0, len, true);
            map.removeAllLayers();
            map.addLayer(newLayer);
            newLayer.setName("Merged Layer");
            map.addTileset(tmh.getSet());
        } else {
            while (map.getTotalLayers() > 1) {
                map.mergeLayerDown(map.getTotalLayers() - 1);
            }
        }
        editor.setCurrentLayer(0);
    }
}