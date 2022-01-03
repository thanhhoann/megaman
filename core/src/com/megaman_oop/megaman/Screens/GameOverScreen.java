package com.megaman_oop.megaman.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
  private SpriteBatch batch = new SpriteBatch();
  float elapsed;

  int buttonHeight = 30;
  int quitButtonWidth = 130;
  int playAgainButtonWidth = 160;
  int playAgainButton_X = 120;
  int playAgainButton_Y = 70;
  int quitButton_X = 130;
  int quitButton_Y = 30;

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
        if (((MegaMan) game).cam.getInputInGameWorld().x > playAgainButton_X
                && ((MegaMan) game).cam.getInputInGameWorld().x < playAgainButton_X + playAgainButtonWidth
                && ((MegaMan) game).cam.getInputInGameWorld().y < playAgainButton_Y + buttonHeight
                && ((MegaMan) game).cam.getInputInGameWorld().y > playAgainButton_Y) {
          gameOverScreen.dispose();
          game.setScreen(new PlayScreen((MegaMan) game));
        }

        //Quit button
        if (((MegaMan) game).cam.getInputInGameWorld().x > quitButton_X
                && ((MegaMan) game).cam.getInputInGameWorld().x < quitButton_X + quitButtonWidth
                && ((MegaMan) game).cam.getInputInGameWorld().y < quitButton_Y + buttonHeight
                && ((MegaMan) game).cam.getInputInGameWorld().y > quitButton_Y) {
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
    Gdx.gl.glClearColor(0,0,0,0);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    batch.begin();
    batch.draw(gameoverBackground.getKeyFrame(elapsed), 0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());

    //Render play again button
    if (((MegaMan) game).cam.getInputInGameWorld().x > playAgainButton_X
            && ((MegaMan) game).cam.getInputInGameWorld().x < playAgainButton_X + playAgainButtonWidth
            && ((MegaMan) game).cam.getInputInGameWorld().y < playAgainButton_Y + buttonHeight
            && ((MegaMan) game).cam.getInputInGameWorld().y > playAgainButton_Y) {
      batch.draw(playAgainBtnActive, 350, 200);
    } else {
      batch.draw(playAgainBtnInactive, 350, 200);
    }

    //Render quit button
    if (((MegaMan) game).cam.getInputInGameWorld().x > quitButton_X
            && ((MegaMan) game).cam.getInputInGameWorld().x < quitButton_X + quitButtonWidth
            && ((MegaMan) game).cam.getInputInGameWorld().y < quitButton_Y + buttonHeight
            && ((MegaMan) game).cam.getInputInGameWorld().y > quitButton_Y) {
      batch.draw(quitBtnActive, 400, 100);
    } else {
      batch.draw(quitBtnInactive, 400, 100);
    }

    batch.end();
    stage.draw();
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
