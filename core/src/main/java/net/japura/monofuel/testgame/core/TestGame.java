package net.japura.monofuel.testgame.core;

import static playn.core.PlayN.*;

import java.util.ArrayList;

import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import playn.core.Pointer;

import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;

public class TestGame implements Game {
	
	  public static int WIDTH = 1440;
	  public static int HEIGHT = 900;
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
	  
	  World world;
	  Image grassTile;
	  
	  ArrayList<Shape> shapeList = new ArrayList<Shape>();
	
  @Override
  public void init() {
	  
	graphics().setSize(WIDTH, HEIGHT);
	  
	// create and add background image layer
	Image bgImage = assets().getImage("images/bg.png");
	ImageLayer bgLayer = graphics().createImageLayer(bgImage);
	bgLayer.setScale(HEIGHT/bgLayer.height(), WIDTH/bgLayer.width());
	bgLayer.setDepth(0);
	graphics().rootLayer().add(bgLayer);
	
	//creates a menu
	final MenuBar gameMenu = new MenuBar(horizontal);
	  
    
    //loads grass.png
    grassTile = assets().getImage("images/grass.png");
	
    /*
    //grid[] is the x and y sizes of the array
    for (int x = 0; x < grid[0]; x++){
    	fgLayer.add(new ArrayList<ImageLayer>());
    	for (int y = 0; y < grid[1]; y++){
    		
    		//adds a grass tile to each ImageLayer in the array
    		fgLayer.get(x).add(graphics().createImageLayer(grassTile));
    		fgLayer.get(x).get(y).setDepth(1);
    		
    		//sets the default scale and location of each tile
    		fgLayer.get(x).get(y).setTranslation((16*scale*x)+pan[0],(16*scale*y)+pan[1]);
    		fgLayer.get(x).get(y).setScale(scale);
    	}
    }*/
    
    
    
    //iterate through every x and y block
    /*
    for (int x = 0; x < grid[0]; x++){
    	for (int y = 0; y < grid[1]; y++){
    		//add every item in the fgLayer arraylist to the graphics().rootlayer() for rendering
    		graphics().rootLayer().add(fgLayer.get(x).get(y));
    	}
    }*/
    
    
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
    	
    	public void onPointerEnd(Pointer.Event event) {
    		do {
    		if (horizontal) {
    			if (event.x() > gameMenu.width()){
    				click(event.x(),event.y());
    				break;
    			}
    		} else {
    			if (event.y() > gameMenu.width()){
    				click(event.x(),event.y());
    				break;
    			}
    		}
    		gameMenu.click(event.x(), event.y());
    		} while (false);
    	}
    });

    //physics code
    Vec2 gravity = new Vec2( 0.0f, 50.0f);
    
    boolean doSleep = true;
    
    world = new World(gravity, doSleep);

    //creates object
	shapeList.add(new Shape(world, "STATIC",new float[] {512,32}, new float[] {500,500}));
	shapeList.get(0).createLayer(graphics().createImageLayer(grassTile),1);
	
	
	//sets the default scale and location of each tile
	graphics().rootLayer().add(shapeList.get(0).getLayer());
    
    //sets the load bool to true so the paint and update methods know we're ready
    loaded = true;
  }
  
  public void click(float x, float y) {
	  switch (mode) {
	  case 0:
		  createBox(x,y);
		  break;
	  case 1:
		  
		  break;
	  }
  }
  
  public static void setMode(int modeSet) {
	  mode = modeSet;
	  System.out.println(mode);
  }
  
  public void createBox(float x, float y) {
	  
	  int index = shapeList.size();
	  
	  shapeList.add(new Shape(world, "DYNAMIC",new float[] {32,32},new float[] {x+16,y+16}));
	  shapeList.get(index).createLayer(graphics().createImageLayer(grassTile),1);
		
	  //sets the default scale and location of each tile
	  graphics().rootLayer().add(shapeList.get(index).getLayer());
  }
  
  //public void weldBox(Shape[] boxlist) {}

  @Override
  public void paint(float alpha) {
	  if (loaded) { //check if the init has finished

		  //paints every block  
		  for (Shape item : shapeList) {
			  item.paint(alpha);
		  }
		  
	  }
  }

  @Override
  public void update(float delta) {
	  if (loaded) { //check if the init has finished

		  //step world physics
		  world.step(delta/1000, 1,1);
		  
		//updates every block  
		  for (Shape item : shapeList) {
			  item.updateLocation(delta);
		  }
		  
	  }
  }

  @Override
  public int updateRate() {
    return 5; //milliseconds to wait each update step
  }
  
}


