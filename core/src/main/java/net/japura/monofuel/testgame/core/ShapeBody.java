package net.japura.monofuel.testgame.core;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class ShapeBody {
	
	static int bodies = 0;
	int thisbody;
	float[] cameraLocation = TestGame.getCamera();
	BodyDef shapeBodyDef;
	Body body;
	PolygonShape shape;
	World thisWorld;
	float scale;
	float oldAngle;
	float[] oldLocation;
	
	ArrayList<Shape> shapeList = new ArrayList<Shape>();
	
	
	public ShapeBody(String type, float[] location) {
		
				//set if the body is static or dynamic
				shapeBodyDef = new BodyDef();
				if (type.equals("STATIC")){
					shapeBodyDef.type = BodyType.STATIC;
				} else if (type.equals("DYNAMIC")) {
					shapeBodyDef.type = BodyType.DYNAMIC;
				}
				
				//places the shape definition on the world
			    shapeBodyDef.position.x = location[0];
			    shapeBodyDef.position.y = location[1];
			    
			    scale = Shape.getScale();
			    
			    //creates a physics body from the shape definition
			    thisWorld = TestGame.getWorld();
			    body = thisWorld.createBody(shapeBodyDef);
			    thisbody = bodies++;
			    
			  //set delta info for calculations
				oldAngle = body.getAngle();
				oldLocation = new float[] {body.getPosition().x,body.getPosition().y};
				shapeList.add(new Shape(this,new float[] {32,32},location,new int[] {0,0},"grass",1));
		
	}
	
	public ShapeBody(String[][] arrayBody, String type, float[] location, int depth) {
		/*
		 *  H = basic hull section
		 *  C = main cpu (camera follows)
		 *  LG:U+D+L+R = laser gun with direction
		 *  PG:U+D+L+R = projectile gun with direction
		 *  E:U+D+L+R = engine
		 *  T:U+D+L+R = tractor beam
		 * 
		 */
		System.out.println("new body at " + location[0] + " : "+ location[1]);
		
		//set if the body is static or dynamic
		shapeBodyDef = new BodyDef();
		if (type.equals("STATIC")){
			shapeBodyDef.type = BodyType.STATIC;
		} else if (type.equals("DYNAMIC")) {
			shapeBodyDef.type = BodyType.DYNAMIC;
		}
		
		//places the shape definition on the world
	    shapeBodyDef.position.x = location[0];
	    shapeBodyDef.position.y = location[1];
	    
	    //creates a physics body from the shape definition
	    thisWorld = TestGame.getWorld();
	    body = thisWorld.createBody(shapeBodyDef);
	    thisbody = bodies++;
	    
		
	    scale = Shape.getScale();
		
		float[] thisLocation;
		
		for (int i = 0; i < arrayBody.length; i++) {
			for (int k = 0; k < arrayBody[i].length; k++) {
				//thisLocation = new float[] {0,0};
				//thisLocation[0] = location[0]+((arrayBody[i].length-(arrayBody[i].length/2)+i)*32);
				//System.out.println("location modifier: " + ((arrayBody[i].length-(arrayBody[i].length/2)+i)*32));
				//thisLocation[1] = location[1]+((arrayBody[i].length-(arrayBody[i].length/2)+k)*32);
				//System.out.println("location modifier: " + ((arrayBody[i].length-(arrayBody[i].length/2)+k)*32));
				
				//System.out.println(thisLocation[0] + " : " + thisLocation[1]);
				if (arrayBody[i][k].equals("H")) {
					shapeList.add(new Shape(this,new float[] {32,32}, location,new int[] {i,k},"hull",depth));
					//System.out.println("adding hull section");
				}
				if (arrayBody[i][k].equals("C")) {
					shapeList.add(new Shape(this,new float[] {32,32}, location,new int[] {i,k},"cpu",depth));
					TestGame.addCPU(shapeList.get(shapeList.size()-1));
					//System.out.println("adding cpu section");
				}
				if (arrayBody[i][k].equals("A")) {
					shapeList.add(new Shape(this,new float[] {32,32}, location,new int[] {i,k},"asteroid",depth));
					TestGame.addCPU(shapeList.get(shapeList.size()-1));
					//System.out.println("adding cpu section");
				}
				if (arrayBody[i][k].equals("E")) {
					shapeList.add(new Shape(this,new float[] {32,32}, location,new int[] {i,k},"cargo",depth));
					TestGame.addCPU(shapeList.get(shapeList.size()-1));
					//System.out.println("adding cpu section");
				}
			}
		}
		
		//set delta info for calculations
				oldAngle = body.getAngle();
				oldLocation = new float[] {body.getPosition().x,body.getPosition().y};
		
	}
	
	public void paint(float alpha) {
		
		//move the object based on how many frames have passed and how much the object has moved
	    float x = ((body.getPosition().x/scale) * alpha) + ((oldLocation[0]/scale) * (1f - alpha));
	    float y = ((body.getPosition().y/scale) * alpha) + ((oldLocation[1]/scale) * (1f - alpha));
	    float a = (body.getAngle() * alpha) + (oldAngle * (1f - alpha));
	    for (Shape item : shapeList) {
	    //item.getAsset().setTranslation(x + (TestGame.WIDTH/2-(cameraLocation[0]/scale)),
	    					 //y + (TestGame.HEIGHT/2-(cameraLocation[1]/scale)));
	    //item.getAsset().setRotation(a);
	    item.paint(new float[] {x + (TestGame.WIDTH/2-(cameraLocation[0]/scale)),
	    					 y + (TestGame.HEIGHT/2-(cameraLocation[1]/scale))}, a);
	    }
		
	}
	
	//updates the deltas to be used in graphical calculations
	public void updateLocation(float alpha) {
		cameraLocation = TestGame.getCamera();
		oldLocation[0] = body.getPosition().x;
	    oldLocation[1] = body.getPosition().y;
		oldAngle = body.getAngle();
	}
	
	//returns the location of the object
	public float[] getLocation() {
		return new float[] {body.getPosition().x,body.getPosition().y};
	}
	
	//checks if the tested point is inside this object
	public boolean checkCollision(float x, float y) {
		for (Shape item : shapeList) {
			if (item.getShape().testPoint(
					body.getTransform(),
				               new Vec2((x - (TestGame.WIDTH/2-(cameraLocation[0]/scale)))*scale,
				            		    (y - (TestGame.HEIGHT/2-(cameraLocation[1]/scale)))*scale))) {
				return true;
			}
		}
		
		return false;
	}
	
	//applies a force to this object of this vector from this object
	public void applyForce(float x, float y) {
		body.applyForce(new Vec2((((x - (TestGame.WIDTH/2-(cameraLocation[0]/scale)))*scale)-body.getPosition().x)*100,
				                 (((y - (TestGame.HEIGHT/2-(cameraLocation[1]/scale)))*scale)-body.getPosition().y)*100),
				                 body.getWorldCenter());
	}
	
	
	
	public void addFixture(FixtureDef fd) {
		body.createFixture(fd);
	}
	
	public Body getBody() {
		return body;
	}
	
	public int getNumber() {
		return thisbody;
	}
	
	public Shape getShape(int index) {
		return shapeList.get(index);
	}
	
	public int getSize() {
		return shapeList.size();
	}
	
}
