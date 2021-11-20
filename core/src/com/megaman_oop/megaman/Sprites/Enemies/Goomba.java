package com.megaman_oop.megaman.Sprites.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Screens.PlayScreen;
import com.megaman_oop.megaman.Sprites.MainCharacter;


public class Goomba extends com.megaman_oop.megaman.Sprites.Enemies.Enemy {
  private float stateTime;
  private Animation walkAnimation;
  private Array<TextureRegion> frames;
  private boolean setToDestroy;
  private boolean destroyed;
  float angle;

  public Goomba(PlayScreen screen, float x, float y) {
    super(screen, x, y);
    frames = new Array<TextureRegion>();
    //    for (int i = 0; i < 2; i++)
    //      frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16,
    // 16));
    walkAnimation = new Animation(0.4f, frames);
    stateTime = 0;
    setBounds(getX(), getY(), 16 / MegaMan.PPM, 16 / MegaMan.PPM);
    setToDestroy = false;
    destroyed = false;
    angle = 0;
  }

  public void update(float dt) {
    stateTime += dt;
    if (setToDestroy && !destroyed) {
      world.destroyBody(b2body);
      destroyed = true;
      setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
      stateTime = 0;
    } else if (!destroyed) {
      b2body.setLinearVelocity(velocity);
      setPosition(
          b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
      setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
    }
  }

  @Override
  protected void defineEnemy() {
    BodyDef bdef = new BodyDef();
    bdef.position.set(getX(), getY());
    bdef.type = BodyDef.BodyType.DynamicBody;
    b2body = world.createBody(bdef);

    FixtureDef fdef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(6 / MegaMan.PPM);
    fdef.filter.categoryBits = MegaMan.ENEMY_BIT;
    fdef.filter.maskBits =
        MegaMan.GROUND_BIT
            | MegaMan.COIN_BIT
            | MegaMan.BRICK_BIT
            | MegaMan.ENEMY_BIT
            | MegaMan.OBJECT_BIT
            | MegaMan.MARIO_BIT;

    fdef.shape = shape;
    b2body.createFixture(fdef).setUserData(this);

    // Create the Head here:
    PolygonShape head = new PolygonShape();
    Vector2[] vertice = new Vector2[4];
    vertice[0] = new Vector2(-5, 8).scl(1 / MegaMan.PPM);
    vertice[1] = new Vector2(5, 8).scl(1 / MegaMan.PPM);
    vertice[2] = new Vector2(-3, 3).scl(1 / MegaMan.PPM);
    vertice[3] = new Vector2(3, 3).scl(1 / MegaMan.PPM);
    head.set(vertice);

    fdef.shape = head;
    fdef.restitution = 0.5f;
    fdef.filter.categoryBits = MegaMan.ENEMY_HEAD_BIT;
    b2body.createFixture(fdef).setUserData(this);
  }

  public void draw(Batch batch) {
    if (!destroyed || stateTime < 1) super.draw(batch);
  }

  @Override
  public void hitOnHead(MainCharacter mainCharacter) {
    setToDestroy = true;
    MegaMan.manager.get("audio/sounds/stomp.wav", Sound.class).play();
  }

  @Override
  public void hitByEnemy(Enemy enemy) {
    if (enemy instanceof Turtle && ((Turtle) enemy).currentState == Turtle.State.MOVING_SHELL)
      setToDestroy = true;
    else reverseVelocity(true, false);
  }
}
