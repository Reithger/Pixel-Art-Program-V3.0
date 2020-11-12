package manager;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import manager.component.picture.ArtPicture;

public class Pen {

	private Color col;
	private HashMap<String, Changes> changes;
	
	public Pen() {
		col = Color.black;
		changes = new HashMap<String, Changes>();
	}
	
	public void setColor(Color in) {
		col = in;
	}
	
	public void initializeCanvas(String nom, ArtPicture aP) {
		if(changes.get(nom) == null) {
			changes.put(nom, new Changes(nom));
		}
		Changes c = changes.get(nom);
		for(int i = 0; i < aP.getWidth(); i++) {
			for(int j = 0; j < aP.getHeight(); j++) {
				aP.setPixel(i, j, Color.white);
				c.addChange(i, j,  new Color[][] {{Color.white}});
			}
		}
	}
	
	public void draw(String nom, ArtPicture aP, int x, int y) {
		aP.setPixel(x, y, col);	//Most basic form of drawing
		if(changes.get(nom) == null) {
			changes.put(nom, new Changes(nom));
		}
		changes.get(nom).addChange(x, y, new Color[][] {{col}});
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
			cols = new Color[0][0];
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
			int colsHeight = cols.length > 0 ? cols[0].length : 0;
			if(xIn < x || yIn < y || (xIn - x + colsIn.length) >= cols.length || (yIn - y + colsIn[0].length) >= colsHeight) {
				int difX = xIn - x;
				int difY = yIn - y;
				int wid = cols.length - difY;
				wid = wid > colsIn.length + difX ? wid : colsIn.length + difX;
				wid = wid > cols.length ? wid : cols.length;
				int hei = colsHeight - difY;
				hei = hei > colsIn[0].length + difY ? hei : colsIn[0].length + difY;
				hei = hei > colsHeight ? hei : colsHeight;
				Color[][] newCol = new Color[wid][hei];
				int chngX = (difX > 0 ? 0 : difX);
				int chngY = (difY > 0 ? 0 : difY);
				for(int i = 0; i < cols.length; i++) {
					for(int j = 0; j < colsHeight; j++) {
						newCol[i - chngX][j - chngY] = cols[i][j];
					}
				}
				cols = newCol;
				x += chngX;
				y += chngY;
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
