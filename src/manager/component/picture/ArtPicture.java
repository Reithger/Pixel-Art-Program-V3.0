package manager.component.picture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ArtPicture implements Comparable<ArtPicture>{

//---  Instance Variables   -------------------------------------------------------------------
	
	private Color[][] colorData;
	private int layer;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public ArtPicture(int wid, int hei, int lay) {
		colorData = new Color[wid][hei];
		for(int i = 0; i < wid; i++) {
			for(int j = 0; j < hei; j++) {
				
			}
		}
		layer = lay;
	}
	
//---  Operations   ---------------------------------------------------------------------------

	public BufferedImage generateImage() {
		int wid = colorData.length;
		int hei = colorData[0].length;
		BufferedImage out = new BufferedImage(wid, hei, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < out.getWidth(); i++) {
			for(int j = 0; j < out.getHeight(); j++) {
				out.setRGB(i, j, colorData[i][j].getRGB());
			}
		}
		return out;
	}

	public void export(String path, String saveType) {
		File savePoint = new File(path);
		if(savePoint.exists()) {
			savePoint.delete();
		}
		try {
			ImageIO.write(generateImage(), saveType, savePoint);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setWidth(int wid) {
		Color[][] out = new Color[wid][colorData[0].length];
		for(int i = 0; i < wid; i++) {
			for(int j = 0; j < out[i].length; j++) {
				if(i < colorData.length) {
					out[i][j] = colorData[i][j];
				}
			}
		}
		colorData = out;
	}
	
	public void setHeight(int hei) {
		Color[][] out = new Color[colorData.length][hei];
		for(int i = 0; i < out.length; i++) {
			for(int j = 0; j < hei; j++) {
				if(j < colorData[i].length) {
					out[i][j] = colorData[i][j];
				}
			}
		}
		colorData = out;
	}
	
	public void setPixel(int x, int y, Color col) {
		if(x < colorData.length && x >= 0 && y < colorData[x].length && y >= 0)
			colorData[x][y] = col;
	}
	
	public void setRegion(int x, int y, Color[][] cols) {
		for(int i = x; i < colorData.length; i++) {
			for(int j = y; j < colorData[i].length; j++) {
				setPixel(i, j, cols[i - x][j - y]);
			}
		}
	}
	
	public void setColorData(Color[][] data) {
		colorData = data;
	}
	
	public void setLayer(int lay) {
		layer = lay;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getWidth() {
		return colorData.length;
	}
	
	public int getHeight() {
		return colorData[0].length;
	}

	public Color getColor(int x, int y) {
		if(x < colorData.length && x >= 0 && y < colorData[x].length && y >= 0)
			return colorData[x][y];
		return null;
	}

	public Color[][] getColorData(){
		return colorData;
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
