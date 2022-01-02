package com.megaman_oop.megaman.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Screens.PlayScreen;

public class Hat extends Sprite{
    PlayScreen screen;
    World world;
    Array<TextureRegion> frames;
    Animation<TextureRegion> hatAnimation;
    float stateTime;
    Body b2body;

    public Hat(PlayScreen screen, float x, float y){
        this.screen = screen;
        this.world = screen.getWorld();
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("christmas-hat"), 54, 55, 162, 161));
        hatAnimation = new Animation<TextureRegion>(0.3f,frames);
        setRegion(hatAnimation.getKeyFrame(0));
        setBounds(x, y, 15 / MegaMan.PPM, 15 / MegaMan.PPM);
        defineHat();
    }

    public void defineHat(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(),getY() + 10 / MegaMan.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / MegaMan.PPM);
        fixtureDef.filter.categoryBits = MegaMan.ITEM_BIT;
        fixtureDef.filter.maskBits = MegaMan.MEGAMAN_BIT;
        fixtureDef.shape = shape;
        b2body.createFixture(fixtureDef).setUserData(this);
    } 

    @Override 
    public void draw(Batch batch){
        super.draw(batch);
    }

    public void update(float dt){
        b2body.setLinearVelocity(0,0);
    }
}