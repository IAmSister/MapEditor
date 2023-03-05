package tiled.mapeditor.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import tiled.mapeditor.MapEditor;
import java.util.HashMap;
import tiled.mapeditor.util.ObjectLayerInfo;

public class SavePriorityFileAction extends AbstractAction {

	MapEditor editor;
	
	public SavePriorityFileAction(MapEditor editor) {
		this.editor = editor;
		this.putValue(AbstractAction.NAME, "保存属性文件");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "将当前地图对应的属性信息保存为xls文件");
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(editor.getAppFrame());
		File file = chooser.getSelectedFile();
		if(file != null){
			// 打开文件写入地图信息
			try {
				FileOutputStream fos = new FileOutputStream(file); 
				HSSFWorkbook wb = new HSSFWorkbook(); 

				// get all priority names
				HashMap ohm = this.editor.getAllObjects();
				if (ohm.size() <= 0) 
					return;
				
				HashMap o = (HashMap)ohm.get(ohm.keySet().toArray()[0]);
				Object[] priorityNames = o.keySet().toArray();
				
				HashMap layers = this.editor.getMap();
				Object[] layerNames = layers.keySet().toArray();
				for (int i=0; i<layerNames.length; i++) {
					String layerName = (String)layerNames[i];
					HSSFSheet s = wb.createSheet(); 
					wb.setSheetName(i, layerName);

					HSSFRow row = s.createRow(0); 					
					for (int x=0; x<priorityNames.length; x++) {
						HSSFCell cell = row.createCell((short)x); 
						cell.setCellValue(new HSSFRichTextString((String)priorityNames[x]));
					}
					
					HashMap elements = ((ObjectLayerInfo)layers.get(layerName)).getObjects();
					Object[] objectNames = elements.keySet().toArray();
					for (int j=0; j<objectNames.length; j++) {
						String elementName = (String)objectNames[j];
						HashMap elementPriority = (HashMap)elements.get(elementName);
						HSSFRow row1 = s.createRow(j+1); 
						
						for (int x=0; x<priorityNames.length; x++) {
							HSSFCell cell1 = row1.createCell((short)x); 
							if ("Name".equalsIgnoreCase((String)priorityNames[x])) {
								cell1.setCellValue(new HSSFRichTextString(elementName));								
							}else {
								cell1.setCellValue(new HSSFRichTextString((String)elementPriority.get((String)priorityNames[x])));
							}

						}
					}
				}

				wb.write(fos); 
				fos.close(); 
				
			}catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
