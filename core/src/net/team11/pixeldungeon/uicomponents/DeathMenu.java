package net.team11.pixeldungeon.uicomponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entity.system.RenderSystem;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionFade;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;

public class DeathMenu extends Stage {
    private ShapeRenderer shapeRenderer;
    private EntityEngine engine;
    private Player player;

    private boolean visible = false;
    private boolean loaded = false;

    private Table deathTable;
    private Image deathImage;
    private Animation<TextureRegion> deathAnimation;
    private String deathAnimationName;
    private float darken;
    private final float darkenTarget = 0.9f;
    private float delta;


    public DeathMenu(SpriteBatch spriteBatch, EntityEngine engine) {
        super(new FitViewport(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT, new OrthographicCamera()), spriteBatch);
        shapeRenderer = new ShapeRenderer();
        this.engine = engine;
        player = (Player) engine.getEntities(PlayerComponent.class).get(0);
        darken = 0.4f;
        delta = 0;
        loaded = false;
        deathAnimationName = AssetName.DEATH_FIRE;
        setupTable();
        addActor(deathTable);
    }

    private void setupTable() {
        float padding = 25 * PixelDungeon.SCALAR;

        Label titleLabel = new Label(Messages.DEATH_MESSAGE_CAMELCASE, new Label.LabelStyle(Assets.getInstance().getFont(Assets.P_FONT), Color.WHITE));
        titleLabel.setFontScale(1.5f * PixelDungeon.SCALAR);

        TextButton retryButton = new TextButton(Messages.RETRY, Assets.getInstance().getSkin(Assets.UI_SKIN));
        retryButton.getLabel().setFontScale(PixelDungeon.SCALAR);
        retryButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                player.respawn();
                return true;
            }
        });
        retryButton.setVisible(false);

        TextButton mainMenuButton = new TextButton(Messages.QUIT_TO_MAIN, Assets.getInstance().getSkin(Assets.UI_SKIN));
        mainMenuButton.getLabel().setFontScale(PixelDungeon.SCALAR);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getActors().removeIndex(0);
                setupDialog();
                addActor(deathTable);
                return true;
            }
        });
        mainMenuButton.setVisible(false);

        deathAnimation = new Animation<TextureRegion>(2f,
                Assets.getInstance().getPlayerTexture(Assets.PLAYER_DEATH)
                        .findRegions(deathAnimationName),
                Animation.PlayMode.LOOP);
        deathImage = new Image(deathAnimation.getKeyFrame(delta));
        float w = deathImage.getWidth() * 15 * PixelDungeon.SCALAR;
        float h = deathImage.getHeight() * 15 * PixelDungeon.SCALAR;

        deathTable = new Table();
        deathTable.add(titleLabel).pad(padding * 3);
        deathTable.row().pad(padding);
        deathTable.add(deathImage).pad(padding).size(w,h);
        deathTable.row().pad(padding);
        deathTable.add(retryButton).pad(padding);
        deathTable.row().pad(padding);
        deathTable.add(mainMenuButton).pad(padding);
        deathTable.pack();

        deathTable.setPosition(PixelDungeon.V_WIDTH/2 - deathTable.getWidth()/2,PixelDungeon.V_HEIGHT/2 - deathTable.getHeight()/2);
        deathImage.setVisible(true);
    }

    private void setupDialog() {
        float padding = 25 * PixelDungeon.SCALAR;

        Label titleLabel = new Label(Messages.ARE_YOU_SURE, new Label.LabelStyle(Assets.getInstance().getFont(Assets.P_FONT), Color.WHITE));
        titleLabel.setFontScale(1.5f * PixelDungeon.SCALAR);

        TextButton yesButton = new TextButton(Messages.YES, Assets.getInstance().getSkin(Assets.UI_SKIN));
        yesButton.getLabel().setFontScale(PixelDungeon.SCALAR);
        yesButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                engine.finish();
                ScreenManager.getInstance().changeScreen(ScreenEnum.MAIN_MENU,
                        ScreenTransitionFade.init(1f));
                return true;
            }
        });

        TextButton noButton = new TextButton(Messages.NO, Assets.getInstance().getSkin(Assets.UI_SKIN));
        noButton.getLabel().setFontScale(PixelDungeon.SCALAR);
        noButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getActors().removeIndex(0);
                setupTable();
                addActor(deathTable);
                return true;
            }
        });

        deathTable = new Table();
        deathTable.add(titleLabel).colspan(5).pad(padding,padding*7,padding,padding*7);
        deathTable.row().pad(padding);
        deathTable.add().pad(padding);
        deathTable.add(yesButton).pad(padding).size(yesButton.getPrefWidth()*2,yesButton.getPrefHeight()).right();
        deathTable.add().pad(padding/2);
        deathTable.add(noButton).pad(padding).size(yesButton.getPrefWidth()*2,yesButton.getPrefHeight()).left();
        deathTable.add().pad(padding);
        deathTable.setSize(PixelDungeon.V_WIDTH*4/5,PixelDungeon.V_HEIGHT*4/5);

        deathTable.setPosition(PixelDungeon.V_WIDTH/2 - deathTable.getWidth()/2,PixelDungeon.V_HEIGHT/2 - deathTable.getHeight()/2);
        deathImage.setVisible(false);
    }

    public void setVisible(boolean visible, String deathAnimation) {
        this.visible = visible;
        this.deathAnimationName = deathAnimation;
        if (visible) {
            loaded = false;
            delta = 0;
            darken = 0.4f;
            this.deathAnimation = new Animation<TextureRegion>(2f,
                    Assets.getInstance().getPlayerTexture(Assets.PLAYER_DEATH)
                            .findRegions(deathAnimationName),
                    Animation.PlayMode.LOOP);
            update(0);
            Gdx.input.setInputProcessor(this);
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void update(float delta) {
        if (visible) {
            if (darken < darkenTarget) {
                darken = darken + .01f;
            }
            if (darken >= darkenTarget && !loaded) {
                for (Cell cell : deathTable.getCells()) {
                    if (!cell.getActor().isVisible()) {
                        cell.getActor().setVisible(true);
                    }
                }
                loaded = true;
            }

            this.delta += delta * RenderSystem.FRAME_SPEED;

            if (deathImage.isVisible()) {
                deathTable.getCell(deathImage).setActor(
                        deathImage = new Image(deathAnimation.getKeyFrame(this.delta)));
            }
        }
    }

    @Override
    public void draw() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0,0,0,darken));
        shapeRenderer.rect(0,0,PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        super.draw();
    }
}
