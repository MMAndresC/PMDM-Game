package com.svalero.game.screen;

import com.badlogic.gdx.Screen;
import com.svalero.game.MyGame;
import com.svalero.game.managers.LogicManager;
import com.svalero.game.managers.RenderManager;

public class GameScreen implements Screen {

    private final MyGame game;

    private RenderManager renderManager;

    private LogicManager logicManager;



    public GameScreen(MyGame game) {
        this.game = game;
        loadManagers(game);
    }

    private void loadManagers(MyGame game) {
        logicManager = new LogicManager(game, this);
        renderManager = new RenderManager(logicManager);
    }

    @Override
    public void show() {
        if(logicManager.getFreezeTime() > 0)
            logicManager.adjustFreezeTime();
    }

    @Override
    public void render(float dt) {
        logicManager.update(dt);
        renderManager.render(dt);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {

    }
}
