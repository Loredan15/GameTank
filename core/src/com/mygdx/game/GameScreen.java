package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.unit.BotTank;
import com.mygdx.game.unit.PlayerTank;
import com.mygdx.game.unit.Tank;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font24;
    private Map map;
    private PlayerTank player;
    private BulletEmitter bulletEmitter;
    private BotEmitter botEmitter;
    private float gameTimer;
    private float worldTimer;
    private Stage stage;
    private boolean paused;
    private Vector2 mousePosition;
    private TextureRegion cursor;

    private static final boolean FRIENDLY_FIRE = false;


    public Map getMap() {
        return map;
    }

    public PlayerTank getPlayer() {
        return player;
    }

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }

    public Vector2 getMousePosition() {
        return mousePosition;
    }

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        TextureAtlas atlas = new TextureAtlas("game.pack");
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        cursor = new TextureRegion(atlas.findRegion("cursor"));
        map = new Map(atlas);
        player = new PlayerTank(this, atlas);
        bulletEmitter = new BulletEmitter(atlas);
        botEmitter = new BotEmitter(this, atlas);
        gameTimer = 6.0f;
        stage = new Stage();
        mousePosition = new Vector2();

        Skin skin = new Skin();
        skin.add("simpleButton", new TextureRegion(atlas.findRegion("SimpleButton")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;

        Group group = new Group();
        final TextButton pauseButton = new TextButton("Pause", textButtonStyle);
        final TextButton exitButton = new TextButton("Exit", textButtonStyle);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = !paused;
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        pauseButton.setPosition(0, 40);
        exitButton.setPosition(0, 0);
        group.addActor(pauseButton);
        group.addActor(exitButton);
        group.setPosition(1130, 640);
        stage.addActor(group);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Слежение за игроком
//        ScreenManager.getInstance().getCamera().position.set(player.getPosition().x / 2, player.getPosition().y / 2, 0);
//        ScreenManager.getInstance().getCamera().update();

        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);
        batch.begin();

        map.render(batch);
        player.render(batch);
        botEmitter.render(batch);
        bulletEmitter.render(batch);
        player.renderHUD(batch, font24);


        batch.draw(cursor, mousePosition.x - cursor.getRegionWidth() / 2, mousePosition.y - cursor.getRegionHeight() / 2, cursor.getRegionWidth() / 2, cursor.getRegionHeight() / 2, cursor.getRegionWidth(), cursor.getRegionHeight(), 1, 1, -worldTimer * 45);
        batch.end();

        stage.draw();
    }

    public void update(float dt) {
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
        ScreenManager.getInstance().getViewport().unproject(mousePosition);
        worldTimer += dt;
        if (!paused) {
            gameTimer += dt;
            if (gameTimer > 5.0f) {
                gameTimer = 0;

                float coordx, coordy;
                do {
                    coordx = MathUtils.random(0, Gdx.graphics.getWidth());
                    coordy = MathUtils.random(0, Gdx.graphics.getHeight());
                } while (!map.isAreaClear(coordx, coordy, 20));

                botEmitter.activate(coordx, coordy);
            }
            player.update(dt);
            botEmitter.update(dt);
            bulletEmitter.update(dt);
            checkCollisions();
        }
        stage.act(dt);
    }

    public void checkCollisions() {
        for (int i = 0; i < bulletEmitter.getBullets().length; i++) {
            Bullet bullet = bulletEmitter.getBullets()[i];
            if (bullet.isActive()) {
                for (int j = 0; j < botEmitter.getBots().length; j++) {
                    BotTank bot = botEmitter.getBots()[j];
                    if (bot.isActive()) {
                        if (checkBulletAndTank(bot, bullet) && bot.getCircle().contains(bullet.getPosition())) {
                            bullet.deactivate();
                            bot.takeDamage(bullet.getDamage());
                            break;
                        }
                    }
                }
                if (checkBulletAndTank(player, bullet) && player.getCircle().contains(bullet.getPosition())) {
                    bullet.deactivate();
                    player.takeDamage(bullet.getDamage());
                }
                map.checkWallAndBulletCollision(bullet);
            }
        }
    }

    public boolean checkBulletAndTank(Tank tank, Bullet bullet) {
        if (!FRIENDLY_FIRE) {
            return tank.getOwnerType() != bullet.getOwner().getOwnerType();
        } else {
            return tank != bullet.getOwner();
        }
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
