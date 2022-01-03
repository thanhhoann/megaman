package com.megaman_oop.megaman.Sprites.Interface;

import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Sprites.MainCharacter;

public interface ItemBehaviour {
    void defineItem();
    void update(float dt);
    void useByMegaman(MainCharacter mainCharacter);
    boolean isDestroyed();
    void setActive();

}
