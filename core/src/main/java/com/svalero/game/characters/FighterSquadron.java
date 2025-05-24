package com.svalero.game.characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.svalero.game.constants.Constants.FIGHTER_FIRE_RATE;

@Data
public class FighterSquadron {

    private final Fighter front;
    private final Fighter left;
    private final Fighter right;
    private final float targetY;
    private boolean inPosition;
    private float lastShot;
    private List<Projectile> projectiles;

    public FighterSquadron(Fighter front, Fighter left, Fighter right, float targetY) {
        this.front = front;
        this.left = left;
        this.right = right;
        this.targetY = targetY;
        this.inPosition = false;
        this.lastShot = TimeUtils.nanoTime() / 1_000_000_000f;
        projectiles = new ArrayList<Projectile>();
    }

    public void update(float dt, Vector2 rangerPosition) {
        float currentTime = TimeUtils.nanoTime() / 1_000_000_000f;
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
            if (!front.isDestroyed()) front.updateHitBox();
            if (!left.isDestroyed()) left.updateHitBox();
            if (!right.isDestroyed()) right.updateHitBox();

            //Shoot?
            if (currentTime - lastShot >= FIGHTER_FIRE_RATE) {
                lastShot = currentTime;
                if(!front.isDestroyed()) projectiles.add(front.fireProjectile());
                if(!right.isDestroyed()) projectiles.add(right.fireProjectile());
                if(!left.isDestroyed()) projectiles.add(left.fireProjectile());
            }

            //Update position projectiles
            for(Projectile projectile : projectiles) {
                projectile.update(dt);
                if(projectile.isOutOfBounds()) projectile.dispose(); //Free resources
            }

            //Remove out of screen
            projectiles.removeIf(Projectile::isOutOfBounds);
        }
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
