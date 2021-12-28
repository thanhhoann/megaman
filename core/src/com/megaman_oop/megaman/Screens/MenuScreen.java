package com.megaman_oop.megaman.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.megaman_oop.megaman.GifDecoder;
import com.megaman_oop.megaman.MegaMan;


public class MenuScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    Animation<TextureRegion> animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("menu_background.gif").read());
    Texture start = new Texture(Gdx.files.internal("menu_start.png"));
    Texture rule = new Texture(Gdx.files.internal("menu_rule.png"));
    Texture quit = new Texture(Gdx.files.internal("menu_quit.png"));
    Drawable drawableStart = new TextureRegionDrawable(new TextureRegion(start));
    ImageButton startBtn;
    SpriteBatch batch = new SpriteBatch();
    float elapsed;

    private final Game game;

    public MenuScreen(final Game game) {
        this.game = game;
        viewport = new FitViewport(MegaMan.V_WIDTH, MegaMan.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((MegaMan) game).batch);
        Gdx.input.setInputProcessor(stage);

        startBtn = new ImageButton(drawableStart, drawableStart);
        startBtn.setPosition(75, 300);
        startBtn.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen((MegaMan) game));
                dispose();
            }
        });

        stage.addActor(startBtn);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        elapsed += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(animation.getKeyFrame(elapsed), 0, 0);
        batch.end();
        stage.act();
        stage.draw();
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
        stage.dispose();
    }
}

