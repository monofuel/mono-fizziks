package net.japura.monofuel.testgame.core;

import static playn.core.PlayN.*;

import java.awt.Canvas;
import java.util.ArrayList;

import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import playn.core.Pointer;
import playn.core.Pointer.Adapter;
import playn.core.Pointer.Event;
//import org.jbox2d.*;

public class TestGame implements Game {
	
	  public final static float WIDTH = graphics().width();
	  public final static float HEIGHT = graphics().height();
	  float scale = 2;
	  
	  final int[] grid = new int[] {10,10};
	  float[] pan = new float[] {0,0};
	  boolean panning = false;
	  float deltaX = 0;
	  float deltaY = 0;
	  MenuBar gameMenu;
	  boolean loaded = false;
	  static int mode;
	  static boolean horizontal = true;
	  public static Image boxButtonImage = assets().getImage("boxButton.png");
	  public static Image weldButtonImage = assets().getImage("weldButton.png");
	  
	  ArrayList<ArrayList<ImageLayer>> fgLayer = new ArrayList<ArrayList<ImageLayer>>();
	
  @Override
  public void init() {
	// create and add background image layer
	Image bgImage = assets().getImage("images/bg.png");
	ImageLayer bgLayer = graphics().createImageLayer(bgImage);
	graphics().rootLayer().add(bgLayer);
	
	//creates a menu
	final MenuBar gameMenu = new MenuBar(horizontal);
	  
    
    //loads grass.png
    Image grassTile = assets().getImage("images/grass.png");
    
    //grid[] is the x and y sizes of the array
    for (int x = 0; x < grid[0]; x++){
    	fgLayer.add(new ArrayList<ImageLayer>());
    	for (int y = 0; y < grid[1]; y++){
    		
    		//adds a grass tile to each ImageLayer in the array
    		fgLayer.get(x).add(graphics().createImageLayer(grassTile));
    		
    		//sets the default scale and location of each tile
    		fgLayer.get(x).get(y).setTranslation((16*scale*x)+pan[0],(16*scale*y)+pan[1]);
    		fgLayer.get(x).get(y).setScale(scale);
    	}
    }
    
    //iterate through every x and y block
    for (int x = 0; x < grid[0]; x++){
    	for (int y = 0; y < grid[1]; y++){
    		//add every item in the fgLayer arraylist to the graphics().rootlayer() for rendering
    		graphics().rootLayer().add(fgLayer.get(x).get(y));
    	}
    }
    
    
    //create a mouse listener to handle the mousewheel
    mouse().setListener(new Mouse.Adapter() {

    	public void onMouseWheelScroll(Mouse.WheelEvent event) {
    		
    		//if they are zooming in, or if the scale isn't too small, scroll.
    		if (event.velocity() > 0 || scale > 1) {
	    		scale += event.velocity()/4;
    		}
    		
    	}
	});
    
    //pointer listener to get mouse/touch location
    pointer().setListener(new Pointer.Adapter() {
    	
    	public void onPointerStart(Pointer.Event event) {
    		//set new start location
    		deltaX = event.x();
    		deltaY = event.y();
    	}
    	
    	public void onPointerDrag(Pointer.Event event) {
    		//compares current location with last location and pans
    		pan[0] += event.x()-deltaX;
    		pan[1] += event.y()-deltaY;
    		
    		//sets new delta for next update
    		deltaX = event.x();
    		deltaY = event.y();
    	}
    	
    	public void onPointerStop(Pointer.Event event) {
    		do {
    		if (horizontal) {
    			if (event.x() > gameMenu.width()){
    				break;
    			}
    		} else {
    			if (event.y() > gameMenu.width()){
    				break;
    			}
    		}
    		gameMenu.click(event.x(), event.y());
    		} while (false);
    	}
    });
    
    //sets the load bool to true so the paint and update methods know we're ready
    loaded = true;
  }
  
  public void click(float x, float y) {
	  switch (mode) {
	  case 1:
		  createBox(x,y);
		  break;
	  case 2:
		  
		  break;
	  }
  }
  
  public static void setMode(int modeSet) {
	  mode = modeSet;
	  System.out.println(mode);
  }
  
  public void createBox(float x, float y) {
	  
  }
  
  public void weldBox(Box[] boxlist) {
	  
  }

  @Override
  public void paint(float alpha) {
	  if (loaded) { // check if the init has finished
		  //gameMenu.update();
	  }
  }

  @Override
  public void update(float delta) {
	  if (loaded) { //check if the init has finished
		  
		  //iterate through every x and y block
		  for (int x = 0; x < grid[0]; x++){
		    	for (int y = 0; y < grid[1]; y++){
		    		
		    		//if the scale or panning location has changed, adjust it
		    		fgLayer.get(x).get(y).setTranslation((16*scale*x)+pan[0],(16*scale*y)+pan[1]);
		    		
		    		//TODO- check if scale has changed before setting scale
		    		fgLayer.get(x).get(y).setScale(scale);
		    	}
		    }
	  }
  }

  @Override
  public int updateRate() {
    return 25;
  }
  
}


