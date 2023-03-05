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

package tiled.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Vector;
import javax.swing.event.EventListenerList;

import tiled.mapeditor.Resources;
import tiled.mapeditor.util.MapChangeListener;
import tiled.mapeditor.util.MapChangedEvent;

/**
 * The Map class is the focal point of the <code>tiled.core</code> package.
 * This class also handles notifing listeners if there is a change to any layer
 * or object contained by the map.
 * 
 * ��ͼ������ģ�ͣ��൱��MVC�е�Model��MapView����ͼ
 *
 * @version $Id: Map.java,v 1.2 2007/11/13 06:41:54 chaiyp Exp $
 */
public class Map extends MultilayerPlane
{
    /** Orthogonal. */
    public static final int MDO_ORTHO   = 1;
    /** Isometric. */
    public static final int MDO_ISO     = 2;
    /** Oblique. */
    public static final int MDO_OBLIQUE = 3;
    /** Hexagonal. */
    public static final int MDO_HEX     = 4;
    /** Shifted (used for iso and hex). */
    public static final int MDO_SHIFTED = 5;
    /** Staggered Isometric */
    public static final int MDO_STAGGERED = 6;

    private Vector<MapLayer> specialLayers;
    private Vector<TileSet> tilesets;
    private LinkedList<MapObject> objects;

    private int tileWidth, tileHeight;
    private int totalObjects;
    private int orientation = MDO_ORTHO;
    private EventListenerList mapChangeListeners;
    private Properties properties;
    private String filename;

    /**
     * @param width  the map width in tiles.
     * @param height the map height in tiles.
     */
    public Map(int width, int height) {
        super(width, height);

        mapChangeListeners = new EventListenerList();
        properties = new Properties();
        tilesets = new Vector<TileSet>();
        specialLayers = new Vector<MapLayer>();
        objects = new LinkedList<MapObject>();
    }

    /**
     * Adds a change listener. The listener will be notified when the map
     * changes in certain ways.
     *
     * @param l the change listener to add
     * @see MapChangeListener#mapChanged(MapChangedEvent)
     */
    public void addMapChangeListener(MapChangeListener l) {
        mapChangeListeners.add(MapChangeListener.class, l);
    }

    /**
     * Removes a change listener.
     * @param l
     */
    public void removeMapChangeListener(MapChangeListener l) {
        mapChangeListeners.remove(MapChangeListener.class, l);
    }

    /**
     * Notifies all registered map change listeners about a change.
     */
    protected void fireMapChanged() {
        Object[] listeners = mapChangeListeners.getListenerList();
        MapChangedEvent event = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == MapChangeListener.class) {
                if (event == null) event = new MapChangedEvent(this);
                ((MapChangeListener)listeners[i + 1]).mapChanged(event);
            }
        }
    }

    /**
     * Causes a MapChangedEvent to be fired.
     */
    public void touch() {
        fireMapChanged();
    }

    public void addLayerSpecial(MapLayer l) {
        l.setMap(this);
        specialLayers.add(l);
        fireMapChanged();
    }

    public MapLayer addLayer(MapLayer l) {
        l.setMap(this);
        super.addLayer(l);
        fireMapChanged();
        return l;
    }

    /**
     * Create a new empty TileLayer with the dimensions of the map. By default,
     * the new layer's name is set to "Layer [layer index]"
     *
     * @return The new TileLayer instance.
     */
    public MapLayer addLayer() {
        MapLayer layer = new TileLayer(this, bounds.width, bounds.height);
        layer.setName(Resources.getString("general.layer.layer") + " " + super.getTotalLayers());
//        System.out.println("MapLayerName = " + Resources.getString("general.layer.layer") + " " + super.getTotalLayers());
        super.addLayer(layer);
        fireMapChanged();
        return layer;
    }
    
    public void setLayer(int index, MapLayer l) {
        l.setMap(this);
        super.setLayer(index, l);
        fireMapChanged();
    }

    /**
     * Adds a Tileset to this Map. If the set is already attached to this map,
     * <code>addTileset</code> simply returns.
     *
     * @param s a tileset to add
     */
    public void addTileset(TileSet s) {
        if (s == null || tilesets.indexOf(s) > -1) {
            return;
        }

        Tile t = s.getTile(0);

        if (t != null) {
            int tw = t.getWidth();
            int th = t.getHeight();
            if (tw != tileWidth) {
                if (tileWidth == 0) {
                    tileWidth = tw;
                    tileHeight = th;
                }
            }
        }

        tilesets.add(s);
        fireMapChanged();
    }

    /**
     * Removes a {@link TileSet} from the map, and removes any tiles in the set
     * from the map layers. A {@link MapChangedEvent} is fired when all
     * processing is complete.
     *
     * @param s TileSet to remove
     * @throws LayerLockedException
     */
    public void removeTileset(TileSet s) throws LayerLockedException {
        // Sanity check
        if (tilesets.indexOf(s) == -1)
            return;

        // Go through the map and remove any instances of the tiles in the set
        Iterator<?> tileIterator = s.iterator();
        while (tileIterator.hasNext()) {
            Tile tile = (Tile)tileIterator.next();
            Iterator<MapLayer> layerIterator = getLayers();
            while (layerIterator.hasNext()) {
                MapLayer ml = (MapLayer)layerIterator.next();
                if (ml instanceof TileLayer) {
                    ((TileLayer)ml).removeTile(tile);
                }
            }
        }

        tilesets.remove(s);
        fireMapChanged();
    }

    public void addObject(MapObject o) {
        objects.add(o);
    }

    public Iterator<MapObject> getObjects() {
        return objects.iterator();
    }

    /**
     * @return the map properties
     */
    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties prop) {
        properties = prop;
    }

    /**
     * Calls super method, and additionally fires a {@link MapChangedEvent}.
     *
     * @see MultilayerPlane#removeLayer(int)
     */
    public MapLayer removeLayer(int index) {
        MapLayer layer = super.removeLayer(index);
        fireMapChanged();
        return layer;
    }

    public void removeLayerSpecial(MapLayer layer) {
        if (specialLayers.remove(layer)) {
            fireMapChanged();
        }
    }

    public void removeAllSpecialLayers() {
        specialLayers.clear();
        fireMapChanged();
    }

    /**
     * Calls super method, and additionally fires a {@link MapChangedEvent}.
     *
     * @see MultilayerPlane#removeAllLayers
     */
    public void removeAllLayers() {
        super.removeAllLayers();
        fireMapChanged();
    }

    /**
     * Calls super method, and additionally fires a {@link MapChangedEvent}.
     *
     * @see MultilayerPlane#setLayerVector
     */
    public void setLayerVector(Vector<MapLayer> layers) {
        super.setLayerVector(layers);
        fireMapChanged();
    }

    /**
     * Calls super method, and additionally fires a {@link MapChangedEvent}.
     *
     * @see MultilayerPlane#swapLayerUp
     */
    public void swapLayerUp(int index) {
        super.swapLayerUp(index);
        fireMapChanged();
    }

    /**
     * Calls super method, and additionally fires a {@link MapChangedEvent}.
     *
     * @see MultilayerPlane#swapLayerDown
     */
    public void swapLayerDown(int index) {
        super.swapLayerDown(index);
        fireMapChanged();
    }

    /**
     * Calls super method, and additionally fires a {@link MapChangedEvent}.
     *
     * @see MultilayerPlane#mergeLayerDown
     */
    public void mergeLayerDown(int index) {
        super.mergeLayerDown(index);
        fireMapChanged();
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Sets a new tile width.
     * 
     * @param width 
     */
    public void setTileWidth(int width) {
        tileWidth = width;
        fireMapChanged();
    }

    /**
     * Sets a new tile height.
     * 
     * @param height 
     */
    public void setTileHeight(int height) {
        tileHeight = height;
        fireMapChanged();
    }

    /**
     * Calls super method, and additionally fires a {@link MapChangedEvent}.
     *
     * @see MultilayerPlane#resize
     */
    public void resize(int width, int height, int dx, int dy) {
        super.resize(width, height, dx, dy);
        fireMapChanged();
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        // TODO: fire mapChangedNotification about orientation change
    }

    public String getFilename() {
        return filename;
    }

    public Iterator<MapLayer> getLayersSpecial() {
        return specialLayers.iterator();
    }

    /**
     * Returns a vector with the currently loaded tilesets.
     * 
     * @return Vector
     */
    public Vector<TileSet> getTilesets() {
        return tilesets;
    }

    /**
     * Get the tile set that matches the given global tile id, only to be used
     * when loading a map.
     * 
     * @param gid 
     * @return TileSet
     */
    public TileSet findTileSetForTileGID(int gid) {
        Iterator<TileSet> itr = tilesets.iterator();
        TileSet has = null;
        while (itr.hasNext()) {
            TileSet ts = (TileSet)itr.next();
            if (ts.getFirstGid() <= gid) {
                has = ts;
            }
        }
        return has;
    }

    /**
     * Returns width of map in tiles.
     * 
     * @return int
     */
    public int getWidth() {
        return bounds.width;
    }

    /**
     * Returns height of map in tiles.
     * 
     * @return int
     */
    public int getHeight() {
        return bounds.height;
    }

    /**
     * Returns default tile width for this map.
     */
    public int getTileWidth() {
        return tileWidth;
    }

    /**
     * Returns default tile height for this map.
     */
    public int getTileHeight() {
        return tileHeight;
    }

    /**
     * Returns wether the given tile coordinates fall within the map
     * boundaries.
     *
     * @param x The tile-space x-coordinate
     * @param y The tile-space y-coordinate
     * @return <code>true</code> if the point is within the map boundaries,
     *         <code>false</code> otherwise
     */
    public boolean contains(int x, int y) {
        return x >= 0 && y >= 0 && x < bounds.width && y < bounds.height;
    }

    /**
     * Returns the maximum tile height. This is the height of the highest tile
     * in all tilesets or the tile height used by this map if it's smaller.
     *
     * @return int The maximum tile height
     */
    public int getTileHeightMax() {
        int maxHeight = tileHeight;
        Iterator<TileSet> itr = tilesets.iterator();

        while (itr.hasNext()) {
            int height = ((TileSet)itr.next()).getTileHeight();
            if (height > maxHeight) {
                maxHeight = height;
            }
        }

        return maxHeight;
    }

    /**
     * Returns the sum of the size of each tile set.
     *
     * @return
     */
    /*
    public int getTotalTiles() {
        int totalTiles = 0;
        Iterator itr = tilesets.iterator();

        while (itr.hasNext()) {
            TileSet cur = (TileSet)itr.next();
            totalTiles += cur.getTotalTiles();
        }

        return totalTiles;
    }
    */

    /**
     * Returns the amount of objects on the map.
     *
     * @return The total objects in the map
     */
    public int getTotalObjects() {
        return totalObjects;
    }

    /**
     * Returns the orientation of this map. Orientation will be one of
     * {@link Map#MDO_ISO}, {@link Map#MDO_ORTHO}, {@link Map#MDO_HEX},
     * {@link Map#MDO_OBLIQUE} and {@link Map#MDO_SHIFTED}.
     *
     * @return The orientation from the enumerated set
     */
    public int getOrientation() {
        return orientation;
    }

    /**
     * Returns string describing the map. The form is <code>Map[width x height
     * x layers][tileWidth x tileHeight]</code>, for example <code>
     * Map[64x64x2][24x24]</code>.
     *
     * @return string describing map
     */
    public String toString() {
        return "Map[" + bounds.width + "x" + bounds.height + "x" +
            getTotalLayers() + "][" + tileWidth + "x" +
            tileHeight + "]";
    }
}
