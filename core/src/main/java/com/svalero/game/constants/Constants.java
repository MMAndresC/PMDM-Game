package com.svalero.game.constants;

import com.svalero.game.utils.AnimationInfo;

public interface Constants {

    //Skin
    String MENU_SKIN = "uiskin.json";

    //Atlas

    String PLAYER_ATLAS = "player.atlas";

    //Resources dir

    String BACKGROUNDS = "backgrounds";
    String UI = "ui";
    String PLAYER = "player";
    String FONTS = "fonts";

    //Files

    String MENU_BACKGROUND = "Main_menu.png";
    String LEVEL_1_BACKGROUND = "Level_1.png";

    //Regions

    AnimationInfo HERO_ENGINE_EFFECTS_IDLE = new AnimationInfo("Main-Ship-Engine-Effect-Idle", 4);
    AnimationInfo HERO_ENGINE_EFFECTS_POWERING = new AnimationInfo("Main-Ship-Engine-Effect-Powering", 4);
    String HERO_FULL_HEALTH = "Main-Ship-Full-health";
    String HERO_ENGINE = "Main-Ship-Engines-Supercharged";


    //Config UI

    int PADDING_BUTTON = 20;
    int PADDING_TITLE = 50;
    int WIDTH_BUTTON = 500;
    int HEIGHT_BUTTON = 120;

    //Config game

    float BACKGROUND_SPEED = 200f;
    float HERO_SPEED = 500f;
    float HERO_SCALE = 2f; //Double size

}
