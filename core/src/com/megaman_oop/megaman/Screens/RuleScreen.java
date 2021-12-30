package com.megaman_oop.megaman.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.megaman_oop.megaman.MegaMan;


public class RuleScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    Texture ruleScreenBackground = new Texture(Gdx.files.internal("ruleScreen_background.png"));
    Texture startBtnInactive, startBtnActive;
    Texture returnBtnInactive, returnBtnActive;


    //Height, width and coordinates of buttons
    int buttonY = 15;
    int buttonHeight = 30;
    int buttonWidth = 130;
    int startButtonX = 230;
    int returnButtonX = 30;

    private final Game game;

    public RuleScreen(final Game game) {
        this.game = game;
        viewport = new FitViewport(MegaMan.V_WIDTH, MegaMan.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((MegaMan) game).batch);
        startBtnInactive = new Texture(Gdx.files.internal("start_inactive.png"));
        startBtnActive = new Texture(Gdx.files.internal("start_active.png"));
        returnBtnInactive = new Texture(Gdx.files.internal("return_inactive.png"));
        returnBtnActive = new Texture(Gdx.files.internal("return_active.png"));

        final RuleScreen ruleScreen = this;

        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                //Start button
                if (((MegaMan) game).cam.getInputInGameWorld().x > startButtonX
                        && ((MegaMan) game).cam.getInputInGameWorld().x < startButtonX + buttonWidth
                        && ((MegaMan) game).cam.getInputInGameWorld().y < buttonY + buttonHeight
                        && ((MegaMan) game).cam.getInputInGameWorld().y > buttonY) {
                    ruleScreen.dispose();
                    game.setScreen(new PlayScreen((MegaMan) game));
                }

                //Return Button
                if (((MegaMan) game).cam.getInputInGameWorld().x > returnButtonX
                        && ((MegaMan) game).cam.getInputInGameWorld().x < returnButtonX + buttonWidth
                        && ((MegaMan) game).cam.getInputInGameWorld().y < buttonY + buttonHeight
                        && ((MegaMan) game).cam.getInputInGameWorld().y > buttonY) {
                    ruleScreen.dispose();
                    game.setScreen(new MenuScreen((MegaMan) game));
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

        Gdx.gl.glClearColor(0, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ((MegaMan) game).batch.begin();
        ((MegaMan) game).batch.draw(ruleScreenBackground, 0, 0);

        //Start button render
        if (((MegaMan) game).cam.getInputInGameWorld().x > 230
                && ((MegaMan) game).cam.getInputInGameWorld().x < 230 + buttonWidth
                && ((MegaMan) game).cam.getInputInGameWorld().y < buttonY + buttonHeight
                && ((MegaMan) game).cam.getInputInGameWorld().y > buttonY) {
            ((MegaMan) game).batch.draw(startBtnActive, 700, 50);
        } else {
            ((MegaMan) game).batch.draw(startBtnInactive, 700, 50);
        }

        //Return button render
        if (((MegaMan) game).cam.getInputInGameWorld().x > 30
                && ((MegaMan) game).cam.getInputInGameWorld().x < 30 + buttonWidth
                && ((MegaMan) game).cam.getInputInGameWorld().y < buttonY + buttonHeight
                && ((MegaMan) game).cam.getInputInGameWorld().y > buttonY) {
            ((MegaMan) game).batch.draw(returnBtnActive, 75, 50);
        } else {
            ((MegaMan) game).batch.draw(returnBtnInactive, 75, 50);
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