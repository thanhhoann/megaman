package com.megaman_oop.megaman.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Scenes.Hud;
import com.megaman_oop.megaman.Screens.PlayScreen;
import com.megaman_oop.megaman.Sprites.MainCharacter;

/**
 * Created by brentaureli on 8/28/15.
 */
public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MegaMan.BRICK_BIT);
    }

    @Override
    public void onHeadHit(MainCharacter mainCharacter) {
        if(mainCharacter.isBig()) {
            setCategoryFilter(MegaMan.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScore(200);
            MegaMan.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
        }
        MegaMan.manager.get("audio/sounds/bump.wav", Sound.class).play();
    }

}
