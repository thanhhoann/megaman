package com.megaman_oop.megaman.Tools;

import com.badlogic.gdx.physics.box2d.*;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Sprites.Enemies.Enemy;
import com.megaman_oop.megaman.Sprites.MainCharacter;
import com.megaman_oop.megaman.Sprites.Other.Bullet;
import com.megaman_oop.megaman.Sprites.Other.FireBall;

public class WorldContactListener implements ContactListener {
  @Override
  public void beginContact(Contact contact) {
    Fixture fixA = contact.getFixtureA();
    Fixture fixB = contact.getFixtureB();

    int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

    switch (cDef) {
      case MegaMan.ENEMY_HEAD_BIT | MegaMan.FIREBALL_BIT:
        if (fixA.getFilterData().categoryBits == MegaMan.ENEMY_HEAD_BIT)
          ((Enemy) fixA.getUserData()).hitByMegaman((FireBall) fixB.getUserData());
        else ((Enemy) fixB.getUserData()).hitByMegaman((FireBall) fixA.getUserData());
        break;
      case MegaMan.FIREBALL_BIT | MegaMan.OBJECT_BIT:
        if (fixA.getFilterData().categoryBits == MegaMan.FIREBALL_BIT)
          ((FireBall) fixA.getUserData()).setToDestroy();
        else ((FireBall) fixB.getUserData()).setToDestroy();
        break;
      case MegaMan.BULLET_BIT | MegaMan.GROUND_BIT:
        if (fixA.getFilterData().categoryBits == MegaMan.BULLET_BIT)
          ((Bullet) fixA.getUserData()).setToDestroy();
        else ((Bullet) fixB.getUserData()).setToDestroy();
        break;
      case MegaMan.MEGAMAN_BIT | MegaMan.ENEMY_BIT:
      case MegaMan.MEGAMAN_BIT | MegaMan.ENEMY_HEAD_BIT:
        if (fixA.getFilterData().categoryBits == MegaMan.MEGAMAN_BIT)
          ((MainCharacter) fixA.getUserData()).hit((Enemy) fixB.getUserData());
        else ((MainCharacter) fixB.getUserData()).hit((Enemy) fixA.getUserData());
        break;
      case MegaMan.MEGAMAN_BIT | MegaMan.BULLET_BIT:
        if (fixA.getFilterData().categoryBits == MegaMan.MEGAMAN_BIT)
          ((MainCharacter) fixA.getUserData()).shot((Bullet) fixB.getUserData());
        else ((MainCharacter) fixB.getUserData()).shot((Bullet) fixA.getUserData());
        break;
    }
  }

  @Override
  public void endContact(Contact contact) {}

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {}

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {}
}
