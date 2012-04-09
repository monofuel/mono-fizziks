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
	  
	  World world;
	  Image grassTile;

	  
	  ArrayList<Shape> shapeList = new ArrayList<Shape>();
	
  @Override
  public void init() {
	// create and add background image layer
	Image bgImage = assets().getImage("images/bg.png");
	ImageLayer bgLayer = graphics().createImageLayer(bgImage);
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
    /*
    BodyDef groundBodyDef = new BodyDef();
    groundBodyDef.type = BodyType.STATIC;
    groundBodyDef.position.x = 200;
    groundBodyDef.position.y = 300;
    
    Body body = world.createBody(groundBodyDef);
    PolygonShape shape = new PolygonShape();
    shape.setAsBox(5, 2, new Vec2(5,2), 0);
    
    FixtureDef fd = new FixtureDef();
    fd.shape = shape;
    fd.density = 1000f;
    fd.restitution = 1f;
    body.createFixture(fd);
	*/
    
    //adds a grass tile to each ImageLayer in the array
	shapeList.add(new Shape(world, "STATIC",new float[] {2,2},new float[] {300,200}));
	shapeList.get(0).createLayer(graphics().createImageLayer(grassTile),1);
	
	
	//sets the default scale and location of each tile
	shapeList.get(0).updateLocation();
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
	  System.out.println("creating box at:" + x + "," + y);
	  int index = shapeList.size();
	  
	  shapeList.add(new Shape(world, "DYNAMIC",new float[] {2,2},new float[] {x,y}));
	  shapeList.get(index).createLayer(graphics().createImageLayer(grassTile),1);
		
	  //sets the default scale and location of each tile
	  shapeList.get(index).updateLocation();
	  graphics().rootLayer().add(shapeList.get(index).getLayer());
  }
  
  //public void weldBox(Shape[] boxlist) {
	  
  //}

  @Override
  public void paint(float alpha) {
	  if (loaded) { // check if the init has finished
		  //gameMenu.update();
	  }
  }

  @Override
  public void update(float delta) {
	  if (loaded) { //check if the init has finished

		  world.step(delta/1000, 1,1);
		  
		  //updates every block
		  
		  for (Shape item : shapeList) {
			  item.updateLocation();
		  }
		  
		  //iterate through every x and y block
		  /*
		  for (int x = 0; x < grid[0]; x++){
		    	for (int y = 0; y < grid[1]; y++){
		    		
		    		//if the scale or panning location has changed, adjust it
		    		fgLayer.get(x).get(y).setTranslation((16*scale*x)+pan[0],(16*scale*y)+pan[1]);
		    		
		    		//TODO- check if scale has changed before setting scale
		    		fgLayer.get(x).get(y).setScale(scale);
		    	}
		    }*/
	  }
  }

  @Override
  public int updateRate() {
    return 25;
  }
  
}


