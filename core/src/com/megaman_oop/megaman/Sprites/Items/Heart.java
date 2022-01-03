package com.megaman_oop.megaman.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Screens.PlayScreen;
import com.megaman_oop.megaman.Sprites.Interfaces.ItemBehaviour;
import com.megaman_oop.megaman.Sprites.MainCharacter;

public class Heart extends Sprite implements ItemBehaviour {
    PlayScreen screen;
    World world;
    boolean destroyed;
    boolean setToDestroy;
    float stateTime;
    Body body;

    public Heart(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        setRegion(screen.getAtlas().findRegion("heart"),0,0,32,32);
        setBounds(x, y, 10/ MegaMan.PPM, 10/ MegaMan.PPM);
        defineItem();
        body.setActive(false);
    }

    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type= BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5/ MegaMan.PPM);
        fdef.filter.categoryBits= MegaMan.ITEM_BIT;
        fdef.filter.maskBits = MegaMan.GROUND_BIT |
                 MegaMan.MEGAMAN_BIT
                | MegaMan.ENEMY_BIT
                | MegaMan.ENEMY_HEAD_BIT
                | MegaMan.OBJECT_BIT;
        fdef.shape= shape;
        body.createFixture(fdef).setUserData(this);
    }
    public void update(float dt){
        stateTime += dt;
        setPosition(body.getPosition().x , body.getPosition().y );
        if((stateTime > 2 || setToDestroy) && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
        }
    }

    @Override
    public void setToDestroy() {
        setToDestroy = true;
    }

    @Override
    public void setActive() {
        body.setActive(true);
    }

    public boolean isDestroyed() {
        return destroyed;
    }



}
