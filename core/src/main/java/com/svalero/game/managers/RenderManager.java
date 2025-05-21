package com.svalero.game.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderManager {

    private LogicManager logicManager;

    private SpriteBatch batch;

    public RenderManager(LogicManager logicManager) {
        this.logicManager = logicManager;
        this.batch = new SpriteBatch();
    }

    public void render() {

    }
}
