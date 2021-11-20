package com.megaman_oop.megaman.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Scenes.Hud;
import com.megaman_oop.megaman.Screens.PlayScreen;
import com.megaman_oop.megaman.Sprites.Items.ItemDef;
import com.megaman_oop.megaman.Sprites.Items.Mushroom;
import com.megaman_oop.megaman.Sprites.MainCharacter;

public class Coin extends InteractiveTileObject {
  private static TiledMapTileSet tileSet;
  private final int BLANK_COIN = 28;

  public Coin(PlayScreen screen, MapObject object) {
    super(screen, object);
    tileSet = map.getTileSets().getTileSet("tileset_gutter");
    fixture.setUserData(this);
    setCategoryFilter(MegaMan.COIN_BIT);
  }

  private void setCategoryFilter(short coinBit) {}

  public void onHeadHit(MainCharacter mainCharacter) {
    if (getCell().getTile().getId() == BLANK_COIN)
      MegaMan.manager.get("audio/sounds/bump.wav", Sound.class).play();
    else {
      if (object.getProperties().containsKey("mushroom")) {
        screen.spawnItem(
            new ItemDef(
                new Vector2(body.getPosition().x, body.getPosition().y + 16 / MegaMan.PPM),
                Mushroom.class));
        MegaMan.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
      } else MegaMan.manager.get("audio/sounds/coin.wav", Sound.class).play();
      getCell().setTile(tileSet.getTile(BLANK_COIN));
      Hud.addScore(100);
    }
  }
}
