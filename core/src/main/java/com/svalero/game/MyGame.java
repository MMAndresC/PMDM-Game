package com.svalero.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.svalero.game.screen.MainMenuScreen;


public class MyGame extends Game {

    private Skin skin;

    @Override
    public void create() {
        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public Skin getSkin() {
        if(skin == null ) {
            skin = new Skin(Gdx.files.internal("ui/skins/uiskin.json"));
        }
        return skin;
    }
}
