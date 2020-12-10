package manager.curator.picture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import misc.Canvas;

public class ArtPicture implements Comparable<ArtPicture>{

//---  Instance Variables   -------------------------------------------------------------------
	
	private Canvas canvas;
	private int layer;
	private boolean update;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public ArtPicture(int wid, int hei, int lay) {
		canvas = new Canvas(wid, hei) {
			@Override
			public void initialize() {
				for(int i = 0; i < getCanvasWidth(); i++) {
					for(int j = 0; j < getCanvasHeight(); j++) {
						this.setPixelColor(i, j, new Color(255, 255, 255, 0));
					}
				}
			}
		};
		layer = lay;
	}
	
	public ArtPicture(File in, int lay) throws IOException {
		canvas = new Canvas(in);
		layer = lay;
	}
	
//---  Operations   ---------------------------------------------------------------------------

	public BufferedImage generateImage() {
		return canvas.getImage();
	}

	public void export(String path, String name, String saveType) {
		File savePoint = new File(path + "/" + name + "." + saveType);
		if(savePoint.exists()) {
			savePoint.delete();
		}
		try {
			System.out.println(ImageIO.write(generateImage(), saveType, savePoint));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setWidth(int wid) {
		canvas.updateCanvasSize(wid, canvas.getCanvasHeight());
		update = true;
	}
	
	public void setHeight(int hei) {
		canvas.updateCanvasSize(canvas.getCanvasWidth(), hei);
		update = true;
	}
	
	public void setPixel(int x, int y, Color col) {
		canvas.setPixelColor(x, y, col);
		update = true;
	}
	
	public void setRegion(int x, int y, Color[][] cols) {
		for(int i = x; i < canvas.getCanvasWidth(); i++) {
			for(int j = y; j < canvas.getCanvasHeight(); j++) {
				canvas.setPixelColor(i, j, cols[i - x][j - y]);
			}
		}
		update = true;
	}
	
	public void setColorData(Color[][] data) {
		canvas.updateCanvas(data);
		update = true;
	}
	
	public void setLayer(int lay) {
		layer = lay;
	}
	
	public void releaseUpdate() {
		update = false;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public boolean getUpdate() {
		return update;
	}
	
	public int getWidth() {
		return canvas.getCanvasWidth();
	}
	
	public int getHeight() {
		return canvas.getCanvasHeight();
	}

	public Color getColor(int x, int y) {
		return canvas.getPixelColor(x, y);
	}

	public Color[][] getColorData(){
		return canvas.getColorData();
	}

	public int getLayer() {
		return layer;
	}

//---  Mechanics   ----------------------------------------------------------------------------
	
	@Override
	public int compareTo(ArtPicture o) {
		int l = o.getLayer();
		if(getLayer() > l) {
			return 1;
		}
		else if(getLayer() < l) {
			return -1;
		}
		else {
			return 0;
		}
	}

}
