package visual.drawboard.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import visual.composite.HandlePanel;
import visual.panel.CanvasPanel;

//TODO: Can edit a Picture and select what layer you want to work on, or can generate a layer on its own to edit and reintegrate

public class DisplayPicture implements Display{
	
//---  Constants   ----------------------------------------------------------------------------
	
	private final static int SAVE_TYPE_PNG = 0;
	private final static int SAVE_TYPE_JPG = 1;

//---  Instance Variables   -------------------------------------------------------------------
	
	private Image bI;
	private int width;
	private int height;
	private int activeLayer;
	private double scale;

//---  Constructors   -------------------------------------------------------------------------
	
	public DisplayPicture(int wid, int hei, Image inImg) {
		bI = inImg;
		width = wid;
		height = hei;
		scale = 1;
		activeLayer = 1;
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public HandlePanel generateDisplay(int x, int y) {
		int wid = (int)(scale * width);
		int hei = (int)(scale * height);
		HandlePanel p = new HandlePanel(x, y, wid, hei) {
			@Override
			public void keyBehaviour(char event) {
				//TODO: Keyboard shortcuts for Picture
			}
		};
		p.addImage("img", 10, false, 0, 0, false, bI);
		return p;
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
