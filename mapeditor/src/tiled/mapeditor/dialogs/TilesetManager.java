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
 *  Rainer Deyke <rainerd@eldwood.com>
 */

package tiled.mapeditor.dialogs;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import tiled.core.*;
import tiled.io.MapHelper;
import tiled.io.MapWriter;
import tiled.mapeditor.util.*;
import tiled.mapeditor.plugin.PluginClassLoader;
//import tiled.mapeditor.Resources;

public class TilesetManager extends JDialog implements ActionListener,
       ListSelectionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Map map;

    private JButton saveAsButton, saveButton, embedButton;
    private JButton removeButton, editButton, closeButton;
    private JTable tilesetTable;

    private static final String DIALOG_TITLE = "�زļ�������";//Resources.getString("dialog.tilesetmanager.title");
    private static final String CLOSE_BUTTON = "�ر�";//Resources.getString("general.button.close");
    private static final String REMOVE_BUTTON = "�Ƴ�";//Resources.getString("general.button.remove");
    private static final String EMBED_BUTTON = "Ƕ��";//Resources.getString("dialog.tilesetmanager.embed.button");
    private static final String SAVE_AS_BUTTON = "���Ϊ";//Resources.getString("action.map.saveas.name");
    private static final String EDIT_BUTTON = "�༭";//Resources.getString("dialog.tilesetmanager.edit.button");
    private static final String SAVE_BUTTON = "����";//Resources.getString("action.map.save.name");

    public TilesetManager(JFrame parent, Map map) {
        super(parent, DIALOG_TITLE, true);
        this.map = map;
        init();
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void init() {
        // Create the tileset table
        tilesetTable = new JTable(new TilesetTableModel(map));
        tilesetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tilesetTable.getSelectionModel().addListSelectionListener(this);
        JScrollPane tilesetScrollPane = new JScrollPane(tilesetTable);
        tilesetScrollPane.setPreferredSize(new Dimension(360, 150));

        // Create the buttons
        saveButton = new JButton(SAVE_BUTTON);
        editButton = new JButton(EDIT_BUTTON);
        saveAsButton = new JButton(SAVE_AS_BUTTON);
        embedButton = new JButton(EMBED_BUTTON);
        removeButton = new JButton(REMOVE_BUTTON);
        closeButton = new JButton(CLOSE_BUTTON);

        saveAsButton.addActionListener(this);
        saveButton.addActionListener(this);
        embedButton.addActionListener(this);
        removeButton.addActionListener(this);
        editButton.addActionListener(this);
        closeButton.addActionListener(this);

        // Create the main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.gridwidth = 7;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        mainPanel.add(tilesetScrollPane, c);
        c.insets = new Insets(5, 0, 0, 5);
        c.gridy = 1;
        c.weighty = 0;
        c.weightx = 0;
        c.gridwidth = 1;
        mainPanel.add(saveButton, c);
        mainPanel.add(saveAsButton, c);
        mainPanel.add(embedButton, c);
        mainPanel.add(removeButton, c);
        mainPanel.add(editButton, c);
        c.weightx = 1;
        mainPanel.add(Box.createGlue(), c);
        c.weightx = 0;
        c.insets = new Insets(5, 0, 0, 0);
        mainPanel.add(closeButton, c);

        getContentPane().add(mainPanel);
        getRootPane().setDefaultButton(closeButton);

        tilesetTable.changeSelection(0, 0, false, false);
    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        int selectedRow = tilesetTable.getSelectedRow();
        Vector tilesets = map.getTilesets();
        TileSet set = null;
        try {
            set = (TileSet)tilesets.get(selectedRow);
        } catch (IndexOutOfBoundsException e) {
        }


        if (command.equals(CLOSE_BUTTON)) {
            dispose();
        } else if (command.equals(EDIT_BUTTON)) {
            if (map != null && selectedRow >= 0) {
                TileDialog tileDialog = new TileDialog(this, set, map);
                tileDialog.setVisible(true);
            }
        } else if (command.equals(REMOVE_BUTTON)) {
            if (checkSetUsage(set) > 0) {
                int ret = JOptionPane.showConfirmDialog(this,
                        "�زļ����ڱ�ʹ�ã�ȷ��Ҫ�Ƴ���",//Resources.getString("action.tileset.remove.in-use.message"),
                        "��ʾ",//Resources.getString("action.tileset.remove.in-use.title"),
                        JOptionPane.YES_NO_OPTION);
                if (ret != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            try {
                map.removeTileset(set);
                updateTilesetTable();
            } catch (LayerLockedException e) {
            	JOptionPane.showMessageDialog(this,
                        "�������زļ��Ĳ�����������״̬,\n��Ҫ�Ƚ������������ȫ�Ƴ���",//Resources.getString("action.tileset.remove.error.layer-locked.message"),
                        "��ʾ",//Resources.getString("action.tileset.remove.error.title"),
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (command.equals(SAVE_AS_BUTTON)) {
            JFileChooser ch = new JFileChooser(map.getFilename());

            MapWriter[] writers = PluginClassLoader.getInstance().getWriters();
            for (int i = 0; i < writers.length; i++) {
                try {
                    ch.addChoosableFileFilter(new TiledFileFilter(
                                writers[i].getFilter(), writers[i].getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ch.addChoosableFileFilter
              (new TiledFileFilter(TiledFileFilter.FILTER_TSX));
            int ret = ch.showSaveDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                String filename = ch.getSelectedFile().getAbsolutePath();
                File exist = new File(filename);

                if (exist.exists() && JOptionPane.showConfirmDialog(this,
                        "�ļ��Ѿ����ڣ�Ҫ������")==//Resources.getString("general.file.exists.message")) ==
                        JOptionPane.OK_OPTION || !exist.exists()) {
                    try {
                        MapHelper.saveTileset(set, filename);
                        set.setSource(filename);
                        embedButton.setEnabled(true);
                        saveButton.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (command.equals(SAVE_BUTTON)) {
            try {
                MapHelper.saveTileset(set, set.getSource());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (command.equals(EMBED_BUTTON)) {
            set.setSource(null);
            embedButton.setEnabled(false);
            saveButton.setEnabled(false);
        }
    }

    private void updateTilesetTable() {
        ((TilesetTableModel)tilesetTable.getModel()).setMap(map);
        tilesetTable.repaint();
    }

    private int checkSetUsage(TileSet s) {
        int used = 0;
        Iterator tileIterator = s.iterator();

        while (tileIterator.hasNext()) {
            Tile tile = (Tile)tileIterator.next();
            Iterator layerIterator = map.getLayers();

            while (layerIterator.hasNext()) {
                MapLayer ml = (MapLayer)layerIterator.next();
                if (ml.isUsed(tile)) {
                    used++;
                    break;
                }
            }
        }

        return used;
    }

    public void valueChanged(ListSelectionEvent event) {
        updateButtons();
    }

    private void updateButtons() {
        int selectedRow = tilesetTable.getSelectedRow();
        Vector tilesets = map.getTilesets();
        TileSet set = null;
        try {
            set = (TileSet)tilesets.get(selectedRow);
        } catch (IndexOutOfBoundsException e) {
        }

        editButton.setEnabled(set != null);
        removeButton.setEnabled(set != null);
        saveButton.setEnabled(set != null && set.getSource() != null);
        embedButton.setEnabled(set != null && set.getSource() != null);
    }
}
