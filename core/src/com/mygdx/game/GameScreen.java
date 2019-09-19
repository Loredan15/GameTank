package com.mygdx.game;

import com.badlogic.gdx.Gdx;
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
import com.mygdx.game.utils.GameType;
import com.mygdx.game.utils.KeysControl;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends AbstractScreen {
    private SpriteBatch batch;
    private BitmapFont font24;
    private TextureAtlas atlas;
    private Map map;
    private GameType gameType;

    private List<PlayerTank> players;
    private BulletEmitter bulletEmitter;
    private ItemEmitter itemEmitter;
    private BotEmitter botEmitter;
    private float gameTimer;
    private float worldTimer;
    private Stage stage;
    private boolean paused;
    private Vector2 mousePosition;
    private TextureRegion cursor;

//    private Sound sound;
//    private Music music;

    private static final boolean FRIENDLY_FIRE = false;

    public ItemEmitter getItemEmitter() {
        return itemEmitter;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public Map getMap() {
        return map;
    }

    public List<PlayerTank> getPlayers() {
        return players;
    }

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }

    public Vector2 getMousePosition() {
        return mousePosition;
    }

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
        this.gameType = GameType.ONE_PLAYER;
    }

    @Override
    public void show() {
//        sound = Gdx.audio.newSound(Gdx.files.internal("boom.wav"));
//        music = Gdx.audio.newMusic(Gdx.files.internal("song.mp3"));
//        sound.play();
//        music.play();
        atlas = new TextureAtlas("game.pack");
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        cursor = new TextureRegion(atlas.findRegion("cursor"));
        map = new Map(atlas);
        players = new ArrayList<>();
        players.add(new PlayerTank(1, this, KeysControl.createStandartControl1(), atlas));
        if (gameType == GameType.TWO_PLAYERS) {
            players.add(new PlayerTank(2, this, KeysControl.createStandartControl2(), atlas));
        }
        bulletEmitter = new BulletEmitter(atlas);
        itemEmitter = new ItemEmitter(atlas);
        botEmitter = new BotEmitter(this, atlas);
        gameTimer = 100.0f;
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
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.MENU);
            }
        });

        pauseButton.setPosition(0, 40);
        exitButton.setPosition(0, 0);
        group.addActor(pauseButton);
        group.addActor(exitButton);
        group.setPosition(1130, 640);
        stage.addActor(group);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCursorCatched(true);
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
        for (int i = 0; i < players.size(); i++) {
            players.get(i).render(batch);
        }
        itemEmitter.render(batch);

        botEmitter.render(batch);
        bulletEmitter.render(batch);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).renderHUD(batch, font24);
        }
        batch.end();
        stage.draw();

        batch.begin();
        batch.draw(cursor, mousePosition.x - cursor.getRegionWidth() / 2, mousePosition.y - cursor.getRegionHeight() / 2, cursor.getRegionWidth() / 2, cursor.getRegionHeight() / 2, cursor.getRegionWidth(), cursor.getRegionHeight(), 1, 1, -worldTimer * 45);
        batch.end();
    }

    public void update(float dt) {
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
        ScreenManager.getInstance().getViewport().unproject(mousePosition);
        worldTimer += dt;
        if (!paused) {
            gameTimer += dt;
            if (gameTimer > 15.0f) {
                gameTimer = 0;

                for (int i = 0; i < 5; i++) {
                    float coordx, coordy;
                    do {
                        coordx = MathUtils.random(0, Gdx.graphics.getWidth());
                        coordy = MathUtils.random(0, Gdx.graphics.getHeight());
                    } while (!map.isAreaClear(coordx, coordy, 20));

                    botEmitter.activate(coordx, coordy);
                }
            }

            for (int i = 0; i < players.size(); i++) {
                players.get(i).update(dt);
            }
            botEmitter.update(dt);
            bulletEmitter.update(dt);
            itemEmitter.update(dt);
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
                for (int j = 0; j < players.size(); j++) {
                    PlayerTank player = players.get(j);
                    if (checkBulletAndTank(player, bullet) && player.getCircle().contains(bullet.getPosition())) {
                        bullet.deactivate();
                        player.takeDamage(bullet.getDamage());
                    }
                }
                map.checkWallAndBulletCollision(bullet);
            }
        }

        for (int i = 0; i < itemEmitter.getItems().length; i++) {
            if (itemEmitter.getItems()[i].isActive()) {
                Item item = itemEmitter.getItems()[i];
                for (int j = 0; j < players.size(); j++) {
                    if (players.get(j).getCircle().contains(item.getPosition())) {
                        players.get(j).consumePowerUp(item);
                        item.deactivate();
                        break;
                    }
                }
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
    public void dispose() {
        atlas.dispose();
        font24.dispose();
    }
}
