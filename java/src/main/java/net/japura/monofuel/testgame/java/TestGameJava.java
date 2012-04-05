package net.japura.monofuel.testgame.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import net.japura.monofuel.testgame.core.TestGame;

public class TestGameJava {

  public static void main(String[] args) {
    JavaPlatform platform = JavaPlatform.register();
    platform.assets().setPathPrefix("net/japura/monofuel/testgame/resources");
    PlayN.run(new TestGame());
  }
}
