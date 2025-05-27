package com.svalero.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
import com.svalero.game.managers.R;

import java.io.File;

import static com.svalero.game.constants.Constants.*;

public class SettingsScreen implements Screen {

    private MyGame game;

    private Stage stage;

    private Skin skin;

    private TextureRegion background;

    public SettingsScreen(MyGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal(UI + File.separator + MENU_SKIN));
        this.background = R.getUITexture(WINDOW);
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(0, 0, 0, 1);

        Preferences prefs = Gdx.app.getPreferences("game_settings");
        boolean soundEnabled = prefs.getBoolean("sounds_enabled", true);
        float musicVolume = prefs.getFloat("music_volume", 1f);

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
        Label musicLabel = new Label("Sound VFX", skin);

        Table musicRow = new Table();
        musicRow.add(soundCheckbox).padRight(PADDING_CHECKBOX).padTop(PADDING_CHECKBOX).center();
        musicRow.add(musicLabel).center();
        musicRow.align(Align.center);

        // Volume Slider
        int volume = Math.round(musicVolume * 100);
        Label volumeLabel = new Label("Volume: " + volume, skin);
        Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, skin, "fancy");
        volumeSlider.setValue(musicVolume);

        // Save preferences listener
        soundCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isChecked = soundCheckbox.isChecked();
                Gdx.app.getPreferences("game_settings").putBoolean("sound_enabled", isChecked).flush();
            }
        });

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float currentValue = volumeSlider.getValue();
                int volume = Math.round(currentValue * 100);
                volumeLabel.setText("Volume: " + volume);
                Gdx.app.getPreferences("game_settings").putFloat("music_volume", currentValue).flush();
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

}
