package com.svalero.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.svalero.game.MyGame;
import com.svalero.game.managers.LogicManager;
import com.svalero.game.managers.RenderManager;

public class GameScreen implements Screen {

    private final MyGame game;

    private RenderManager renderManager;

    private LogicManager logicManager;

    private Vector2 rangerPosition;

    public GameScreen(MyGame game) {
        this.game = game;
        rangerPosition = new Vector2();
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
        if(logicManager == null) return;
        if(logicManager.getRanger() == null) return;
        if(logicManager.getRanger().getPosition() == null) return;

        //Resize changes position of ranger, when come back of pause, reposition it
        if (height == 0) {
            rangerPosition = new Vector2(logicManager.getRanger().getPosition());
            game.setScreen(new PauseScreen(game, this));

        } else{
            Vector2 zeroPosition = new Vector2(logicManager.getRanger().getRangerWidth(), 0);
            if(!logicManager.getRanger().getPosition().equals(zeroPosition)) return;
            logicManager.getRanger().setPosition(rangerPosition);
        }

    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
