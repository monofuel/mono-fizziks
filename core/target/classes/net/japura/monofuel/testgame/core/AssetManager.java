package net.japura.monofuel.testgame.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import java.util.HashMap;
import java.util.Map;

import playn.core.*;
import playn.core.ImmediateLayer.Renderer;

/**
 * 
 * @author monofuel
 * 
 * Main class for a static object to handle all the images.
 */

public class AssetManager {
	
	static Map<String,Image> imageMap = new HashMap<String, Image>(); //HashMap of image files
	static Map<Integer,GameAsset> assetMap = new HashMap<Integer,GameAsset>(); //HashMap of game tiles
	static Map<String,int[]> tilesetMap = new HashMap<String,int[]>();
	static int lastKey = 0; //key used for current game tile
	
	public AssetManager() {
		
		//manually done elements on the tileset
		tilesetMap.put("hull",new int[] {0,0});
		tilesetMap.put("cargo",new int[] {1,0});
		tilesetMap.put("thruster",new int[] {2,0});
		tilesetMap.put("cpu",new int[] {3,0});
		tilesetMap.put("capacitor",new int[] {4,0});
		tilesetMap.put("fullcargo",new int[] {5,0});
		tilesetMap.put("engine",new int[] {6,0});
		tilesetMap.put("tractorbeam",new int[] {7,0});
		tilesetMap.put("solarpane",new int[] {8,0});
		tilesetMap.put("minithruster",new int[] {9,0});
		tilesetMap.put("builder",new int[] {10,0});
		tilesetMap.put("projgun",new int[] {11,0});
		tilesetMap.put("coolant",new int[] {0,1});
		tilesetMap.put("engine2",new int[] {1,1});
		tilesetMap.put("???",new int[] {2,1});
		tilesetMap.put("scoop",new int[] {3,1});
		tilesetMap.put("armor",new int[] {4,1});
		tilesetMap.put("radar",new int[] {5,1});
		tilesetMap.put("asteroid",new int[] {6,1});
	
	}
	
	//creates a new asset
	public int newAsset(String imageName, int depth) {
		
		
		lastKey++;//increases the current key index
		
		if (!imageMap.containsKey(imageName)) { //checks if the image requested is already loaded
			loadImage(imageName); //if not, it loads the image.
		}
		
		//once the image is loaded, a new game asset is created with the image file, depth, and the current key index
		assetMap.put(lastKey, new GameAsset(imageMap.get(imageName), depth, lastKey));
		return lastKey; //returns the game asset key so this asset can be accessed remotely
	}
	
	//loads the image file requested
	private void loadImage(String imageName) {
		
		if (tilesetMap.containsKey(imageName)) {
			imageMap.put(imageName,fromTile(tilesetMap.get(imageName)));
			System.out.println("loading tile " + imageName + " from tileset"); //debug output
			return;
		}
		// else load from png
	    imageMap.put(imageName,assets().getImage("/images/"+imageName+".png"));
	    System.out.println("loading file resources/"+imageName+".png"); //debug output
	}
	
	Image tileset = assets().getImage("/images/ships3.png");
	
	private Image fromTile(int[] loc) {
		
		System.out.println("loading tile from "+loc[0]*16+ ":"+ loc[1]*16);
		CanvasImage finalImage = graphics().createImage(16,16);
		Canvas C = finalImage.canvas();
		C.drawImage(tileset, 0, 0, 16, 16, loc[0]*16, loc[1]*16, 16, 16);
		
		return finalImage;
		
		
		
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
		public void setOrigin(float[] location) {
			assetLayer.setOrigin(location[0],location[1]);
		}
		
		//sets the origin of the object to it's middle
		public void defaultOrigin() {
			assetLayer.setOrigin(assetLayer.width()/2, assetLayer.height()/2);
		}
		
		//rotates the object a set amount
		public void setRotation(float angle) {
			assetLayer.setRotation(angle);
		}
		
	}
}
