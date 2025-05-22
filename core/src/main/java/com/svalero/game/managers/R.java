package com.svalero.game.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.svalero.game.utils.AnimationInfo;

import java.io.File;

import static com.svalero.game.constants.Constants.*;


public class R {

    private static final AssetManager assetManager = new AssetManager();

    private static final String RANGER_ATLAS_PATH = RANGER + File.separator + RANGER_ATLAS;
    private static final String ENEMIES_ATLAS_PATH = ENEMIES + File.separator + ENEMIES_ATLAS;

    public static void loadAllResources() {
        assetManager.load(RANGER_ATLAS_PATH, TextureAtlas.class);
        assetManager.load(ENEMIES_ATLAS_PATH, TextureAtlas.class);
    }

    public static TextureRegion getRangerTexture(String name) {
        return assetManager.get(RANGER_ATLAS_PATH, TextureAtlas.class).findRegion(name);
    }

    public static Array<TextureRegion> getRangerRegions(AnimationInfo animationInfo) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i <= animationInfo.getTotalFrames(); i++) {
            TextureRegion frame = R.getRangerTexture(animationInfo.getName() + i);
            if (frame == null) throw new RuntimeException("Error loading " + animationInfo.getName() + " frames");
            frames.add(frame);
        }
        return frames;
    }

    public static TextureRegion getEnemyTexture(String name){
        return assetManager.get(ENEMIES_ATLAS_PATH, TextureAtlas.class).findRegion(name);
    }

    public static Array<TextureRegion> getEnemiesRegions(AnimationInfo animationInfo) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i <= animationInfo.getTotalFrames(); i++) {
            TextureRegion frame = R.getEnemyTexture(animationInfo.getName() + i);
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
