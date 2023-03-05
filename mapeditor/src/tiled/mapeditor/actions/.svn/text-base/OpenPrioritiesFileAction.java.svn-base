package tiled.mapeditor.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import tiled.mapeditor.MapEditor;
import tiled.mapeditor.util.ObjectLayerInfo;

public class OpenPrioritiesFileAction extends AbstractAction {
	MapEditor editor;
	
	public OpenPrioritiesFileAction(MapEditor editor) {
		this.editor = editor;
		this.putValue(AbstractAction.NAME, "打开属性文件");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "打开描述属性的xls文件");
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(editor.getAppFrame());
		File file = chooser.getSelectedFile();
		if(file != null){
			// 打开文件
			try {

				HashMap layers = new HashMap();
				
		   		HSSFWorkbook xls = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(file)));
	    		int sheetNum = xls.getNumberOfSheets();
		   		System.out.println("sheetNum = " + sheetNum);
		   		
		   		this.editor.getMap().clear();
		   		this.editor.getBelowPanel().removeAllTab();
		   		
		   		for (int i=0;i<sheetNum;i++) {
			   		HSSFSheet mysheet = xls.cloneSheet(i);
			   		String layerName = xls.getSheetName(i);
			   		System.out.println(i + " : " + layerName);
		    		int firstRowNum = mysheet.getFirstRowNum();
		    		int lastRowNum = mysheet.getLastRowNum();
		    		System.out.println("firstRowNum = " + firstRowNum);
		    		System.out.println("lastRowNum = " + lastRowNum);
		    		
		    		ObjectLayerInfo info = new ObjectLayerInfo(layerName, "", 0);
		    		
		    		HashMap objects = new HashMap();
		    		// firstRow: Priority Name
		    		HSSFRow row=mysheet.getRow(firstRowNum);
		    		int firstCellNum = row.getFirstCellNum();
					int lastCellNum = row.getLastCellNum();
					String[] priorityNameArr = new String[lastCellNum-firstCellNum+1];
					for (int x=firstCellNum;x<=lastCellNum;x++) {
						HSSFCell cell = row.getCell((short)x);
						priorityNameArr[x-firstCellNum] = cell.toString();
					}

		    		for(int j=firstRowNum+1; j<=lastRowNum; j++) {
		    			HashMap priorities = new HashMap(); 
		    			row=mysheet.getRow(j);
		    			firstCellNum = row.getFirstCellNum();
						lastCellNum = row.getLastCellNum();
						for (int x=firstCellNum;x<=lastCellNum;x++) {
							HSSFCell cell = row.getCell((short)x);
							priorities.put(priorityNameArr[x-firstCellNum], cell.toString());
						}
			    		String objectName = (String)priorities.get("Name");
			    		objects.put(objectName, priorities);
		    		}

		    		info.setObjects(objects);
		    		this.editor.getMap().put(layerName, info);
		    		
		    		// update UI
		    		this.editor.getBelowPanel().addTab(layerName);
		    		this.editor.getLayerEditorPanel().updateTable();
		   		}

			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
