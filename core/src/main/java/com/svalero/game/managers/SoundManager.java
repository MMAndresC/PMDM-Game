package com.svalero.game.managers;

import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    public static void play(String fileName, float volume) {
        if(R.isLoadedSound(fileName) && ConfigurationManager.isSoundEnabled()) {
            Sound sound = R.getSound(fileName);
            sound.play(volume);
        }
    }
}
