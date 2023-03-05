package tiled.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Stack;

import javax.swing.SwingConstants;

import tiled.core.Map;
import tiled.core.ObjectGroup;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.core.TileSet;
import tiled.mapeditor.selection.SelectionLayer;

public class StaggeredMapView extends MapView {

	int[][] masks;
	public static final int MM_CENTER = 0;
	public static final int MM_NORTHEAST = 1;
	public static final int MM_SOUTHEAST = 2;
	public static final int MM_SOUTHWEST = 3;
	public static final int MM_NORTHWEST = 4;

	protected StaggeredMapView(Map map) {
		super(map);
		
		initMasks();
		
		initPathTileSet();
	}
	
	private void initPathTileSet(){
		if(map.getTilesets().size() > 0){
			return;
		}
        Dimension tsize = getTileSize();
		int width = tsize.width;
		int height = tsize.height;

		Polygon poly = new Polygon();
		poly.addPoint(width/2, 0);
		poly.addPoint(width, height/2);
		poly.addPoint(width/2, height);
		poly.addPoint(0, height/2);

		// 创建一个TileSet，包含路点和透明路点
		TileSet set = new TileSet();
		set.setName("Path");
		
		Tile tile = new Tile();
		BufferedImage img = new BufferedImage(tsize.width, tsize.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(new Color(0x00000000, true));
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(new Color(0x90FF0000, true));
		g2d.fillPolygon(poly);
		g2d.dispose();
		tile.setImage(img);
		tile.setName(Tile.TILE_PATH_NORMAL);
		set.addTile(tile);
		
		tile = new Tile();
		img = new BufferedImage(tsize.width, tsize.height, BufferedImage.TYPE_INT_ARGB);
		g2d = img.createGraphics();
		g2d.setColor(new Color(0x00000000, true));
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(new Color(0x9000FF00, true));
		g2d.fillPolygon(poly);
		g2d.dispose();
		tile.setImage(img);
		tile.setName(Tile.TILE_PATH_TRANSPARENT);
		set.addTile(tile);
		
		map.addTileset(set);
		
		// 创建一个TileSet，用于设定物品可能活动的范围等地图坐标信息
		TileSet set1 = new TileSet();
		set1.setName("Area");
		
		Tile tile1 = new Tile();
		BufferedImage img1 = new BufferedImage(tsize.width, tsize.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d1 = img1.createGraphics();
		g2d1.setColor(new Color(0x00000000, true));
		g2d1.fillRect(0, 0, width, height);
		g2d1.setColor(new Color((float)0.0, (float)0.0, (float)1.0));
		g2d1.fillPolygon(poly);
		g2d1.dispose();
		tile1.setImage(img1);
		tile1.setName(Tile.TILE_AREA_NORMAL);
		set1.addTile(tile1);
		
		map.addTileset(set1);
	}
	
	private void initMasks(){
        Dimension tsize = getTileSize();
		int width = tsize.width;
		int height = tsize.height;
		
		Polygon poly = new Polygon();
		poly.addPoint(width/2, 0);
		poly.addPoint(width, height/2);
		poly.addPoint(width/2, height);
		poly.addPoint(0, height/2);
		
		
		BufferedImage bufimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics2D g2d = bufimage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);
		
		g2d.setColor(Color.BLACK);
		g2d.fillPolygon(poly);

		masks = new int[height][width];
		int w2 = width/2;
		int h2 = height/2;
		for(int row=0; row<height; row++){
			for(int col=0; col<width; col++){
				int rgb = bufimage.getRGB(col, row);
				if(rgb == 0xFF000000){
					masks[row][col] = MM_CENTER;
				}
				else if(row < h2){
					if(col < w2){
						masks[row][col] = MM_NORTHWEST;
//						bufimage.setRGB(col, row, Color.RED.getRGB());
					}
					else{
						masks[row][col] = MM_NORTHEAST;
//						bufimage.setRGB(col, row, Color.GREEN.getRGB());
					}
				}
				else{
					if(col < w2){
						masks[row][col] = MM_SOUTHWEST;
//						bufimage.setRGB(col, row, Color.BLUE.getRGB());
					}
					else{
						masks[row][col] = MM_SOUTHEAST;
//						bufimage.setRGB(col, row, Color.YELLOW.getRGB());
					}
				}
			}
		}
		
		g2d.dispose();
		bufimage = null;
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
        Dimension tsize = getTileSize();
        if (orientation == SwingConstants.VERTICAL) {
            return (visibleRect.height / tsize.height) * tsize.height;
        } else {
            return (visibleRect.width / tsize.width) * tsize.width;
        }
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
        Dimension tsize = getTileSize();
        if (orientation == SwingConstants.VERTICAL) {
            return tsize.height;
        } else {
            return tsize.width;
        }
	}

	@Override
	protected void paintLayer(Graphics2D g2d, TileLayer layer) {
        // Determine tile size and offset
        Dimension tsize = getTileSize();
        if (tsize.width <= 0 || tsize.height <= 0) return;
        Polygon gridPoly = createGridPolygon(0, -tsize.height, 0);

        // Determine area to draw from clipping rectangle
        Rectangle clipRect = g2d.getClipBounds();
        Point leftup = screenToTileCoords(clipRect.x, clipRect.y);
        int startX = leftup.x-1<0? 0:leftup.x-1;
        int startY = leftup.y-1<0? 0:leftup.y-1;

        Point rightbot = screenToTileCoords(clipRect.x + clipRect.width, clipRect.y+clipRect.height);
        int endX = rightbot.x >= map.getWidth()? map.getWidth():rightbot.x+1;
        int endY = rightbot.y >= map.getHeight()? map.getHeight():rightbot.y+1;
        // (endY +2 for high tiles, could be done more properly)
        
        int ystep = tsize.height/2;

        // Draw this map layer
    	int gy = startY*ystep+ystep;
        for (int y = startY;y < endY; y++) {
        	int gx = 0;
        	if(y%2 == 0){
        		gx = startX*tsize.width-tsize.width/2;
        	}
        	else{
        		gx = startX*tsize.width;
        	}
            for (int x = startX;x < endX; x++) {
                Tile tile = layer.getTileAt(x, y);
                if (tile != null) {
                    if (layer instanceof SelectionLayer) {
                        gridPoly.translate(gx, gy);
                        g2d.fillPolygon(gridPoly);
                        gridPoly.translate(-gx, -gy);
                        //paintEdge(g, layer, gx, gy);
                    } else {
                        tile.draw(g2d, gx, gy, zoom);
                    }
                }
                gx += tsize.width;
            }
            gy+=ystep;
        }

	}

	@Override
    protected Polygon createGridPolygon(int tx, int ty, int border) {
        Dimension tileSize = getTileSize();
        tileSize.width -= border * 2;
        tileSize.height -= border * 2;

        Polygon poly = new Polygon();
        poly.addPoint(tx + tileSize.width / 2 + border, ty + border);
        poly.addPoint(tx + tileSize.width, ty + tileSize.height / 2 + border);
        poly.addPoint(tx + tileSize.width / 2 + border,
                ty + tileSize.height + border);
        poly.addPoint(tx + border, ty + tileSize.height / 2 + border);
        return poly;
    }

	@Override
	protected void paintLayer(Graphics2D g2d, ObjectGroup og) {
	}

	@Override
	public Dimension getPreferredSize() {
        Dimension tsize = getTileSize();

        return new Dimension(
                map.getWidth() * tsize.width-tsize.width/2,
                map.getHeight() * tsize.height/2-tsize.height/2);
	}


	@Override
	protected void paintCoordinates(Graphics2D g2d) {
        // Determine tile size
        Dimension tsize = getTileSize();
        if (tsize.width <= 0 || tsize.height <= 0) return;

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Determine tile size and offset
        Font font = new Font("SansSerif", Font.PLAIN, tsize.height / 4);
        g2d.setFont(font);
        FontRenderContext fontRenderContext = g2d.getFontRenderContext();

        // Determine lines to draw from clipping rectangle
        Rectangle clipRect = g2d.getClipBounds();
        Point startTile = screenToTileCoords(clipRect.x, clipRect.y);
        if(startTile.y >= 1){
        	startTile.y--;
        }
        
        Point endTile = screenToTileCoords(clipRect.x+clipRect.width, clipRect.y+clipRect.height);

        int ystep = tsize.height/2;
    	int gy = startTile.y*ystep-ystep;
        for (int y = startTile.y; y <= endTile.y; y ++) {
        	int gx = 0;
        	if(y%2 == 0){
        		gx = startTile.x*tsize.width-tsize.width/2;
        	}
        	else{
        		gx = startTile.x*tsize.width;
        	}
        	for (int x = startTile.x; x <= endTile.x; x ++) {
                String coords = "(" + x + "," + y + ")";
                Rectangle2D textSize =
                    font.getStringBounds(coords, fontRenderContext);

                int fx = gx + (int)((tsize.width - textSize.getWidth()) / 2);
                int fy = gy + (int)((tsize.height + textSize.getHeight()) / 2);

                g2d.drawString(coords, fx, fy);
                gx += tsize.width;
            }
            gy+=tsize.height/2;
        }

	}

	public void repaintRegion(Rectangle region) {
        Dimension tsize = getTileSize();
        if (tsize.width <= 0 || tsize.height <= 0) return;
        Point startPt = tileToScreenCoords(region.x>=1?region.x-1:0, region.y);
        Point endPt = tileToScreenCoords(region.x+region.width, region.y+region.height);

        // Calculate the visible corners of the region
        int startX = startPt.x;
        int startY = startPt.y;
        int endX = endPt.x+tsize.width;
        int endY = endPt.y+tsize.height;

        Rectangle dirty =
            new Rectangle(startX, startY, endX - startX, endY - startY);

        repaint(dirty);
    }

	@Override
	protected void paintGrid(Graphics2D g2d) {
        // Determine tile size
        Dimension tsize = getTileSize();
        if (tsize.width <= 0 || tsize.height <= 0) return;


        // Determine lines to draw from clipping rectangle
        Rectangle clipRect = g2d.getClipBounds();
        Point startTile = screenToTileCoords(clipRect.x, clipRect.y);
        
        Point endTile = screenToTileCoords(clipRect.x+clipRect.width, clipRect.y+clipRect.height);
        endTile.x++;
        endTile.y++;

        Polygon gridPoly = createGridPolygon(0, -tsize.height, 0);
        int ystep = tsize.height/2;
    	int gy = startTile.y*ystep+ystep;
        for (int y = startTile.y; y <= endTile.y; y +=2) {
        	int gx = 0;
        	if(y%2 == 0){
        		gx = startTile.x*tsize.width-tsize.width/2;
        	}
        	else{
        		gx = startTile.x*tsize.width;
        	}
        	for (int x = startTile.x; x <= endTile.x; x ++) {
            	gridPoly.translate(gx, gy);
            	g2d.drawPolygon(gridPoly);
            	gridPoly.translate(-gx, -gy);
                gx += tsize.width;
            }
            gy+=tsize.height;
        }
	}



	@Override
	public Point screenToTileCoords(int x, int y) {
        Dimension tileSize = getTileSize();
		Point centerCellCoords = new Point(x/tileSize.width, y/tileSize.height*2+1);
		
		int offx = x%tileSize.width;
		int offy = y%tileSize.height;
		switch(masks[offy][offx]){
			case MM_NORTHEAST:{
				centerCellCoords.x+=1;
				centerCellCoords.y --;
				break;
			}
			case MM_NORTHWEST:{
//				centerCellCoords.x -=
				centerCellCoords.y --;
				break;
			}
			case MM_SOUTHEAST:{
				centerCellCoords.x +=1;
				centerCellCoords.y ++;
				break;
			}
			case MM_SOUTHWEST:{
//				centerCellCoords.x 
				centerCellCoords.y ++;
				break;
			}
		}
//        System.out.println("[S2T]("+x+","+y+")<->("+centerCellCoords.x+","+centerCellCoords.y+")");
		return centerCellCoords;
	}

	@Override
	public Point tileToScreenCoords(double x, double y) {
		Point rt;
        Dimension tsize = getTileSize();
        int intY = (int)(y+0.0001);
        if(intY%2 == 0){
        	rt = new Point((int)(x*tsize.width-tsize.width/2), (int)((intY-1)*tsize.height/2));
        }
        else{
        	rt = new Point((int)(x*tsize.width), (int)((intY-1)*tsize.height/2));
        }
//        System.out.println("[T2S]("+x+","+y+")<->("+rt.x+","+rt.y+")");
        return rt;
	}

    protected Dimension getTileSize() {
        return new Dimension(
                (int)(map.getTileWidth() * zoom),
                (int)(map.getTileHeight() * zoom));
    }
    
    private Point walk(Point p, int dir){
    	Point pt = new Point(p.x, p.y);
    	switch(dir){
	    	case MM_NORTHEAST:{
				pt.y--;
				pt.x+= p.y%2;
	    		break;
	    	}
	    	case MM_NORTHWEST:{
				pt.y--;
				pt.x+=(p.y%2)-1;
	    		break;
	    	}
	    	case MM_SOUTHEAST:{
				pt.y++;
				pt.x+= p.y%2;
	    		break;
	    	}
	    	case MM_SOUTHWEST:{
				pt.y++;
				pt.x+= (p.y%2)-1; 
	    		break;
	    	}
    	}
    	return pt;
    }

    public void addNeighborPoint(Stack<Point> stack, Point p){
        stack.push(walk(p, MM_NORTHEAST));
        stack.push(walk(p, MM_NORTHWEST));
        stack.push(walk(p, MM_SOUTHEAST));
        stack.push(walk(p, MM_SOUTHWEST));
    }

}
