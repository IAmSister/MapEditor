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

package tiled.mapeditor.util;

import java.awt.*;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @version $Id: MapEventAdapter.java,v 1.1 2007/10/17 07:56:37 gulei Exp $
 */
public class MapEventAdapter
{
    public static final int ME_MAPACTIVE   = 1;
    public static final int ME_MAPINACTIVE = 2;

    private LinkedList listeners;

    public MapEventAdapter() {
        listeners = new LinkedList();
    }

    /**
     * Adds a Component to the list of listeners of map events. Checks that
     * the component is not already in the list.
     * 
     * @param obj the listener to add
     */
    public void addListener(Component obj) {
        /* Small sanity check - don't add it if it's already there.
         * Really only useful to the removeListener() func, as
         * LinkedList.remove() only removes the first instance of a given
         * object.
         */
        if (listeners.indexOf(obj) == -1) {
            listeners.add(obj);
        }
    }

    /**
     * Removes a component from the list of listeners.
     * 
     * @param obj	the Component to remove
     */
    public void removeListener(Component obj) {
        listeners.remove(obj);
    }

    /**
     * Fires an event to notify all listeners.
     * 
     * @param type 	the event type
     */
    public void fireEvent(int type) {
        //TODO: the idea is to extend this to allow for a multitude of
        // different event types at some point...
        if (type == ME_MAPACTIVE) {
            enableEvent();
        } else if (type == ME_MAPINACTIVE) {
            disableEvent();
        }
    }

    private void enableEvent() {
        Component c;
        ListIterator li = listeners.listIterator();
        while (li.hasNext()) {
            c = (Component) li.next();
            c.setEnabled(true);
        }
    }

    private void disableEvent(){
        Component c;
        ListIterator li = listeners.listIterator();
        while (li.hasNext()) {
            c = (Component) li.next();
            c.setEnabled(false);
        }
    }
}
