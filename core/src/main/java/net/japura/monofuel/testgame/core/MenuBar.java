package net.japura.monofuel.testgame.core;

import static playn.core.PlayN.*;

import java.util.ArrayList;

import net.japura.monofuel.testgame.core.MenuBar.Button;

import playn.core.Graphics;
import playn.core.Image;
import playn.core.ImageLayer;


public class MenuBar {
	
	ArrayList<Button> icons = new ArrayList<Button>();
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
		
		icons.add(new BoxButton("Box", assets().getImage("boxButton.png")));
		icons.add(new WeldButton("Weld", assets().getImage("weldButton.png")));
		
		
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
	public class BoxButton extends Button {

		public BoxButton(String name, Image icon) {
			super(name, icon);
			
		}
		public void onClick() {
			TestGame.setMode(0);
		}
	}
	
	public class WeldButton extends Button {

		public WeldButton(String name, Image icon) {
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
		for (Button icon : icons) {
			if (checkCollision(new float[] {x,y}, icon.layer() )) {
				icon.onClick();
			}
		}
	}
	
	public boolean checkCollision(float[] coord, ImageLayer layer) {
		if (coord[0] > layer.originX() &&
			coord[1] > layer.originY() &&
			coord[0] < layer.originX()+layer.width() &&
			coord[1] < layer.originY()+layer.height()) {
			return true;
		}
		return false;
		
	}
	
}

