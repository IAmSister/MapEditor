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

package tiled.view;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Stack;

import tiled.core.*;

/**
 * @version $Id: ObliqueMapView.java,v 1.1 2007/10/17 07:56:38 gulei Exp $
 */
public class ObliqueMapView extends MapView
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new oblique map view that displays the specified map.
     *
     * @param map the map to be displayed by this map view
     */
    public ObliqueMapView(Map map) {
        super(map);
    }

    public int getScrollableBlockIncrement(
            Rectangle visibleRect, int orientation, int direction)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getScrollableUnitIncrement(
            Rectangle visibleRect, int orientation, int direction)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public Dimension getPreferredSize() {
        // TODO Auto-generated method stub
        return new Dimension(0, 0);
    }

    protected void paintLayer(Graphics2D g2d, TileLayer layer) {
        // TODO Auto-generated method stub
    }

    protected void paintLayer(Graphics2D g2d, ObjectGroup og) {
        // TODO Auto-generated method stub
    }

    protected void paintGrid(Graphics2D g2d) {
        // TODO: Implement paintGrid for ObliqueMapView
    }

    protected void paintCoordinates(Graphics2D g2d) {
        // TODO: Implement paintCoordinates for ObliqueMapView
    }

    public Point screenToTileCoords(int x, int y) {
        // TODO Auto-generated method stub
        return null;
    }

    protected Polygon createGridPolygon(int tx, int ty, int border) {
        // TODO Auto-generated method stub
        return null;
    }

    public Point tileToScreenCoords(double x, double y) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void addNeighborPoint(Stack<Point> stack, Point p){
        stack.push(new Point(p.x, p.y - 1));
        stack.push(new Point(p.x, p.y + 1));
        stack.push(new Point(p.x + 1, p.y));
        stack.push(new Point(p.x - 1, p.y));
    }

}
