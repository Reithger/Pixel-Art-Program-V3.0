package drawsurface;

import java.awt.Color;
import java.awt.image.BufferedImage;

import visual.panel.CanvasPanel;
import visual.panel.ElementPanel;

public class Layer implements Comparable<Layer>, Display{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static int SAVE_TYPE_PNG = 0;
	private final static int SAVE_TYPE_JPG = 1;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private Color[][] colorData;
	private int layerHeight;
	private double scale;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Layer(int wid, int hei, int lay) {
		colorData = new Color[wid][hei];
		layerHeight = lay;
		scale = 1;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public ElementPanel generateDisplay(int x, int y) {
		int wid = (int)(scale * colorData.length);
		int hei = (int)(scale * colorData[0].length);
		ElementPanel p = new ElementPanel(x, y, wid, hei) {
			@Override
			public void keyBehaviour(char event) {
				//TODO: Keyboard shortcuts for Layer
			}
		};
		p.addImage("img", 10, false, 0, 0, false, generateImage());
		return p;
	}

	@Override
	public CanvasPanel generateCanvas(int x, int y) {
		int wid = (int)(scale * colorData.length);
		int hei = (int)(scale * colorData[0].length);
		CanvasPanel c = new CanvasPanel(x, y, wid, hei) {
			@Override
			public void keyEvent(char event) {
				//TODO: Keyboard shortcuts for Layer
			}
		};
		c.updateCanvas(colorData);
		return c;
	}
	
	public BufferedImage generateImage() {
		int wid = (int)(scale * colorData.length);
		int hei = (int)(scale * colorData[0].length);
		BufferedImage out = new BufferedImage(wid, hei, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < out.getWidth(); i++) {
			for(int j = 0; j < out.getHeight(); j++) {
				out.setRGB(i, j, colorData[(int)(i / scale)][(int)(j / scale)].getRGB());
			}
		}
		return out;
	}

	public void export(String path) {
		//TODO: Export image
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setScale(double in) {
		scale = in;
	}
	
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
	
	public void setColor(int x, int y, Color col) {
		if(x < colorData.length && x >= 0 && y < colorData[x].length && y >= 0)
			colorData[x][y] = col;
	}
	
	public void setColorData(Color[][] data) {
		colorData = data;
	}
	
	public void setLayerHeight(int lay) {
		layerHeight = lay;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public double getScale() {
		return scale;
	}
	
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

	public int getLayerHeight() {
		return layerHeight;
	}

//---  Mechanics   ----------------------------------------------------------------------------
	
	@Override
	public int compareTo(Layer o) {
		int l = o.getLayerHeight();
		if(getLayerHeight() > l) {
			return 1;
		}
		else if(getLayerHeight() < l) {
			return -1;
		}
		else {
			return 0;
		}
	}

	
}
