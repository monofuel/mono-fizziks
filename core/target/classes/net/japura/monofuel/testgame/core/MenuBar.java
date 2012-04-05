package net.japura.monofuel.testgame.core;

import static playn.core.PlayN.*;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;

import javax.swing.event.EventListenerList;

import playn.core.Graphics;
import playn.core.Image;
import playn.core.ImageLayer;


public class MenuBar {
	
	ArrayList<Button> icons = new ArrayList();
	Image buttonBG;
	ImageLayer iconsLayer;
	ImageLayer buttonBGLayer;
	
	public MenuBar(boolean horizontal) {
		
		//loads the image for the menu bar background
		Image buttonBG = assets().getImage("images/buttonBG.png");
		buttonBGLayer = graphics().createImageLayer(buttonBG);
		
		//check is the game is in horizontal or else portrait mode
		if (horizontal) {
			
			//resizes the menu bar to fit the screen height
			if (TestGame.HEIGHT != buttonBGLayer.height()) {
				buttonBGLayer.setScale(TestGame.HEIGHT/buttonBGLayer.height());
			}	
		} else {
			buttonBGLayer.setRotation((float) Math.PI/2);
			buttonBGLayer.setTranslation(TestGame.WIDTH, 0);
			if (TestGame.WIDTH != buttonBGLayer.height()) {
				buttonBGLayer.setScale(TestGame.WIDTH/buttonBGLayer.height());
			}
		}
		
		//adds the menu bar to rendering
		graphics().rootLayer().add(buttonBGLayer);
		
		icons.add(new boxButton("Box", assets().getImage("boxButton.png")));
		icons.add(new weldButton("Weld", assets().getImage("weldButton.png")));
		
		
		for (Button item : icons) {
			item.layer().setTranslation(TestGame.HEIGHT/icons.size(), 10);
			graphics().rootLayer().add(item.layer());
		}
	}
	
	
	
	public class Button {
		ImageLayer iconImage;
		
		public Button(String name, Image icon) {
			iconImage = graphics().createImageLayer(icon);
		}
		
		public void onClick() {
			
		}
		
		public ImageLayer layer() {
			return iconImage;
		}
		
	}
	public class boxButton extends Button {

		public boxButton(String name, Image icon) {
			super(name, icon);
			
		}
		public void onClick() {
			TestGame.setMode(0);
		}
	}
	
	public class weldButton extends Button {

		public weldButton(String name, Image icon) {
			super(name, icon);
			
		}
		
		public void onClick() {
			TestGame.setMode(1);
		}
	}
	
	public float width() {
		return buttonBGLayer.width();
	}
	
	public void click(float x, float y) {
		
	}
	
}

