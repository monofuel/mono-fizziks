package net.japura.monofuel.testgame.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import java.util.HashMap;
import java.util.Map;

import playn.core.*;

/**
 * 
 * @author monofuel
 * 
 * Main class for a static object to handle all the images.
 */

public class AssetManager {
	
	static Map<String,Image> imageMap = new HashMap<String, Image>(); //HashMap of image files
	static Map<Integer,GameAsset> assetMap = new HashMap<Integer,GameAsset>(); //HashMap of game tiles
	static int lastKey = 0; //key used for current game tile
	
	//creates a new asset
	public int newAsset(String imageName, int depth) {
		
		lastKey++;//increases the current key index
		
		if (!imageMap.containsKey(imageName)) { //checks if the image requested is already loaded
			loadImage(imageName); //if not, it loads the image.
			System.out.println("loading file resources/"+imageName+".png"); //debug output
		}
		
		//once the image is loaded, a new game asset is created with the image file, depth, and the current key index
		assetMap.put(lastKey, new GameAsset(imageMap.get(imageName), depth, lastKey));
		return lastKey; //returns the game asset key so this asset can be accessed remotely
	}
	
	//loads the image file requested
	private void loadImage(String imageName) {
		imageMap.put(imageName,assets().getImage("/images/"+imageName+".png"));
	}
	
	//returns the game asset at a specified index
	public GameAsset getAsset(int index) {
		return  assetMap.get(index);
	}
	
	/**
	 * 
	 * @author monofuel
	 * Game Asset object manages each object and handles modifications to their attributes
	 * IMPORTANT: this does not include physical attributes, only graphical.
	 */
	
	public class GameAsset {
		ImageLayer assetLayer;
		
		//constructor that creates an image and sets the depth
		public GameAsset(Image imageName, int depth, int key) {
			assetLayer = graphics().createImageLayer(imageName);
			assetLayer.setDepth(depth);
			graphics().rootLayer().add(assetLayer);
		}
		
		
		//sets the scale of the object
		public void setScale(float height, float width) {
			
			//divides the desired size by the current to get the difference ratio for the scaling
			assetLayer.setScale(height/assetLayer.height(),
								width/assetLayer.width());
		}
		
		//sets the depth of the object
		public void setDepth(int depth) {
			assetLayer.setDepth(depth);
		}
		
		//sets the location of the object (in pixels)
		public void setTranslation(float x,float y) {
			assetLayer.setTranslation(x, y);
		}
		
		//sets the origin of the object to it's middle
		public void setOrigin() {
			assetLayer.setOrigin(assetLayer.width()/2, assetLayer.height()/2);
		}
		
		//rotates the object a set amount
		public void setRotation(float angle) {
			assetLayer.setRotation(angle);
		}
		
	}
}
