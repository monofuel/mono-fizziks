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
	
	public Shape(World world,String type,float[] size, float[] location) {
		shapeBodyDef = new BodyDef();
		if (type.equals("STATIC")){
			shapeBodyDef.type = BodyType.STATIC;
		} else if (type.equals("DYNAMIC")) {
			shapeBodyDef.type = BodyType.DYNAMIC;
		}
	    shapeBodyDef.position.x = location[0];
	    shapeBodyDef.position.y = location[1];
	    
	    body = world.createBody(shapeBodyDef);
	    shape = new PolygonShape();
	    shape.setAsBox(size[0], size[1], new Vec2(location[0],location[1]), 0);
	    
	    fd = new FixtureDef();
	    fd.shape = shape;
	    fd.density = 100f;
	    fd.restitution = 0.7f;
	    body.createFixture(fd);
	}
	
	public void createLayer(ImageLayer createImage,int depth) {
		image = createImage;
		image.setDepth(depth);
	}
	
	public ImageLayer getLayer() {
		return image;
	}
	public void updateLocation() {
		image.setRotation(body.getAngle());
		image.setTranslation(body.getPosition().x, body.getPosition().y);
	}
	
}
