package com.mygdx.game.unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.TankRpgGame;
import com.mygdx.game.Weapon;
import com.mygdx.game.utils.TankOwner;

public class PlayerTank extends Tank {

    int lifes;

    public PlayerTank(TankRpgGame game, TextureAtlas atlas) {
        super(game);
        this.ownerType = TankOwner.PLAYER;
        this.weapon = new Weapon(atlas);
        this.texture = atlas.findRegion("playerTankBase");
        this.textureHp = atlas.findRegion("bar");
        this.position = new Vector2(100.0f, 100.0f);
        this.speed = 100.0f;
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.hpMax = 10;
        this.hp = this.hpMax;
        this.circle = new Circle(position.x, position.y, width + height / 2);
        this.lifes = 5;
    }

    public void update(float dt) {
        checkMovement(dt);
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        rotateTurretToPoint(mx, my, dt);
        //isKeyJustPressed - отслеживает нажатие кнопки, зажать нельзя
        if (Gdx.input.isTouched()) {
            fire(dt);
        }
        super.update(dt);
    }

    @Override
    public void destroy() {
        lifes--;
        hp = hpMax;
    }

    public void checkMovement(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            position.x -= speed * dt;
            angle = 180.0f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            position.x += speed * dt;
            angle = 0.0f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            position.y += speed * dt;
            angle = 90.0f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            position.y -= speed * dt;
            angle = 270.0f;
        }
    }
}
