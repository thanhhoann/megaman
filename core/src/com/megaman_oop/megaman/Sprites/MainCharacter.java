package com.megaman_oop.megaman.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Screens.PlayScreen;
import com.megaman_oop.megaman.Sprites.Enemies.Enemy;
import com.megaman_oop.megaman.Sprites.Enemies.Turtle;
import com.megaman_oop.megaman.Sprites.Other.FireBall;

public class MainCharacter extends Sprite {
  public enum State {
    FALLING,
    JUMPING,
    STANDING,
    RUNNING,
    SHOOTING,
    SITTING,
    DEAD;
  }

  public State currentState;
  public State previousState;

  public World world;
  public Body b2body;

  private TextureRegion megamanStand;
  private TextureRegion megamanSit;
  private TextureRegion megamanDead;

  private Animation<TextureRegion> megamanRun;
  private Animation<TextureRegion> megamanJump;
  private Animation<TextureRegion> megamanShoot;

  private float stateTimer;

  private boolean runningRight;
  private boolean timeToRedefineMario;
  private boolean megamanIsSitting;
  private boolean megamanIsDead;

  private PlayScreen screen;

  private Array<FireBall> fireballs;

  public MainCharacter(PlayScreen screen) {
    // initialize default values
    this.screen = screen;
    this.world = screen.getWorld();
    currentState = State.STANDING;
    previousState = State.STANDING;
    stateTimer = 0;
    runningRight = true;

    Array<TextureRegion> frames = new Array<TextureRegion>();

    // get run animation frames and add them to megamanRun Animation
    for (int i = 1; i < 4; i++) {
      frames.add(
          new TextureRegion(
              screen.getAtlas().findRegion("megasprite_remake"), i * 105, 0, 90, 110));
    }
    megamanRun = new Animation<TextureRegion>(0.1f, frames);
    frames.clear();

    // get jump animation frames and add them to megamanJump Animation
    for (int i = 1; i < 4; i++)
      frames.add(
          new TextureRegion(
              screen.getAtlas().findRegion("megasprite_remake"), i * 110, 120, 90, 110));
    megamanJump = new Animation<TextureRegion>(0.2f, frames);
    frames.clear();

    // get jump animation frames and add them to megamanJump Animation
    for (int i = 1; i < 4; i++)
      frames.add(
          new TextureRegion(
              screen.getAtlas().findRegion("megasprite_remake"), i * 110, 120, 90, 110));
    megamanShoot = new Animation<TextureRegion>(0.2f, frames);
    frames.clear();

    // create texture region for megaman standing
    megamanStand =
        new TextureRegion(screen.getAtlas().findRegion("megasprite_remake"), 0, 0, 90, 110);

    // define megaman in Box2d
    defineMario();

    // set initial values for megamans location, width and height.
    setBounds(0, 0, 30 / MegaMan.PPM, 30 / MegaMan.PPM);
    setRegion(megamanStand);

    fireballs = new Array<FireBall>();
  }

  public void update(float dt) {
    if (screen.getHud().isTimeUp() && !isDead()) die();

    // update our sprite to correspond with the position of our Box2D body
    setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    // update sprite with the correct frame depending on megamans current action
    setRegion(getFrame(dt));

    //    if (timeToRedefineMario) redefineMario();

    for (FireBall ball : fireballs) {
      ball.update(dt);
      if (ball.isDestroyed()) fireballs.removeValue(ball, true);
    }
  }

  public TextureRegion getFrame(float dt) {
    // get megaman current state. ie. jumping, running, standing...
    currentState = getState();

    TextureRegion region;

    if (currentState == State.SITTING) System.out.println("SITTING");

    // depending on the state, get corresponding animation keyFrame.
    switch (currentState) {
      case DEAD:
        region = megamanDead;
        break;
      case RUNNING:
        region = megamanRun.getKeyFrame(stateTimer, true);
        break;
      case JUMPING:
        region = megamanJump.getKeyFrame(stateTimer, true);
        break;
      case SITTING:
        region = megamanSit;
      case SHOOTING:
        region = megamanJump.getKeyFrame(stateTimer, true);
      case STANDING:
      default:
        region = megamanStand;
        break;
    }

    // if megaman is running left and the texture isn't facing left... flip it.
    if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
      region.flip(true, false);
      runningRight = false;
    }

    // if megaman is running right and the texture isn't facing right... flip it.
    else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
      region.flip(true, false);
      runningRight = true;
    }

    // if the current state is the same as the previous state increase the state timer.
    // otherwise, the state has changed, and we need to reset timer.
    stateTimer = currentState == previousState ? stateTimer + dt : 0;
    // update previous state
    previousState = currentState;
    // return our final adjusted frame
    return region;
  }

  public State getState() {
    // Test to Box2D for velocity on the X and Y-Axis
    // if megaman is going positive in Y-Axis he is jumping... or if he just jumped and is falling
    // remain in jump state

    if (megamanIsDead) return State.DEAD;
    else if ((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING)
        || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
      return State.JUMPING;
    // if negative in Y-Axis megaman is falling
    else if (b2body.getLinearVelocity().y < 0) return State.FALLING;
    // if megaman is positive or negative in the X axis he is running
    else if (b2body.getLinearVelocity().x != 0) return State.RUNNING;
    // if none of these return then he must be standing
    else return State.STANDING;
  }

  public void die() {

    if (!isDead()) {
      // TODO: change to appropriate music
      MegaMan.manager.get("audio/music/megaman_music.ogg", Music.class).stop();
      MegaMan.manager.get("audio/sounds/megamandie.wav", Sound.class).play();
      megamanIsDead = true;
      Filter filter = new Filter();
      filter.maskBits = MegaMan.NOTHING_BIT;

      for (Fixture fixture : b2body.getFixtureList()) {
        fixture.setFilterData(filter);
      }

      b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
    }
  }

  public boolean isDead() {
    return megamanIsDead;
  }

  public float getStateTimer() {
    return stateTimer;
  }

  public void jump() {
    if (currentState != State.JUMPING) {
      b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
      currentState = State.JUMPING;
    }
  }

  public void hit(Enemy enemy) {
    if (enemy instanceof Turtle && ((Turtle) enemy).currentState == Turtle.State.STANDING_SHELL)
      ((Turtle) enemy)
          .kick(
              enemy.b2body.getPosition().x > b2body.getPosition().x
                  ? Turtle.KICK_RIGHT
                  : Turtle.KICK_LEFT);
    else {
      die();
    }
  }

  public void redefineMario() {
    Vector2 position = b2body.getPosition();
    world.destroyBody(b2body);

    BodyDef bodyDef = new BodyDef();
    bodyDef.position.set(position);
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    b2body = world.createBody(bodyDef);

    FixtureDef fixtureDef = new FixtureDef();
    CircleShape shape = new CircleShape();
    //    shape.setRadius(10 / MegaMan.PPM);
    //    fixtureDef.filter.categoryBits = MegaMan.MARIO_BIT;
    //    fixtureDef.filter.maskBits =
    //        MegaMan.GROUND_BIT
    //            | MegaMan.COIN_BIT
    //            | MegaMan.BRICK_BIT
    //            | MegaMan.ENEMY_BIT
    //            | MegaMan.OBJECT_BIT
    //            | MegaMan.ENEMY_HEAD_BIT
    //            | MegaMan.ITEM_BIT;

    //    fixtureDef.shape = shape;
    //    b2body.createFixture(fixtureDef).setUserData(this);

    //    EdgeShape head = new EdgeShape();
    //    head.set(
    //        new Vector2(-2 / MegaMan.PPM, 6 / MegaMan.PPM),
    //        new Vector2(2 / MegaMan.PPM, 6 / MegaMan.PPM));
    //    fixtureDef.filter.categoryBits = MegaMan.MARIO_HEAD_BIT;
    //    fixtureDef.shape = head;
    //    fixtureDef.isSensor = true;

    b2body.createFixture(fixtureDef).setUserData(this);

    timeToRedefineMario = false;
  }

  public void defineMario() {
    BodyDef bodyDef = new BodyDef();
    bodyDef.position.set(32 / MegaMan.PPM, 32 / MegaMan.PPM);
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    b2body = world.createBody(bodyDef);

    FixtureDef fixtureDef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(10 / MegaMan.PPM);
    fixtureDef.filter.categoryBits = MegaMan.MARIO_BIT;
    fixtureDef.filter.maskBits =
        MegaMan.GROUND_BIT
            | MegaMan.COIN_BIT
            | MegaMan.BRICK_BIT
            | MegaMan.ENEMY_BIT
            | MegaMan.OBJECT_BIT
            | MegaMan.ENEMY_HEAD_BIT
            | MegaMan.ITEM_BIT;

    fixtureDef.shape = shape;
    b2body.createFixture(fixtureDef).setUserData(this);

    //    EdgeShape head = new EdgeShape();
    //    head.set(
    //        new Vector2(-2 / MegaMan.PPM, 6 / MegaMan.PPM),
    //        new Vector2(2 / MegaMan.PPM, 6 / MegaMan.PPM));
    //    fixtureDef.filter.categoryBits = MegaMan.MARIO_HEAD_BIT;
    //    fixtureDef.shape = head;
    //    fixtureDef.isSensor = true;

    b2body.createFixture(fixtureDef).setUserData(this);
  }

  public void setCurrentState(State currentState) {
    this.currentState = currentState;
  }

  public void fire() {
    if (currentState != State.SHOOTING) {
      b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
      currentState = State.SHOOTING;
    }
    fireballs.add(
        new FireBall(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight));
  }

  public void draw(Batch batch) {
    super.draw(batch);
    for (FireBall ball : fireballs) ball.draw(batch);
  }
}
