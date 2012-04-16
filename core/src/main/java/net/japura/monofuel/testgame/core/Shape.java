package net.japura.monofuel.testgame.core;

import net.japura.monofuel.testgame.core.AssetManager.GameAsset;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
//import org.jbox2d.dynamics.joints.*;

public class Shape {

	BodyDef shapeBodyDef;
	Body body;
	PolygonShape shape;
	FixtureDef fd;
	//ImageLayer image;
	float[] bodySize;
	float[] bodyLocation;
	float oldAngle;
	float[] oldLocation;
	World thisWorld;
	AssetManager manager;
	GameAsset asset;
	
	int assetIndex = -1;
	
	//set scale of pixels to jbox2d grid
	float scale = 0.05f;
	
	//creates shape object
	//TODO: make the shape class modular with sub-classes of specific shapes
	public Shape(String type,float[] size, float[] location,String imageName, int depth) {
		
		
		thisWorld = TestGame.getWorld();
		manager = TestGame.getManager();
		
		//converts the pixel size and location to physics units
		bodySize = new float[] {size[0]*scale,size[1]*scale};
		bodyLocation = new float[] {location[0]*scale, location[1]*scale};
		
		//set if the body is static or dynamic
		shapeBodyDef = new BodyDef();
		if (type.equals("STATIC")){
			shapeBodyDef.type = BodyType.STATIC;
		} else if (type.equals("DYNAMIC")) {
			shapeBodyDef.type = BodyType.DYNAMIC;
		}
		
		//places the shape definition on the world
	    shapeBodyDef.position.x = bodyLocation[0];
	    shapeBodyDef.position.y = bodyLocation[1];
	    
	    //creates a physics body from the shape definition
	    body = thisWorld.createBody(shapeBodyDef);
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
	    body.createFixture(fd);
	    
	    //set delta info for calculations
		oldAngle = body.getAngle();
		oldLocation = new float[] {body.getPosition().x,body.getPosition().y};
		
		createLayer(imageName,depth);
		
	}
	
	//attach the ImageLayer graphic to the object
	public void createLayer(String createImage,int depth) {
		assetIndex = manager.newAsset(createImage,1);
		asset = manager.getAsset(assetIndex);
		asset.setScale(bodySize[0]/scale*2, bodySize[1]/scale*2);
		asset.setTranslation(body.getPosition().x/scale, body.getPosition().y/scale);
		asset.setOrigin();
		
	}
	
	public void paint(float alpha) {

		 //move the object based on how many frames have passed and how much the object has moved
	    float x = ((body.getPosition().x/scale) * alpha) + ((oldLocation[0]/scale) * (1f - alpha));
	    float y = ((body.getPosition().y/scale) * alpha) + ((oldLocation[1]/scale) * (1f - alpha));
	    float a = (body.getAngle() * alpha) + (oldAngle * (1f - alpha));
	    asset.setTranslation(x, y);
	    asset.setRotation(a);
		
	}
	
	//updates the deltas to be used in graphical calculations
	public void updateLocation(float alpha) {
		oldLocation[0] = body.getPosition().x;
	    oldLocation[1] = body.getPosition().y;
		oldAngle = body.getAngle();
		
	}
	
	//checks if the tested point is inside this object
	public boolean checkCollision(float x, float y) {
		return shape.testPoint(body.getTransform(), new Vec2(x*scale,y*scale));
	}
	
	//applies a force to this object of this vector from this object
	public void applyForce(float x, float y) {
		body.applyForce(new Vec2(((x*scale)-body.getPosition().x)*100,((y*scale)-body.getPosition().y)*100), body.getWorldCenter());
	}
	
	/*
	public void weldBox(Shape item) {
		WeldJointDef weldJoint = new WeldJointDef();
		weldJoint.bodyA = item.getBody();
		weldJoint.bodyB = this.getBody();
		//jointList.add(new WeldJoint(thisWorld.getPool(),weldJoint);
	}*/
	
	public Body getBody() {
		return body;
	}

}
