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

import javax.swing.KeyStroke;

import tiled.mapeditor.MapEditor;
//import tiled.mapeditor.Resources;

/**
 * Exits the map editor.
 *
 * @version $Id: ExitAction.java,v 1.1 2007/10/17 07:56:37 gulei Exp $
 */
public class ExitAction extends AbstractFileAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExitAction(MapEditor editor, SaveAction saveAction) {
        super(editor, saveAction,
              "ÍË³ö",//Resources.getString("action.main.exit.name"),
              "ÍË³öµØÍ¼±à¼­Æ÷");//Resources.getString("action.main.exit.tooltip"));

        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
    }

    protected void doPerformAction() {
        editor.shutdown();
        System.exit(0);
    }
}
