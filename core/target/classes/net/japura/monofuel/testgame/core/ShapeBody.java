package net.japura.monofuel.testgame.core;

import java.util.ArrayList;

import org.jbox2d.dynamics.joints.*;

public class ShapeBody {
	
	static int joints = 0;
	int thisJoint;
	
	static ArrayList<Joint> jointList = new ArrayList<Joint>();
	
	
	public ShapeBody(Shape item, Shape item2) {
		WeldJointDef weldJoint = new WeldJointDef();
		
		//weldJoint.bodyA = item.getBody();
		//weldJoint.bodyB = item2.getBody();
		weldJoint.initialize(item.getBody(), item2.getBody(), item.getCenter().add(item2.getCenter()).mul(0.5f) );
			
		jointList.add(WeldJoint.create(TestGame.getWorld(),weldJoint));
		thisJoint = joints++;
		System.out.println("new Joint: " + thisJoint);
		
	}
	
	public int getNumber() {
		return thisJoint;
	}
	
	public static Joint getJoint(int index) {
		return jointList.get(index);
	}
	
	public static int getSize() {
		return jointList.size();
	}
	
	public static void updateShapes(float step) {
		for (Joint item : jointList) {
			item.solvePositionConstraints(step);
		}
	}
	
}
