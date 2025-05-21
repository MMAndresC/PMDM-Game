package com.svalero.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.svalero.game.utils.DrawInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.svalero.game.constants.Constants.BACKGROUND_SPEED;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RenderManager {

    private LogicManager logicManager;

    private SpriteBatch batch;

    private float bgY;

    public RenderManager(LogicManager logicManager) {
        this.logicManager = logicManager;
        this.batch = new SpriteBatch();
    }

    public void render(float dt, Texture background) {
        //Clean
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Draw
        batch.begin();
        drawRanger();
        drawBackground(dt, background);
        batch.end();

        //DEBUG tool to show rectangle border
        /*ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(logicManager.getRanger().getHitBox().x, logicManager.getRanger().getHitBox().y,
            logicManager.getRanger().getHitBox().width, logicManager.getRanger().getHitBox().height);
        shapeRenderer.end();
        shapeRenderer.dispose();*/
    }

    public void drawRanger() {
        logicManager.getRanger().drawRanger();
        DrawInfo body = logicManager.getRanger().getBody();
        DrawInfo engine = logicManager.getRanger().getEngine();
        DrawInfo engineEffect = logicManager.getRanger().getEngineEffect();
        batch.draw(body.getRegion(), body.getX(), body.getY(), body.getWidth(), body.getHeight());
        batch.draw(engine.getRegion(), engine.getX(), engine.getY(), engine.getWidth(), engine.getHeight());
        batch.draw(engineEffect.getRegion(), engineEffect.getX(), engineEffect.getY(), engineEffect.getWidth(), engineEffect.getHeight());
    }

    private void updateBackground(float dt, float bgHeight) {
        bgY -= BACKGROUND_SPEED * dt;
        if (bgY <= -bgHeight) {
            bgY = 0;
        }
    }

    private void drawBackground(float dt, Texture background) {
        updateBackground(dt, background.getHeight());
        batch.draw(background, 0, bgY, Gdx.graphics.getWidth(), background.getHeight());
        batch.draw(background, 0, bgY + background.getHeight(), Gdx.graphics.getWidth(), background.getHeight());
    }
}
