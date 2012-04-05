package net.japura.monofuel.testgame.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import net.japura.monofuel.testgame.core.TestGame;

public class TestGameActivity extends GameActivity {

  @Override
  public void main(){
    platform().assets().setPathPrefix("net/japura/monofuel/testgame/resources");
    PlayN.run(new TestGame());
  }
}
