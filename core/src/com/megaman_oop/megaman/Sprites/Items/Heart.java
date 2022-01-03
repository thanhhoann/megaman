package com.megaman_oop.megaman.Sprites.Items;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Screens.PlayScreen;
import com.megaman_oop.megaman.Sprites.Interface.ItemBehaviour;
import com.megaman_oop.megaman.Sprites.MainCharacter;


public class Heart extends Sprite implements ItemBehaviour {
    PlayScreen screen;
    World world;
    boolean destroyed;
    boolean setToDestroy;
    float stateTime;
    Body body;
    Array<TextureRegion> frames;
    Animation<TextureRegion> Animation;

    public Heart(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("heart"),0,0,32,32));
        Animation = new Animation(0.2f, frames);
        setBounds(x, y, 10/ MegaMan.PPM, 10/ MegaMan.PPM);
        defineItem();
        setToDestroy = false;
        setToDestroy = false;
        body.setActive(false);
    }

    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type= BodyDef.BodyType.KinematicBody;
        if (!world.isLocked()) body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(3, 3).scl(1 / MegaMan.PPM);
        vertice[1] = new Vector2(3, -3).scl(1 / MegaMan.PPM);
        vertice[2] = new Vector2(-3, -3).scl(1 / MegaMan.PPM);
        vertice[3] = new Vector2(-3, 3).scl(1 / MegaMan.PPM);
        shape.set(vertice);
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
        setRegion(Animation.getKeyFrame(stateTime, true));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        if( setToDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
            stateTime = 0;
        }
        body.setGravityScale(0F);
    }

    @Override
    public void useByMegaman(MainCharacter mainCharacter) {
        mainCharacter.gainHealth();
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
