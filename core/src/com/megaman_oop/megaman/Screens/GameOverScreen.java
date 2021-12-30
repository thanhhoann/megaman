package com.megaman_oop.megaman.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.megaman_oop.megaman.GifDecoder;
import com.megaman_oop.megaman.MegaMan;


public class GameOverScreen implements Screen {
  private Viewport viewport;
  private Stage stage;
  Animation<TextureRegion> gameoverBackground = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("gameOverScreen_background.gif").read());
  Texture playAgainBtnActive, playAgainBtnInactive;
  Texture quitBtnActive, quitBtnInactive;
  float elapsed;

  int buttonHeight = 30;
  int quitButtonWidth = 130;
  int playAgainButtonWidth = 160;
  int playAgainButton_X = 120;
  int playAgainButton_Y = 70;

  private Game game;

  public GameOverScreen(final Game game) {
    this.game = game;
    viewport = new FitViewport(MegaMan.V_WIDTH, MegaMan.V_HEIGHT, new OrthographicCamera());
    stage = new Stage(viewport, ((MegaMan) game).batch);
    final GameOverScreen gameOverScreen = this;

    quitBtnInactive = new Texture(Gdx.files.internal("quit_inactive.png"));
    quitBtnActive = new Texture(Gdx.files.internal("quit_active.png"));
    playAgainBtnInactive = new Texture(Gdx.files.internal("playagain_inactive.png"));
    playAgainBtnActive = new Texture(Gdx.files.internal("playagain_active.png"));

    Gdx.input.setInputProcessor(new InputAdapter() {

      @Override
      public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //Play again button
        if (((MegaMan) game).cam.getInputInGameWorld().x > 120
                && ((MegaMan) game).cam.getInputInGameWorld().x < 120 + playAgainButtonWidth
                && ((MegaMan) game).cam.getInputInGameWorld().y < 70 + buttonHeight
                && ((MegaMan) game).cam.getInputInGameWorld().y > 70) {
          gameOverScreen.dispose();
          game.setScreen(new PlayScreen((MegaMan) game));
        }

        //Quit button
        if (((MegaMan) game).cam.getInputInGameWorld().x > 130
                && ((MegaMan) game).cam.getInputInGameWorld().x < 130 + quitButtonWidth
                && ((MegaMan) game).cam.getInputInGameWorld().y < 30 + buttonHeight
                && ((MegaMan) game).cam.getInputInGameWorld().y > 30) {
          gameOverScreen.dispose();
          Gdx.app.exit();
        }

        return super.touchUp(screenX, screenY, pointer, button);
      }

    });



  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {

    elapsed += Gdx.graphics.getDeltaTime();
    Gdx.gl.glClearColor(0, 1, 0, 0);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    ((MegaMan) game).batch.begin();
    ((MegaMan) game).batch.draw(gameoverBackground.getKeyFrame(elapsed), 0, 0);

    //Play again button render
    if (((MegaMan) game).cam.getInputInGameWorld().x > 120
            && ((MegaMan) game).cam.getInputInGameWorld().x < 120 + playAgainButtonWidth
            && ((MegaMan) game).cam.getInputInGameWorld().y < 70 + buttonHeight
            && ((MegaMan) game).cam.getInputInGameWorld().y > 70) {
      ((MegaMan) game).batch.draw(playAgainBtnActive, 350, 200);
    } else {
      ((MegaMan) game).batch.draw(playAgainBtnInactive, 350, 200);
    }

    //Quit button render
    if (((MegaMan) game).cam.getInputInGameWorld().x > 130
            && ((MegaMan) game).cam.getInputInGameWorld().x < 130 + quitButtonWidth
            && ((MegaMan) game).cam.getInputInGameWorld().y < 30 + buttonHeight
            && ((MegaMan) game).cam.getInputInGameWorld().y > 30) {
      ((MegaMan) game).batch.draw(quitBtnActive, 400, 100);
    } else {
      ((MegaMan) game).batch.draw(quitBtnInactive, 400, 100);
    }

    ((MegaMan) game).batch.end();

  }

  @Override
  public void resize(int width, int height) {}

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {
    Gdx.input.setInputProcessor(null);
  }
}
