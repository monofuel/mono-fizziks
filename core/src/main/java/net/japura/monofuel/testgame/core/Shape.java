package net.japura.monofuel.testgame.core;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
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
	float scale = 0.02f;
	
	public Shape(World world,String type,float[] size, float[] location) {
		//System.out.println("creating box at:" + location[0] + "," + location[1]+ "with size of: " + size[0] + "," + size[1]);
		
		bodySize = new float[] {size[0]*scale,size[1]*scale};
		bodyLocation = new float[] {location[0]*scale, location[1]*scale};
		
		shapeBodyDef = new BodyDef();
		if (type.equals("STATIC")){
			shapeBodyDef.type = BodyType.STATIC;
		} else if (type.equals("DYNAMIC")) {
			shapeBodyDef.type = BodyType.DYNAMIC;
		}
	    shapeBodyDef.position.x = bodyLocation[0];
	    shapeBodyDef.position.y = bodyLocation[1];
	    
	    body = world.createBody(shapeBodyDef);
	    shape = new PolygonShape();
	    
	    //shape.setAsBox(bodySize[0],bodySize[1], new Vec2(bodySize[0]/2,bodySize[1]/2), 0);
	    shape.setAsBox(bodySize[0],bodySize[1]);
	    
	    fd = new FixtureDef();
	    fd.shape = shape;
	    fd.density = .9f;
	    fd.friction = 0.3f;
	    fd.restitution = 0.6f;
	    body.createFixture(fd);
	    
	    //set delta info
		oldAngle = body.getAngle();
		oldLocation = new float[] {body.getPosition().x,body.getPosition().y};
	}
	
	public void createLayer(ImageLayer createImage,int depth) {
		image = createImage;
		image.setDepth(depth);
		//image.setScale(scale);
		image.setScale((bodySize[0]/scale*2)/image.width(),(bodySize[1]/scale*2)/image.height());
		image.setTranslation(body.getPosition().x/scale, body.getPosition().y/scale);
		image.setOrigin(image.height()/2,image.width()/2);
		
	}
	
	public ImageLayer getLayer() {
		return image;
	}
	public void paint(float alpha) {

		 // interpolate based on previous state
	    float x = ((body.getPosition().x/scale) * alpha) + ((oldLocation[0]/scale) * (1f - alpha));
	    float y = ((body.getPosition().y/scale) * alpha) + ((oldLocation[1]/scale) * (1f - alpha));
	    float a = (body.getAngle() * alpha) + (oldAngle * (1f - alpha));
	    image.setTranslation(x, y);
	    image.setRotation(a);
		
	}
	
	public void updateLocation(float alpha) {
		oldLocation[0] = body.getPosition().x;
	    oldLocation[1] = body.getPosition().y;
		oldAngle = body.getAngle();
		
	}
	
}
