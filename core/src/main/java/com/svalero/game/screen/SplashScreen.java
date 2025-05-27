package com.svalero.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.svalero.game.MyGame;
import com.svalero.game.managers.R;

import java.io.File;

import static com.svalero.game.constants.Constants.*;

public class SplashScreen implements Screen {

    private Stage stage;

    private final MyGame game;

    private Skin skin;

    private ProgressBar progressBar;

    private float controlTime;

    private final Texture background;

    private final SpriteBatch batch;

    private Label loadingLabel;
    private float ellipsisTime = 0f;
    private int dotCount = 0;


    public SplashScreen(MyGame game) {
        this.game = game;
        skin = new Skin();
        batch = new SpriteBatch();
        controlTime = 0f;
        this.background = new Texture(Gdx.files.internal(BACKGROUNDS + File.separator + SPLASH_SCREEN_BACKGROUND));
    }

    @Override
    public void show() {
        controlTime = TimeUtils.nanoTime() / 1_000_000_000f;

        stage = new Stage(new ScreenViewport());

        skin = new Skin(Gdx.files.internal(UI + File.separator + MENU_SKIN));

        Table table = createTable();

        stage.addActor(table);

        // Load assets
        R.loadAllResources();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update AssetManager
        if (R.update()) {
            // Small delay
            float currentTime = TimeUtils.nanoTime() / 1_000_000_000f;
            progressBar.setValue(1f); // Force complete the progress bar
            if (currentTime > controlTime + DELAY_SPLASH_SCREEN) {
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        } else {
            // Update progress bar
            progressBar.setValue(R.getProgress());
        }

        //Label animation
        ellipsisTime += dt;
        if (ellipsisTime >= 0.5f) {
            dotCount = (dotCount + 1) % 4;
            String dots = ".".repeat(dotCount);
            loadingLabel.setText("Starting" + dots);
            ellipsisTime = 0f;
        }

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

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
        stage.dispose();
        skin.dispose();
    }

    public Table createTable(){

        Table table = new Table();
        progressBar = new ProgressBar(0f, 1f, 0.01f, false, skin, "fancy");
        progressBar.setAnimateDuration(0.25f);
        progressBar.setValue(0);

        loadingLabel = new Label("Starting...", skin);

        table.setFillParent(true);
        table.center();
        table.add(loadingLabel).padBottom(10f).align(Align.left).row();
        table.add(progressBar).width(WIDTH_PROGRESS_BAR).height(HEIGHT_PROGRESS_BAR).row();

        return table;
    }
}
