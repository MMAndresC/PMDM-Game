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
import com.svalero.game.managers.ConfigurationManager;
import com.svalero.game.managers.GamepadManager;

import java.io.File;

import static com.svalero.game.constants.Constants.*;

public class MainMenuScreen implements Screen {

    private final Stage stage;

    private final MyGame game;

    private final Texture background;

    private final SpriteBatch batch;

    private float bgX;

    private TextButton[] menuButtons;
    private int selectedIndex = 0;


    public MainMenuScreen(MyGame game) {
        this.game = game;
        batch = new SpriteBatch();
        this.background = new Texture(Gdx.files.internal(BACKGROUNDS + File.separator + MENU_BACKGROUND));
        // Initialize stage in constructor
        stage = new Stage(new ScreenViewport());
        Table table = createOptionsTable();
        stage.addActor(table);
        //Global load
        ConfigurationManager.loadPreferences();
        //Play intro music
        game.getMusicManager().play(INTRO_MUSIC, true, ConfigurationManager.getMusicVolume());
    }


    @Override
    public void show() {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        Gdx.input.setInputProcessor(stage);

        //Show mouse if not gamepad connected
        if(!game.getGamepadManager().isControllerConnected())
            Gdx.input.setCursorCatched(false);
    }

    @Override
    public void render(float dt) {
        //Clean screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Set background
        batch.begin();
        drawBackground(dt);
        batch.end();

        if (stage != null) {
            stage.act(dt);
            handleControllerInput(dt);
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (batch != null) batch.dispose();
        if (background != null) background.dispose();
    }

    private void handleControllerInput(float dt){
        GamepadManager gamepadManager = game.getGamepadManager();

        if (!gamepadManager.isControllerConnected()) return;

        gamepadManager.update(dt);

        float yAxis = gamepadManager.getAxisLeftY();

        // Default inverted axis, correct it
        if (gamepadManager.isReady()) {
            // Up stick or up pad code
            if ((yAxis < -0.5f || gamepadManager.isButtonPressed(UP_PAD))
                && selectedIndex > 0
            ) {
                updateSelectedButton(selectedIndex - 1);
                gamepadManager.setCooldown(INPUT_DELAY);
                // Down stick or down pad code
            } else if ((yAxis > 0.5f || gamepadManager.isButtonPressed(DOWN_PAD))
                && selectedIndex < menuButtons.length - 1
            ) {
                updateSelectedButton(selectedIndex + 1);
                gamepadManager.setCooldown(INPUT_DELAY);
            }
        }

        // Button X confirm option
        if (gamepadManager.isReady() && gamepadManager.isButtonPressed(X_BUTTON)) {
            gamepadManager.setCooldown(INPUT_DELAY); // Activating cooldown

            switch (selectedIndex) {
                case 0 -> {
                    dispose();
                    game.setScreen(new GameScreen(game));
                }
                case 1 -> game.setScreen(new SettingsScreen(game));
                case 2 -> Gdx.app.exit();
            }
        }
    }

    private void updateSelectedButton(int newIndex) {
        menuButtons[selectedIndex].setColor(1, 1, 1, 1); //Not selected color
        selectedIndex = newIndex;
        menuButtons[selectedIndex].setColor(0.6f, 0.4f, 0.8f, 1f); // Selected color
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

        TextButton exitBtn = new TextButton("Close Game", skin);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //Add in array to control option selected with gamepad
        menuButtons = new TextButton[] { playBtn, settingsBtn, exitBtn };
        //First button selected
        menuButtons[selectedIndex].setColor(0.6f, 0.4f, 0.8f, 1f);

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

        float bgWidth = background.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // To loop image
        for (int i = 0; i < 3; i++) {
            float x = bgX + i * bgWidth;
            batch.draw(background, x, 0, bgWidth, screenHeight);
        }
    }
}
