package com.megaman_oop.megaman.Sprites.Other;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Screens.PlayScreen;

public class FireBall extends Sprite {

  PlayScreen screen;
  World world;
  Array<TextureRegion> frames;
  Animation<TextureRegion> fireAnimation;
  float stateTime;
  boolean destroyed;
  boolean setToDestroy;
  boolean fireRight;
  boolean fireLeft;

  Body b2body;

  public FireBall(PlayScreen screen, float x, float y, boolean fireRight) {
    this.fireRight = fireRight;
    this.screen = screen;
    this.world = screen.getWorld();
    frames = new Array<TextureRegion>();
    for (int i = 1; i <= 3; i++) {
      if (i == 1)
        frames.add(
            new TextureRegion(
                screen.getAtlas().findRegion("megasprite_remake"), 10, 400, 140, 110));
      if (i == 2)
        frames.add(
            new TextureRegion(
                screen.getAtlas().findRegion("megasprite_remake"), 170, 400, 140, 110));
      if (i == 3)
        frames.add(
            new TextureRegion(
                screen.getAtlas().findRegion("megasprite_remake"), 350, 400, 140, 110));
    }
    fireAnimation = new Animation<TextureRegion>(0.4f, frames);
    setRegion(fireAnimation.getKeyFrame(0));
    setBounds(x, y, 25 / MegaMan.PPM, 25 / MegaMan.PPM);
    defineFireBall();
  }

  public void defineFireBall() {
    BodyDef bodyDef = new BodyDef();
    bodyDef.position.set(fireRight ? getX() + 12 / MegaMan.PPM : getX() - 12 / MegaMan.PPM, getY());
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    if (!world.isLocked()) b2body = world.createBody(bodyDef);

    FixtureDef fixtureDef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(3 / MegaMan.PPM);
    fixtureDef.filter.categoryBits = MegaMan.FIREBALL_BIT;
    fixtureDef.filter.maskBits =
        MegaMan.GROUND_BIT
            | MegaMan.COIN_BIT
            | MegaMan.BRICK_BIT
            | MegaMan.ENEMY_BIT
            | MegaMan.OBJECT_BIT;

    fixtureDef.shape = shape;
    fixtureDef.restitution = 1;
    fixtureDef.friction = 0;
    b2body.createFixture(fixtureDef).setUserData(this);
    b2body.setLinearVelocity(new Vector2(1, 0));
  }

  public void update(float dt) {
    stateTime += dt;
    setRegion(fireAnimation.getKeyFrame(stateTime, false));
    setPosition(b2body.getPosition().x, b2body.getPosition().y);
    if ((stateTime > 3 || setToDestroy) && !destroyed) {
      world.destroyBody(b2body);
      destroyed = true;
    }
    b2body.setLinearVelocity(b2body.getLinearVelocity().x, 1f);
    if ((fireRight && b2body.getLinearVelocity().x < 0)
        || (!fireRight && b2body.getLinearVelocity().x > 0)) setToDestroy();
  }

  public void setToDestroy() {
    setToDestroy = true;
  }

  public boolean isDestroyed() {
    return destroyed;
  }
}