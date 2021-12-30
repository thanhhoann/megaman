package com.megaman_oop.megaman.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.megaman_oop.megaman.Screens.PlayScreen;
import com.megaman_oop.megaman.Sprites.MainCharacter;
import com.megaman_oop.megaman.Sprites.Other.FireBall;

public class MiniBot extends Enemy{
    public enum State {
        FLYING,
        SHOOTING
    }
    public SmallBot.State currentState;
    public SmallBot.State previousState;
    private float stateTime;
    private Animation forwardAnimation;
    private Animation backwardAnimation;
    private Array<TextureRegion> frames;

    private boolean setToDestroy;
    private boolean destroyed;

    public MiniBot(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for(int i = 1; i <= 4; i++) {
            if(i == 1){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("enemysprite1"), 31, 288, 90, 130));}
            if(i== 2){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("enemysprite1"), 113, 288, 90, 130));}
            if(i == 3){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("enemysprite1"), 232, 288, 90, 130));}
            if(i ==4) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("enemysprite1"), 329, 288, 90, 130));}
        }
    }

    @Override
    protected void defineEnemy() {

    }

    @Override
    public void update(float dt, MainCharacter mainCharacter) {

    }

    @Override
    public void hitByMegaman(FireBall fireBall) {

    }

    @Override
    public void shootBullet() {

    }
}
