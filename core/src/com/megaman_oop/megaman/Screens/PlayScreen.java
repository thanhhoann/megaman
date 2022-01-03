package com.megaman_oop.megaman.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Scenes.Hud;
import com.megaman_oop.megaman.Sprites.Enemies.Enemy;
import com.megaman_oop.megaman.Sprites.Enemies.FinalBoss;
import com.megaman_oop.megaman.Sprites.MainCharacter;
import com.megaman_oop.megaman.Tools.B2WorldCreator;
import com.megaman_oop.megaman.Tools.CameraStyles;
import com.megaman_oop.megaman.Tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen implements Screen {
  // Reference to our Game, used to set Screens
  private MegaMan game;
  private PlayScreen playScreen;
  private TextureAtlas atlas;

  //public static boolean alreadyDestroyed = false;

  // basic play-screen variables
  private OrthographicCamera gamecam;
  private Viewport gamePort;
  private Hud hud;

  // Tiled map variables
  private TmxMapLoader maploader;
  private TiledMap map;
  private OrthogonalTiledMapRenderer renderer;
  public int levelWidth;
  public int levelHeight;
  public int tileWidth;
  public int tileHeight;

  // Box2d variables
  private World world;
  private Box2DDebugRenderer b2dr;
  private B2WorldCreator creator;

  // sprites
  private MainCharacter player;
  private Texture health;

  // Music
  private Music music;



  public PlayScreen(MegaMan game) {
    atlas = new TextureAtlas("MEGAMAN_ENEMY.atlas");
    this.game = game;

    // create cam used to follow MEGAMAN through cam world
    gamecam = new OrthographicCamera();

    // create a FitViewport to maintain virtual aspect ratio despite screen size
    // current width is 4.0F, current height is 3.08F
    gamePort =
        new FitViewport(MegaMan.V_WIDTH / MegaMan.PPM, MegaMan.V_HEIGHT / MegaMan.PPM, gamecam);

    // create our game HUD for scores/timers/level info
    hud = new Hud( ((MegaMan) game).batch);

    // Load our map and setup our map renderer
    maploader = new TmxMapLoader();
    map = maploader.load("map.tmx");
    MapProperties props = map.getProperties();
    levelWidth = props.get("width", Integer.class);
    levelHeight = props.get("height", Integer.class);
    tileHeight = props.get("tileheight", Integer.class);
    tileWidth = props.get("tilewidth", Integer.class);

    renderer = new OrthogonalTiledMapRenderer(map, 1 / MegaMan.PPM);

    // initially set our game camera to be centered correctly at the start of map
    gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 1.5F, 0);

    // create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
    world = new World(new Vector2(0, -10), true);
    // allows for debug lines of our box2d world.

    b2dr = new Box2DDebugRenderer();

    creator = new B2WorldCreator(this);

    // create MEGAMAN in our game world
    player = new MainCharacter(this);

    world.setContactListener(new WorldContactListener());

    music = MegaMan.manager.get("audio/music/bgmusic.ogg", Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();

    health = new Texture("health.png");

  }


  public TextureAtlas getAtlas() {
    return atlas;
  }


  @Override
  public void show() {}

  public void handleInput(float dt) {
    if (player.currentState != MainCharacter.State.DEAD) {
      // JUMP
      if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) player.jump();
      // RUNNING RIGHT
      if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
        player.b2body.applyLinearImpulse(
            new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
      // RUNNING LEFT
      if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
        player.b2body.applyLinearImpulse(
            new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
      // SHOOTING
      if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) player.shoot();
      // SITTING
      if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) player.sit();
    }
  }

  public void update(float dt) {
    // handle user input first
    handleInput(dt);

    // takes 1 step in the physics simulation(60 times per second)
    world.step(1 / 60f, 6, 2);

    player.update(dt);

    for (Enemy enemy : creator.getEnemies()) {
      enemy.update(dt, player);
      if (enemy.getX() < player.getX() + 224 / MegaMan.PPM) {
        enemy.b2body.setActive(true);
      }
    }
    for(FinalBoss finalBoss: creator.getFinalBosses())
      if(finalBoss.getX() < player.getX() + 256/MegaMan.PPM){
        finalBoss.b2body.setActive(true);
        CameraStyles.averageBetweenTarget(gamecam, player, finalBoss);
      }

    hud.update(dt);
    
    CameraStyles.lerpToCharacter(gamecam, player);

    float startX = gamecam.viewportWidth / 2;
    float startY = gamecam.viewportHeight / 2;
    CameraStyles.boundary(
        gamecam,
        startX,
        startY,
        levelWidth * tileWidth - startX * 2,
        levelHeight * tileHeight - startY * 2);
    Vector2 focalPoint = new Vector2();
    focalPoint.x = (float)1873;
    focalPoint.y = (float)432.5;

    // tell our renderer to draw only what our camera can see in our game world.
    renderer.setView(gamecam);

  }

  @Override
  public void render(float delta) {
    // separate our update logic from render
    update(delta);

    // Clear the game screen with Black
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // render our game map
    renderer.render();

    // renderer our Box2DDebugLines

   // b2dr.render(world, gamecam.combined);



    ((MegaMan) game).batch.setProjectionMatrix(gamecam.combined);
    ((MegaMan) game).batch.begin();
    player.draw( ((MegaMan) game).batch);
    for (Enemy enemy : creator.getEnemies()) {
      enemy.draw( ((MegaMan) game).batch);
    }

    ((MegaMan) game).batch.end();

    // Set our batch to now draw what the Hud camera sees.
    ((MegaMan) game).batch.setProjectionMatrix(hud.stage.getCamera().combined);
    hud.stage.draw();

    //draw health bar
    ((MegaMan) game).batch.begin();
    if(player.getHealthBar()>6){
      ((MegaMan)game).batch.setColor(Color.GREEN);
    }
    else if(player.getHealthBar()>3)
      ((MegaMan)game).batch.setColor(Color.ORANGE);
    else 
      ((MegaMan)game).batch.setColor(Color.RED);
    ((MegaMan)game).batch.draw(health, 0, 0, MegaMan.V_WIDTH*player.getHealthBar()/10, 10);
    ((MegaMan)game).batch.setColor(Color.WHITE);
    ((MegaMan) game).batch.end();

    if (gameOver()) {
      music.stop();
      dispose();
      game.setScreen(new GameOverScreen(game));
    }
  }

  public boolean gameOver() {
    if ((player.currentState == MainCharacter.State.DEAD && player.getStateTimer() > 2) || player.getY()<0) {
      return true;
    }
      return false;
  }

  @Override
  public void resize(int width, int height) {
    // updated our game viewport
    gamePort.update(width, height);
  }

  public TiledMap getMap() {
    return map;
  }

  public World getWorld() {
    return world;
  }

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void hide() {}

  @Override
  public void dispose() {
    // dispose of all our opened resources
    map.dispose();
    renderer.dispose();
    world.dispose();
    b2dr.dispose();
    hud.dispose();
  }

  public Hud getHud() {
    return hud;
  }
}
