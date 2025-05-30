package com.svalero.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.svalero.game.levels.Level;

import java.io.File;

import static com.svalero.game.constants.Constants.LEVELS;


public class LevelManager {

    public Level getCurrentLevel(int numberLevel){
        String level = "level" + numberLevel + ".json";
        FileHandle file = Gdx.files.internal(LEVELS + File.separator + level);
        if(file.exists()){
            String jsonString = file.readString();
            Json json = new Json();
            try {
                return json.fromJson(Level.class, jsonString);
            } catch (Exception e) {
                Gdx.app.error("LevelManager", "Error al leer nivel: ", e);
            }

        }
        return null;
    }
}
