package com.svalero.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.svalero.game.MyGame;
import com.svalero.game.managers.R;

import java.io.File;

import static com.svalero.game.constants.Constants.MENU_SKIN;
import static com.svalero.game.constants.Constants.UI;

public class SplashScreen implements Screen {

    private Stage stage;

    private final MyGame game;

    private Skin skin;

    private ProgressBar progressBar;

    //TODO sin hacer esquema muy básico para que el assetManager cargue bien
    public SplashScreen(MyGame game) {
        this.game = game;
        skin = new Skin();
    }

    @Override
    public void show() {
        // Crear Stage
        stage = new Stage(new ScreenViewport());

        // Crear Skin sencilla o reutilizar tu skin
        skin = new Skin(Gdx.files.internal(UI + File.separator + MENU_SKIN));

        // Crear ProgressBar

        progressBar = new ProgressBar(0f, 1f, 0.01f, false, skin, "fancy");
        progressBar.setAnimateDuration(0.25f);
        progressBar.setValue(0);

        // Tabla para centrar
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(progressBar).width(400).height(40);

        stage.addActor(table);

        // Iniciar carga de recursos
        R.loadAllResources();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualizar AssetManager
        if (R.update()) {
            // Small delay to ensure everything is properly loaded
            if (progressBar.getValue() >= 1f) {
                game.setScreen(new MainMenuScreen(game));
                return;
            } else {
                progressBar.setValue(1f); // Force complete the progress bar
            }
        } else {
            // Mostrar progreso
            progressBar.setValue(R.getProgress());
        }

        stage.act(dt);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

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
        skin.dispose();
    }
}
