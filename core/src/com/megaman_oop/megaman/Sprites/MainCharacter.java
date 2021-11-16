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
    GROWING,
    DEAD
  }

  public State currentState;
  public State previousState;

  public World world;
  public Body b2body;

  private TextureRegion marioStand;
  private TextureRegion marioJump;
  private TextureRegion marioDead;
  private TextureRegion bigMarioStand;
  private TextureRegion bigMarioJump;

  private Animation<TextureRegion> marioRun;
  private Animation<TextureRegion> bigMarioRun;
  private Animation<TextureRegion> growMario;

  private float stateTimer;

  private boolean runningRight;
  private boolean marioIsBig;
  private boolean runGrowAnimation;
  private boolean timeToDefineBigMario;
  private boolean timeToRedefineMario;
  private boolean marioIsDead;

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

    // get run animation frames and add them to marioRun Animation
    for (int i = 1; i < 4; i++)
      frames.add(
          new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
    marioRun = new Animation<TextureRegion>(0.1f, frames);

    frames.clear();

    for (int i = 1; i < 4; i++)
      frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
    bigMarioRun = new Animation<TextureRegion>(0.1f, frames);

    frames.clear();

    // get set animation frames from growing mario
    frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
    frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
    frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
    frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
    growMario = new Animation<TextureRegion>(0.2f, frames);

    // get jump animation frames and add them to marioJump Animation
    marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
    bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);

    // create texture region for mario standing
    marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
    bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);

    // create dead mario texture region
    marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

    // define mario in Box2d
    defineMario();

    // set initial values for marios location, width and height. And initial frame as marioStand.
    setBounds(0, 0, 16 / MegaMan.PPM, 16 / MegaMan.PPM);
    setRegion(marioStand);

    fireballs = new Array<FireBall>();
  }

  public void update(float dt) {

    // time is up : too late mario dies T_T
    // the !isDead() method is used to prevent multiple invocation
    // of "die music" and jumping
    // there is probably better ways to do that, but it works for now.
    if (screen.getHud().isTimeUp() && !isDead()) {
      die();
    }

    // update our sprite to correspond with the position of our Box2D body
    if (marioIsBig)
      setPosition(
          b2body.getPosition().x - getWidth() / 2,
          b2body.getPosition().y - getHeight() / 2 - 6 / MegaMan.PPM);
    else
      setPosition(
          b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    // update sprite with the correct frame depending on marios current action
    setRegion(getFrame(dt));
    if (timeToDefineBigMario) defineBigMario();
    if (timeToRedefineMario) redefineMario();

    for (FireBall ball : fireballs) {
      ball.update(dt);
      if (ball.isDestroyed()) fireballs.removeValue(ball, true);
    }
  }

  public TextureRegion getFrame(float dt) {
    // get marios current state. ie. jumping, running, standing...
    currentState = getState();

    TextureRegion region;

    // depending on the state, get corresponding animation keyFrame.
    switch (currentState) {
      case DEAD:
        region = marioDead;
        break;
      case GROWING:
        region = growMario.getKeyFrame(stateTimer);
        if (growMario.isAnimationFinished(stateTimer)) {
          runGrowAnimation = false;
        }
        break;
      case JUMPING:
        region = marioIsBig ? bigMarioJump : marioJump;
        break;
      case RUNNING:
        region =
            (marioIsBig
                ? bigMarioRun.getKeyFrame(stateTimer, true)
                : marioRun.getKeyFrame(stateTimer, true));

        break;
      case FALLING:
      case STANDING:
      default:
        region = marioIsBig ? bigMarioStand : marioStand;
        break;
    }

    // if mario is running left and the texture isn't facing left... flip it.
    if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
      region.flip(true, false);
      runningRight = false;
    }

    // if mario is running right and the texture isn't facing right... flip it.
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
    // if mario is going positive in Y-Axis he is jumping... or if he just jumped and is falling
    // remain in jump state
    if (marioIsDead) return State.DEAD;
    else if (runGrowAnimation) return State.GROWING;
    else if ((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING)
        || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
      return State.JUMPING;
    // if negative in Y-Axis mario is falling
    else if (b2body.getLinearVelocity().y < 0) return State.FALLING;
    // if mario is positive or negative in the X axis he is running
    else if (b2body.getLinearVelocity().x != 0) return State.RUNNING;
    // if none of these return then he must be standing
    else return State.STANDING;
  }

  public void grow() {
    if (!isBig()) {
      runGrowAnimation = true;
      marioIsBig = true;
      timeToDefineBigMario = true;
      setBounds(getX(), getY(), getWidth(), getHeight() * 2);
      MegaMan.manager.get("audio/sounds/power.wav", Sound.class).play();
    }
  }

  public void die() {

    if (!isDead()) {

      MegaMan.manager.get("audio/music/mario_music.ogg", Music.class).stop();
      MegaMan.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
      marioIsDead = true;
      Filter filter = new Filter();
      filter.maskBits = MegaMan.NOTHING_BIT;

      for (Fixture fixture : b2body.getFixtureList()) {
        fixture.setFilterData(filter);
      }

      b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
    }
  }

  public boolean isDead() {
    return marioIsDead;
  }

  public float getStateTimer() {
    return stateTimer;
  }

  public boolean isBig() {
    return marioIsBig;
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
      if (marioIsBig) {
        marioIsBig = false;
        timeToRedefineMario = true;
        setBounds(getX(), getY(), getWidth(), getHeight() / 2);
        MegaMan.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
      } else {
        die();
      }
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
    shape.setRadius(6 / MegaMan.PPM);
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

    EdgeShape head = new EdgeShape();
    head.set(
        new Vector2(-2 / MegaMan.PPM, 6 / MegaMan.PPM),
        new Vector2(2 / MegaMan.PPM, 6 / MegaMan.PPM));
    fixtureDef.filter.categoryBits = MegaMan.MARIO_HEAD_BIT;
    fixtureDef.shape = head;
    fixtureDef.isSensor = true;

    b2body.createFixture(fixtureDef).setUserData(this);

    timeToRedefineMario = false;
  }

  public void defineBigMario() {
    Vector2 currentPosition = b2body.getPosition();
    world.destroyBody(b2body);

    BodyDef bodyDef = new BodyDef();
    bodyDef.position.set(currentPosition.add(0, 10 / MegaMan.PPM));
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    b2body = world.createBody(bodyDef);

    FixtureDef fixtureDef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(6 / MegaMan.PPM);
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
    shape.setPosition(new Vector2(0, -14 / MegaMan.PPM));
    b2body.createFixture(fixtureDef).setUserData(this);

    EdgeShape head = new EdgeShape();
    head.set(
        new Vector2(-2 / MegaMan.PPM, 6 / MegaMan.PPM),
        new Vector2(2 / MegaMan.PPM, 6 / MegaMan.PPM));
    fixtureDef.filter.categoryBits = MegaMan.MARIO_HEAD_BIT;
    fixtureDef.shape = head;
    fixtureDef.isSensor = true;

    b2body.createFixture(fixtureDef).setUserData(this);
    timeToDefineBigMario = false;
  }

  public void defineMario() {
    BodyDef bodyDef = new BodyDef();
    bodyDef.position.set(32 / MegaMan.PPM, 32 / MegaMan.PPM);
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    b2body = world.createBody(bodyDef);

    FixtureDef fixtureDef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(6 / MegaMan.PPM);
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

    EdgeShape head = new EdgeShape();
    head.set(
        new Vector2(-2 / MegaMan.PPM, 6 / MegaMan.PPM),
        new Vector2(2 / MegaMan.PPM, 6 / MegaMan.PPM));
    fixtureDef.filter.categoryBits = MegaMan.MARIO_HEAD_BIT;
    fixtureDef.shape = head;
    fixtureDef.isSensor = true;

    b2body.createFixture(fixtureDef).setUserData(this);
  }

  public void fire() {
    fireballs.add(
        new FireBall(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight));
  }

  public void draw(Batch batch) {
    super.draw(batch);
    for (FireBall ball : fireballs) ball.draw(batch);
  }
}
