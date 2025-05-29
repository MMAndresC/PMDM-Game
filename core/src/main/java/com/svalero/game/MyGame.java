package com.svalero.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.svalero.game.managers.MusicManager;
import com.svalero.game.managers.SoundManager;
import com.svalero.game.screen.SplashScreen;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;

import static com.svalero.game.constants.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class MyGame extends Game {

    private Skin skin;

    private MusicManager musicManager;


    @Override
    public void create() {
        musicManager = new MusicManager();
        setScreen(new SplashScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        if (musicManager != null) {
            musicManager.dispose();
        }

        super.dispose();
    }

    public Skin getSkin() {
        if(skin == null ) {
            skin = new Skin(Gdx.files.internal(UI + File.separator + MENU_SKIN));
        }
        return skin;
    }
}
