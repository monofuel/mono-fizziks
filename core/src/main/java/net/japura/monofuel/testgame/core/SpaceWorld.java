package net.japura.monofuel.testgame.core;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

/**
 * 
 * @author monofuel
 *
 * handles the game world
 */
public class SpaceWorld {
    
    Vec2 gravity = new Vec2( 0.0f, 0.0f);
    
    boolean doSleep = true;
    
    World world = new World(gravity, doSleep);
    
	public World getWorld() {
		return world;
	}
}
