package net.japura.monofuel.testgame.core;

import static playn.core.PlayN.*;

import java.util.ArrayList;
import java.util.Random;

import playn.core.Game;
import playn.core.Mouse;
import playn.core.Pointer;

import net.japura.monofuel.testgame.core.AssetManager.GameAsset;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;

public class TestGame implements Game {
	  //sets the default screen width and height
	  public static int WIDTH = 1440;
	  public static int HEIGHT = 900;
	  
	  //sets the scale for the game (not menu)
	  //TODO: scrolling is broken
	  static float scale = 2;
	  
	  //offset for the camera location, and what CPU the camera is focused on
	  static float[] cameraLocation = new float[] {0,0};
	  static int currentCamera = 0;
	  
	  //change in the mouse location since last tick
	  float mouseDeltaX = 0;
	  float mouseDeltaY = 0;
	  
	  //creates the basic menu object
	  static MenuBar gameMenu;
	  
	  //waits before fireing up the game
	  static boolean loaded = false;
	  
	  //is the simulation paused?
	  static boolean paused = false;
	  
	  //determines the action of the mouse button
	  static int mode;
	  
	  //the currently selected box and what it's touching
	  static ShapeBody selectedBox;
	  static ShapeBody contactingBox;
	  
	  //is the mouse currently moving a box?
	  static boolean moving;
	  
	  //should the screen be horizontal or portrait
	  //TODO: if statement to decide based on aspect ratio
	  static boolean horizontal = true;
	  
	  //creates the physics world
	  static World world;

	  //creates the array of crafts/stellar objects
	  static ArrayList<ShapeBody> shapeList = new ArrayList<ShapeBody>();
	  //creates the array of player-owned CPU's
	  static ArrayList<Shape> cpuList = new ArrayList<Shape>();
	  //creates the array of contacting objects
	  static ArrayList<Contact> contactArray = new ArrayList<Contact>();
	  
	  //creates the main game asset manager to handle tilesets and object loading
	  public static AssetManager manager = new AssetManager();
	  
  @Override
  public void init() {
	
	//sets screen size
	//TODO: automate this task for varying screen sizes
	graphics().setSize(WIDTH, HEIGHT);
	
	//creates the background layers and set it's size
	int bg = manager.newAsset("bg",0); 
	manager.getAsset(bg).setScale(HEIGHT, WIDTH);
	
	//creates a menu
	final MenuBar gameMenu = new MenuBar(horizontal);

	
	//
	// input handling
	//
	
	//
	//TODO: ponder if this is a good way to handle mouse input.
	//the main game class has listeners for each event and 
	//manipulates variables that affect how the game flows through
	//each tick. however, would it be better if each occasion
	//some form of input was needed, if a separate listener was
	//implemented for it in the desired function?
	
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
    		mouseDeltaX = event.x();
    		mouseDeltaY = event.y();
    		mouseDown(event.x(), event.y());
    	}
    	
    	public void onPointerDrag(Pointer.Event event) {
    		//compares current location with last location and pans
    		
    		//sets new delta for next update
    		mouseDeltaX = event.x();
    		mouseDeltaY = event.y();
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
    
    class ContList implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			// TODO Auto-generated method stub
			boolean contacting = false;
			for (Contact item : contactArray) {
				if (item.equals(contact)) {
					contacting = true;
				}
			}
			if (!contacting) {
				contactArray.add(contact);
			}
			
		}

		@Override
		public void endContact(Contact contact) {
			try {
					contactArray.remove(contact);
			} catch (Exception e) {
				//HA! you really expected me to catch my errors?
			}
			
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
    Vec2 gravity = new Vec2( 0.0f, 0.0f);
    
    boolean doSleep = true;
    
    world = new World(gravity, doSleep);
    
    world.setContactListener(new ContList());
    
    //shapeList.add(new Shape("DYNAMIC",new float[] {32,32},new float[] {WIDTH/2,HEIGHT/2},"cpu",1));
    //cpuList.add(shapeList.get(shapeList.size()-1));
    
    String[][] shipBody = new String[][]
    		{{" ","H","H", " "},
    		 {"H","C","E", "H"},
    		 {" ","H","H", " "}};
    String[][] asteroid = new String[][]
    		{{" ","A"," "},
    		 {"A","A","A"},
    		 {"A","A","A"},
    		 {" ","A"," "}};
    
    
    ShapeBody ship = new ShapeBody(shipBody,"DYNAMIC",new float[] {0,0},1);
    shapeList.add(ship);
    
    for (int i = 0; i < 10; i++) {
    	Random randint = new Random();
    	randint.nextInt(100);
    	shapeList.add(new ShapeBody(asteroid,"DYNAMIC",new float[] {1*(randint.nextInt(100)-50),
    																1*(randint.nextInt(100)-50)},1));
    }
    
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
		  selectBox(x,y);
		  break;
	  case 2:
		  break;
	  case 3:
		  weldBox(x,y);
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
	  case 3:
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
	  for (Contact item : contactArray) {
		  if (((Shape)item.m_fixtureA.m_userData).equals(selectedBox)) {
			  //contactingBox = ((Shape)item.m_fixtureB.m_userData);
		  } else if (((Shape)item.m_fixtureB.m_userData).equals(selectedBox)) {
			  //contactingBox = ((Shape)item.m_fixtureA.m_userData);
		  }
	  }
  }
  
  public static void moveBoxStart(float x, float y) {
	  moving = true;
	  ShapeBody foundBox = findBox(x,y);
	  if (foundBox != null) {
		  selectedBox = foundBox;
	  }
  }
  
  public static void moveBoxStop() {
	  moving = false;
	  if (contactingBox != null) {
		  //new ShapeBody(selectedBox, contactingBox);
	  }
	  selectedBox = null;
	  contactingBox = null;
  }
  
  public static ShapeBody findBox(float x, float y) {
	  for (ShapeBody item : shapeList) {
		  if (item.checkCollision(x, y)) {
			  return item;
		  }
	  }
	  return null;
  }
  
  public void createBox(float x, float y) {
	  
	  //int index = shapeList.size();
	  
	  String[][] newBlock = new String[][]
	    		{{" ","A"," "},
	    		 {"A","A","A"},
	    		 {" ","A"," "}};
	  System.out.println("camera at "+ cameraLocation[0] + ":" + cameraLocation[1]);
	    ShapeBody roid = new ShapeBody(newBlock,"DYNAMIC",new float[] {(x-(WIDTH/2)-8)*Shape.getScale()+cameraLocation[0],
	    															   (y-(HEIGHT/2)-8)*Shape.getScale()+cameraLocation[1]} ,1);
	    shapeList.add(roid);
	    
	    
	  
	  //shapeList.add(new Shape("DYNAMIC",new float[] {32,32},new float[] {x+16,y+16},"grass",1).getParent());
	  //shapeList.add(new ShapeBody(new String[][] {{"H"}},"DYNAMIC",new float[] {WIDTH/2+200,HEIGHT/2+200},1));
	  //shapeList.add(new ShapeBody("DYNAMIC",new float[] {x+16,y+16}));
	  //shapeList.get(index).createLayer("grass",1);
		
	  //sets the default scale and location of each tile
	  //graphics().rootLayer().add(shapeList.get(index).getLayer());
  }
  
  public void selectBox(float x, float y) {
	  selectedBox = findBox(x,y);
	  if (selectedBox != null) {
	  setMode(3);
	  }
  }
  
  public void weldBox(float x, float y) {
	  ShapeBody secondBox = findBox(x,y);
	  if (secondBox != null) {
		  //new ShapeBody(selectedBox, secondBox);
		  setMode(1);
	  }
  }
  
  public static void setCamera(float[] position) {
	  
	  //sets camera location with slight lag behind the ship
	  cameraLocation[0] += (position[0]-cameraLocation[0])/8;
	  cameraLocation[1] += (position[1]-cameraLocation[1])/8;
  }
  
  public static float[] getCamera() {
	  return cameraLocation;
  }
  
  public static void addCPU(Shape cpuShape) {
	  cpuList.add(cpuShape);
  }

  @Override
  public void paint(float alpha) {
	  if (loaded) { //check if the init has finished

		  //paints every block  
		  for (ShapeBody item : shapeList) {
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
		  for (ShapeBody item : shapeList) {
			  item.updateLocation(delta);
		  }
		  
		  //updates camera location
		  setCamera(cpuList.get(currentCamera).getLocation());
		  
		  
	  }
  }

  @Override
  public int updateRate() {
    return 10; //milliseconds to wait each update step
  }
  
}


