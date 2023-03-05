package tiled.mapeditor.util;

import java.util.HashMap;

public class ObjectLayerInfo {

	private String layerName;
	private String layerDes;
	private int type;
	private HashMap priorities;		// Object Layer 自身的属性
	private HashMap objects;		// 每个object的key是Name(String), value是属性(HashMap)
	
	public ObjectLayerInfo() {
		this.layerName = "";
		this.layerDes = "";
		this.type = 0;
		this.priorities = new HashMap();
		this.objects = new HashMap();
	}
	
	public ObjectLayerInfo(String layerName, String layerDes, int type) {
		this.layerName = layerName;
		this.layerDes = layerDes;
		this.type = type;
		this.priorities = new HashMap();
		this.objects = new HashMap();
	}
	
	public boolean addObject(String name, HashMap priorities) {
		if (this.objects.containsKey(name)) 
			return false;
		
		this.objects.put(name, priorities);
		return true;
	}
	
	public boolean delObject(String name) {
		if (this.objects.containsKey(name)) {
			this.objects.remove(name);
			return true;
		}
		return false;
	}
	
	public HashMap getObjects() {
		return this.objects;
	}
	
	public void setObjects(HashMap objects) {
		this.objects = objects;
	}
	
	public String getDes() {
		return this.layerDes;
	}
}
