package net.japura.monofuel.testgame.core;

import net.japura.monofuel.testgame.core.AssetManager.GameAsset;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
//import org.jbox2d.dynamics.joints.*;

public class Shape {

	Body body;
	PolygonShape shape;
	FixtureDef fd;
	//ImageLayer image;
	float[] bodySize;
	float[] bodyLocation;
	float oldAngle;
	float[] oldLocation;
	float[] cameraLocation = TestGame.getCamera();
	int[] offset;
	World thisWorld;
	AssetManager manager;
	GameAsset asset;
	
	ShapeBody parentBody = null;
	int assetIndex = -1;
	
	//set scale of pixels to jbox2d grid
	static float scale = 0.05f;
	
	//creates shape object
	//TODO: make the shape class modular with sub-classes of specific shapes
	public Shape(String type,float[] size, float[] location,String imageName, int depth) {
		
		System.out.println(" Shape with this constructor is DEPRICIATED!");
		
		//convert x and y from screen coords to their proper world coords with camera offset
		location = new float[] {location[0] - (TestGame.WIDTH/2-(cameraLocation[0]/scale)),
		location[1] - (TestGame.HEIGHT/2-(cameraLocation[1]/scale))};
		
		//converts the pixel size and location to physics units
		bodySize = new float[] {size[0]*scale,size[1]*scale};
		bodyLocation = new float[] {location[0]*scale, location[1]*scale};
		
		thisWorld = TestGame.getWorld();
		manager = TestGame.getManager();
	    
		parentBody = new ShapeBody(type, bodyLocation);
		
	    //create a new basic shape
	    shape = new PolygonShape();
	    
	    //set the shape as a box of the size of our desired box in physics units
	    shape.setAsBox(bodySize[0],bodySize[1]);
	    
	    //define physical features of the object
	    fd = new FixtureDef();
	    fd.shape = shape;
	    fd.density = 1f;
	    fd.friction = 0.9f;
	    fd.restitution = 0.3f;
	    fd.userData = this;
	    
	    parentBody.addFixture(fd);
	    body = parentBody.getBody();
	    
	    
	    
	    //set delta info for calculations
		oldAngle = body.getAngle();
		oldLocation = new float[] {body.getPosition().x,body.getPosition().y};
		
		createLayer(imageName,depth);
		
	}
	
	
public Shape(ShapeBody parent,float[] size, float[] location,int[] newoffset, String imageName, int depth) {
		
		//convert x and y from screen coords to their proper world coords with camera offset
		location = new float[] {location[0] - (TestGame.WIDTH/2-(cameraLocation[0]*scale)),
		location[1] - (TestGame.HEIGHT/2-(cameraLocation[1]*scale))};
		
		//converts the pixel size and location to physics units
		bodySize = new float[] {size[0]*scale,size[1]*scale};
		//bodyLocation = new float[] {location[0]*scale, location[1]*scale};
		
		thisWorld = TestGame.getWorld();
		manager = TestGame.getManager();
		
		offset = newoffset;
		
	    //create a new basic shape
	    shape = new PolygonShape();
	    
	    //set the shape as a box of the size of our desired box in physics units
	    shape.setAsBox(bodySize[0],bodySize[1],new Vec2((offset[0]*64)*scale,(offset[1]*64)*scale), 0);
	    //Transform altTransform = new Transform();
	    //altTransform.set(new Vec2((offset[0]*64)*scale,(offset[1]*64)*scale), 0);
	    //System.out.print("alt: " + altTransform.position.x + " " + altTransform.position.y + "   ");
	    //shape.centroid(altTransform);

	    //define physical features of the object
	    fd = new FixtureDef();
	    fd.shape = shape;
	    fd.density = 1f;
	    fd.friction = 0.2f;
	    fd.restitution = 0.5f;
	    fd.userData = this;
	    
	    //parent.addFixture(fd);
	    
	    
	    
	    body = parent.getBody();
	    body.createFixture(fd);
	    
		
		createLayer(imageName,depth);
		
	}
	
	
	//attach the ImageLayer graphic to the object
	public void createLayer(String createImage,int depth) {
		assetIndex = manager.newAsset(createImage,1);
		asset = manager.getAsset(assetIndex);
		asset.setScale(bodySize[0]/scale*2, bodySize[1]/scale*2);
		asset.setTranslation(body.getPosition().x/scale, body.getPosition().y/scale);
		asset.defaultOrigin();
	}
	
	
	
	public GameAsset getAsset() {
		return asset;
	}
	
	/*
	public void weldBox(Shape item) {
		WeldJointDef weldJoint = new WeldJointDef();
		weldJoint.bodyA = item.getBody();
		weldJoint.bodyB = this.getBody();
		//jointList.add(new WeldJoint(thisWorld.getPool(),weldJoint);
	}*/
	
	//
	//joint methods
	//
	
	public void paint(float[] location, float rotation) {
		asset.setTranslation((location[0]+(offset[0]*64)*(float)Math.cos(rotation)-(offset[1]*64)*(float)Math.sin(rotation)),
				(location[1]+(offset[0]*64)*(float)Math.sin(rotation)+(offset[1]*64)*(float)Math.cos(rotation)));
		asset.setRotation(rotation);
	}
	
	public Vec2 getCenter() {
		return body.getWorldCenter();
	}
	
	public Body getBody() {
		return body;
	}
	
	public PolygonShape getShape() {
		return shape;
	}
	
	public static float getScale() {
		return scale;
	}
	
	public void setJoin(ShapeBody parent) {
		parentBody = parent;
	}
	
	public void delJoin() {
		parentBody = null;
	}

	public ShapeBody getParent() {
		return parentBody;
	}
	
	public float[] getLocation() {
		return new float[] {body.getPosition().x,body.getPosition().y};
	}
}
