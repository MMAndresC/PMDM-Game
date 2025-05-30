package com.svalero.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.svalero.game.MyGame;
import com.svalero.game.managers.ConfigurationManager;
import com.svalero.game.managers.GamepadManager;
import com.svalero.game.managers.R;
import lombok.Data;

import static com.svalero.game.constants.Constants.*;

@Data
public class GameOverScreen implements Screen {

    private MyGame game;

    private float score;

    private Stage stage;

    private Skin skin;

    private TextureRegion background;

    private TextButton[] menuButtons;
    private int selectedIndex = 0;

    public GameOverScreen(MyGame game, float score) {
        this.game = game;
        this.score = score;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.skin = game.getSkin();
        this.background = R.getUITexture(WINDOW);
        game.getMusicManager().play(GAME_OVER_MUSIC, true, ConfigurationManager.getMusicVolume());
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(0, 0, 0, 1);

        //Show mouse if not gamepad connected
        if(!game.getGamepadManager().isControllerConnected())
            Gdx.input.setCursorCatched(false);

        // Create content table
        Table table = createContentTable();

        // Background table
        Image backgroundImage = new Image(background);
        backgroundImage.setScaling(Scaling.stretch);
        backgroundImage.setSize(table.getWidth(), table.getHeight());

        // Load background in container table
        Table container = new Table();
        container.setFillParent(true);
        container.center();
        container.add(backgroundImage).width(table.getWidth()).height(table.getHeight());

        // Load table content
        stage.addActor(container);
        stage.addActor(table);
    }

    @Override
    public void render(float dt) {
        //Clean screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(dt);
        handleControllerInput(dt);
        stage.draw();
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
        skin.dispose();
    }

    public Table createContentTable(){
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.pad(PADDING_GAME_OVER_TABLE);

        // 60% width, 100% height
        table.setWidth(Gdx.graphics.getWidth() * 0.6f);
        table.setHeight(Gdx.graphics.getHeight());

        Label gameOverLabel = new Label("GAME OVER", skin, "title");
        gameOverLabel.setAlignment(Align.center);

        Label scoreLabel = new Label("SCORE", skin);
        String formatted = String.format("%06.0f", score);
        Label scoreValue = new Label(formatted, skin, "title");

        TextButton newGameBtn = new TextButton("New Game", skin);
        TextButton menuBtn = new TextButton("Return to Main Menu", skin);
        TextButton exitBtn = new TextButton("Close Game", skin);

        // Listeners
        newGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //Add in array to control option selected with gamepad
        menuButtons = new TextButton[] { newGameBtn, menuBtn, exitBtn };
        //First button selected
        menuButtons[selectedIndex].setColor(0.6f, 0.4f, 0.8f, 1f);

        table.add(gameOverLabel).padBottom(PADDING_GAME_OVER_TITLE).row();
        table.add(scoreLabel).padBottom(PADDING_GAME_OVER_SCORE).row();
        table.add(scoreValue).padBottom(PADDING_GAME_OVER_SCORE_VALUE).row();
        table.add(newGameBtn).padBottom(PADDING_BUTTON).width(WIDTH_BUTTON_GAME_OVER).height(HEIGHT_BUTTON_GAME_OVER).row();
        table.add(menuBtn).padBottom(PADDING_BUTTON).width(WIDTH_BUTTON_GAME_OVER).height(HEIGHT_BUTTON_GAME_OVER).row();
        table.add(exitBtn).width(WIDTH_BUTTON_GAME_OVER).height(HEIGHT_BUTTON_GAME_OVER).row();
        return table;
    }

    private void handleControllerInput(float dt){
        game.getGamepadManager().update(dt);
        GamepadManager gamepadManager = game.getGamepadManager();

        if (!gamepadManager.isControllerConnected()) return;

        float yAxis = gamepadManager.getAxisLeftY();

        // Default inverted axis, correct it
        if (gamepadManager.isReady()) {
            // Up stick or up pad code
            if ((yAxis < -0.5f || gamepadManager.isButtonPressed(11))
                && selectedIndex > 0
            ) {
                updateSelectedButton(selectedIndex - 1);
                gamepadManager.setCooldown(INPUT_DELAY);
                // Down stick or down pad code
            } else if ((yAxis > 0.5f || gamepadManager.isButtonPressed(12))
                && selectedIndex < menuButtons.length - 1
            ) {
                updateSelectedButton(selectedIndex + 1);
                gamepadManager.setCooldown(INPUT_DELAY);
            }
        }

        // Button X confirm option
        if (gamepadManager.isReady() && gamepadManager.isButtonPressed(0)) {
            gamepadManager.setCooldown(INPUT_DELAY); // Activating cooldown

            switch (selectedIndex) {
                case 0 -> game.setScreen(new GameScreen(game));
                case 1 -> game.setScreen(new MainMenuScreen(game));
                case 2 -> Gdx.app.exit();
            }
        }
    }

    private void updateSelectedButton(int newIndex) {
        menuButtons[selectedIndex].setColor(1, 1, 1, 1); //Not selected color
        selectedIndex = newIndex;
        menuButtons[selectedIndex].setColor(0.6f, 0.4f, 0.8f, 1f); // Selected color
    }
}
