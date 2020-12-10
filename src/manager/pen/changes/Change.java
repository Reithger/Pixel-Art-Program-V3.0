package manager.pen.changes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import misc.Canvas;

public class Change{
		
//---  Instance Variables   -------------------------------------------------------------------
	
	private int x;
	private int y;
	private Color[][] cols;
	private int layer;
	private String name;
	private boolean overwrite;

	private volatile boolean mutex;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Change(String nom, int inLay) {
		name = nom;
		layer = inLay;
		cols = null;
		overwrite = true;
	}
	
//---  Operations   ---------------------------------------------------------------------------

	public void addChange(Change in) {
		addChange(in.getX(), in.getY(), in.getColors());
	}
	
	public void addChange(int xIn, int yIn, Color[][] colsIn) {
		if(colsIn == null) {
			return;
		}
		openLock();
		if(cols == null) {
			x = xIn;
			y = yIn;
			cols = colsIn;
			closeLock();
			return;
		}
		int minX = xIn < x ? xIn : x;
		int minY = yIn < y ? yIn : y;
		int maxX = (xIn + colsIn.length > x + cols.length) ? (xIn + colsIn.length) : (x + cols.length);
		int maxY = (yIn + colsIn[0].length > y + cols[0].length) ? (yIn + colsIn[0].length) : (y + cols[0].length);
		int wid = maxX - minX;
		int hei = maxY - minY;
		if(wid != cols.length || hei != cols[0].length) {
			Color[][] newCol = new Color[wid][hei];
			for(int i = 0; i < cols.length; i++) {
				for(int j =  0; j < cols[i].length; j++) {
					int usX = x - minX + i;
					int usY = y - minY + j;
					newCol[usX][usY] = cols[i][j];
				}
			}
			cols = newCol;
			x = minX;
			y = minY;
		}
		
		for(int i = 0; i < colsIn.length; i++) {
			for(int j = 0; j < colsIn[i].length; j++) {
				int usX = xIn - x + i;
				int usY = yIn - y + j;
				if((overwrite || cols[usX][usY] == null) && colsIn[i][j] != null && usX < cols.length && usY < cols[usX].length) {
					cols[usX][usY] = colsIn[i][j];
				}
			}
		}
		closeLock();
	}
	
	public void addChange(int xIn, int yIn, Color col) {
		addChange(xIn, yIn, new Color[][] {{col}});
	}
	
	public void save(String path) {
		if(cols == null) {
			return;
		}
		Canvas can = new Canvas(cols);
		BufferedImage img = can.getImage();
		File f = new File(path + ".png");
		try {
			ImageIO.write(img, "png", f);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setName(String in) {
		name = in;
	}
	
	public void setOverwrite(boolean in) {
		overwrite = in;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getLayer() {
		return layer;
	}
	
	public String getName() {
		return name;
	}
	
	public Color[][] getColors(){
		return cols;
	}

//---  Mechanics   ----------------------------------------------------------------------------
	
	private void openLock() {
		while(mutex) {}
		mutex = true;
	}
	
	private void closeLock() {
		mutex = false;
	}
	
}
