package com.svalero.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.svalero.game.characters.*;
import com.svalero.game.characters.Character;
import com.svalero.game.items.PowerUp;
import com.svalero.game.projectiles.Projectile;
import com.svalero.game.projectiles.Ray;
import com.svalero.game.utils.DrawInfo;
import com.svalero.game.utils.DrawInfoEffect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;

import static com.svalero.game.constants.Constants.*;
import static com.svalero.game.utils.ShowRectangleDebug.DEBUG_showEnemiesRectangle;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RenderManager {

    private LogicManager logicManager;

    private SpriteBatch batch;

    private BitmapFont font;

    private float bgY;

    private Texture background;

    public RenderManager(LogicManager logicManager) {
        this.logicManager = logicManager;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont(Gdx.files.internal(FONTS + OPTION_FONT));
        updateLevelBackground();
    }

    public void render(float dt) {
        //Clean
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Draw
        batch.begin();
        drawBackground(dt);
        drawUI();
        drawPowerUps();
        drawRanger();
        drawRangerProjectile();
        drawEnemies(dt);
        drawEnemiesProjectiles();
        drawExplosions();
        if(logicManager.isLevelOver())
            drawLevelUI();
        if(logicManager.isChangeBackground()){
            updateLevelBackground();
            logicManager.setChangeBackground(false);
        }
        batch.end();

        /**
         * DEBUG tool to show rectangle border
         */

        //Ranger
        /*if(logicManager.getRanger().getHitBox() != null){
            DEBUG_showRangerRectangle(logicManager.getRanger().getHitBox());
        }*/
        //Enemies
        //DEBUG_showEnemiesRectangle(logicManager.getEnemyManager().getEnemies());
        //Ray
        //DEBUG_showRaysPolygon(logicManager.getEnemyManager().getProjectiles());
        //Projectiles
        //DEBUG_showProjectilesRectangle(logicManager.getEnemyManager().getProjectiles());
    }

    public void drawPowerUps(){
        List<PowerUp> powerUps = logicManager.getPowerUpManager().getPowerUps();
        for(PowerUp powerUp : powerUps){
            DrawInfoEffect effect = powerUp.drawWithRotationEffect();
            batch.draw(effect.getRegion(), effect.getX(), effect.getY(), effect.getOriginX(), effect.getOriginY(),
                effect.getWidth(), effect.getHeight(), effect.getScaleX(), effect.getScaleY(), effect.getRotation()
            );
        }
    }

    public void updateLevelBackground(){
        String backgroundLevel = logicManager.getBackground();
        this.background = new Texture(Gdx.files.internal(BACKGROUNDS + File.separator + backgroundLevel));
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
            if(ranger.isShieldActive()){
                DrawInfo shield = ranger.getShield();
                batch.draw(shield.getRegion(), shield.getX(), shield.getY(), shield.getWidth(), shield.getHeight());
            }
        }
    }

    public void drawRangerProjectile(){
        for(Projectile projectile : logicManager.getRanger().getProjectiles()) {
            batch.draw(projectile.getCurrentFrame(), projectile.getPosition().x, projectile.getPosition().y);
        }
    }

    public void drawEnemiesProjectiles(){
        for(Projectile projectile: logicManager.getEnemyManager().getProjectiles()) {
            if(projectile instanceof Ray){
                DrawInfoEffect drawInfo = ((Ray) projectile).getDrawInfo();
                batch.draw(drawInfo.getRegion(), drawInfo.getX(), drawInfo.getY(), drawInfo.getOriginX(),
                    drawInfo.getOriginY(), drawInfo.getWidth(), drawInfo.getHeight(), drawInfo.getScaleX(),
                    drawInfo.getScaleY(), drawInfo.getRotation());
            } else{
                float scale = projectile.getScale();
                float width = projectile.getCurrentFrame().getRegionWidth() * scale;
                float height = projectile.getCurrentFrame().getRegionHeight() * scale;
                batch.draw(projectile.getCurrentFrame(), projectile.getPosition().x, projectile.getPosition().y,
                    width, height);
            }
        }
    }

    private void updateBackground(float dt, float bgHeight) {
        bgY -= BACKGROUND_SPEED * dt;
        if (bgY <= -bgHeight) {
            bgY += bgHeight;
        }
    }

    private void drawBackground(float dt) {
        float bgHeight = background.getHeight();
        float screenWidth = Gdx.graphics.getWidth();

        updateBackground(dt, bgHeight);

        // Cover screen
        for (int i = 0; i < 3; i++) {
            float y = bgY + i * bgHeight;
            batch.draw(background, 0, y, screenWidth, bgHeight);
        }
    }

    private void drawEnemies(float dt) {
        List<Character> enemies = logicManager.getEnemyManager().getEnemies();
        for(Character enemy: enemies) {

            //Not render destroyed enemies
            if(enemy.isDestroyed()) continue;

            boolean shouldDraw = true;
            if (enemy.isHitEffect()) {
                float time = TimeUtils.nanoTime() / 1_000_000_000f;
                shouldDraw = ((int)(time * 10) % 2 != 0);  // Draw in odd frames
            }
            if(!shouldDraw) continue;

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
            }else if(enemy instanceof Frigate) {
                DrawInfo body = ((Frigate) enemy).getBody();
                DrawInfo engine = ((Frigate) enemy).getEngine();
                batch.draw(body.getRegion(), body.getX(), body.getY(), body.getWidth(), body.getHeight());
                batch.draw(engine.getRegion(), engine.getX(), engine.getY(), engine.getWidth(), engine.getHeight());
            }else if(enemy instanceof Dreadnought){
                DrawInfo body = ((Dreadnought) enemy).getBody();
                DrawInfo engine = ((Dreadnought) enemy).getEngine();
                batch.draw(body.getRegion(), body.getX(), body.getY(), body.getWidth(), body.getHeight());
                batch.draw(engine.getRegion(), engine.getX(), engine.getY(), engine.getWidth(), engine.getHeight());
            }else
                batch.draw(enemy.getCurrentFrame(), enemy.getPosition().x, enemy.getPosition().y);
        }
    }

    private void drawUI(){
        float screenWidth = Gdx.graphics.getWidth();
        TextureRegion statsBar = R.getUITexture(STATS_BAR);
        batch.draw(statsBar, 0, 0, WIDTH_STATS_BAR, HEIGHT_STATS_BAR);

        int numberLevel = logicManager.getNumberLevel();
        font.draw(batch, "Level " + numberLevel, 30f, HEIGHT_STATS_BAR - 12);
        font.draw(batch, "Score", 255f, HEIGHT_STATS_BAR - 12);

        float score = logicManager.getRanger().getScore();
        String formatted = String.format("%06.0f", score);
        font.draw(batch, formatted , 440f, HEIGHT_STATS_BAR - 12);

        TextureRegion healthBar = R.getUITexture(HEALTH_BAR);
        batch.draw(healthBar,screenWidth - WIDTH_HEALTH_BAR,0, WIDTH_HEALTH_BAR, HEIGHT_HEALTH_BAR);

        TextureRegion shipIcon = R.getUITexture(SHIP_ICON);
        font.draw(batch, "X " + logicManager.getRanger().getLives(), screenWidth - WIDTH_HEALTH_BAR - SPACING, HEIGHT_HEALTH_BAR - 10);
        batch.draw(shipIcon, screenWidth - WIDTH_HEALTH_BAR - SPACING - WIDTH_SHIP_ICON - 10, 0, WIDTH_SHIP_ICON, HEIGHT_SHIP_ICON);

        float healthPercentage;
        TextureRegion healthStat;
        if(logicManager.getRanger().isShieldActive()){
            healthStat = R.getUITexture(HEALTH_SHIELD_STAT);
            healthPercentage = logicManager.getRanger().getPointsLifeShield() / RANGER_HIT_POINTS;
        }else{
            healthStat = R.getUITexture(HEALTH_STAT);
            healthPercentage = logicManager.getRanger().getHitPoints() / RANGER_HIT_POINTS;
        }
        float width = healthPercentage * WIDTH_HEALTH_STAT;
        batch.draw(healthStat, screenWidth - WIDTH_HEALTH_BAR + 5f, 5f, width, HEIGHT_STATS_BAR - 10f);

        if(logicManager.getRanger().isShieldActive()){
            TextureRegion shieldIcon = R.getUITexture(SHIELD_ICON);
            batch.draw(shieldIcon, (screenWidth / 2f) - WIDTH_POWER_UP_ICON + 5, 0, WIDTH_POWER_UP_ICON, HEIGHT_POWER_UP_ICON);
        }

        if(logicManager.getRanger().isDoubleCannonActive()){
            TextureRegion damageIcon = R.getUITexture(DAMAGE_ICON);
            batch.draw(damageIcon, (screenWidth / 2f) + WIDTH_POWER_UP_ICON - 5, 0, WIDTH_POWER_UP_ICON, HEIGHT_POWER_UP_ICON);
        }
    }

    private void drawLevelUI() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float x = (screenWidth - WIDTH_LEVEL_WINDOW) / 2f;
        float y = (screenHeight - HEIGHT_LEVEL_WINDOW) / 2f;

        TextureRegion levelWindow = R.getUITexture(LEVEL_WINDOW);
        batch.draw(levelWindow, x, y, WIDTH_LEVEL_WINDOW, HEIGHT_LEVEL_WINDOW);

        int numberLevel = logicManager.getNumberLevel();
        float centerX = x + WIDTH_LEVEL_WINDOW / 2f;

        GlyphLayout layout = new GlyphLayout();

        float lineSpacing = 40f;
        float totalHeight = lineSpacing * 6;

        float startY = y + (HEIGHT_LEVEL_WINDOW + totalHeight) / 1.8f;

        String text = "LEVEL " + numberLevel;
        layout.setText(font, text);
        font.draw(batch, text, centerX - layout.width / 2f, startY);

        text = "COMPLETED";
        layout.setText(font, text);
        font.draw(batch, text, centerX - layout.width / 2f, startY - lineSpacing);

        text = "BONUS";
        layout.setText(font, text);
        font.draw(batch, text, centerX - layout.width / 2f, startY - lineSpacing * 2.5f);

        TextureRegion shipIcon = R.getUITexture(SHIP_ICON);
        float iconY = startY - lineSpacing * 5f;
        float iconX = centerX - WIDTH_SHIP_ICON * 2.5f;

        text = logicManager.getRanger().getLives() + "   X 1000";
        layout.setText(font, text);
        batch.draw(shipIcon, iconX, iconY, WIDTH_SHIP_ICON, HEIGHT_SHIP_ICON);
        float textY = iconY + HEIGHT_SHIP_ICON / 2f + layout.height / 2f;
        font.draw(batch, text, iconX + WIDTH_SHIP_ICON + 10f, textY);

        text = "SCORE";
        layout.setText(font, text);
        font.draw(batch, text, centerX - layout.width / 2f, startY - lineSpacing * 5.7f);

        float score = logicManager.getRanger().getScore();
        float sumBonus = logicManager.getRanger().getLives() * SCORE_BONUS_LIVES;
        String formatted = String.format("%06.0f", score + sumBonus);
        layout.setText(font, formatted);
        font.draw(batch, formatted, centerX - layout.width / 2f, startY - lineSpacing * 7f);
    }

}
