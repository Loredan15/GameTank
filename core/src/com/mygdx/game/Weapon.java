package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Weapon {
    private Texture texture;
    private float firePeriod;
    private int damage;

    public Texture getTexture() {
        return texture;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getDamage() {
        return damage;
    }

    public Weapon() {
        this.texture = new Texture("simple_weapon.png");
        this.firePeriod = 0.3f;
        this.damage = 1;
    }
}
