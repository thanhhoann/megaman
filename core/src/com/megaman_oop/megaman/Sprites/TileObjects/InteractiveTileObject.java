package com.megaman_oop.megaman.Sprites.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Screens.PlayScreen;


public abstract class InteractiveTileObject {
  protected World world;
  protected TiledMap map;
  protected Rectangle bounds;
  protected Body body;
  protected PlayScreen screen;
  protected MapObject object;

  protected Fixture fixture;

  public InteractiveTileObject(PlayScreen screen, MapObject object) {
    this.object = object;
    this.screen = screen;
    this.world = screen.getWorld();
    this.map = screen.getMap();
    this.bounds = ((RectangleMapObject) object).getRectangle();

    BodyDef bodyDef = new BodyDef();
    FixtureDef fixtureDef = new FixtureDef();
    PolygonShape shape = new PolygonShape();

    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(
        (bounds.getX() + bounds.getWidth() / 2) / MegaMan.PPM,
        (bounds.getY() + bounds.getHeight() / 2) / MegaMan.PPM);

    body = world.createBody(bodyDef);

    shape.setAsBox(bounds.getWidth() / 2 / MegaMan.PPM, bounds.getHeight() / 2 / MegaMan.PPM);
    fixtureDef.shape = shape;
    fixture = body.createFixture(fixtureDef);
  }

  public TiledMapTileLayer.Cell getCell() {
    TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
    return layer.getCell(
        (int) (body.getPosition().x * MegaMan.PPM / 16),
        (int) (body.getPosition().y * MegaMan.PPM / 16));
  }
}
