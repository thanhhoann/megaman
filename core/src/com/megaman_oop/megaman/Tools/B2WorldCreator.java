package com.megaman_oop.megaman.Tools;


import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Screens.PlayScreen;
import com.megaman_oop.megaman.Sprites.Enemies.Enemy;
import com.megaman_oop.megaman.Sprites.Enemies.FinalBoss;
import com.megaman_oop.megaman.Sprites.Enemies.FlyBot;
import com.megaman_oop.megaman.Sprites.Enemies.SmallBot;
import com.megaman_oop.megaman.Sprites.Items.Heart;

public class B2WorldCreator {
  private Array<SmallBot> smallBots;
  private Array<FlyBot> flyBots;
  private Array<FinalBoss> finalBosses;

  public B2WorldCreator(PlayScreen screen) {
    World world = screen.getWorld();
    TiledMap map = screen.getMap();
    // create body and fixture variables
    BodyDef bodyDef = new BodyDef();
    PolygonShape shape = new PolygonShape();
    FixtureDef fixtureDef = new FixtureDef();
    Body body;

    // create ground bodies/fixtures
    for (RectangleMapObject object :
        map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = object.getRectangle();

      bodyDef.type = BodyDef.BodyType.StaticBody;
      bodyDef.position.set(
          (rect.getX() + rect.getWidth() / 2) / MegaMan.PPM,
          (rect.getY() + rect.getHeight() / 2) / MegaMan.PPM);

      body = world.createBody(bodyDef);

      shape.setAsBox(rect.getWidth() / 2 / MegaMan.PPM, rect.getHeight() / 2 / MegaMan.PPM);
      fixtureDef.shape = shape;
      body.createFixture(fixtureDef);
    }

    // create pipe bodies/fixtures
    for (RectangleMapObject object :
        map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = object.getRectangle();

      bodyDef.type = BodyDef.BodyType.StaticBody;
      bodyDef.position.set(
          (rect.getX() + rect.getWidth() / 2) / MegaMan.PPM,
          (rect.getY() + rect.getHeight() / 2) / MegaMan.PPM);

      body = world.createBody(bodyDef);

      shape.setAsBox(rect.getWidth() / 2 / MegaMan.PPM, rect.getHeight() / 2 / MegaMan.PPM);
      fixtureDef.shape = shape;
      fixtureDef.filter.categoryBits = MegaMan.OBJECT_BIT;
      body.createFixture(fixtureDef);
    }
    // create all smallBots
    smallBots = new Array<SmallBot>();
    for (RectangleMapObject object :
            map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = object.getRectangle();
      smallBots.add(new SmallBot(screen, rect.getX() / MegaMan.PPM, rect.getY() / MegaMan.PPM,
              new Heart(screen, rect.getX() / MegaMan.PPM , rect.getY()  / MegaMan.PPM)));
    }
    //create FlyBot
    flyBots = new Array<FlyBot>();
    for (RectangleMapObject object :
            map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = object.getRectangle();
            flyBots.add(new FlyBot(screen, rect.getX() / MegaMan.PPM, rect.getY() / MegaMan.PPM,null));
    }
    finalBosses = new Array<FinalBoss>();
    for (RectangleMapObject object :
            map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = object.getRectangle();
      finalBosses.add(new FinalBoss(screen,rect.getX()/MegaMan.PPM,rect.getY()/MegaMan.PPM, null));
    }
  }

  public Array<SmallBot> getSmallBots() {
    return smallBots;
  }

  public Array<FlyBot> getFlyBots() {
    return flyBots;
  }

  public Array<FinalBoss> getFinalBosses() {
    return finalBosses;
  }

  public Array<Enemy> getEnemies() {
    Array<Enemy> enemies = new Array<Enemy>();
    enemies.addAll(smallBots);
    enemies.addAll(flyBots);
    enemies.addAll(finalBosses);
    return enemies;
  }
}
