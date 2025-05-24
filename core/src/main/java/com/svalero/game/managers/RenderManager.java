package com.svalero.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.svalero.game.characters.*;
import com.svalero.game.characters.Character;
import com.svalero.game.utils.DrawInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.svalero.game.constants.Constants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RenderManager {

    private LogicManager logicManager;

    private SpriteBatch batch;

    private BitmapFont font;

    private float bgY;

    public RenderManager(LogicManager logicManager) {
        this.logicManager = logicManager;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont(Gdx.files.internal("fonts/font32Option.fnt"));
    }

    public void render(float dt, Texture background) {
        //Clean
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Draw
        batch.begin();
        drawBackground(dt, background);
        drawUI();
        drawRanger();
        drawRangerProjectile();
        drawEnemies(dt);
        drawEnemiesProjectiles();
        drawExplosions();
        batch.end();

        //DEBUG tool to show rectangle border
        //Ranger
       /* ShowRectangleDebug.inRed(
            logicManager.getRanger().getHitBox().x,
            logicManager.getRanger().getHitBox().y,
            logicManager.getRanger().getHitBox().width,
            logicManager.getRanger().getHitBox().height);*/
    }

    public void drawExplosions() {
        for(Explosion explosion: logicManager.getExplosions()){
            DrawInfo drawInfo = explosion.getExplosion();
            batch.draw(drawInfo.getRegion(), drawInfo.getX(), drawInfo.getY(),
                drawInfo.getWidth(), drawInfo.getHeight());
        }
        logicManager.getExplosions().removeIf(Explosion::isFinished);
    }

    public void drawRanger() {
        //Nor render if is destroyed
        if(logicManager.getRanger().isDestroyed()) return;

        boolean shouldDraw = true;
        Ranger ranger = logicManager.getRanger();
        if (ranger.isImmune()) {
            float time = TimeUtils.nanoTime() / 1_000_000_000f;
            shouldDraw = ((int)(time * 10) % 2 != 0);  // Draw in odd frames
        }

        if (shouldDraw || !ranger.isImmune()) {
            DrawInfo body = ranger.getBody();
            DrawInfo engine = ranger.getEngine();
            DrawInfo engineEffect = ranger.getEngineEffect();
            batch.draw(body.getRegion(), body.getX(), body.getY(), body.getWidth(), body.getHeight());
            batch.draw(engine.getRegion(), engine.getX(), engine.getY(), engine.getWidth(), engine.getHeight());
            batch.draw(engineEffect.getRegion(), engineEffect.getX(), engineEffect.getY(), engineEffect.getWidth(), engineEffect.getHeight());
        }
    }

    public void drawRangerProjectile(){
        for(Projectile projectile : logicManager.getRanger().getProjectiles()) {
            batch.draw(projectile.getCurrentFrame(), projectile.getPosition().x, projectile.getPosition().y);
        }
    }

    public void drawEnemiesProjectiles(){
        for(Projectile projectile: logicManager.getEnemyManager().getProjectiles()) {
            batch.draw(projectile.getCurrentFrame(), projectile.getPosition().x, projectile.getPosition().y);
        }
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

    private void drawEnemies(float dt) {
        List<Character> enemies = logicManager.getEnemyManager().getEnemies();
        for(Character enemy: enemies) {

            //Not render inactives or destroyed enemies
            if(enemy.isInactive() || enemy.isDestroyed()) continue;

            if(enemy instanceof GunTurret) {
                DrawInfo body = ((GunTurret) enemy).getBody();
                DrawInfo gun = ((GunTurret) enemy).getGun();
                DrawInfo mount = ((GunTurret) enemy).getMount();
                batch.draw(mount.getRegion(), mount.getX(), mount.getY());
                batch.draw(gun.getRegion(), gun.getX(), gun.getY());
                batch.draw(body.getRegion(), body.getX(), body.getY());
            }else if(enemy instanceof Kamikaze) {
                DrawInfo body = ((Kamikaze) enemy).getBody();
                DrawInfo engineEffect = ((Kamikaze) enemy).getEngineEffect();
                batch.draw(body.getRegion(), body.getX(), body.getY(), body.getWidth(), body.getHeight());
                batch.draw(engineEffect.getRegion(), engineEffect.getX(), engineEffect.getY(), engineEffect.getWidth(), engineEffect.getHeight());
            }else
                batch.draw(enemy.getCurrentFrame(), enemy.getPosition().x, enemy.getPosition().y);
        }
    }

    private void drawUI(){
        float screenWidth = Gdx.graphics.getWidth();
        TextureRegion statsBar = R.getUITexture(STATS_BAR);
        batch.draw(statsBar, 0, 0, WIDTH_STATS_BAR, HEIGHT_STATS_BAR);
        //TODO cambiar cuando se haga el level manager
        font.draw(batch, "Level 1", 30f, HEIGHT_STATS_BAR - 12);
        font.draw(batch, "Score", 255f, HEIGHT_STATS_BAR - 12);
        float hitPoints = logicManager.getRanger().getScore();
        String formatted = String.format("%06.0f", hitPoints);
        font.draw(batch, formatted , 440f, HEIGHT_STATS_BAR - 12);
        TextureRegion healthBar = R.getUITexture(HEALTH_BAR);
        batch.draw(healthBar,screenWidth - WIDTH_HEALTH_BAR,0, WIDTH_HEALTH_BAR, HEIGHT_HEALTH_BAR);
        TextureRegion shipIcon = R.getUITexture(SHIP_ICON);
        font.draw(batch, "X " + logicManager.getRanger().getLives(), screenWidth - WIDTH_HEALTH_BAR - SPACING, HEIGHT_HEALTH_BAR - 10);
        batch.draw(shipIcon, screenWidth - WIDTH_HEALTH_BAR - SPACING - WIDTH_SHIP_ICON - 10, 0, WIDTH_SHIP_ICON, HEIGHT_SHIP_ICON);
        TextureRegion healthStat = R.getUITexture(HEALTH_STAT);
        float healthPercentage = logicManager.getRanger().getHitPoints() / RANGER_HIT_POINTS;
        float width = healthPercentage * WIDTH_HEALTH_STAT;
        batch.draw(healthStat, screenWidth - WIDTH_HEALTH_BAR + 5f, 5f, width, HEIGHT_STATS_BAR - 10f);
    }

}
