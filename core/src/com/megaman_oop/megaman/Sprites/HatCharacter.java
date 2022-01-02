package com.megaman_oop.megaman.Sprites;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.megaman_oop.megaman.Screens.PlayScreen;

public class HatCharacter extends Decorator{
    private MainCharacter mainCharacter;
    Hat hat;
    PlayScreen screen;

    public HatCharacter(Character character){
        super(character);
    }

    public void hatDecor(){
        hat = new Hat(this.screen, mainCharacter.b2body.getPosition().x, mainCharacter.b2body.getPosition().y);
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
