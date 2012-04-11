package net.japura.monofuel.testgame.core;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import playn.core.ImageLayer;


public class Shape {

	BodyDef shapeBodyDef;
	Body body;
	PolygonShape shape;
	FixtureDef fd;
	ImageLayer image;
	float[] bodySize;
	float[] bodyLocation;
	float oldAngle;
	float[] oldLocation;
	
	//set scale of pixels to jbox2d grid
	float scale = 0.05f;
	
	//creates shape object
	//TODO: make the shape class modular with sub-classes of specific shapes
	public Shape(World world, String type,float[] size, float[] location) {
	
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
	    body = world.createBody(shapeBodyDef);
	    //create a new basic shape
	    shape = new PolygonShape();
	    
	    //set the shape as a box of the size of our desired box in physics units
	    shape.setAsBox(bodySize[0],bodySize[1]);
	    
	    //define physical features of the object
	    fd = new FixtureDef();
	    fd.shape = shape;
	    fd.density = 1f;
	    fd.friction = 0.9f;
	    fd.restitution = 0.4f;
	    body.createFixture(fd);
	    
	    //set delta info for calculations
		oldAngle = body.getAngle();
		oldLocation = new float[] {body.getPosition().x,body.getPosition().y};
	}
	
	//attach the ImageLayer graphic to the object
	public void createLayer(ImageLayer createImage,int depth) {
		image = createImage;
		image.setDepth(depth);
		
		//increase the scale so the box graphic matches the physical dimensions of the box
		image.setScale((bodySize[0]/scale*2)/image.width(),(bodySize[1]/scale*2)/image.height());
		
		//moves the box to it's location
		image.setTranslation(body.getPosition().x/scale, body.getPosition().y/scale);
		
		//sets the origin for the object to rotate around
		image.setOrigin(image.height()/2,image.width()/2);
		
	}
	
	public ImageLayer getLayer() {
		return image;
	}
	public void paint(float alpha) {

		 //move the object based on how many frames have passed and how much the object has moved
	    float x = ((body.getPosition().x/scale) * alpha) + ((oldLocation[0]/scale) * (1f - alpha));
	    float y = ((body.getPosition().y/scale) * alpha) + ((oldLocation[1]/scale) * (1f - alpha));
	    float a = (body.getAngle() * alpha) + (oldAngle * (1f - alpha));
	    image.setTranslation(x, y);
	    image.setRotation(a);
		
	}
	
	//updates the deltas to be used in graphical calculations
	public void updateLocation(float alpha) {
		oldLocation[0] = body.getPosition().x;
	    oldLocation[1] = body.getPosition().y;
		oldAngle = body.getAngle();
		
	}
	
}
