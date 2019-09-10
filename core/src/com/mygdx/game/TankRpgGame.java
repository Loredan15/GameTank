package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TankRpgGame extends ApplicationAdapter {
    SpriteBatch batch;
    private Tank tank;

    //Прицеливание мышкой


    @Override
    public void create() {
        batch = new SpriteBatch();
        tank = new Tank();

    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        Gdx.gl.glClearColor(0, 0.6f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        tank.render(batch);

        batch.end();
    }

    public void update(float dt) {
        tank.update(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();

    }
}
