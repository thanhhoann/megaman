package com.megaman_oop.megaman.Sprites.Other;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Screens.PlayScreen;

public class Bullet extends Sprite {
    PlayScreen screen;
    World world;
    Array<TextureRegion> frames;
    Animation<TextureRegion> fireAnimation;
    float stateTime;
    boolean fireDown;
    boolean destroyed;
    boolean setToDestroy;
    Body b2body;

    public Bullet(PlayScreen screen, float x, float y, boolean fireDown) {
        this.fireDown = fireDown;
        this.screen = screen;
        this.world = screen.getWorld();
        for (int i = 1; i <= 3; i++) {
            if (i == 1)
                frames.add(
                        new TextureRegion(
                                screen.getAtlas().findRegion("megasprite_remake"), 56, 992, 24, 24));
            if (i == 2)
                frames.add(
                        new TextureRegion(
                                screen.getAtlas().findRegion("megasprite_remake"),342 , 992, 24, 24));
            if (i == 3)
                frames.add(
                        new TextureRegion(
                                screen.getAtlas().findRegion("megasprite_remake"), 620, 992, 24, 24));
        }
        fireAnimation =  new Animation<TextureRegion>(0.4f, frames);
        setRegion(fireAnimation.getKeyFrame(0));
        setBounds(x, y, 10/ MegaMan.PPM, 10/ MegaMan.PPM);
        defineBullet();
    }

    private void defineBullet() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        if (!world.isLocked()) b2body = world.createBody(bodyDef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 /MegaMan.PPM);
        fdef.filter.categoryBits = MegaMan.BRICK_BIT;
        fdef.filter.maskBits =
                MegaMan.GROUND_BIT
                        | MegaMan.COIN_BIT
                        | MegaMan.BRICK_BIT
                        | MegaMan.MEGAMAN_BIT
                        | MegaMan.OBJECT_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        fdef.isSensor = true;
        b2body.setLinearVelocity(new Vector2(0, -2.5f));
    }
    public void update(float dt){
        stateTime += dt;
        setRegion(fireAnimation.getKeyFrame(stateTime, true));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if((stateTime > 3 || setToDestroy) && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
    }
    public void setToDestroy() {
        setToDestroy = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

}
