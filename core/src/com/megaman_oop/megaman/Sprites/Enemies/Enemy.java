package com.megaman_oop.megaman.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.megaman_oop.megaman.Screens.PlayScreen;
import com.megaman_oop.megaman.Sprites.Interface.ItemBehaviour;
import com.megaman_oop.megaman.Sprites.MainCharacter;
import com.megaman_oop.megaman.Sprites.Other.FireBall;


public abstract class Enemy extends Sprite {
  protected World world;
  protected PlayScreen screen;
  protected ItemBehaviour itemBehaviour;
  public Body b2body;
  public Vector2 velocity;

  public Enemy(PlayScreen screen, float x, float y,ItemBehaviour itemBehaviour) {
    this.world = screen.getWorld();
    this.screen = screen;
    this.itemBehaviour = itemBehaviour;
    setPosition(x, y);
    defineEnemy();
    velocity = new Vector2(1, 0);
    b2body.setActive(false);
  }

  protected abstract void defineEnemy();

  public abstract void update(float dt, MainCharacter mainCharacter);

  public abstract void hitByMegaman(FireBall fireBall);

  public abstract void shootBullet();
  public void reverseVelocity(boolean x, boolean y) {
    if (x) velocity.x = -velocity.x;
    if (y) velocity.y = -velocity.y;
  }
}
