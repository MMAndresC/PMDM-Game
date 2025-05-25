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
import com.svalero.game.utils.DrawInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
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

    private Texture background;

    public RenderManager(LogicManager logicManager) {
        this.logicManager = logicManager;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont(Gdx.files.internal("fonts/font32Option.fnt"));
        String backgroundLevel = logicManager.getBackground();
        this.background = new Texture(Gdx.files.internal(BACKGROUNDS + File.separator + backgroundLevel));
    }

    public void render(float dt) {
        //Clean
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Draw
        batch.begin();
        drawBackground(dt);
        drawUI();
        drawRanger();
        drawRangerProjectile();
        drawEnemies(dt);
        drawEnemiesProjectiles();
        drawExplosions();
        if(logicManager.isLevelOver())
            drawLevelUI();
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

    private void drawBackground(float dt) {
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
        TextureRegion healthStat = R.getUITexture(HEALTH_STAT);
        float healthPercentage = logicManager.getRanger().getHitPoints() / RANGER_HIT_POINTS;
        float width = healthPercentage * WIDTH_HEALTH_STAT;
        batch.draw(healthStat, screenWidth - WIDTH_HEALTH_BAR + 5f, 5f, width, HEIGHT_STATS_BAR - 10f);
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

        // Centrado vertical de las 4 líneas
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

        text = "X 1000";
        layout.setText(font, text);

        TextureRegion shipIcon = R.getUITexture(SHIP_ICON);
        float totalWidth = WIDTH_SHIP_ICON  + SPACING + layout.width;
        float iconX = centerX - totalWidth / 2f;
        float iconY = startY - lineSpacing * 5f;

        batch.draw(shipIcon, iconX, iconY, WIDTH_SHIP_ICON, HEIGHT_SHIP_ICON);
        float textY = iconY + HEIGHT_SHIP_ICON / 2f + layout.height / 2f;
        font.draw(batch, text, iconX + WIDTH_SHIP_ICON + SPACING, textY);

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
