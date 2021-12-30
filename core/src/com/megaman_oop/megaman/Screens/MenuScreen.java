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


public class MenuScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    Animation<TextureRegion> animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("menu_background.gif").read());
    Texture startBtnInactive, startBtnActive;
    Texture ruleBtnInactive, ruleBtnActive;
    Texture quitBtnInactive, quitBtnActive;
    float elapsed;

    //Height, width and coordinates of buttons
    int buttonHeight = 30;
    int buttonWidth = 130;
    int buttonX = 30;
    int startButtonY = 100;
    int ruleButtonY = 60;
    int quitButtonY = 30;

    private final Game game;

    public MenuScreen(final Game game) {
        this.game = game;
        viewport = new FitViewport(MegaMan.V_WIDTH, MegaMan.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((MegaMan) game).batch);
        startBtnInactive = new Texture(Gdx.files.internal("start_inactive.png"));
        startBtnActive = new Texture(Gdx.files.internal("start_active.png"));
        ruleBtnInactive = new Texture(Gdx.files.internal("rules_inactive.png"));
        ruleBtnActive = new Texture(Gdx.files.internal("rules_active.png"));
        quitBtnInactive = new Texture(Gdx.files.internal("quit_inactive.png"));
        quitBtnActive = new Texture(Gdx.files.internal("quit_active.png"));

        final MenuScreen menuScreen = this;

        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                //Start button
                if (((MegaMan) game).cam.getInputInGameWorld().x > buttonX
                        && ((MegaMan) game).cam.getInputInGameWorld().x < buttonX + buttonWidth
                        && ((MegaMan) game).cam.getInputInGameWorld().y < startButtonY + buttonHeight
                        && ((MegaMan) game).cam.getInputInGameWorld().y > startButtonY) {
                    menuScreen.dispose();
                    game.setScreen(new PlayScreen((MegaMan) game));
                }

                //Rule button
                if (((MegaMan) game).cam.getInputInGameWorld().x > buttonX
                        && ((MegaMan) game).cam.getInputInGameWorld().x < buttonX + buttonWidth
                        && ((MegaMan) game).cam.getInputInGameWorld().y < ruleButtonY + buttonHeight
                        && ((MegaMan) game).cam.getInputInGameWorld().y > ruleButtonY) {
                    menuScreen.dispose();
                    game.setScreen(new RuleScreen((MegaMan) game));
                }

                //Quit button
                if (((MegaMan) game).cam.getInputInGameWorld().x > buttonX
                        && ((MegaMan) game).cam.getInputInGameWorld().x < buttonX + buttonWidth
                        && ((MegaMan) game).cam.getInputInGameWorld().y < quitButtonY + buttonHeight
                        && ((MegaMan) game).cam.getInputInGameWorld().y > quitButtonY) {
                    menuScreen.dispose();
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
        Gdx.gl.glClearColor(0, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ((MegaMan) game).batch.begin();
        ((MegaMan) game).batch.draw(animation.getKeyFrame(elapsed), 0, 0);

        //Start button render
        if (((MegaMan) game).cam.getInputInGameWorld().x > buttonX
                && ((MegaMan) game).cam.getInputInGameWorld().x < buttonX + buttonWidth
                && ((MegaMan) game).cam.getInputInGameWorld().y < startButtonY + buttonHeight
                && ((MegaMan) game).cam.getInputInGameWorld().y > startButtonY) {
            ((MegaMan) game).batch.draw(startBtnActive, 75, 300);
        } else {
            ((MegaMan) game).batch.draw(startBtnInactive, 75, 300);
        }

        //Rule button render
        if (((MegaMan) game).cam.getInputInGameWorld().x > buttonX
                && ((MegaMan) game).cam.getInputInGameWorld().x < buttonX + buttonWidth
                && ((MegaMan) game).cam.getInputInGameWorld().y < ruleButtonY + buttonHeight
                && ((MegaMan) game).cam.getInputInGameWorld().y > ruleButtonY) {
            ((MegaMan) game).batch.draw(ruleBtnActive, 75, 200);
        } else {
            ((MegaMan) game).batch.draw(ruleBtnInactive, 75, 200);
        }

        //Quit button render
        if (((MegaMan) game).cam.getInputInGameWorld().x > buttonX
                && ((MegaMan) game).cam.getInputInGameWorld().x < buttonX + buttonWidth
                && ((MegaMan) game).cam.getInputInGameWorld().y < quitButtonY + buttonHeight
                && ((MegaMan) game).cam.getInputInGameWorld().y > quitButtonY) {
            ((MegaMan) game).batch.draw(quitBtnActive, 75, 100);
        } else {
            ((MegaMan) game).batch.draw(quitBtnInactive, 75, 100);
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

