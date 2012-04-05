package net.japura.monofuel.testgame.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import net.japura.monofuel.testgame.core.TestGame;

public class TestGameHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("testgame/");
    PlayN.run(new TestGame());
  }
}
