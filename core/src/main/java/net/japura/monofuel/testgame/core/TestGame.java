package net.japura.monofuel.testgame.core;

import static playn.core.PlayN.*;

import java.util.ArrayList;

import playn.core.Game;
import playn.core.Image;
import playn.core.Mouse;
import playn.core.Pointer;

import net.japura.monofuel.testgame.core.AssetManager.GameAsset;

import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;

public class TestGame implements Game {
	
	  public static int WIDTH = 1440;
	  public static int HEIGHT = 900;
	  float scale = 2;
	  static float[] cameraLocation = new float[] {0,0};
	  static int currentCamera = 0;
	  
	  float deltaX = 0;
	  float deltaY = 0;
	  static MenuBar gameMenu;
	  static boolean loaded = false;
	  static boolean paused = false;
	  static int mode;
	  static Shape selectedBox;
	  static boolean moving;
	  static boolean horizontal = true;
	  
	  static World world;

	  static ArrayList<Shape> shapeList = new ArrayList<Shape>();
	  static ArrayList<Shape> cpuList = new ArrayList<Shape>();
	  
	  public static AssetManager manager = new AssetManager();
	
  @Override
  public void init() {
	  
	graphics().setSize(WIDTH, HEIGHT);
	
	//creates the background layers and set it's size
	int bg = manager.newAsset("bg",0); 
	manager.getAsset(bg).setScale(HEIGHT, WIDTH);
	
	//creates a menu
	final MenuBar gameMenu = new MenuBar(horizontal);

	
	//
	// input handling
	//
	
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
    		mouseDown(event.x(), event.y());
    	}
    	
    	public void onPointerDrag(Pointer.Event event) {
    		//compares current location with last location and pans
    		
    		//sets new delta for next update
    		deltaX = event.x();
    		deltaY = event.y();
    		if (moving && selectedBox != null) {
    			moveBox(event.x(),event.y());
    		}
    		
    	}
    	
    	public void onPointerEnd(Pointer.Event event) {
    		do {
    		if (horizontal) {
    			if (event.x() > gameMenu.width()){
    				mouseUp(event.x(),event.y());
    				break;
    			}
    		} else {
    			if (event.y() > gameMenu.width()){
    				mouseUp(event.x(),event.y());
    				break;
    			}
    		}
    		if (moving) {
    			moveBoxStop();
    		}
    		gameMenu.click(event.x(), event.y());
    		} while (false);
    	}
    });
    
    
    //
    //physics world
    //
    
    
    Vec2 gravity = new Vec2( 0.0f, 0.0f);
    
    boolean doSleep = true;
    
    world = new World(gravity, doSleep);
    
    shapeList.add(new Shape("DYNAMIC",new float[] {32,32},new float[] {WIDTH/2,HEIGHT/2},"cpu",1));
    cpuList.add(shapeList.get(shapeList.size()-1));
    
    //sets the load bool to true so the paint and update methods know we're ready
    loaded = true;
  }
  
  public static AssetManager getManager() {
	  return manager;
  }
  
  public static World getWorld() {
	  return world;
  }
  
  
  public void mouseUp(float x, float y) {
	  switch (mode) {
	  case 0:
		  createBox(x,y);
		  break;
	  case 1:
		  
		  break;
	  case 2:
		  break;
	  }
  }
  
  public void mouseDown(float x, float y) {
	  switch (mode) {
	  case 0:
		  break;
	  case 1:
		  break;
	  case 2:
		  moveBoxStart(x,y);
		  break;
	  }
  }
  
  public static void setMode(int modeSet) {
	  mode = modeSet;
	  System.out.println("setting mode to: " + mode);
  }
  
  public static void pause(boolean status) {
	  paused = status;
  }
  
  public static void moveBox(float x, float y) {
	  selectedBox.applyForce(x, y);
  }
  
  public static void moveBoxStart(float x, float y) {
	  moving = true;
	  Shape foundBox = findBox(x,y);
	  if (foundBox != null) {
		  selectedBox = foundBox;
	  }
  }
  
  public static void moveBoxStop() {
	  moving = false;
	  selectedBox = null;
  }
  
  public static Shape findBox(float x, float y) {
	  for (Shape item : shapeList) {
		  if (item.checkCollision(x, y)) {
			  return item;
		  }
	  }
	  return null;
  }
  
  public void createBox(float x, float y) {
	  
	  //int index = shapeList.size();
	  
	  shapeList.add(new Shape("DYNAMIC",new float[] {32,32},new float[] {x+16,y+16},"grass",1));
	  //shapeList.get(index).createLayer("grass",1);
		
	  //sets the default scale and location of each tile
	  //graphics().rootLayer().add(shapeList.get(index).getLayer());
  }
  
  //public void weldBox(Shape[] boxlist) {}
  
  public static void setCamera(float[] position) {
	  
	  //sets camera location with slight lag behind the ship
	  cameraLocation[0] += (position[0]-cameraLocation[0])/5;
	  cameraLocation[1] += (position[1]-cameraLocation[1])/5;
  }
  
  public static float[] getCamera() {
	  return cameraLocation;
  }

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
		  if (!paused) {
			  world.step(delta/1000, 1,1);
		  }
		  
		  
		  //updates every block  
		  for (Shape item : shapeList) {
			  item.updateLocation(delta);
		  }
		  
		  //updates camera location
		  setCamera(cpuList.get(currentCamera).getLocation());
		  
		  
	  }
  }

  @Override
  public int updateRate() {
    return 25; //milliseconds to wait each update step
  }
  
}


