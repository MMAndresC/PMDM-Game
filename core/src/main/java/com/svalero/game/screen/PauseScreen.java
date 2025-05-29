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
import com.svalero.game.managers.R;
import lombok.Data;

import java.io.File;

import static com.svalero.game.constants.Constants.*;
import static com.svalero.game.constants.Constants.HEIGHT_BUTTON_GAME_OVER;

@Data
public class PauseScreen implements Screen {

    private MyGame game;

    private GameScreen gameScreen;

    private Stage stage;

    private Skin skin;

    private TextureRegion background;

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
        Label musicLabel = new Label("Sound SFX", skin);

        Table musicRow = new Table();
        musicRow.add(soundCheckbox).padRight(PADDING_CHECKBOX).padTop(PADDING_CHECKBOX).center();
        musicRow.add(musicLabel).center();
        musicRow.align(Align.center);

        // Volume Slider
        int volume = Math.round(musicVolume * 100);
        Label volumeLabel = new Label("Volume: " + volume, skin);
        Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, skin, "fancy");
        volumeSlider.setValue(musicVolume);

        TextButton resumeBtn = new TextButton("Resume", skin);
        TextButton menuBtn = new TextButton("Return to Main Menu", skin);
        TextButton exitBtn = new TextButton("Exit", skin);

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
}
