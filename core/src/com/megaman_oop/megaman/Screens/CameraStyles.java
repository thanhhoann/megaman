package com.megaman_oop.megaman.Screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Sprites.MainCharacter;
import com.megaman_oop.megaman.Sprites.Enemies.Enemy;

public class CameraStyles {
    public static void lerpToCharacter(Camera camera, MainCharacter character){
        Vector3 position = camera.position;
        position.x = camera.position.x + (character.getX()- camera.position.x)*.1f;
        position.y = camera.position.y + (character.getY() - camera.position.y)*.05f;
        camera.position.set(position);
        camera.update();
    }

    public static void boundary(Camera camera, float startX, float startY, float width, float height){
        Vector3 position = camera.position;
        if(position.x < startX){
            position.x = startX;
        }
        if (position.y < startY){
            position.y = startY;
        }
        if (position.x > startX + width){
            position.x = startX + width;
        }
        if (position.y > startY + height){
            position.y = startY + height;
        }
        camera.position.set(position);
        camera.update();
    }
    
    public static void averageBetweenTarget(Camera camera, MainCharacter mainCharacter, Enemy enemy){
        float avgX = (mainCharacter.getX() + enemy.getX())/2;
        float avgY = (mainCharacter.getY() + enemy.getY())/2;
        
        Vector3 position = camera.position;
        position.x = camera.position.x + (avgX- camera.position.x)*.1f;
        position.y = camera.position.y + (avgY - camera.position.y)*.1f;
        camera.position.set(position);
        camera.update();
    }
}
