package com.svalero.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.svalero.game.characters.Character;
import com.svalero.game.characters.Fighter;
import com.svalero.game.characters.GunTurret;
import com.svalero.game.characters.Projectile;
import com.svalero.game.utils.DrawInfo;
import com.svalero.game.utils.ShowRectangleDebug;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
        drawBackground(dt, background);
        drawRanger();
        drawRangerProjectile();
        drawEnemies(dt);
        drawFighterSquadrons();
        batch.end();

        //DEBUG tool to show rectangle border
        //Ranger
       /* ShowRectangleDebug.inRed(
            logicManager.getRanger().getHitBox().x,
            logicManager.getRanger().getHitBox().y,
            logicManager.getRanger().getHitBox().width,
            logicManager.getRanger().getHitBox().height);*/
    }

    public void drawRanger() {
        DrawInfo body = logicManager.getRanger().getBody();
        DrawInfo engine = logicManager.getRanger().getEngine();
        DrawInfo engineEffect = logicManager.getRanger().getEngineEffect();
        batch.draw(body.getRegion(), body.getX(), body.getY(), body.getWidth(), body.getHeight());
        batch.draw(engine.getRegion(), engine.getX(), engine.getY(), engine.getWidth(), engine.getHeight());
        batch.draw(engineEffect.getRegion(), engineEffect.getX(), engineEffect.getY(), engineEffect.getWidth(), engineEffect.getHeight());
    }

    public void drawRangerProjectile(){
        for(Projectile projectile : logicManager.getRanger().getProjectiles()) {
            batch.draw(projectile.getTexture(), projectile.getPosition().x, projectile.getPosition().y);
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
            if(enemy instanceof GunTurret) {
                DrawInfo body = ((GunTurret) enemy).getBody();
                DrawInfo gun = ((GunTurret) enemy).getGun();
                DrawInfo mount = ((GunTurret) enemy).getMount();
                batch.draw(mount.getRegion(), mount.getX(), mount.getY());
                batch.draw(gun.getRegion(), gun.getX(), gun.getY());
                batch.draw(body.getRegion(), body.getX(), body.getY());
            }else
                batch.draw(enemy.getCurrentFrame(), enemy.getPosition().x, enemy.getPosition().y);
        }
    }

    private void drawFighterSquadrons() {
        List<Fighter> fighters = logicManager.getFighterSquadronManager().getAllFighters();
        for (Fighter fighter : fighters) {
            batch.draw(fighter.getCurrentFrame(), fighter.getPosition().x, fighter.getPosition().y);
        }
    }

}
