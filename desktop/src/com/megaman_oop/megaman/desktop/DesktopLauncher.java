package com.megaman_oop.megaman.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.megaman_oop.megaman.MegaMan;

public class DesktopLauncher {
  public static void main(String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.title = "Mega Man";
    config.width = 1200;
    config.height = 624;
    new LwjglApplication(new MegaMan(), config);
  }
}
