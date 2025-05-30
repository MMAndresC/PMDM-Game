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

import static com.svalero.game.constants.Constants.*;

public class SettingsScreen implements Screen {

    private final MyGame game;

    private final Stage stage;

    private final Skin skin;

    private final TextureRegion background;

    private Label soundLabel;

    private Label volumeLabel;

    //Gamepad
    private int selectedIndex = 0;
    private Actor[] menuElements;

    public SettingsScreen(MyGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.skin = game.getSkin();
        this.background = R.getUITexture(WINDOW);
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(0, 0, 0, 1);

        //Show mouse if not controller connected
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
    public void resize(int i, int i1) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        skin.dispose();
    }

    public Table createContentTable(boolean soundEnabled, float musicVolume) {
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.pad(PADDING_GAME_OVER_TABLE);

        // 60% width, 100% height
        float tableWidth = Gdx.graphics.getWidth() * SCALE_TABLE;
        float tableHeight = Gdx.graphics.getHeight();
        table.setSize(tableWidth, tableHeight);

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

        // Divider
        Image divider1 = new Image(new TextureRegionDrawable(R.getUITexture(DIVIDER)));
        divider1.setWidth(tableWidth);
        divider1.setHeight(HEIGHT_DIVIDER);

        Label controlsLabel = new Label("CONTROLS", skin, "title");
        controlsLabel.setAlignment(Align.left);

        // Control mappings
        Table controlMove = createControlRow(KEYS_MOVE, "Move");
        Table controlShoot = createControlRow(KEYS_SHOOT, "Shoot");
        Table controlPause = createControlRow(KEYS_PAUSE, "Pause");

        Image divider2 = new Image(new TextureRegionDrawable(R.getUITexture(DIVIDER)));
        divider2.setWidth(tableWidth);
        divider2.setHeight(HEIGHT_DIVIDER);

        // Back Button
        TextButton backBtn = new TextButton("Back", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        //Add components to control with gamepad
        menuElements = new Actor[]{soundCheckbox, volumeSlider, backBtn};
        soundLabel.setColor(0.6f, 0.4f, 0.8f, 1f);

        // Build layout
        table.add(settingLabel).center().padBottom(PADDING_SETTING_LABEL).row();
        table.add(musicRow).center().padBottom(PADDING_BUTTON).row();
        table.add(volumeLabel).padBottom(PADDING_BUTTON).center().row();
        table.add(volumeSlider).width(WIDTH_SLIDER).height(HEIGHT_SLIDER).center().padBottom(PADDING_BUTTON).row();

        table.add(divider1).width(Gdx.graphics.getWidth() * WIDTH_SCALE_DIVIDER).height(HEIGHT_DIVIDER).center().padBottom(PADDING_BUTTON).row();
        table.add(controlsLabel).padBottom(PADDING_BUTTON).row();
        table.add(controlMove).center().padBottom(PADDING_BUTTON).row();
        table.add(controlShoot).center().padBottom(PADDING_BUTTON).row();
        table.add(controlPause).center().padBottom(PADDING_BUTTON).row();
        table.add(divider2).width(Gdx.graphics.getWidth() * WIDTH_SCALE_DIVIDER).height(HEIGHT_DIVIDER).center().padBottom(PADDING_BUTTON * 2).row();

        table.add(backBtn).width(WIDTH_BUTTON_GAME_OVER).height(HEIGHT_BUTTON_GAME_OVER).padBottom(PADDING_BUTTON).row();

        return table;
    }

    private Table createControlRow(String textureName, String labelText) {
        Table row = new Table();

        Image keyImage = new Image(new TextureRegionDrawable(R.getUITexture(textureName)));
        keyImage.setScaling(Scaling.fit);

        Container<Image> imageContainer = new Container<>(keyImage);
        imageContainer.size(WIDTH_KEYS, HEIGHT_KEYS);
        imageContainer.fill();

        if (!labelText.equals(KEYS_MOVE)) {
            keyImage.setScaling(Scaling.fit);
        }

        row.add(imageContainer).padRight(PADDING_IMAGE).center();

        Label actionLabel = new Label(labelText, skin);
        row.add(actionLabel).center().left();

        row.align(Align.left);
        return row;
    }

    private void handleControllerInput(float dt){
        GamepadManager gamepadManager = game.getGamepadManager();
        game.getGamepadManager().update(dt);

        if (!gamepadManager.isControllerConnected()) return;

        float yAxis = gamepadManager.getAxisLeftY();
        float xAxis = gamepadManager.getAxisLeftX();

        //Vertical navigation between elements
        if (gamepadManager.isReady()) {
            if ((yAxis < -0.5f || gamepadManager.isButtonPressed(11))
                && selectedIndex > 0
            ) {
                updateSelectedElement(selectedIndex - 1);
                gamepadManager.setCooldown(INPUT_DELAY);
            } else if ((yAxis > 0.5f || gamepadManager.isButtonPressed(12))
                && selectedIndex < menuElements.length - 1
            ) {
                updateSelectedElement(selectedIndex + 1);
                gamepadManager.setCooldown(INPUT_DELAY);
            }
        }

        // Save selected element
        Actor current = menuElements[selectedIndex];

        // Press X in checkbox & button
        if (gamepadManager.isReady() && gamepadManager.isButtonPressed(0)) {
            gamepadManager.setCooldown(INPUT_DELAY); //Activating cooldown

            if (current instanceof CheckBox checkBox) {
                checkBox.toggle();
                ConfigurationManager.setSoundEnabled(checkBox.isChecked());
            } else if (current instanceof TextButton) {
                game.setScreen(new MainMenuScreen(game));
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
