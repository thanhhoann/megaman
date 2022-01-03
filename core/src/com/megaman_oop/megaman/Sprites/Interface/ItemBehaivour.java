package com.megaman_oop.megaman.Sprites.Interface;

public interface ItemBehaivour {
    void defineItem();
    void update(float dt);
    void setToDestroy();
    boolean isDestroyed();
    public void setActive();
}
