package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Unit.BotTank;

public class BotEmitter {

    private BotTank[] bots;

    public BotTank[] getBots() {
        return bots;
    }

    public static final int MAX_BOT_COUNT = 200;

    public BotEmitter(TankRpgGame game) {
        this.bots = new BotTank[MAX_BOT_COUNT];
        for (int i = 0; i < bots.length; i++) {
            this.bots[i] = new BotTank(game);
        }
    }

    public void activate(float x, float y) {
        for (int i = 0; i < bots.length; i++) {
            if (!bots[i].isActive()) {
                bots[i].activate(x, y);
                break;
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < bots.length; i++) {
            if (bots[i].isActive()) {
                bots[i].render(batch);
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < bots.length; i++) {
            if (bots[i].isActive()) {
                bots[i].update(dt);
            }
        }
    }
}
