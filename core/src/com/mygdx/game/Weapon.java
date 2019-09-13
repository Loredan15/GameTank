package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon {
    private TextureRegion texture;
    private float firePeriod;
    private int damage;

    public TextureRegion getTexture() {
        return texture;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getDamage() {
        return damage;
    }

    public Weapon(TextureAtlas atlas) {
        this.texture = atlas.findRegion("simpleWeapon");
        this.firePeriod = 0.3f;
        this.damage = 1;
    }
}
