package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.unit.Tank;

public class Bullet {
    private Tank owner;
    private Vector2 position;
    private Vector2 velocity;
    private int damage;
    private boolean active;

    public int getDamage() {
        return damage;
    }

    public Tank getOwner() {
        return owner;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isActive() {
        return active;
    }

    public Bullet() {
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.active = false;
        this.damage = 0;
    }


    public void deactivate() {
        this.active = false;
    }

    public void activate(Tank owner, float x, float y, float vx, float vy, int damage) {
        this.owner = owner;
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.active = true;
        this.damage = damage;
    }

    public void update(float dt) {
        //x += vx * dt;
        //y += vy * dt;
        position.mulAdd(velocity, dt);

        if (position.x < 0.0f || position.x > Gdx.graphics.getWidth() || position.y < 0.0f || position.y > Gdx.graphics.getHeight()) {
            active = false;
        }
    }
}

