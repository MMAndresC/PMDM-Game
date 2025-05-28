package com.svalero.game.screen;

import com.badlogic.gdx.Screen;
import com.svalero.game.MyGame;
import com.svalero.game.managers.LogicManager;
import com.svalero.game.managers.MusicManager;
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
        logicManager.setPaused(false);
        if(logicManager.getFreezeTime() > 0)
            logicManager.adjustFreezeTime();
    }

    @Override
    public void render(float dt) {
        if (logicManager.isPaused()) return;
        logicManager.update(dt);
        renderManager.render(dt);
    }

    @Override
    public void resize(int width, int height) {
        if(logicManager == null) return;
        if(logicManager.getRanger() == null) return;
        if(logicManager.getRanger().getPosition() == null) return;

        //Resize changes position of ranger, when come back of pause, reposition it
        if (height == 0) {
            logicManager.setPaused(true);
        }
    }

    @Override public void pause() {logicManager.setPaused(true);}
    @Override public void resume() {logicManager.setPaused(false);}
    @Override public void hide() {}
    @Override public void dispose() {}
}
