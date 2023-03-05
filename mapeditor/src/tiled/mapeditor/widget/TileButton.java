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

package tiled.mapeditor.widget;

import java.awt.Image;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import tiled.core.Tile;
import tiled.mapeditor.Resources;

/**
 * @version $Id: TileButton.java,v 1.1 2007/10/17 07:56:38 gulei Exp $
 */
public class TileButton extends JButton
{
    private static final int ICON_SIZE = 22;
    private static final Icon DEFAULT_ICON = Resources.getIcon("empty.png");

    public TileButton() {
        setMargin(new Insets(0, 0, 0, 0));
        setIcon(DEFAULT_ICON);
    }

    public void setTile(Tile tile) {
        Icon icon = DEFAULT_ICON;

        if (tile != null && tile.getImage() != null) {
            Image tileImg = tile.getImage();
            int imgWidth = tileImg.getWidth(null);

            if (imgWidth > ICON_SIZE) {
                icon = new ImageIcon(tileImg.getScaledInstance(ICON_SIZE,
                        (tileImg.getHeight(null) * ICON_SIZE) / imgWidth,
                        Image.SCALE_SMOOTH));
            } else {
                icon = new ImageIcon(tileImg);
            }
        }

        setIcon(icon);
    }
}
