package com.megaman_oop.megaman.Sprites;

import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class DecoratedCharacter implements Character{
    protected Character character;
    public DecoratedCharacter(Character character){
        this.character = character;
    }
    @Override
    public void draw(Batch batch){
        character.draw(batch);
    };  
}
