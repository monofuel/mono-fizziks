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
	
	//set scale of pixels to jbox2d grid
	float scale = 0.1f;
	
	public Shape(World world,String type,float[] size, float[] location) {
		System.out.println("creating box at:" + location[0] + "," + location[1]+ "with size of: " + size[0] + "," + size[1]);
		
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
	    bodySize = new float[] {size[0]*scale,size[1]*scale};
	    shape.setAsBox(bodySize[0]/2,bodySize[1]/2, new Vec2(bodyLocation[0],bodyLocation[1]), 0);
	    
	    fd = new FixtureDef();
	    fd.shape = shape;
	    fd.density = 1f;
	    fd.restitution = 0.2f;
	    body.createFixture(fd);
	    
	    //set delta info
		oldAngle = body.getAngle();
	}
	
	public void createLayer(ImageLayer createImage,int depth) {
		image = createImage;
		image.setDepth(depth);
		//image.setSize(bodySize[0]/scale,bodySize[1]/scale);
		image.setScale((bodySize[0]/scale)/image.width(),(bodySize[1]/scale)/image.height());
		image.setOrigin(bodySize[0]/scale,bodySize[1]/scale);
		image.setTranslation(body.getPosition().x/scale, body.getPosition().y/scale);
	}
	
	public ImageLayer getLayer() {
		return image;
	}
	public void updateLocation() {
		image.setRotation(oldAngle-body.getAngle());
		image.setOrigin((bodySize[0]/scale)+body.getPosition().x,(bodySize[1]/scale)+body.getPosition().y);
		image.setTranslation(body.getPosition().x/scale, body.getPosition().y/scale);
		oldAngle = body.getAngle();
	}
	
}
