package com.svalero.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GamepadManager extends ControllerAdapter {

    private static Controller controller;
    private static float axisLeftX = 0f;
    private static float axisLeftY = 0f;

    private static final boolean[] buttonStates = new boolean[20];

    private float inputCooldown = 0f;

    public void init() {
        if (Controllers.getControllers().size > 0) {
            controller = Controllers.getControllers().first();
            Controllers.addListener(new GamepadManager());
            Gdx.app.log("ControllerManager", "Controller connected: " + controller.getName());
        }else
            Gdx.app.log("ControllerManager", "No controller detected");
    }

    public boolean isControllerConnected() {
        return controller != null;
    }

    public float getAxisLeftX() {
        return axisLeftX;
    }

    public float getAxisLeftY() {
        return axisLeftY;
    }

    public boolean isButtonPressed(int button) {
        return buttonStates[button];
    }

    public boolean isPausePressed() {
        return buttonStates[6];
    }

    public int getButtonCount() {
        return buttonStates.length;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisIndex, float value) {
        if (axisIndex == 0) axisLeftX = value;
        if (axisIndex == 1) axisLeftY = value;
        return true;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (buttonCode < buttonStates.length)
            buttonStates[buttonCode] = true;
        return true;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        if (buttonCode < buttonStates.length)
            buttonStates[buttonCode] = false;
        return true;
    }

    public void update(float dt) {
        if (inputCooldown > 0f) {
            inputCooldown -= dt;
        }
    }

    public void setCooldown(float cooldown) {
        this.inputCooldown = cooldown;
    }

    public boolean isReady() {
        return inputCooldown <= 0f;
    }

}
