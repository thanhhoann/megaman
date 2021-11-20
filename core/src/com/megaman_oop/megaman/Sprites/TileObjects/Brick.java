package com.megaman_oop.megaman.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Screens.PlayScreen;
import com.megaman_oop.megaman.Sprites.MainCharacter;

public class Brick extends InteractiveTileObject {
  public Brick(PlayScreen screen, MapObject object) {
    super(screen, object);
    fixture.setUserData(this);
  }

  public void onHeadHit(MainCharacter mainCharacter) {
    MegaMan.manager.get("audio/sounds/bump.wav", Sound.class).play();
  }
}
