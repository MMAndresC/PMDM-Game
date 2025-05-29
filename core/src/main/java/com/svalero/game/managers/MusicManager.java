package com.svalero.game.managers;

import com.badlogic.gdx.audio.Music;

public class MusicManager {

    private Music currentMusic;

    public void play(String fileName, boolean looping, float volume) {
        System.out.println("Playing music " + fileName);
        if(!R.isLoadedMusic(fileName)) return;
        Music music = R.getMusic(fileName);
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
        }
        currentMusic = music;
        currentMusic.setLooping(looping);
        currentMusic.setVolume(volume);
        currentMusic.play();
    }

    public void stop() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    public void dispose() {
        if (currentMusic != null) {
            currentMusic.dispose();
        }
    }

    public void setVolume(float volume) {
        if (currentMusic != null) {
            currentMusic.setVolume(volume);
        }
    }

    public boolean isPlaying() {
        return currentMusic != null && currentMusic.isPlaying();
    }
}
