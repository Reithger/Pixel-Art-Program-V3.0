package manager;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import manager.curator.picture.LayerPicture;

public class Pen {

	private Color col;
	private volatile HashMap<String, Changes> changes;
	private volatile boolean mutex;
	
	public Pen() {
		col = Color.black;
		mutex = false;
		changes = new HashMap<String, Changes>();
	}
	
	public void openLock() {
		while(mutex) {}
		mutex = true;
	}
	
	public void closeLock() {
		mutex = false;
	}
	
	public void setColor(Color in) {
		col = in;
	}
	
	public void initializeCanvas(LayerPicture lP, int layer) {
		openLock();
		for(int i = 0; i < lP.getWidth(); i++) {
			for(int j = 0; j < lP.getHeight(); j++) {
				lP.setPixel(i, j, Color.white, layer);
			}
		}
		closeLock();
	}
	
	public void draw(String nom, LayerPicture lP, int layer, int x, int y) {
		openLock();
		lP.setPixel(x, y, col, layer);	//Most basic form of drawing
		if(changes.get(nom) == null) {
			changes.put(nom, new Changes(nom));
		}
		changes.get(nom).addChange(x, y, new Color[][] {{col}});
		closeLock();
	}
	
	public ArrayList<String> getChangeNames() {
		ArrayList<String> out = new ArrayList<String>();
		for(String s : changes.keySet()) {
			out.add(s);
		}
		return out;
	}
	
	public int getChangeX(String ref) {
		return changes.get(ref).getX();
	}
	
	public int getChangeY(String ref) {
		return changes.get(ref).getY();
	}
	
	public Color[][] getChangeColors(String nom){
		return changes.get(nom).getColors();
	}
	
	public void disposeChanges() {
		changes = new HashMap<String, Changes>();
	}
	
	class Changes{
		
		private int x;
		private int y;
		private Color[][] cols;
		private String name;
		
		public Changes(String nom) {
			name = nom;
			cols = null;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public String getName() {
			return name;
		}
		
		public Color[][] getColors(){
			return cols;
		}
		
		public void addChange(int xIn, int yIn, Color[][] colsIn) {
			if(cols == null) {
				x = xIn;
				y = yIn;
				cols = colsIn;
				return;
			}
			int minX = xIn < x ? xIn : x;
			int minY = yIn < y ? yIn : y;
			int maxX = xIn + colsIn.length > x + cols.length ? xIn + colsIn.length : x + cols.length;
			int maxY = yIn + colsIn[0].length > y + cols[0].length ? yIn + colsIn.length : y + cols[0].length;
			int wid = maxX - minX;
			int hei = maxY - minY;
			if(wid != cols.length || hei != cols[0].length) {
				Color[][] newCol = new Color[wid][hei];
				for(int i = x - minX; i < cols.length; i++) {
					for(int j =  y - minY; j < cols[i].length; j++) {
						newCol[i - (x - minX)][j - (y - minY)] = cols[i][j];
					}
				}
				cols = newCol;
				x = minX;
				y = minY;
			}
			
			for(int i = 0; i < colsIn.length; i++) {
				for(int j = 0; j < colsIn[i].length; j++) {
					if(colsIn[i][j] != null)
						cols[xIn - x + i][yIn - y + j] = colsIn[i][j];
				}
			}
		}
		
	}
	
}
