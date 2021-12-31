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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.megaman_oop.megaman.GifDecoder;
import com.megaman_oop.megaman.MegaMan;


public class PreGameScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    Texture background;
    Texture skipBtnInactive, skipBtnActive;


    //Height, width and coordinates of buttons
    int buttonY = 180;
    int buttonX = 330;
    int buttonHeight = 25;
    int buttonWidth = 50;


    private final Game game;

    public PreGameScreen(final Game game) {
        this.game = game;
        viewport = new FitViewport(MegaMan.V_WIDTH, MegaMan.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((MegaMan) game).batch);
        skipBtnInactive = new Texture(Gdx.files.internal("skip_inactive.png"));
        skipBtnActive = new Texture(Gdx.files.internal("skip_active.png"));

        //Background texture
        background = new Texture(Gdx.files.internal("preGameScreen_background_1.png"));

        final PreGameScreen preGameScreen = this;

        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                //Skip button
                if (((MegaMan) game).cam.getInputInGameWorld().x > buttonX
                        && ((MegaMan) game).cam.getInputInGameWorld().x < buttonX + buttonWidth
                        && ((MegaMan) game).cam.getInputInGameWorld().y < buttonY + buttonHeight
                        && ((MegaMan) game).cam.getInputInGameWorld().y > buttonY) {
                    preGameScreen.dispose();
                    game.setScreen(new PlayScreen((MegaMan) game));
                }

                return super.touchUp(screenX, screenY, pointer, button);
            }

        });

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                background = new Texture(Gdx.files.internal("preGameScreen_background_2.png"));
            }
        }, 3);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                background = new Texture(Gdx.files.internal("preGameScreen_background_3.png"));
            }
        }, 6);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                preGameScreen.dispose();
                game.setScreen(new PlayScreen((MegaMan) game));
            }
        }, 9);

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ((MegaMan) game).batch.begin();
        ((MegaMan) game).batch.draw(background, 0, 0);

        //Render Skip button
        if (((MegaMan) game).cam.getInputInGameWorld().x > buttonX
                && ((MegaMan) game).cam.getInputInGameWorld().x < buttonX + buttonWidth
                && ((MegaMan) game).cam.getInputInGameWorld().y < buttonY + buttonHeight
                && ((MegaMan) game).cam.getInputInGameWorld().y > buttonY) {
            ((MegaMan) game).batch.draw(skipBtnActive, 1000, 550);
        } else {
            ((MegaMan) game).batch.draw(skipBtnInactive, 1000, 550);
        }

        ((MegaMan) game).batch.end();
    }


    @Override
    public void resize(int width, int height) {

    }

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