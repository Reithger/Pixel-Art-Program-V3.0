package drawsurface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import visual.panel.CanvasPanel;
import visual.panel.ElementPanel;

public class Picture implements Display{
	
//---  Constants   ----------------------------------------------------------------------------
	
	private final static int SAVE_TYPE_PNG = 0;
	private final static int SAVE_TYPE_JPG = 1;

//---  Instance Variables   -------------------------------------------------------------------
	
	private ArrayList<Layer> layers;
	private Image bI;
	private int width;
	private int height;
	private int activeLayer;
	private double scale;

//---  Constructors   -------------------------------------------------------------------------
	
	public Picture(int wid, int hei) {
		layers = new ArrayList<Layer>();
		bI = null;
		width = wid;
		height = hei;
		scale = 1;
		activeLayer = 1;
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public ElementPanel generateDisplay(int x, int y) {
		int wid = (int)(scale * width);
		int hei = (int)(scale * height);
		ElementPanel p = new ElementPanel(x, y, wid, hei) {
			@Override
			public void keyBehaviour(char event) {
				//TODO: Keyboard shortcuts for Picture
			}
		};
		p.addImage("img", 10, false, 0, 0, false, bI);
		return p;
	}

	@Override
	public CanvasPanel generateCanvas(int x, int y) {
		int wid = (int)(scale * width);
		int hei = (int)(scale * height);
		CanvasPanel c = new CanvasPanel(x, y, wid, hei) {
			@Override
			public void keyEvent(char event) {
				//TODO: Keyboard shortcuts for Picture (change draw layer)
			}
			
			@Override
			public void commandUnder(Graphics g) {
				g.drawImage(generateImageSetLayers(0, activeLayer - 1), 0, 0, null);
			}
			
		};
		c.updateCanvas(layers.get(activeLayer).getColorData());
		return c;
	}
	
	//-- Layers  ----------------------------------------------
	
	public void addLayer(Layer in) {
		if(in.getWidth() != width || in.getHeight() != height) {
			System.out.println("Error: New Layer object not of same size as composite Picture object");
			return;
		}
		layers.add(in);
		Collections.sort(layers);
	}
	
	public void addLayer(Layer in, int lH) {
		if(lH <= layers.size()) {
			layers.add(lH, in);
			updateLayers();
		}
		else {
			addLayer(in);
		}
	}

	public void removeLayer(int ind) {
		layers.remove(ind);
		updateLayers();
	}
	
	private void updateLayers() {
		for(int i = 0; i < layers.size(); i++) {
			layers.get(i).setLayerHeight(i);
		}
	}
	
	//-- Image Work  ------------------------------------------
	
	public void export(String filepath) {
		//TODO: Encode all the layers into one file that can be read back in
	}

	public void saveImage(String filepath, int type) {
		//TODO:
	}
	
	public void updateDisplayImage() {
		bI = generateImage();
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image drawImage = bI.getScaledInstance((int)(bI.getWidth(null) * scale), (int)(bI.getHeight(null) * scale), Image.SCALE_DEFAULT);
		while(!tk.prepareImage(drawImage, -1, -1, null)){	}
		bI = drawImage;
	}
	
	public BufferedImage generateImage() {
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Color[][] merge = new Color[width][height];
		updateLayers();
		for(Layer l : layers) {
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

	public Image generateImageSetLayers(int startLay, int endLay) {
		if(startLay < 0 || startLay > endLay || endLay >= layers.size()) {
			System.out.println("Error: Illegal start or end indexes for generating an Image using a specific series of Layers");
			//TODO: Generate warning box
			return generateImage();
		}
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Color[][] merge = new Color[width][height];
		updateLayers();
		for(int h = startLay; h < endLay; h++) {
			Layer l = layers.get(h);
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
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image drawImage = bI.getScaledInstance((int)(out.getWidth(null) * scale), (int)(out.getHeight(null) * scale), Image.SCALE_DEFAULT);
		while(!tk.prepareImage(drawImage, -1, -1, null)){	}
		return drawImage;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setScale(double in) {
		scale = in;
		updateDisplayImage();
	}
	
//---  Getter Methods   -----------------------------------------------------------------------

	public Image getImage() {
		return bI;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
}
