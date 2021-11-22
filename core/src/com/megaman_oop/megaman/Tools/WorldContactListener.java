package com.megaman_oop.megaman.Tools;

import com.badlogic.gdx.physics.box2d.*;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Sprites.Enemies.Enemy;
import com.megaman_oop.megaman.Sprites.Items.Item;
import com.megaman_oop.megaman.Sprites.MainCharacter;
import com.megaman_oop.megaman.Sprites.Other.FireBall;

public class WorldContactListener implements ContactListener {
  @Override
  public void beginContact(Contact contact) {
    Fixture fixA = contact.getFixtureA();
    Fixture fixB = contact.getFixtureB();

    int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

    switch (cDef) {
      case MegaMan.MEGAMAN_HEAD_BIT | MegaMan.BRICK_BIT:
      case MegaMan.ENEMY_HEAD_BIT | MegaMan.MEGAMAN_BIT:
        if (fixA.getFilterData().categoryBits == MegaMan.ENEMY_HEAD_BIT)
          ((Enemy) fixA.getUserData()).hitByMegaman((MainCharacter) fixB.getUserData());
        else ((Enemy) fixB.getUserData()).hitByMegaman((MainCharacter) fixA.getUserData());
        break;
      case MegaMan.ENEMY_BIT | MegaMan.OBJECT_BIT:
        if (fixA.getFilterData().categoryBits == MegaMan.ENEMY_BIT)
          ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
        else ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
        break;
      case MegaMan.MEGAMAN_BIT | MegaMan.ENEMY_BIT:
        if (fixA.getFilterData().categoryBits == MegaMan.MEGAMAN_BIT)
          ((MainCharacter) fixA.getUserData()).hit((Enemy) fixB.getUserData());
        else ((MainCharacter) fixB.getUserData()).hit((Enemy) fixA.getUserData());
        break;
      case MegaMan.ENEMY_BIT:
        ((Enemy) fixA.getUserData()).hitByEnemy((Enemy) fixB.getUserData());
        ((Enemy) fixB.getUserData()).hitByEnemy((Enemy) fixA.getUserData());
        break;
      case MegaMan.ITEM_BIT | MegaMan.OBJECT_BIT:
        if (fixA.getFilterData().categoryBits == MegaMan.ITEM_BIT)
          ((Item) fixA.getUserData()).reverseVelocity(true, false);
        else ((Item) fixB.getUserData()).reverseVelocity(true, false);
        break;
      case MegaMan.ITEM_BIT | MegaMan.MEGAMAN_BIT:
        if (fixA.getFilterData().categoryBits == MegaMan.ITEM_BIT)
          ((Item) fixA.getUserData()).use((MainCharacter) fixB.getUserData());
        else ((Item) fixB.getUserData()).use((MainCharacter) fixA.getUserData());
        break;
      case MegaMan.FIREBALL_BIT | MegaMan.OBJECT_BIT:
        if (fixA.getFilterData().categoryBits == MegaMan.FIREBALL_BIT)
          ((FireBall) fixA.getUserData()).setToDestroy();
        else ((FireBall) fixB.getUserData()).setToDestroy();
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
