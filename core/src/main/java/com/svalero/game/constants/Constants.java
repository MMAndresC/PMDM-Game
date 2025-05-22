package com.svalero.game.constants;

import com.svalero.game.utils.AnimationInfo;

public interface Constants {

    //Skin
    String MENU_SKIN = "uiskin.json";

    //Atlas

    String RANGER_ATLAS = "ranger.atlas";
    String ENEMIES_ATLAS = "enemies.atlas";

    //Resources dir

    String BACKGROUNDS = "backgrounds";
    String UI = "ui";
    String RANGER = "ranger";
    String FONTS = "fonts";
    String ENEMIES = "enemies";

    //Files

    String MENU_BACKGROUND = "Main_menu.png";
    String LEVEL_1_BACKGROUND = "Level_1.png";

    //Regions

    AnimationInfo RANGER_ENGINE_EFFECTS_IDLE = new AnimationInfo("Main-Ship-Engine-Effect-Idle", 4);
    AnimationInfo RANGER_ENGINE_EFFECTS_POWERING = new AnimationInfo("Main-Ship-Engine-Effect-Powering", 4);
    String RANGER_FULL_HEALTH = "Main-Ship-Full-health";
    String RANGER_ENGINE = "Main-Ship-Engines-Supercharged";
    String RANGER_AMMO_PLASMA = "Projectile-Plasma-Large";
    String RANGER_AMMO_LASER = "Projectile-Laser-Large";
    String RANGER_AMMO_MINI_GUN = "Projectile-Minigun-Large";
    AnimationInfo ASTEROID = new AnimationInfo("Asteroid", 4);
    AnimationInfo FIGHTER_LEFT = new AnimationInfo("Enemy-Fighter-Left", 2);
    AnimationInfo FIGHTER_RIGHT = new AnimationInfo("Enemy-Fighter-Right", 2);
    String FIGHTER_IDLE = "Enemy-Fighter-Idle";

    //Config UI

    int PADDING_BUTTON = 20;
    int PADDING_TITLE = 50;
    int WIDTH_BUTTON = 500;
    int HEIGHT_BUTTON = 120;

    //Config game

    float BACKGROUND_SPEED = 200f;
    float RANGER_SPEED = 500f;
    float RANGER_SCALE = 2f; //Double size
    float RANGER_FIRE_RATE = 0.7f;
    float RANGER_BULLET_DAMAGE = 10f;
    float RANGER_BULLET_SPEED = 100f;
    float RANGER_HIT_POINTS = 200f;

    float ASTEROID_HIT_POINTS = 100;
    float ASTEROID_SPEED = 200f;
    int ASTEROID_SHOWER = 10;

    float FIGHTER_HIT_POINTS = 20f;
    float FIGHTER_SPEED = 25f;
    float FIGHTER_BEAM_DAMAGE = 5f;
    float FIGHTER_DELAY_ACTION = 2f; //2 seconds between actions

    float ENEMY_SPAWN_DELAY = 5f; //5 seconds

    //Enum
    enum STATUS { OUT, ACTIVE, DESTROYED};
    enum ENEMY_TYPE {ASTEROID, TURRET, FIGHTER_SQUADRON, KAMIKAZE};
    enum FORMATION {RIGHT, LEFT,FRONT}

}
