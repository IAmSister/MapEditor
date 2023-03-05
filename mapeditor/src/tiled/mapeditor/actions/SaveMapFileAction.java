package tiled.mapeditor.actions;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.mapeditor.MapEditor;

import java.util.prefs.Preferences;

public class SaveMapFileAction extends AbstractAction {
	MapEditor editor;
	
	public SaveMapFileAction(MapEditor editor){
		this.editor = editor;
		this.putValue(AbstractAction.NAME, "保存地图文件");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "将当前地图保存为地图文件");
	}
	
	
	public void actionPerformed(ActionEvent e) {
		// 用户选择一个文件，然后保存
		Map map = editor.getCurrentMap();
		
		if (!checkTiles(map))
		{
			editor.showWarning("警告", "可走格子数小于总格子数的四分之一！");
			return;
		}
		
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(editor.getAppFrame());
		File file = chooser.getSelectedFile();
		if(file != null){
			// 打开文件写入地图信息
			try {
				PrintWriter outf = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
				int mapPixelWidth = editor.getMapView().getBackgroundImage().getWidth(null);
				int mapPixelHeight = editor.getMapView().getBackgroundImage().getHeight(null);
				outf.println(map.getWidth()+","+map.getHeight()+","+map.getTileWidth()+","+map.getTileHeight()+","+mapPixelWidth+","+mapPixelHeight);
				TileLayer layer = (TileLayer)map.getLayer(0);
				for(int i=0; i<map.getHeight(); i++){
					for(int j=0; j<map.getWidth(); j++){
						Tile tile = layer.getTileAt(j, i);
						if(tile == null){
							outf.print("X");
						}
						else if(tile.getName().equals(Tile.TILE_PATH_NORMAL)){
							outf.print("O");
						}
						else if(tile.getName().equals(Tile.TILE_PATH_TRANSPARENT)){
							outf.print("o");
						}
						else{
							outf.print("X");
						}
					}
					outf.println();
				}
				outf.close();
				
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		
	}

	private boolean checkTiles(Map map)
	{
		int totalTileNum = 0;
		int pathTileNum = 0;
		TileLayer layer = (TileLayer)map.getLayer(0);
		for(int i=0; i<map.getHeight(); i++){
			for(int j=0; j<map.getWidth(); j++){
				Tile tile = layer.getTileAt(j, i);
				if (tile != null)
				{
					if(tile.getName().equals(Tile.TILE_PATH_NORMAL)){
						pathTileNum++;
					}
					else if(tile.getName().equals(Tile.TILE_PATH_TRANSPARENT)){
						pathTileNum++;
					}
				}
				
				totalTileNum++;
			}
		}
		return ((float)pathTileNum / (float)totalTileNum >= 0.25f);

	}
}
