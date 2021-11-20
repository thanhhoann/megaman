package com.megaman_oop.megaman.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.megaman_oop.megaman.MegaMan;

public class HtmlLauncher extends GwtApplication {

  @Override
  public GwtApplicationConfiguration getConfig() {
    // Resizable application, uses available space in browser
    return new GwtApplicationConfiguration(true);
    // Fixed sike application:
    //     return new GwtApplicationConfiguration(1200, 624);
  }

  @Override
  public ApplicationListener createApplicationListener() {
    return new MegaMan();
  }
}
