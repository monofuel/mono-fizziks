package net.japura.monofuel.testgame.core;

import static playn.core.PlayN.*;

import java.util.ArrayList;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;

public class MenuBar {
	
	ArrayList<Button> icons = new ArrayList<Button>();
	Image buttonBG;
	ImageLayer iconsLayer;
	ImageLayer buttonBGLayer;
	
	public MenuBar(boolean horizontal) {
		
		//loads the image for the menu bar background
		Image buttonBG = assets().getImage("images/buttonBG.png");
		buttonBGLayer = graphics().createImageLayer(buttonBG);
		buttonBGLayer.setDepth(2);
		
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
		
		icons.add(new BoxButton("Box", assets().getImage("images/boxButton.png"),assets().getImage("images/boxButtonDown.png")));
		icons.add(new WeldButton("Weld", assets().getImage("images/weldButton.png"),assets().getImage("images/weldButtonDown.png")));
		
		int itin = 1;
		for (Button item : icons) {
			item.layer().setDepth(3);
			item.setOrder(itin);
			item.layer().setTranslation(10,((TestGame.HEIGHT-item.layer().scaledHeight())/(icons.size()+1))*itin++);
			graphics().rootLayer().add(item.layer());
		}
	}
	
	
	
	public class Button {
		ImageLayer iconDownImage,iconUpImage;
		String name;
		int order;
		boolean isDown = false;
		
		public Button(String thisName, Image icon, Image iconDown) {
			iconDownImage = graphics().createImageLayer(iconDown);
			iconUpImage = graphics().createImageLayer(icon);
			name = thisName;
		}
		
		public void onClick() {
			if (!isDown) {
				for (Button item : icons) {
					if (item != this) {
						item.setUp();
					}
				}
				iconDownImage.setDepth(3);
				iconDownImage.setTranslation(10, ((TestGame.HEIGHT-iconDownImage.scaledHeight())/(icons.size()+1))*order);
				graphics().rootLayer().add(iconDownImage);
				isDown = true;
			}
		}
		
		public ImageLayer layer() {
			return iconUpImage;
		}
		
		public String getName() {
			return name;
		}
		
		public void setOrder(int requestOrder) {
			order = requestOrder;
		}
		
		public int getOrder() {
			return order;
		}
		public void setUp() {
			try {
			graphics().rootLayer().remove(iconDownImage);
			} catch (Exception e) {
				//catch if no buttons are down
			}
			iconUpImage.setDepth(3);
			iconUpImage.setTranslation(10,((TestGame.HEIGHT-iconUpImage.scaledHeight())/(icons.size()+1))*order);
			graphics().rootLayer().add(iconUpImage);
			isDown = false;
		}
	}
	public class BoxButton extends Button {

		public BoxButton(String name, Image icon, Image iconDown) {
			super(name, icon, iconDown);
			
		}
		public void onClick() {
			super.onClick();
			TestGame.setMode(0);
		}
		
	}
	
	public class WeldButton extends Button {

		public WeldButton(String name, Image icon, Image iconDown) {
			super(name, icon, iconDown);
			
		}
		
		public void onClick() {
			super.onClick();
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
				break;
			}
		}
	}
	
	public boolean checkCollision(float[] coord, ImageLayer layer) {
		
		if (Layer.Util.hitTest(layer, coord[0],coord[1])){
			return true;
		}
		
		return false;
		
	}
	
}

