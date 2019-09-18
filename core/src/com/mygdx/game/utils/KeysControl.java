package com.mygdx.game.utils;

import com.badlogic.gdx.Input;

public class KeysControl {
    public enum Targeting {
        MOUSE, KEYBOARD
    }

    private int up;
    private int down;
    private int left;
    private int right;

    private Targeting targeting;

    private int fire;
    private int roteteTurretLeft;
    private int roteteTurretRight;

    public KeysControl(int up, int down, int left, int right, Targeting targeting, int fire, int roteteTurretLeft, int roteteTurretRight) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.targeting = targeting;
        this.fire = fire;
        this.roteteTurretLeft = roteteTurretLeft;
        this.roteteTurretRight = roteteTurretRight;
    }

    public static KeysControl createStandartControl1() {
        KeysControl keysControl = new KeysControl(
                Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Targeting.MOUSE, 0, 0, 0
        );
        return keysControl;
    }

    public static KeysControl createStandartControl2() {
        KeysControl keysControl = new KeysControl(
                Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Targeting.KEYBOARD, Input.Keys.U, Input.Keys.Y, Input.Keys.I
        );
        return keysControl;
    }

    public int getUp() {
        return up;
    }

    public int getDown() {
        return down;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public Targeting getTargeting() {
        return targeting;
    }

    public int getFire() {
        return fire;
    }

    public int getRoteteTurretLeft() {
        return roteteTurretLeft;
    }

    public int getRoteteTurretRight() {
        return roteteTurretRight;
    }
}
