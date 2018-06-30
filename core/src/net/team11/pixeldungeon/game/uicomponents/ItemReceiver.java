package net.team11.pixeldungeon.game.uicomponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.game.items.Item;
import net.team11.pixeldungeon.game.uicomponents.inventory.InventorySlot;
import net.team11.pixeldungeon.utils.assets.Assets;

public class ItemReceiver extends Stage {
    private ShapeRenderer shapeRenderer;
    private Label messageLabel;

    private Item item;

    private boolean visible = false;
    private boolean backPressed = false;
    private InventorySlot itemSlot;
    private Table itemTable;

    public ItemReceiver(SpriteBatch spriteBatch) {
        super(new FitViewport(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT, new OrthographicCamera()), spriteBatch);
        shapeRenderer = new ShapeRenderer();
        itemTable = new Table();
        itemSlot = new InventorySlot();
        setupLabel();
        setupTable();
        addActor(itemTable);
    }

    private void setupLabel() {
        messageLabel = new Label("", Assets.getInstance().getSkin(Assets.UI_SKIN));
        messageLabel.setAlignment(Align.center);
        messageLabel.setWrap(true);
        messageLabel.setFontScale(PixelDungeon.SCALAR);

        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return backPressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                backPressed = false;
            }
        });
    }

    private void setupTable() {
        float padding = 50 * PixelDungeon.SCALAR;
        itemTable.reset();
        itemTable.add(messageLabel).left().pad(padding).expandX().fillX().center();
        itemTable.row();
        itemTable.add(itemSlot).pad(padding).center();
        itemTable.setSize(PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        itemTable.setPosition(PixelDungeon.V_WIDTH/2 - itemTable.getWidth()/2,PixelDungeon.V_HEIGHT/2 - itemTable.getHeight()/2);
    }

    public void init(Item item, String message) {
        messageLabel.setText(message);
        this.item = item;
        itemSlot.setItem(item,2);
        setupTable();
        setVisible(true);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        backPressed = false;
        if (!visible) {
            itemSlot.setItem(null);
        } else {
            Gdx.input.setInputProcessor(this);
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isBackPressed() {
        return backPressed;
    }

    public void update() {
        if (visible) {
            itemSlot.setItem(item,2);
        }
    }

    @Override
    public void draw() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0,0,0,0.60f));
        shapeRenderer.rect(0,0,PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        super.draw();
    }
}
