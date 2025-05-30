package com.svalero.game.constants;

import com.badlogic.gdx.math.Vector2;
import com.svalero.game.utils.AnimationInfo;

import java.util.List;

public interface Constants {

    //Skin
    String MENU_SKIN = "ui.json";

    //Atlas

    String RANGER_ATLAS = "ranger.atlas";
    String ENEMIES_ATLAS = "enemies.atlas";
    String UI_ATLAS = "ui.atlas";

    //Resources dir

    String BACKGROUNDS = "backgrounds";
    String UI = "ui";
    String RANGER = "ranger";
    String FONTS = "fonts";
    String ENEMIES = "enemies";
    String MUSIC = "music";
    String SOUNDS = "sounds";
    String ASSETS = "assets";
    String OPTION_FONT = "font32Option.fnt";
    String LEVELS = "levels";

    //Files

    String MENU_BACKGROUND = "Main_menu.png";
    String SPLASH_SCREEN_BACKGROUND = "Splash_screen.png";

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
    String FRIGATE = "Frigate-Base";
    AnimationInfo FRIGATE_ENGINE = new AnimationInfo("Frigate-Engine", 8);
    AnimationInfo FRIGATE_BODY = new AnimationInfo("Frigate-Body", 5);
    AnimationInfo FRIGATE_RAY = new AnimationInfo("Ray", 4);
    AnimationInfo DREADNOUGHT_ENGINE = new AnimationInfo("Dreadnought-Engine", 8);
    AnimationInfo DREADNOUGHT_BODY = new AnimationInfo("Dreadnought-body", 34);
    AnimationInfo DREADNOUGHT_PROTON = new AnimationInfo("Proton", 6);
    //Explosions
    AnimationInfo DEFAULT_ENEMY_EXPLOSION = new AnimationInfo("Explosion", 9);
    AnimationInfo KAMIKAZE_EXPLOSION = new AnimationInfo("Kamikaze-Explosion", 8);
    AnimationInfo ASTEROID_EXPLOSION = new AnimationInfo("Asteroid-Explosion", 7);
    AnimationInfo RANGER_EXPLOSION = new AnimationInfo("Explosion", 9);
    AnimationInfo FRIGATE_EXPLOSION = new AnimationInfo("Frigate-Explosion", 16);
    AnimationInfo DREADNOUGHT_EXPLOSION = new AnimationInfo("Dreadnought-Explosion", 18);
    //Power Ups
    String SHIELD = "Powerup-Shield";
    String DAMAGE = "Powerup-Damage";
    String HEALTH ="Powerup-Health";
    AnimationInfo RANGER_SHIELD = new AnimationInfo("Shield", 12);
    //UI
    String STATS_BAR = "Stats_Bar";
    String HEALTH_BAR = "Health_Bar_Table";
    String SHIP_ICON = "Ship-Icon";
    String HEALTH_STAT = "Loading-Bar-Orange2";
    String HEALTH_SHIELD_STAT = "Loading-Bar-Blue2";
    String LEVEL_WINDOW = "Level-Window";
    String WINDOW = "Window";
    String SHIELD_ICON = "Shield-Icon";
    String DAMAGE_ICON = "Damage-Icon";
    String KEYS_MOVE = "Keys-Move";
    String KEYS_PAUSE = "Keys-Pause";
    String KEYS_SHOOT = "Keys-Shoot";
    String DIVIDER = "slider-fancy";
    String PAUSE = "Pause-Header";

    //Music
    String INTRO_MUSIC = "DavidKBD-Cosmic-Pack-01-Cosmic-Journey-full.ogg";
    String DEFAULT_MUSIC = "DavidKBD-Cosmic-Pack-05-Stellar-Confrontation-full.ogg";
    String LUNAR_RAMPAGE = "DavidKBD-Cosmic-Pack-06-Lunar-Rampage-full.ogg";
    String GOD_OF_DARKNESS = "DavidKBD-Eternity-Pack-06-God-of-darkness-loop.ogg";
    String SUFFOCATION = "DavidKBD-Eternity-Pack-07-Suffocation-loop.ogg";
    String GAME_OVER_MUSIC = "DavidKBD-Eternity-Pack-02-Agony-Space-deep-loop.ogg";
    String LEVEL_OVER_MUSIC = "DavidKBD-Cosmic-Pack-04-Orbit-Oddyseey-variation1.ogg";
    List<String> MUSIC_LIST = List.of(INTRO_MUSIC, DEFAULT_MUSIC, LUNAR_RAMPAGE,
        GOD_OF_DARKNESS, SUFFOCATION, GAME_OVER_MUSIC, LEVEL_OVER_MUSIC);

    //Sound
    String DEFAULT_EXPLOSION_SOUND = "DefaultExplosion.wav";
    String DREADNOUGHT_EXPLODING = "DreadnoughtExploding.wav";
    String RANGER_EXPLODING = "RangerExploding.wav";
    String MISSILE_SOUND = "Missile.wav";
    String POWER_UP_SOUND = "powerup.wav";
    String RANGER_BEAM_SOUND = "RangerBeam.wav";
    String RAY_SOUND = "Ray.wav";
    String RANGER_HIT_SOUND = "RangerHit.wav";
    String SHIELD_HIT_SOUND = "ShieldHit.wav";
    String PROTON_SOUND = "Proton.wav";
    String SQUADRON_BEAM_SOUND = "SquadronBeam.wav";
    List<String> SOUNDS_LIST = List.of(DEFAULT_EXPLOSION_SOUND, DREADNOUGHT_EXPLODING, RANGER_EXPLODING,
        MISSILE_SOUND, POWER_UP_SOUND, RANGER_BEAM_SOUND, RAY_SOUND, RANGER_HIT_SOUND, SHIELD_HIT_SOUND, PROTON_SOUND,
        SQUADRON_BEAM_SOUND
    );
    float MINIMUM_SOUND_VOLUME = 0.1f;
    float LOW_SOUND_VOLUME = 0.3f;
    float MEDIUM_SOUND_VOLUME = 0.5f;
    float HIGH_SOUND_VOLUME = 0.8f;

    //Preferences configuration
    String GAME_SETTINGS = "game-settings";
    String SOUND_ENABLED = "sound_enabled";
    String MUSIC_VOLUME = "music_volume";

    //Config UI
    float PADDING_BUTTON = 20;
    float PADDING_TITLE = 50;
    float WIDTH_BUTTON = 500;
    float HEIGHT_BUTTON = 120;
    float HEIGHT_STATS_BAR = 50;
    float WIDTH_STATS_BAR = 630;
    float HEIGHT_HEALTH_BAR = 50;
    float WIDTH_HEALTH_BAR = 250;
    float HEIGHT_SHIP_ICON = 50;
    float WIDTH_SHIP_ICON = 50;
    float WIDTH_HEALTH_STAT = 200f;
    float SPACING = 100f;
    float WIDTH_LEVEL_WINDOW = 900f;
    float HEIGHT_LEVEL_WINDOW =450f;
    float PADDING_GAME_OVER_TABLE = 30;
    float PADDING_GAME_OVER_TITLE = 40;
    float PADDING_GAME_OVER_SCORE = 10;
    float PADDING_GAME_OVER_SCORE_VALUE = 60;
    float WIDTH_BUTTON_GAME_OVER = 660;
    float HEIGHT_BUTTON_GAME_OVER = 120;
    float PADDING_SETTING_LABEL = 10;
    float WIDTH_SLIDER = 300;
    float HEIGHT_SLIDER = 40;
    float WIDTH_KNOB = 20;
    float HEIGHT_KNOB = 40;
    float WIDTH_POWER_UP_ICON = 50;
    float HEIGHT_POWER_UP_ICON = 50;
    float MENU_BACKGROUND_SPEED = 20;
    float WIDTH_KEYS = 80;
    float HEIGHT_KEYS = 50;
    float HEIGHT_KEY = 40;
    float WIDTH_KEY = 40;
    float HEIGHT_DIVIDER = 10;
    float WIDTH_SCALE_DIVIDER = 0.4f;
    float PADDING_IMAGE = 10;
    float PADDING_CHECKBOX = 20;
    float SCALE_CHECKBOX = 1.5f;
    float SCALE_TABLE = 0.6f;
    float DELAY_SPLASH_SCREEN = 2f;
    float WIDTH_PROGRESS_BAR = 400;
    float HEIGHT_PROGRESS_BAR = 40;
    float DELAY_GAME_OVER = 02f;

    //Config game

    float LEVEL_DELAY = 5f;
    float ENEMY_HIT_EFFECT_DURATION = 0.1f;
    float DEFAULT_FRAME_DURATION = 0.1f;

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
    float RANGER_EXPLOSION_FRAME_DURATION = 0.4f;
    float SCORE_BONUS_LIVES = 1000;
    float RANGER_LENIENT_PERCENTAGE = 0.1f; //Increase rectangle to get items

    float ASTEROID_HIT_POINTS = 70f;
    float ASTEROID_SPEED = 200f;
    int ASTEROID_SHOWER = 10;
    float ASTEROID_POINTS_SCORE = 1000f;

    float FIGHTER_HIT_POINTS = 25f;
    float FIGHTER_SPEED = 5f;
    float FIGHTER_FIRE_RATE = 1.5f;
    float FIGHTER_BEAM_DAMAGE = 20f;
    float FIGHTER_BEAM_SPEED = 100f;
    float FIGHTER_POINTS_SCORE = 100f;

    float GUN_TURRET_HIT_POINTS = 50f;
    float GUN_TURRET_FIRE_RATE = 2f;
    float GUN_TURRET_SPEED = 2f;
    float GUN_TURRET_SCALE = 1.5f;
    float GUN_TURRET_MISSILE_DAMAGE = 80f;
    float GUN_TURRET_MISSILE_SPEED = 200f;
    float GUN_TURRET_POINTS_SCORE = 200f;

    float KAMIKAZE_SPEED = 250f;
    float KAMIKAZE_HIT_POINTS = 40f;
    float KAMIKAZE_SCALE = 1.5f;
    float KAMIKAZE_POINTS_SCORE = 100f;

    float FRIGATE_SPEED = 200f;
    float FRIGATE_HIT_POINTS = 100f;
    float FRIGATE_SCALE = 1.5f;
    float FRIGATE_RAY_DAMAGE = 20f;
    float FRIGATE_RAY_SPEED = 200f;
    float FRIGATE_POINTS_SCORE = 200f;
    float FRIGATE_FIRE_RATE = 6f;
    float RAY_HOLD_DURATION = 4f;

    float DREADNOUGHT_SPEED = 300f;
    float DREADNOUGHT_HIT_POINTS = 400f;
    float DREADNOUGHT_SCALE = 2f;
    float DREADNOUGHT_PROTON_DAMAGE = 5f;
    float DREADNOUGHT_PROTON_SPEED = 200f;
    float DREADNOUGHT_PROTON_SCALE = 1.5f;
    float DREADNOUGHT_POINTS_SCORE = 600f;
    float DREADNOUGHT_FIRE_RATE = 1.5f;
    float DREADNOUGHT_LIMIT_POSITION_Y = 300f;
    Vector2 DIRECTION_CENTER = new Vector2(0, -1).nor();
    Vector2 DIRECTION_LEFT = new Vector2(-1, -1).nor();
    Vector2 DIRECTION_RIGHT = new Vector2(1, -1).nor();


    float POWER_UP_SPEED = 200f;
    float POWER_UP_SCALE = 1.5f;
    int POWER_UP_DUPLICATED_BONUS_POINTS = 1000;

    //Gamepad
    float INPUT_DELAY = 0.2f;
    int X_BUTTON = 0;
    int UP_PAD = 11;
    int DOWN_PAD = 12;
    int LEFT_PAD = 13;
    int RIGHT_PAD = 14;


    //Enum
    enum STATUS { OUT, ACTIVE, DESTROYED, INACTIVE}
    enum ENEMY_TYPE {ASTEROID, GUN_TURRET, FIGHTER_SQUADRON, KAMIKAZE, FRIGATE, DREADNOUGHT}
    enum FORMATION {RIGHT, LEFT,FRONT}
    enum CHARACTER_TYPE {RANGER, ASTEROID, GUN_TURRET, FIGHTER, KAMIKAZE, FRIGATE, DREADNOUGHT}
    enum POWER_UP {DAMAGE, HEALTH, SHIELD}
    enum RAY_STATE { GROWING, HOLDING }
}
