package com.megaman_oop.megaman.Sprites.Interface;

public interface ItemBehaviour {
    void defineItem();
    void update(float dt);
    void setToDestroy();
    boolean isDestroyed();
    public void setActive();
}
