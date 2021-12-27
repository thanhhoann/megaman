package com.megaman_oop.megaman.Sprites.Enemies;

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
import com.megaman_oop.megaman.Sprites.Other.FireBall;

public class SmallBot extends Enemy {

  public enum State {
    FORWARD,
    BACKWARD
  }
  public State currentState;
  public State previousState;
  private float stateTime;
  private Animation forwardAnimation;
  private Animation backwardAnimation;
  private Array<TextureRegion> frames;

  private boolean setToDestroy;
  private boolean destroyed;



  public SmallBot(PlayScreen screen, float x, float y) {
    super(screen, x, y);
    frames = new Array<TextureRegion>();
    frames.add(new TextureRegion(screen.getAtlas().findRegion("enemysprite1"),  484, 430,110,120));
    frames.add(new TextureRegion(screen.getAtlas().findRegion("enemysprite1"),  594, 430,110,120));
    forwardAnimation = new Animation(0.2f, frames);
    backwardAnimation = new Animation(0.2f,frames);
    currentState = previousState = State.FORWARD;
    setToDestroy = false;
    destroyed= false;
    setBounds(getX(), getY(), 32 / MegaMan.PPM, 32 / MegaMan.PPM);
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
            | MegaMan.MEGAMAN_BIT;

    fdef.shape = shape;
    b2body.createFixture(fdef).setUserData(this);

    // Create the hand here:
    PolygonShape leftHand = new PolygonShape();
    Vector2[] vertice = new Vector2[4];
    vertice[0] = new Vector2(5, -8).scl(1 / MegaMan.PPM);
    vertice[1] = new Vector2(5, 8).scl(1 / MegaMan.PPM);
    vertice[2] = new Vector2(3, -8).scl(1 / MegaMan.PPM);
    vertice[3] = new Vector2(3, 8).scl(1 / MegaMan.PPM);
    leftHand.set(vertice);

    fdef.shape = leftHand;
    fdef.restitution = 0.05f;
    fdef.filter.categoryBits = MegaMan.ENEMY_HEAD_BIT;
    b2body.createFixture(fdef).setUserData(this);

    PolygonShape rightHand = new PolygonShape();
    Vector2[] vertice1 = new Vector2[4];
    vertice1[0] = new Vector2(-5, -8).scl(1 / MegaMan.PPM);
    vertice1[1] = new Vector2(-5, 8).scl(1 / MegaMan.PPM);
    vertice1[2] = new Vector2(-3, -8).scl(1 / MegaMan.PPM);
    vertice1[3] = new Vector2(-3, 8).scl(1 / MegaMan.PPM);
    rightHand.set(vertice1);

    fdef.shape = rightHand;
    fdef.restitution = 0.05f;
    fdef.filter.categoryBits = MegaMan.ENEMY_HEAD_BIT;
    b2body.createFixture(fdef).setUserData(this);
  }

  public TextureRegion getFrame(float dt) {
    TextureRegion region;
    switch (currentState) {
      case FORWARD:
      case BACKWARD:
        region = (TextureRegion) backwardAnimation.getKeyFrame(stateTime,true);
        break;
      default:
        region = (TextureRegion) forwardAnimation.getKeyFrame(stateTime,true);
        break;
    }
    if (velocity.x < 0 && region.isFlipX() == false) {
      region.flip(true, false);
    }
    if (velocity.x > 0 && region.isFlipX() == true) {
      region.flip(true, false);
    }
    stateTime = currentState == previousState ? stateTime + dt : 0;
    // update previous state
    previousState = currentState;
    // return our final adjusted frame
    return region;
  }

  public void update(float dt, MainCharacter mainCharacter) {
    if(currentState == State.FORWARD && mainCharacter.b2body.getPosition().x > b2body.getPosition().x){
      currentState = previousState = State.BACKWARD;
      velocity.x = -velocity.x;
      velocity.y = 0;
    }
    if(currentState == State.BACKWARD && mainCharacter.b2body.getPosition().x < b2body.getPosition().x){
      currentState = previousState = State.FORWARD;
      velocity.x = -velocity.x;
      velocity.y = 0;
    }

    setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 /MegaMan.PPM);
    b2body.setLinearVelocity(velocity);
  }


  @Override
  public void hitByMegaman(FireBall fireBall) {
    setToDestroy = true;
    fireBall.setToDestroy();
  }


  @Override
  public void hitByEnemy(Enemy enemy) {

  }
}
