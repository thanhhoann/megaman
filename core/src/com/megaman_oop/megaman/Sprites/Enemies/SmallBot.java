package com.megaman_oop.megaman.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Screens.PlayScreen;
import com.megaman_oop.megaman.Sprites.Interface.ItemBehaviour;
import com.megaman_oop.megaman.Sprites.Items.Heart;
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

  private static int healthBar = 3;
  private boolean setToDestroy;
  private boolean destroyed;


  public SmallBot(PlayScreen screen, float x, float y, ItemBehaviour itemBehaviour) {
    super(screen, x, y, itemBehaviour);
    frames = new Array<TextureRegion>();
    frames.add(new TextureRegion(screen.getAtlas().findRegion("enemysprite1"),  484, 510,110,120));
    frames.add(new TextureRegion(screen.getAtlas().findRegion("enemysprite1"),  594, 510,110,120));
    forwardAnimation = new Animation(0.3f, frames);
    backwardAnimation = new Animation(0.3f,frames);
    currentState = previousState = State.FORWARD;
    setBounds(getX(), getY() , 30 / MegaMan.PPM, 30 / MegaMan.PPM);
    setToDestroy = false;
    destroyed= false;
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
                    | MegaMan.OBJECT_BIT
                    | MegaMan.FIREBALL_BIT
                    | MegaMan.MEGAMAN_BIT;

    fdef.shape = shape;
    b2body.createFixture(fdef).setUserData(this);

    // Create the hand here:
    PolygonShape leftHand = new PolygonShape();
    Vector2[] vertice = new Vector2[4];
    vertice[0] = new Vector2(6, -8).scl(1 / MegaMan.PPM);
    vertice[1] = new Vector2(6, 8).scl(1 / MegaMan.PPM);
    vertice[2] = new Vector2(4, -6).scl(1 / MegaMan.PPM);
    vertice[3] = new Vector2(4, 6).scl(1 / MegaMan.PPM);
    leftHand.set(vertice);

    fdef.shape = leftHand;
    fdef.restitution = 0.5f;
    fdef.filter.categoryBits = MegaMan.ENEMY_HEAD_BIT;
    b2body.createFixture(fdef).setUserData(this);

    PolygonShape rightHand = new PolygonShape();
    Vector2[] vertice1 = new Vector2[4];
    vertice1[0] = new Vector2(-6, -8).scl(1 / MegaMan.PPM);
    vertice1[1] = new Vector2(-6, 8).scl(1 / MegaMan.PPM);
    vertice1[2] = new Vector2(-4, -6).scl(1 / MegaMan.PPM);
    vertice1[3] = new Vector2(-4, 6).scl(1 / MegaMan.PPM);
    rightHand.set(vertice1);

    fdef.shape = rightHand;
    fdef.restitution = 0.5f;
    fdef.filter.categoryBits = MegaMan.ENEMY_HEAD_BIT;
    b2body.createFixture(fdef).setUserData(this);
    b2body.setGravityScale(0F);
  }


  public TextureRegion getFrame(float dt) {
    TextureRegion region;
    switch (currentState) {
      case FORWARD:
      case BACKWARD:
        region = (TextureRegion) backwardAnimation.getKeyFrame(stateTime,true);
        break;
      default:
        region = (TextureRegion) forwardAnimation.getKeyFrame(stateTime, true);
        break;
    }
    if (velocity.x < 0 && !region.isFlipX()) {
      region.flip(true, false);
    }
    if (velocity.x > 0 && region.isFlipX()) {
      region.flip(true, false);
    }
    stateTime = currentState == previousState ? stateTime + dt : 0;
    // update previous state
    previousState = currentState;
    // return our final adjusted frame
    return region;
  }


  public void update(float dt, MainCharacter mainCharacter) {
    stateTime += dt;
    if(setToDestroy && !destroyed) {
      world.destroyBody(b2body);
      destroyed = true;
      stateTime = 0;
      itemBehaviour.setActive();
    }
    else if(!destroyed) {
      if (currentState == State.FORWARD && stateTime > 2 ) {
        currentState =  State.BACKWARD;
        reverseVelocity(true,false);
      }
      else if(currentState == State.BACKWARD && stateTime > 2) {
        currentState = State.FORWARD;
        reverseVelocity(true,false);
      }
      setRegion(getFrame(dt));
      setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 / MegaMan.PPM);
      b2body.setLinearVelocity(velocity);
    }
    itemBehaviour.update(dt);
  }

  @Override
  public void hitByMegaman(FireBall fireBall) {
    healthBar -= 1;
    if(healthBar < 1){
      setToDestroy= true;
    }
  }

  public void draw(Batch batch){
    if(!destroyed || stateTime < 0.5)
      super.draw(batch);
    if(destroyed && itemBehaviour instanceof Heart && stateTime < 2)
      ((Heart) itemBehaviour).draw(batch);
  }

  @Override
  public void shootBullet() {

  }

}
