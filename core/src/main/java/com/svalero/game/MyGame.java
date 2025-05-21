package com.svalero.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.svalero.game.screen.MainMenuScreen;
import com.svalero.game.screen.SplashScreen;

import java.io.File;

import static com.svalero.game.constants.Constants.*;


public class MyGame extends Game {

    private Skin skin;

    @Override
    public void create() {
        ((Game) Gdx.app.getApplicationListener()).setScreen(new SplashScreen(this));
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
            skin = new Skin(Gdx.files.internal(UI + File.separator + MENU_SKIN));
        }
        return skin;
    }
}
