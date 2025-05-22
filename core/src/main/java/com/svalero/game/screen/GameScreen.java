package com.svalero.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.svalero.game.MyGame;
import com.svalero.game.managers.LogicManager;
import com.svalero.game.managers.R;
import com.svalero.game.managers.RenderManager;

import java.io.File;

import static com.svalero.game.constants.Constants.*;

public class GameScreen implements Screen {

    private final MyGame game;

    private RenderManager renderManager;

    private LogicManager logicManager;

    private Texture background;


    public GameScreen(MyGame game) {
        this.game = game;
        loadManagers(game);
    }

    private void loadManagers(MyGame game) {
        logicManager = new LogicManager(game);
        renderManager = new RenderManager(logicManager);
    }

    @Override
    public void show() {
        //TODO ver como paso ese background, cambiara con el level, AQUÍ NO ESTA BIEN
        background = new Texture(Gdx.files.internal(BACKGROUNDS + File.separator + LEVEL_1_BACKGROUND));
    }

    @Override
    public void render(float dt) {
        logicManager.update(dt);
        renderManager.render(dt, background);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        R.dispose();
        background.dispose();
    }
}
