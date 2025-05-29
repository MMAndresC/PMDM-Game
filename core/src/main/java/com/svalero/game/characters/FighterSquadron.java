package com.svalero.game.characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.svalero.game.managers.R;
import com.svalero.game.managers.SoundManager;
import com.svalero.game.projectiles.BeamDefault;
import com.svalero.game.projectiles.Projectile;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.svalero.game.constants.Constants.*;
import static com.svalero.game.constants.Constants.FIGHTER_BEAM_DAMAGE;

@Data
public class FighterSquadron {

    private final Fighter front;
    private final Fighter left;
    private final Fighter right;
    private final float targetY;
    private boolean inPosition;
    private float lastShot;


    public FighterSquadron(Fighter front, Fighter left, Fighter right, float targetY) {
        this.front = front;
        this.left = left;
        this.right = right;
        this.targetY = targetY;
        this.inPosition = false;
        this.lastShot = TimeUtils.nanoTime() / 1_000_000_000f;
        this.front.setSquadron(this);
        this.left.setSquadron(this);
        this.right.setSquadron(this);
    }

    public void update(float dt, Vector2 rangerPosition) {
        float currentY = front.getPosition().y;

        //Spawn out of screen still not in position y obtain random
        if (!inPosition) {
            if (currentY > targetY) {
                Vector2 newFrontPos = new Vector2(front.getPosition().x, currentY - front.getSpeed());
                getSquadronPosition(newFrontPos);
            } else {
                inPosition = true;
            }
        } else {
            // Not move y, follow ranger in x
            float rangerX = rangerPosition.x;
            Vector2 newFrontPos = new Vector2(rangerX, front.getPosition().y);

            //Subtract newFrontPos - front position return Vector2 with difference
            Vector2 direction = new Vector2(newFrontPos).sub(front.getPosition());
            getSquadronPosition(newFrontPos);

            //Change to animation if moving, only if still alive
            if (!front.isDestroyed()){
              if(direction.len() > 1f) front.setAnimationByDirection(direction, dt);
              else front.setIdle();
            }
            if (!left.isDestroyed()) {
                if (direction.len() > 1f) left.setAnimationByDirection(direction, dt);
                else left.setIdle();
            }
            if (!right.isDestroyed()) {
                if (direction.len() > 1f) right.setAnimationByDirection(direction, dt);
                else right.setIdle();
            }

            //Update hit box
            if (!front.isDestroyed()) {
                front.updateHitBox();
                front.updateHitEffect(dt);
            }
            if (!left.isDestroyed()) {
                left.updateHitBox();
                left.updateHitEffect(dt);
            }
            if (!right.isDestroyed()) {
                right.updateHitBox();
                right.updateHitEffect(dt);
            }
        }
    }

    public List<Projectile> createProjectile(){
        float currentTime = TimeUtils.nanoTime() / 1_000_000_000f;
        //Not yet
        if (currentTime - lastShot < FIGHTER_FIRE_RATE) return null;

        //Sound
        SoundManager.play(SQUADRON_BEAM_SOUND, LOW_SOUND_VOLUME);

        lastShot = currentTime;
        List<Projectile> beams = new ArrayList<>();
        TextureRegion frame = R.getEnemyTexture(DEFAULT_ENEMY_PROJECTILE);
        if(!front.isDestroyed()){
            Vector2 origin = new Vector2(front.getPosition().x + (front.getCurrentFrame().getRegionWidth() / 2f ), front.getPosition().y);
            beams.add(new BeamDefault(origin, FIGHTER_BEAM_SPEED, FIGHTER_BEAM_DAMAGE, frame));
        }
        if(!right.isDestroyed()){
            Vector2 origin = new Vector2(right.getPosition().x + (right.getCurrentFrame().getRegionWidth() / 2f ), right.getPosition().y);
            beams.add(new BeamDefault(origin, FIGHTER_BEAM_SPEED, FIGHTER_BEAM_DAMAGE, frame));
        }
        if(!left.isDestroyed()){
            Vector2 origin = new Vector2(left.getPosition().x + (left.getCurrentFrame().getRegionWidth() / 2f ), left.getPosition().y);
            beams.add(new BeamDefault(origin, FIGHTER_BEAM_SPEED, FIGHTER_BEAM_DAMAGE, frame));
        }
        return beams;
    }

    private void getSquadronPosition(Vector2 newFrontPos) {
        front.setPosition(newFrontPos);

        float spacingX = 80f;
        float spacingY = 50f;

        left.setPosition(new Vector2(newFrontPos.x - spacingX, newFrontPos.y + spacingY));
        right.setPosition(new Vector2(newFrontPos.x + spacingX, newFrontPos.y + spacingY));
    }

    public List<Fighter> getFighters() {
        return Arrays.asList(front, left, right);
    }
}
