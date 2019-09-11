package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Vector2 position;
    private Vector2 velocity;
    private int damage;
    private boolean active;

    public int getDamage() {
        return damage;
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

    public void activate(float x, float y, float vx, float vy, int damage) {
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

