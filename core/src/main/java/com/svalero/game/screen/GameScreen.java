package com.svalero.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.game.MyGame;
import com.svalero.game.managers.R;

import java.io.File;

import static com.svalero.game.constants.Constants.*;

public class GameScreen implements Screen {

    private final MyGame game;

    private final SpriteBatch batch;

    private Texture background;
    private float bgY;

    private Vector2 playerPosition;

    private Animation<TextureRegion> engineEffectAnimation;
    private Animation<TextureRegion> engineEffectPoweringAnimation;
    private float animationTime = 0f;

    private TextureRegion body, engine, engineIdle;
    private boolean isMoving = false;

    public GameScreen(MyGame game) {
        this.game = game;
        batch = new SpriteBatch();
    }

    @Override
    public void show() {
        background = new Texture(Gdx.files.internal(BACKGROUNDS + File.separator + LEVEL_1_BACKGROUND));

        body = R.getPlayerTexture(HERO_FULL_HEALTH);
        engine = R.getPlayerTexture(HERO_ENGINE);

        Array<TextureRegion> frames = R.getPlayerRegions(HERO_ENGINE_EFFECTS_IDLE);
        engineEffectAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        engineIdle = engineEffectAnimation.getKeyFrame(0f);

        frames = R.getPlayerRegions(HERO_ENGINE_EFFECTS_POWERING);
        engineEffectPoweringAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);

        playerPosition = new Vector2(
            Gdx.graphics.getWidth() / 2f - body.getRegionWidth() / 2f,
            Gdx.graphics.getHeight() / 2f - body.getRegionHeight() / 2f
        );
    }


    @Override
    public void render(float dt) {
        isMoving = false;
        handleInput(dt);
        updateBackground(dt);
        animationTime += dt;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        drawBackground();
        drawPlayer();
        batch.end();
    }

    private void updateBackground(float dt) {
        bgY -= BACKGROUND_SPEED * dt;
        if (bgY <= -background.getHeight()) {
            bgY = 0;
        }
    }

    private void drawBackground() {
        batch.draw(background, 0, bgY, Gdx.graphics.getWidth(), background.getHeight());
        batch.draw(background, 0, bgY + background.getHeight(), Gdx.graphics.getWidth(), background.getHeight());
    }

    private void handleInput(float dt) {


        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerPosition.x -= HERO_SPEED * dt;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerPosition.x += HERO_SPEED * dt;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerPosition.y += HERO_SPEED * dt;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerPosition.y -= HERO_SPEED * dt;
            isMoving = true;
        }
        float totalShipHeight = (
            engineIdle.getRegionHeight() +
                engine.getRegionHeight() +
            body.getRegionHeight()) * HERO_SCALE;

        float shipWidth = body.getRegionWidth() * HERO_SCALE;

        float minX = shipWidth / 2f;
        float maxX = Gdx.graphics.getWidth() - shipWidth / 2f;

        float minY = 0;
        float maxY = Gdx.graphics.getHeight() - totalShipHeight;

        playerPosition.x = Math.max(minX, Math.min(playerPosition.x, maxX));
        playerPosition.y = Math.max(minY, Math.min(playerPosition.y, maxY));

    }

    private void drawPlayer() {
        float overlap = 6f * HERO_SCALE; // ajustar cuánto se solapan

        float partWidth = body.getRegionWidth() * HERO_SCALE;

        float x = playerPosition.x - partWidth / 2f;
        float y = playerPosition.y;

        float engineEffectHeight = engineIdle.getRegionHeight() * HERO_SCALE;
        float engineHeight = engine.getRegionHeight() * HERO_SCALE;
        float bodyHeight = body.getRegionHeight() * HERO_SCALE;

        float yOffset = y;

        // 1. Engine effect
        TextureRegion currentEngineFrame;
        if(isMoving)
            currentEngineFrame = engineEffectPoweringAnimation.getKeyFrame(animationTime);
        else
            currentEngineFrame = engineEffectAnimation.getKeyFrame(animationTime);

        batch.draw(currentEngineFrame, x + 21f, yOffset, body.getRegionWidth() * 1.1f, engineEffectHeight * 2.5f);
        yOffset += engineEffectHeight - overlap;

        overlap = 38f * HERO_SCALE;

        // 2. Engine
        batch.draw(engine, x, yOffset, partWidth, engineHeight);
        yOffset += engineHeight - overlap;

        // 3. Body
        batch.draw(body, x, yOffset, partWidth, bodyHeight);
        yOffset += bodyHeight - overlap;

    }


    @Override
    public void resize(int width, int height) {
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        batch.dispose();
        background.dispose();
    }
}
