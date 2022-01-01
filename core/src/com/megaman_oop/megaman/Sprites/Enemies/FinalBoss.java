package com.megaman_oop.megaman.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Screens.PlayScreen;
import com.megaman_oop.megaman.Sprites.MainCharacter;
import com.megaman_oop.megaman.Sprites.Other.Bullet;
import com.megaman_oop.megaman.Sprites.Other.FireBall;

public class FinalBoss extends Enemy {

    public enum State{
        STANDING,
        MOVING,
        ATTACKING,
        JUMPING,
        ROLL,
        CLIMBING;
    }
    public State currentState;
    public State previousState;
    private TextureRegion standing;

    private Animation<TextureRegion> moving;
    private Animation<TextureRegion> attacking;
    private Animation<TextureRegion> jumping;
    private Animation<TextureRegion> climbing;
    private static int healthBar;
    private float stateTimer;
    private boolean setToDestroy;
    private boolean destroyed;

    public FinalBoss(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        previousState = currentState = State.STANDING;
        stateTimer = 0;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        //MOVING
        for(int i = 0; i < 7; i++){
            if(i < 4)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("finalboss"),i*120,155,125,93));
            else
                frames.add(new TextureRegion(screen.getAtlas().findRegion("finalboss"),(i-4)*139 + 485,155,125,93));;
        }
        moving = new Animation<TextureRegion>(0.2f,frames);
        frames.clear();
        //ATTACKING
        for(int i = 0; i < 5; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("finalboss"),i*132,280,132,86));
        }
        attacking = new Animation<TextureRegion>(0.2f,frames);
        //JUMPING
        for(int i = 0; i < 8; i++){
            if( i == 0 || i == 7)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("finalboss"),0,0,120,110));
            if(i == 1)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("finalboss"),120,0,137,110));
            else
                frames.add(new TextureRegion(screen.getAtlas().findRegion("finalboss"),(i-1) * 115 + 257,0,115,130));
        }
        jumping = new Animation<TextureRegion>(0.2f,frames);
        frames.clear();
        //STAND
        standing = new TextureRegion(screen.getAtlas().findRegion("finalboss"),0,160,112,82);

        setBounds(getX(), getY() , 40 / MegaMan.PPM, 40 / MegaMan.PPM);
        setToDestroy = false;
        destroyed= false;
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / MegaMan.PPM);
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
        vertice[0] = new Vector2(10, -12).scl(1 / MegaMan.PPM);
        vertice[1] = new Vector2(10, 12).scl(1 / MegaMan.PPM);
        vertice[2] = new Vector2(8, -10).scl(1 / MegaMan.PPM);
        vertice[3] = new Vector2(8, 10).scl(1 / MegaMan.PPM);
        leftHand.set(vertice);

        fdef.shape = leftHand;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = MegaMan.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);

        PolygonShape rightHand = new PolygonShape();
        Vector2[] vertice1 = new Vector2[4];
        vertice1[0] = new Vector2(-10, -12).scl(1 / MegaMan.PPM);
        vertice1[1] = new Vector2(-10, 12).scl(1 / MegaMan.PPM);
        vertice1[2] = new Vector2(-8, -10).scl(1 / MegaMan.PPM);
        vertice1[3] = new Vector2(-8, 10).scl(1 / MegaMan.PPM);
        rightHand.set(vertice1);

        fdef.shape = rightHand;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = MegaMan.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }
    public TextureRegion getFrame(float dt){
        TextureRegion region;
        switch (currentState){
            case ATTACKING:
                region = attacking.getKeyFrame(stateTimer,true);
                break;
            case MOVING:
                region = moving.getKeyFrame(stateTimer,true);
                break;
            case JUMPING:
                region = jumping.getKeyFrame(stateTimer,true);
                break;
            case STANDING:
            default:
                region = standing;
                break;
        }
        if (velocity.x < 0 && !region.isFlipX()) {
            region.flip(true, false);
        }
        if (velocity.x > 0 && region.isFlipX()) {
            region.flip(true, false);
        }
        // if the current state is the same as the previous state increase the state timer.
        // otherwise, the state has changed, and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        // update previous state
        previousState = currentState;
        // return our final adjusted frame
        return region;
    }
    @Override
    public void update(float dt, MainCharacter mainCharacter) {
        stateTimer += dt;
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            stateTimer = 0;
        } else if (!destroyed) {
            setRegion(getFrame(dt));
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 / MegaMan.PPM);
            b2body.setLinearVelocity(velocity);
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

    }
}
