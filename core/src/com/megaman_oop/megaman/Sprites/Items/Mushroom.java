package com.megaman_oop.megaman.Sprites.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Screens.PlayScreen;
import com.megaman_oop.megaman.Sprites.MainCharacter;

public class Mushroom extends Item {
  public Mushroom(PlayScreen screen, float x, float y) {
    super(screen, x, y);
    setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
    velocity = new Vector2(0.7f, 0);
  }

  @Override
  public void defineItem() {
    BodyDef bodyDef = new BodyDef();
    bodyDef.position.set(getX(), getY());
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    body = world.createBody(bodyDef);

    FixtureDef fixtureDef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(6 / MegaMan.PPM);
    fixtureDef.filter.categoryBits = MegaMan.ITEM_BIT;
    fixtureDef.filter.maskBits =
        MegaMan.MEGAMAN_BIT
            | MegaMan.OBJECT_BIT
            | MegaMan.GROUND_BIT
            | MegaMan.COIN_BIT
            | MegaMan.BRICK_BIT;

    fixtureDef.shape = shape;
    body.createFixture(fixtureDef).setUserData(this);
  }

  @Override
  public void use(MainCharacter mainCharacter) {
    destroy();
  }

  @Override
  public void update(float dt) {
    super.update(dt);
    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    velocity.y = body.getLinearVelocity().y;
    body.setLinearVelocity(velocity);
  }
}
