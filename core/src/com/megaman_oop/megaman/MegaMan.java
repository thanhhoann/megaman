package com.megaman_oop.megaman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.megaman_oop.megaman.Screens.*;
import com.megaman_oop.megaman.Tools.GameCamera;

public class MegaMan extends Game {
  // Virtual Screen size and Box2D Scale(Pixels Per Meter)
  public static final int V_WIDTH = 400;
  public static final int V_HEIGHT = 208;
  public static final float PPM = 100;
  

  // Box2D Collision Bits
  public static final short NOTHING_BIT = 0;
  public static final short GROUND_BIT = 1;
  public static final short MEGAMAN_BIT = 2;
  public static final short ITEM_BIT = 4;
  public static final short OBJECT_BIT = 8;
  public static final short ENEMY_BIT = 16;
  public static final short ENEMY_HEAD_BIT = 32;
  public static final short BULLET_BIT = 64;
  public static final short FINAL_BOSS_BIT = 128;
  public static final short FIREBALL_BIT = 256;

  public SpriteBatch batch;

  public static AssetManager manager;
  public GameCamera cam;


  public MegaMan(PlayScreen playScreen) {}

  public MegaMan() {}

  @Override
  public void create() {
    batch = new SpriteBatch();
    manager = new AssetManager();
    cam = new GameCamera(V_WIDTH, V_HEIGHT);

    manager.load("audio/music/bgmusic.ogg",Music.class);
    manager.load("audio/sounds/stomp.wav", Sound.class);
    manager.load("audio/sounds/megamanhurt.wav", Sound.class);
    manager.finishLoading();

    setScreen(new MenuScreen(this));
  }


  @Override
  public void dispose() {
    super.dispose();
    manager.dispose();
    batch.dispose();
  }

  @Override
  public void render() {
    super.render();
  }
}
