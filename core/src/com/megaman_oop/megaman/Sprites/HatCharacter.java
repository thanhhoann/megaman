package com.megaman_oop.megaman.Sprites;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.megaman_oop.megaman.Screens.PlayScreen;

public class HatCharacter extends DecoratedCharacter{
    private MainCharacter mainCharacter;
    HatDecor hat;
    PlayScreen screen;

    public HatCharacter(Character character){
        super(character);
    }

    public void hatDecor(){
        hat = new HatDecor(this.screen, mainCharacter.b2body.getPosition().x, mainCharacter.b2body.getPosition().y);
    }

    public void defineSpecialCharacter() {
        mainCharacter.defineMEGAMAN();
        hatDecor();
    }

    @Override
    public void draw(Batch batch){
        character.draw(batch);
        hat.draw(batch);
    }
}
