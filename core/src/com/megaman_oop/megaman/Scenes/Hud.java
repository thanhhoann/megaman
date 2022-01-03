package com.megaman_oop.megaman.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.megaman_oop.megaman.MegaMan;
import com.megaman_oop.megaman.Screens.PlayScreen;

// import java.util.ArrayList;
// import java.util.Collections;

public class Hud implements Disposable {

  public Stage stage;
  private Viewport viewport;


  // MainCharacter score/time Tracking Variables
  private int worldTimer;
  private boolean timeUp;
  private float timeCount;
  private static int score;



  // Scene2D widgets
  private Label countdownLabel;
  private static Label scoreLabel;
  private Label timeLabel;
  private Label levelLabel;
  private Label worldLevelLabel;
  private Label megamanLabel;

  public Hud(SpriteBatch sb) {
    // define our tracking variables
    worldTimer = 300;
    timeCount = 0;
    score = 0;

    // set up the HUD viewport using a new camera seperate from our game cam
    // define our stage using that viewport and our games spritebatch
    viewport = new FitViewport(MegaMan.V_WIDTH, MegaMan.V_HEIGHT, new OrthographicCamera());
    stage = new Stage(viewport, sb);

    // define a table used to organize our hud's labels
    Table table = new Table();
    // Top-Align table
    table.top();
    // make the table fill the entire stage
    table.setFillParent(true);
    // define our labels using the String, and a Label style consisting of a font and color
    countdownLabel = new Label(String.format("%03d s", worldTimer), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("myfont.fnt")), Color.WHITE));
    scoreLabel = new Label(String.format("SCORE: %03d", score), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("myfont.fnt")), Color.WHITE));
    levelLabel = new Label(String.format("1-1"), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("myfont.fnt")), Color.WHITE));
    timeLabel = new Label("TIME LEFT", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("myfont.fnt")), Color.WHITE));
    worldLevelLabel = new Label("WORLD LEVEL", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("myfont.fnt")), Color.WHITE));
    megamanLabel = new Label("MEGAMAN", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("myfont.fnt")), Color.WHITE));
    // add our labels to our table, padding the top, and giving them all equal width with expandX
    table.add(megamanLabel).expandX().padTop(10);
    table.add(worldLevelLabel).expandX().padTop(10);
    table.add(timeLabel).expandX().padTop(10);
    // add a second row to our table
    table.row();
    table.add(scoreLabel).expandX();
    table.add(levelLabel).expandX();
    table.add(countdownLabel).expandX();
    // add our table to the stage
    stage.addActor(table);
  }

  public void update(float dt) {
    timeCount += dt;
    if (timeCount >= 1) {
      if (worldTimer > 0) {
        worldTimer--;
      } else {
        timeUp = true;
      }
      countdownLabel.setText(String.format("%03d s", worldTimer));
      timeCount = 0;
    }
  }

  public static void addScore(int value) {
    score += value;
    scoreLabel.setText(String.format("SCORE: %03d", score));
  }

  @Override
  public void dispose() {
    stage.dispose();
  }

  public boolean isTimeUp() {
    return timeUp;
  }
}
