package manager.component.picture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class LayerPicture {

	private int width;  //TODO Should store composite image and use sub-images to make recompilation of it after change faster
	private int height;
	private ArrayList<ArtPicture> layers;	
	
	public LayerPicture(String path) {
		File f = new File(path);
		if(f.isDirectory()) {
			
		}
		else {
			
		}
	}
	
	public LayerPicture(int inWid, int inHei) {
		width = inWid;
		height = inHei;
		layers = new ArrayList<ArtPicture>();
	}
	
	public void export(String path, int scale, boolean composite) {
		//TODO: Export bufferedImage to path and, if true on composite, also save a folder of the layers
	}
	
	public void setPixel(int x, int y, Color col, int layer) {
		layers.get(layer).setPixel(x, y, col);
	}
	
	public void setRegion(int x, int y, Color[][] cols, int layer) {
		layers.get(layer).setRegion(x, y, cols);
	}
	
	public void addLayer() {
		addLayer(new ArtPicture(width, height, layers.size()));
	}
	
	public void addLayer(ArtPicture in) {
		if(in.getWidth() != width || in.getHeight() != height) {
			System.out.println("Error: New ArtPicture object not of same size as composite Picture object");
			return;
		}
		layers.add(in);
		Collections.sort(layers);
	}
	
	public void addLayer(ArtPicture in, int lH) {
		if(lH <= layers.size()) {
			layers.add(lH, in);
			updateLayers();
		}
		else {
			addLayer(in);
		}
	}

	public void moveLayers(int st, int en) {
		ArtPicture a = layers.get(st);
		layers.remove(st);
		if(st < en) {
			layers.add(en - 1, a);
		}
		else {
			layers.add(en, a);
		}
	}
	
	public ArtPicture getLayer(int layer) {
		return layers.get(layer);
	}
	
	public void removeLayer(int ind) {
		layers.remove(ind);
		updateLayers();
	}
	
	private void updateLayers() {
		for(int i = 0; i < layers.size(); i++) {
			layers.get(i).setLayer(i);
		}
	}
	
	public BufferedImage generateImage() {
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Color[][] merge = new Color[width][height];
		updateLayers();
		for(ArtPicture l : layers) {
			for(int i = 0; i < l.getWidth(); i++) {	//TODO: Abstract this to do specific pixel locations so can localize changes
				for(int j = 0; j < l.getHeight(); j++) {
					Color c = l.getColor(i, j);
					double alph = c.getAlpha() / 255.0;
					if(c.getAlpha() == 1.0) {
						merge[i][j] = c;
					}
					else {
						Color o = merge[i][j];
						o = o == null ? new Color(0, 0, 0) : o;
						merge[i][j] = new Color((int)(c.getRed() * alph) + (int)(o.getRed() * (1 - alph)), (int)(c.getGreen() * alph) + (int)(o.getGreen() * (1 - alph)), (int)(c.getBlue() * alph) + (int)(o.getBlue() * (1 - alph)));
					}
				}
			}
		}
		for(int i = 0; i < merge.length; i++) {
			for(int j = 0; j < merge[i].length; j++) {
				out.setRGB(i, j, merge[i][j].getRGB());
			}
		}
		return out;
	}

	public BufferedImage generateImageSetLayers(int startLay, int endLay) {
		if(startLay < 0 || startLay > endLay || endLay >= layers.size()) {
			System.out.println("Error: Illegal start or end indexes for generating an Image using a specific series of Layers");
			//TODO: Generate warning box
			return generateImage();
		}
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Color[][] merge = new Color[width][height];
		updateLayers();
		for(int h = startLay; h < endLay; h++) {
			ArtPicture l = layers.get(h);
			for(int i = 0; i < l.getWidth(); i++) {
				for(int j = 0; j < l.getHeight(); j++) {
					Color c = l.getColor(i, j);
					double alph = c.getAlpha() / 255.0;
					if(c.getAlpha() == 1.0) {
						merge[i][j] = c;
					}
					else {
						Color o = merge[i][j];
						o = o == null ? new Color(0, 0, 0) : o;
						merge[i][j] = new Color((int)(c.getRed() * alph) + (int)(o.getRed() * (1 - alph)), (int)(c.getGreen() * alph) + (int)(o.getGreen() * (1 - alph)), (int)(c.getBlue() * alph) + (int)(o.getBlue() * (1 - alph)));
					}
				}
			}
		}
		for(int i = 0; i < merge.length; i++) {
			for(int j = 0; j < merge[i].length; j++) {
				out.setRGB(i, j, merge[i][j].getRGB());
			}
		}
		return out;
	}
	
}
