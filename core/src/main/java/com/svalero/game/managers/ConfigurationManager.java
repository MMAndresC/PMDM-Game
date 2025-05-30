package com.svalero.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import static com.svalero.game.constants.Constants.*;


public class ConfigurationManager {

    private static Preferences prefs;

    public static void loadPreferences() {
        prefs = Gdx.app.getPreferences(GAME_SETTINGS);
    }

    public static boolean isSoundEnabled() {
        return prefs.getBoolean(SOUND_ENABLED, true);
    }

    public static void setSoundEnabled(boolean enabled) {
        prefs.putBoolean(SOUND_ENABLED, enabled).flush();
    }

    public static float getMusicVolume() {
        return prefs.getFloat(MUSIC_VOLUME, 1.0f);
    }

    public static void setMusicVolume(float volume) {
        prefs.putFloat(MUSIC_VOLUME, volume).flush();
    }

}
