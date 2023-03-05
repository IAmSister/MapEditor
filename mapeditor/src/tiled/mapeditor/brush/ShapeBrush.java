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

package tiled.mapeditor.brush;

import java.awt.*;
import java.awt.geom.*;

import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.core.MultilayerPlane;
import tiled.view.MapView;

/**
 * @version $Id: ShapeBrush.java,v 1.1 2007/10/17 07:56:37 gulei Exp $
 */
public class ShapeBrush extends AbstractBrush
{
    protected Area shape;
    protected Tile paintTile;

    public ShapeBrush() {
    }

    public ShapeBrush(Area shape) {
        this.shape = shape;
    }

    public ShapeBrush(AbstractBrush sb) {
        super(sb);
        if (sb instanceof ShapeBrush) {
            shape = ((ShapeBrush) sb).shape;
            paintTile = ((ShapeBrush) sb).paintTile;
        }
    }

    /**
     * Makes this brush a circular brush.
     *
     * @param rad the radius of the circular region
     */
    public void makeCircleBrush(double rad) {
        shape = new Area(new Ellipse2D.Double(0, 0, rad * 2, rad * 2));
        resize((int)(rad * 2), (int)(rad * 2), 0, 0);
    }

    /**
     * Makes this brush a rectangular brush.
     *
     * @param r a Rectangle to use as the shape of the brush
     */
    public void makeQuadBrush(Rectangle r) {
        shape = new Area(new Rectangle2D.Double(r.x, r.y, r.width, r.height));
        resize(r.width, r.height, 0, 0);
    }

    public void makePolygonBrush(Polygon p) {
    }

    public void setSize(int s) {
        if (shape.isRectangular()) {
            makeQuadBrush(new Rectangle(0, 0, s, s));
        } else if (!shape.isPolygonal()) {
            makeCircleBrush(s/2);
        } else {
            // TODO: scale the polygon brush
        }
    }

    public void setTile(Tile t) {
        paintTile = t;
    }

    public Tile getTile() {
        return paintTile;
    }

    public Rectangle getBounds() {
        return shape.getBounds();
    }

    public Shape getShape() {
    	return shape;
    }
    
    public boolean isRectangular() {
        return shape.isRectangular();
    }

    public void drawPreview(Graphics2D g2d, MapView mv) {
    	g2d.fill(shape);
        /*if (shape.isRectangular()) {
            Rectangle bounds = shape.getBounds();
            g2d.fillRect(sx, sy, bounds.width, bounds.height);
        } else if (!shape.isPolygonal()) {
            Rectangle bounds = shape.getBounds();
            g2d.fillOval(sx, sy, bounds.width, bounds.height);
        }*/
    }

    public boolean equals(Brush b) {
        if (b instanceof ShapeBrush) {
            return ((ShapeBrush)b).shape.equals(shape);
        }
        return false;
    }

    public void startPaint(MultilayerPlane mp, int x, int y, int button, int layer) {
        super.startPaint(mp, x, y, button, layer);
    }

    /**
     * Paints the entire area of the brush with the set tile. This brush can
     * affect several layers.
     * @throws Exception
     *
     * @see tiled.mapeditor.brush.Brush#doPaint(int, int)
     */
    public Rectangle doPaint(int x, int y) throws Exception
    {
        Rectangle bounds = shape.getBounds();
        int centerx = x - (bounds.width / 2);
        int centery = y - (bounds.height / 2);

        super.doPaint(x, y);

        // FIXME: This loop does not take all edges into account

        for(int l = 0; l < numLayers; l++) {
            TileLayer tl = (TileLayer)affectedMp.getLayer(initLayer + l);
            if (tl != null) {
                for (int i = 0; i <= bounds.height + 1; i++) {
                    for (int j = 0; j <= bounds.width + 1; j++) {
                        if (shape.contains(j, i)) {
                            tl.setTileAt(j + centerx, i + centery, paintTile);
                        }
                    }
                }
            }
        }

        // Return affected area
        return new Rectangle(centerx, centery, bounds.width, bounds.height);
    }
}
