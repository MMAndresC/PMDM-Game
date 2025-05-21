package com.svalero.game.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.svalero.game.utils.AnimationInfo;

import java.io.File;

import static com.svalero.game.constants.Constants.PLAYER;
import static com.svalero.game.constants.Constants.PLAYER_ATLAS;

public class R {

    private static final AssetManager assetManager = new AssetManager();

    private static final String PLAYER_ATLAS_PATH = PLAYER + File.separator + PLAYER_ATLAS;


    public static void loadAllResources() {
        assetManager.load(PLAYER_ATLAS_PATH, TextureAtlas.class);
    }

    public static TextureRegion getPlayerTexture(String name) {
        return assetManager.get(PLAYER_ATLAS_PATH, TextureAtlas.class).findRegion(name);
    }

    public static Array<TextureRegion> getPlayerRegions(AnimationInfo animationInfo) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i <= animationInfo.getTotalFrames(); i++) {
            TextureRegion frame = R.getPlayerTexture(animationInfo.getName() + i);
            if (frame == null) throw new RuntimeException("Error loading " + animationInfo.getName() + " frames");
            frames.add(frame);
        }
        return frames;
    }

    public static void dispose() {
        assetManager.dispose();
    }

    public static boolean update() {
        return assetManager.update();
    }

    public static float getProgress() {
        return assetManager.getProgress();
    }
}
