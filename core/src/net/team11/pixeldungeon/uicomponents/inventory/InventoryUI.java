package net.team11.pixeldungeon.uicomponents.inventory;

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
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entity.component.InventoryComponent;
import net.team11.pixeldungeon.utils.Assets;

public class InventoryUI extends Stage {
    private ShapeRenderer shapeRenderer;

    private InventoryComponent inventory;
    private boolean visible = false;
    private boolean backPressed;
    private Array<InventorySlot> slotArray = new Array<>(InventoryComponent.MAX_SIZE);

    private Table inventoryTable;

    public InventoryUI(Player player, SpriteBatch spriteBatch) {
        super(new FitViewport(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT, new OrthographicCamera()), spriteBatch);
        this.inventory = player.getComponent(InventoryComponent.class);
        shapeRenderer = new ShapeRenderer();
        setupTable();
        addActor(inventoryTable);
    }

    private void setupTable() {
        inventoryTable = new Table();

        Label titleLabel = new Label("INVENTORY", new Label.LabelStyle(Assets.getInstance().getFont(Assets.P_FONT), Color.WHITE));
        titleLabel.setFontScale(1.2f * PixelDungeon.SCALAR);
        inventoryTable.add(titleLabel).colspan(5).left().pad(40 * PixelDungeon.SCALAR);
        inventoryTable.row();

        for (int i = 0 ; i < InventoryComponent.MAX_SIZE ; i++) {
            if (i == 5) {
                inventoryTable.row();
            }
            slotArray.add(new InventorySlot());
            inventoryTable.add(slotArray.get(i)).pad(15 * PixelDungeon.SCALAR);
        }
        inventoryTable.row().padTop(20 * PixelDungeon.SCALAR);

        Label backLabel = new Label("BACK", new Label.LabelStyle(Assets.getInstance().getFont(Assets.P_FONT), Color.WHITE));
        backLabel.setFontScale(1.2f * PixelDungeon.SCALAR);
        backLabel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return backPressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                backPressed = false;
            }
        });

        inventoryTable.add(backLabel).colspan(5).right().pad(20 * PixelDungeon.SCALAR);

        inventoryTable.setBackground(new NinePatchDrawable(Assets.getInstance().getTextureSet(
                Assets.HUD).createPatch("itemSlot")));
        inventoryTable.pack();

        inventoryTable.setPosition(PixelDungeon.V_WIDTH/2 - inventoryTable.getWidth()/2,PixelDungeon.V_HEIGHT/2 - inventoryTable.getHeight()/2);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (visible) {
            backPressed = false;
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
        for (int i = 0 ; i < InventoryComponent.MAX_SIZE ; i++) {
            if (i < inventory.getItems().size()){
                slotArray.get(i).setItem(inventory.getItems().get(i));
            } else {
                slotArray.get(i).setItem(null);
            }
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
