package com.svalero.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.svalero.game.characters.Fighter;
import com.svalero.game.characters.FighterSquadron;
import com.svalero.game.characters.Projectile;

import static com.svalero.game.constants.Constants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class FighterSquadronManager {

    private final List<FighterSquadron> squadrons;

    public FighterSquadronManager() {
        squadrons = new ArrayList<>();
    }

    public void createSquadron() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float centerX = screenWidth / 2f;

        // Coordinate out of screen to spawn squadron
        float offScreenY = screenHeight + 100f;

        // Coordinate y destination random
        float targetY = screenHeight - MathUtils.random(100f, 200f);

        //Space to set triangle formation
        float spacingX = 80f;
        float spacingY = 80f;

        // Create fighters
        Fighter front = new Fighter();
        front.setFormation(FORMATION.FRONT);
        front.setPosition(new Vector2(centerX, offScreenY));

        Fighter left = new Fighter();
        left.setFormation(FORMATION.LEFT);
        left.setPosition(new Vector2(centerX - spacingX, offScreenY + spacingY));

        Fighter right = new Fighter();
        right.setFormation(FORMATION.RIGHT);
        right.setPosition(new Vector2(centerX + spacingX, offScreenY + spacingY));

        // Create squadron
        FighterSquadron squadron = new FighterSquadron(front, left, right, targetY);
        squadrons.add(squadron);
    }

    public void update(float dt, Vector2 rangerPosition) {
        //Remove squadrons destroyed
        squadrons.removeIf(squadron ->
            squadron.getFighters().stream().allMatch(Fighter::isDestroyed)
        );
        for (FighterSquadron squadron : squadrons) {
            squadron.update(dt, rangerPosition);
        }
    }

    public List<Fighter> getAllFighters() {
        return squadrons.stream()
            .flatMap(squadron -> squadron.getFighters().stream())
            .filter(fighter -> !fighter.isDestroyed())
            .collect(Collectors.toList());
    }

    public List<Projectile> getProjectiles() {
        return squadrons.stream()
            .flatMap(s -> s.getProjectiles().stream())
            .collect(Collectors.toList());
    }


}
