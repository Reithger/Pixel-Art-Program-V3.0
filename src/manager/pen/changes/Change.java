package manager.pen.changes;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import misc.Canvas;

public class Change{
		
//---  Instance Variables   -------------------------------------------------------------------
	
	private int x;
	private int y;
	private Integer[][] cols;
	private boolean overwrite;

	private volatile boolean mutex;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Change() {
		cols = null;
		overwrite = true;
	}
	
//---  Operations   ---------------------------------------------------------------------------

	public void addChange(Change in) {
		addChange(in.getX(), in.getY(), in.getColors());
	}
	
	public void addChange(int xIn, int yIn, Integer[][] colsIn) {
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
			Integer[][] newCol = new Integer[wid][hei];
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
	
	public void addChange(int xIn, int yIn, int rgb) {
		addChange(xIn, yIn, new Integer[][] {{rgb}});
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
	
	public void apply(Canvas in) {
		if(cols == null) {
			return;
		}
		for(int i = x; i < x + cols.length; i++) {
			for(int j = y; j < y + cols[i - x].length; j++) {
				if(cols[i - x][j -y] != null && !cols[i-x][j-y].equals(in.getCanvasIntValue(i, j)))
					in.setCanvasColor(i, j, cols[i - x][j - y]);
			}
		}
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
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
	
	public Integer[][] getColors(){
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
