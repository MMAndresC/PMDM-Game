package com.svalero.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.svalero.game.MyGame;
import com.svalero.game.managers.ConfigurationManager;
import com.svalero.game.managers.GamepadManager;
import com.svalero.game.managers.R;
import lombok.Data;

import static com.svalero.game.constants.Constants.*;
import static com.svalero.game.constants.Constants.HEIGHT_BUTTON_GAME_OVER;

@Data
public class PauseScreen implements Screen {

    private MyGame game;

    private GameScreen gameScreen;

    private Stage stage;

    private Skin skin;

    private TextureRegion background;

    private int selectedIndex = 0;
    private Actor[] menuElements;

    private Label soundLabel;

    private Label volumeLabel;

    public PauseScreen(MyGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.skin = game.getSkin();
        this.background = R.getUITexture(WINDOW);
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(0, 0, 0, 1);

        //Show mouse if not gamepad connected
        if(!game.getGamepadManager().isControllerConnected())
            Gdx.input.setCursorCatched(false);

        boolean soundEnabled = ConfigurationManager.isSoundEnabled();
        float musicVolume = ConfigurationManager.getMusicVolume();

        // Create content table
        Table table = createContentTable(soundEnabled, musicVolume);

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
        if (stage != null) stage.dispose();
        skin.dispose();
    }

    public Table createContentTable(boolean soundEnabled, float musicVolume){
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.pad(PADDING_GAME_OVER_TABLE);

        // 60% width, 100% height
        float tableWidth = Gdx.graphics.getWidth() * SCALE_TABLE;
        float tableHeight = Gdx.graphics.getHeight();

        table.setSize(tableWidth, tableHeight);

        TextureRegion pauseHeaderRegion = R.getUITexture(PAUSE);
        Image pauseHeader = new Image(new TextureRegionDrawable(pauseHeaderRegion));
        pauseHeader.setScaling(Scaling.fit);

        float headerWidth = tableWidth * 0.4f;
        float headerHeight = tableHeight * 0.15f;

        Label settingLabel = new Label("SETTING", skin, "title");
        settingLabel.setAlignment(Align.left);

        // Music Checkbox
        CheckBox soundCheckbox = new CheckBox("", skin, "default");
        soundCheckbox.setChecked(soundEnabled);
        soundCheckbox.setTransform(true);
        soundCheckbox.setScale(SCALE_CHECKBOX);
        soundLabel = new Label("Sound SFX", skin);

        Table musicRow = new Table();
        musicRow.add(soundCheckbox).padRight(PADDING_CHECKBOX).padTop(PADDING_CHECKBOX).center();
        musicRow.add(soundLabel).center();
        musicRow.align(Align.center);

        // Volume Slider
        int volume = Math.round(musicVolume * 100);
        volumeLabel = new Label("Volume: " + volume, skin);
        Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, skin, "fancy");
        volumeSlider.setValue(musicVolume);

        TextButton resumeBtn = new TextButton("Resume", skin);
        TextButton menuBtn = new TextButton("Quit game", skin);
        TextButton exitBtn = new TextButton("Close Game", skin);

        // Save preferences listener
        soundCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isChecked = soundCheckbox.isChecked();
                ConfigurationManager.setSoundEnabled(isChecked);
            }
        });

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = volumeSlider.getValue();
                int volumePerCent = Math.round(volume * 100);
                volumeLabel.setText("Volume: " + volumePerCent);
                ConfigurationManager.setMusicVolume(volume);
                game.getMusicManager().setVolume(volume);
            }
        });

        //Button listeners
        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(gameScreen);
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

        //Add components to control with gamepad
        menuElements = new Actor[]{soundCheckbox, volumeSlider, resumeBtn, menuBtn, exitBtn};
        if(game.getGamepadManager().isControllerConnected())
            soundLabel.setColor(0.6f, 0.4f, 0.8f, 1f);

        table.add(pauseHeader).width(headerWidth).height(headerHeight)
            .padBottom(PADDING_BUTTON).center().row();
        table.add(settingLabel).center().padBottom(PADDING_SETTING_LABEL).row();
        table.add(musicRow).center().padBottom(PADDING_BUTTON).row();
        table.add(volumeLabel).padBottom(PADDING_BUTTON).center().row();
        table.add(volumeSlider).width(WIDTH_SLIDER).height(HEIGHT_SLIDER).padBottom(PADDING_BUTTON).row();
        table.add(resumeBtn).width(WIDTH_BUTTON_GAME_OVER).height(HEIGHT_BUTTON_GAME_OVER).padBottom(PADDING_BUTTON).row();
        table.add(menuBtn).width(WIDTH_BUTTON_GAME_OVER).height(HEIGHT_BUTTON_GAME_OVER).padBottom(PADDING_BUTTON).row();
        table.add(exitBtn).width(WIDTH_BUTTON_GAME_OVER).height(HEIGHT_BUTTON_GAME_OVER).row();

        return table;
    }

    private void handleControllerInput(float dt){
        GamepadManager gamepadManager = game.getGamepadManager();

        if (!gamepadManager.isControllerConnected()) return;

        gamepadManager.update(dt);

        float yAxis = gamepadManager.getAxisLeftY();
        float xAxis = gamepadManager.getAxisLeftX();

        //Vertical navigation between elements
        if (gamepadManager.isReady()) {
            if ((yAxis < -0.5f || gamepadManager.isButtonPressed(UP_PAD))
                && selectedIndex > 0
            ) {
                updateSelectedElement(selectedIndex - 1);
                gamepadManager.setCooldown(INPUT_DELAY);
            } else if ((yAxis > 0.5f || gamepadManager.isButtonPressed(DOWN_PAD))
                && selectedIndex < menuElements.length - 1
            ) {
                updateSelectedElement(selectedIndex + 1);
                gamepadManager.setCooldown(INPUT_DELAY);
            }
        }

        // Save selected element
        Actor current = menuElements[selectedIndex];

        // Press X in checkbox & button
        if (gamepadManager.isReady() && gamepadManager.isButtonPressed(X_BUTTON)) {
            gamepadManager.setCooldown(INPUT_DELAY); //Activating cooldown

            if (current instanceof CheckBox checkBox) {
                checkBox.toggle();
                ConfigurationManager.setSoundEnabled(checkBox.isChecked());
            } else if (current instanceof TextButton) {
                switch(selectedIndex){
                    case 2 -> game.setScreen(gameScreen);
                    case 3 ->  game.setScreen(new MainMenuScreen(game));
                    case 4 ->  Gdx.app.exit();
                }
            }
        }

        // Control slider with horizontal joystick
        if (current instanceof Slider slider) {
            float newValue = slider.getValue() + xAxis * dt; // sensibility by time
            newValue = Math.max(slider.getMinValue(), Math.min(slider.getMaxValue(), newValue));
            slider.setValue(newValue);

            int volumePerCent = Math.round(newValue * 100);
            volumeLabel.setText("Volume: " + volumePerCent);
            ConfigurationManager.setMusicVolume(newValue);
            game.getMusicManager().setVolume(newValue);
        }
    }

    private void updateSelectedElement(int newIndex) {
        //Checkbox change label color
        if(selectedIndex == 0)
            soundLabel.setColor(1, 1, 1, 1);
        else if(selectedIndex == 1){
            volumeLabel.setColor(1, 1, 1, 1);
            menuElements[selectedIndex].setColor(1, 1, 1, 1);
        }else
            menuElements[selectedIndex].setColor(1, 1, 1, 1); // Restore

        selectedIndex = newIndex;

        if(selectedIndex == 0)
            soundLabel.setColor(0.6f, 0.4f, 0.8f, 1f);
        else if(selectedIndex == 1){
            volumeLabel.setColor(0.6f, 0.4f, 0.8f, 1f);
            menuElements[selectedIndex].setColor(0.6f, 0.4f, 0.8f, 1f);
        }else
            menuElements[selectedIndex].setColor(0.6f, 0.4f, 0.8f, 1f); // Highlight
    }

}
