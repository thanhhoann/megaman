package com.megaman_oop.megaman.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Screens.PlayScreen;
import com.megaman_oop.megaman.Sprites.Interface.ItemBehaviour;
import com.megaman_oop.megaman.Sprites.MainCharacter;
import com.megaman_oop.megaman.Sprites.Other.Bullet;
import com.megaman_oop.megaman.Sprites.Other.FireBall;


public class FlyBot extends Enemy {

  private float stateTime;
  private Animation flyAnimation;
  private Array<TextureRegion> frames;
  private boolean setToDestroy;
  private boolean destroyed;
  private Array<Bullet> bullets;

  private static int healthBar = 3;



  public FlyBot(PlayScreen screen, float x, float y, ItemBehaviour itemBehaviour) {
    super(screen, x, y, itemBehaviour);
    frames = new Array<TextureRegion>();
    for(int i =0; i <3; i++){
      frames.add(new TextureRegion(screen.getAtlas().findRegion("enemysprite1"),  i * 130,680 ,130,75));}
    flyAnimation = new Animation(0.2f, frames);
    stateTime = 0;
    setBounds(getX(), getY(), 30 / MegaMan.PPM, 30 / MegaMan.PPM);
    setToDestroy = false;
    destroyed = false;
    bullets = new Array<Bullet>(1);
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
            | MegaMan.FIREBALL_BIT
            | MegaMan.OBJECT_BIT
            | MegaMan.MEGAMAN_BIT;
    fdef.shape = shape;
    b2body.createFixture(fdef).setUserData(this);
    b2body.setGravityScale(0F);
  }


  @Override
  public void update(float dt, MainCharacter mainCharacter) {
    stateTime += dt;
    if(setToDestroy && !destroyed){
      world.destroyBody(b2body);
      destroyed = true;
      stateTime = 0;
    }
    else if(!destroyed) {
      b2body.setLinearVelocity(0,0);
      setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
      setRegion((TextureRegion) flyAnimation.getKeyFrame(stateTime, true));
      shootBullet();
    }
    for (Bullet bullet: bullets) {
      bullet.update(dt);
      if (bullet.isDestroyed()) bullets.removeValue(bullet, true);
    }
  }

  @Override
  public void hitByMegaman(FireBall fireBall) {
    healthBar -= 1;
    if(healthBar < 1){
      setToDestroy= true;
    }
  }

  @Override
  public void shootBullet() {
    if(bullets.isEmpty())
      bullets.add(new Bullet(screen, b2body.getPosition().x , b2body.getPosition().y ));
  }
  public void draw(Batch batch) {
    if (!destroyed) {
      super.draw(batch);
      for (Bullet bullet : bullets) bullet.draw(batch);
    }
  }


}
