package com.svalero.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.svalero.game.MyGame;

import java.io.File;

import static com.svalero.game.constants.Constants.*;

public class MainMenuScreen implements Screen {

    private Stage stage = null;

    private final MyGame game;

    private final Texture background;

    private final SpriteBatch batch;

    private float bgX;

    public MainMenuScreen(MyGame game) {
        this.game = game;
        batch = new SpriteBatch();
        this.background = new Texture(Gdx.files.internal(BACKGROUNDS + File.separator + MENU_BACKGROUND));
        // Initialize stage in constructor
        stage = new Stage(new ScreenViewport());
        loadStage();
    }

    private void loadStage() {
        Table table = createOptionsTable();
        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        //Clean screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Set background
        batch.begin();
        //batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        drawBackground(dt);
        batch.end();

        if (stage != null) {
            stage.act(dt);
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
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
        if (stage != null) stage.dispose();
        if (batch != null) batch.dispose();
        if (background != null) background.dispose();
    }

    private Table createOptionsTable() {
        Skin skin = game.getSkin();
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Title
        Label title = new Label("Space Defenders", skin, "title");
        title.setFontScale(1.2f);

        // Buttons
        TextButton playBtn = new TextButton("Play", skin);
        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new GameScreen(game));
            }
        });

        TextButton settingsBtn = new TextButton("Settings", skin);
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });

        TextButton exitBtn = new TextButton("Exit", skin);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add(title).padBottom(PADDING_TITLE).row();
        table.add(playBtn).width(WIDTH_BUTTON).height(HEIGHT_BUTTON).padBottom(PADDING_BUTTON).row();
        table.add(settingsBtn).width(WIDTH_BUTTON).height(HEIGHT_BUTTON).padBottom(PADDING_BUTTON).row();
        table.add(exitBtn).width(WIDTH_BUTTON).height(HEIGHT_BUTTON).row();

        return table;
    }

    private void updateBackground(float dt, float bgWidth) {
        bgX -= MENU_BACKGROUND_SPEED * dt;
        if (bgX <= -bgWidth) {
            bgX = 0;
        }
    }

    private void drawBackground(float dt) {
        updateBackground(dt, background.getWidth());
        batch.draw(background, bgX, 0,  background.getWidth(), Gdx.graphics.getHeight());
        batch.draw(background, bgX + background.getWidth(), 0, background.getWidth(), Gdx.graphics.getHeight());
    }
}
