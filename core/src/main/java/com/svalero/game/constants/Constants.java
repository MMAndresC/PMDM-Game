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
    String RANGER_SLIGHT_DAMAGED = "Main-Ship-Slight-damage";
    String RANGER_VERY_DAMAGED = "Main-Ship-Very-damaged";
    String RANGER_ENGINE = "Main-Ship-Engines-Supercharged";
    String RANGER_AMMO_PLASMA = "Projectile-Plasma-Large";
    String RANGER_AMMO_LASER = "Projectile-Laser-Large";
    String RANGER_AMMO_MINI_GUN = "Projectile-Minigun-Large";
    AnimationInfo ASTEROID = new AnimationInfo("Asteroid", 4);
    AnimationInfo FIGHTER_LEFT = new AnimationInfo("Enemy-Fighter-Left", 2);
    AnimationInfo FIGHTER_RIGHT = new AnimationInfo("Enemy-Fighter-Right", 2);
    String FIGHTER_IDLE = "Enemy-Fighter-Idle";
    String GUN_TURRET_LEFT = "Gun-Turret-Left";
    String GUN_TURRET_RIGHT = "Gun-Turret-Right";
    String GUN_TURRET_GUN_LEFT = "Gun-Turret-Gun-Left";
    String GUN_TURRET_GUN_RIGHT = "Gun-Turret-Gun-Right";
    String GUN_TURRET_MOUNT = "Gun-Turret-Mount";
    AnimationInfo GUN_TURRET_MISSILES = new AnimationInfo("Missile", 16);
    String KAMIKAZE = "Kamikaze";
    AnimationInfo KAMIKAZE_ENGINE_EFFECTS = new AnimationInfo("Kamikaze-Engine-Effect", 10);
    String DEFAULT_ENEMY_PROJECTILE = "Projectile-Laser-Large";
    //Explosions
    AnimationInfo DEFAULT_ENEMY_EXPLOSION = new AnimationInfo("Explosion", 9);
    AnimationInfo KAMIKAZE_EXPLOSION = new AnimationInfo("Kamikaze-Explosion", 8);
    AnimationInfo ASTEROID_EXPLOSION = new AnimationInfo("Asteroid-Explosion", 7);
    AnimationInfo RANGER_EXPLOSION = new AnimationInfo("Explosion", 9);

    //Config UI

    int PADDING_BUTTON = 20;
    int PADDING_TITLE = 50;
    int WIDTH_BUTTON = 500;
    int HEIGHT_BUTTON = 120;

    //Config game

    float BACKGROUND_SPEED = 200f;
    float RANGER_SPEED = 500f;
    float RANGER_SCALE = 2f; //Double size
    float RANGER_FIRE_RATE = 0.5f;
    float RANGER_BULLET_DAMAGE = 20f;
    float RANGER_BULLET_SPEED = 100f;
    float RANGER_HIT_POINTS = 100f;
    float RANGER_IMMUNITY_DURATION = 3f; //2.5 seconds
    float RANGER_IMMUNITY_HIT_DURATION = 1f;
    int RANGER_LIVES = 3;
    float RANGER_EXPLOSION_SCALE = 5f;

    float ASTEROID_HIT_POINTS = 60f;
    float ASTEROID_SPEED = 200f;
    int ASTEROID_SHOWER = 10;

    float FIGHTER_HIT_POINTS = 20f;
    float FIGHTER_SPEED = 5f;
    float FIGHTER_FIRE_RATE = 1.5f;
    float FIGHTER_BEAM_DAMAGE = 20f;
    float FIGHTER_BEAM_SPEED = 100f;

    float GUN_TURRET_HIT_POINTS = 40f;
    float GUN_TURRET_FIRE_RATE = 2f;
    float GUN_TURRET_SPEED = 2f;
    float GUN_TURRET_SCALE = 1.5f;
    float GUN_TURRET_MISSILE_DAMAGE = 80f;
    float GUN_TURRET_MISSILE_SPEED = 200f;

    float KAMIKAZE_SPEED = 150f;
    float KAMIKAZE_HIT_POINTS = 25f;
    float KAMIKAZE_SCALE = 1.5f;

    float ENEMY_SPAWN_DELAY = 5f; //5 seconds

    //Enum
    enum STATUS { OUT, ACTIVE, DESTROYED, INACTIVE};
    enum ENEMY_TYPE {ASTEROID, GUN_TURRET, FIGHTER_SQUADRON, KAMIKAZE};
    enum FORMATION {RIGHT, LEFT,FRONT}
    enum CHARACTER_TYPE {RANGER, ASTEROID, GUN_TURRET, FIGHTER, KAMIKAZE}

}
