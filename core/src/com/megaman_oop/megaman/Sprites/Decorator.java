package com.megaman_oop.megaman.Sprites;

import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class Decorator implements Character{
    protected Character character;
    public Decorator(Character character){
        this.character = character;
    }
    public void draw(Batch batch){
        character.draw(batch);
    };  
}
