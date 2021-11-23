package com.megaman_oop.megaman.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Screens.PlayScreen;
import com.megaman_oop.megaman.Sprites.Enemies.Enemy;
import com.megaman_oop.megaman.Sprites.Enemies.SmallBot;
import com.megaman_oop.megaman.Sprites.Enemies.Turtle;
import com.megaman_oop.megaman.Sprites.TileObjects.Brick;
import com.megaman_oop.megaman.Sprites.TileObjects.Coin;

public class B2WorldCreator {
  private Array<SmallBot> smallBots;
  private Array<Turtle> turtles;

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

    // create brick bodies/fixtures
    for (MapObject object :
        map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
      new Brick(screen, object);
    }

    // create coin bodies/fixtures
    for (MapObject object :
        map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {

      new Coin(screen, object);
    }

    // create all smallBots
    smallBots = new Array<SmallBot>();
    for (RectangleMapObject object :
        map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = object.getRectangle();
            smallBots.add(new SmallBot(screen, rect.getX() / MegaMan.PPM, rect.getY() / MegaMan.PPM));
    }
    turtles = new Array<Turtle>();
    for (RectangleMapObject object :
        map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = object.getRectangle();
      //      turtles.add(new Turtle(screen, rect.getX() / MegaMan.PPM, rect.getY() / MegaMan.PPM));
    }
  }

  public Array<SmallBot> getSmallBots() {
    return smallBots;
  }

  public Array<Enemy> getEnemies() {
    Array<Enemy> enemies = new Array<Enemy>();
    enemies.addAll(smallBots);
    enemies.addAll(turtles);
    return enemies;
  }
}
