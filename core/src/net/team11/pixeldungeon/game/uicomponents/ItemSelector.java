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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.game.entities.player.Player;
import net.team11.pixeldungeon.game.entities.puzzle.colouredgems.GemPillar;
import net.team11.pixeldungeon.game.entity.component.InventoryComponent;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.game.items.Item;
import net.team11.pixeldungeon.game.items.puzzleitems.ColouredGem;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.game.uicomponents.inventory.InventorySlot;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;

import java.util.ArrayList;

public class ItemSelector extends Stage {
    private ShapeRenderer shapeRenderer;
    private InventoryComponent inventory;

    private boolean visible = false;
    private boolean backPressed = false;
    private Array<InventorySlot> slotArray = new Array<>(InventoryComponent.MAX_SIZE);
    private Table inventoryTable = new Table();

    private Class itemType;
    private Entity callingEntity;
    private ArrayList<Item> items = new ArrayList<>();

    public ItemSelector(Player player, SpriteBatch spriteBatch) {
        super(new FitViewport(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT, new OrthographicCamera()), spriteBatch);
        this.inventory = player.getComponent(InventoryComponent.class);
        shapeRenderer = new ShapeRenderer();
        setupCompleteTable();
        addActor(inventoryTable);
    }

    private void setupCompleteTable() {
        inventoryTable.reset();
        Label titleLabel = new Label(Messages.ITEMSELECT_CAMELCASE,
                Assets.getInstance().getSkin(Assets.UI_SKIN));
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

        TextButton backLabel = new TextButton(Messages.BACK_CAMELCASE, Assets.getInstance().getSkin(Assets.UI_SKIN));
        backLabel.getLabel().setFontScale(1.2f * PixelDungeon.SCALAR);
        backLabel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return backPressed = true;
            }
        });

        inventoryTable.add(backLabel).colspan(5).right().pad(20 * PixelDungeon.SCALAR);
        inventoryTable.setBackground(new NinePatchDrawable(Assets.getInstance().getTextureSet(
                Assets.HUD).createPatch("itemSlot")));
        inventoryTable.pack();

        inventoryTable.setPosition(PixelDungeon.V_WIDTH/2 - inventoryTable.getWidth()/2,PixelDungeon.V_HEIGHT/2 - inventoryTable.getHeight()/2);
    }

    private void setupSingleTable() {
        inventoryTable.reset();
        Label titleLabel = new Label(Messages.ITEMSELECT_CAMELCASE,
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        titleLabel.setFontScale(1.2f * PixelDungeon.SCALAR);
        inventoryTable.add(titleLabel).colspan(5).left().pad(40 * PixelDungeon.SCALAR);
        inventoryTable.row();
        inventoryTable.add(slotArray.get(0)).pad(15 * PixelDungeon.SCALAR).colspan(5);
        inventoryTable.row().padTop(20 * PixelDungeon.SCALAR);

        TextButton backLabel = new TextButton(Messages.BACK_CAMELCASE, Assets.getInstance().getSkin(Assets.UI_SKIN));
        backLabel.getLabel().setFontScale(1.2f * PixelDungeon.SCALAR);
        backLabel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return backPressed = true;
            }
        });

        inventoryTable.add(backLabel).colspan(5).pad(20 * PixelDungeon.SCALAR);
        inventoryTable.setBackground(new NinePatchDrawable(Assets.getInstance().getTextureSet(
                Assets.HUD).createPatch("itemSlot")));
        inventoryTable.pack();

        inventoryTable.setPosition(PixelDungeon.V_WIDTH/2 - inventoryTable.getWidth()/2,PixelDungeon.V_HEIGHT/2 - inventoryTable.getHeight()/2);
    }

    public void init(Class itemType, Entity entity) {
        this.itemType = itemType;
        this.callingEntity = entity;
        setupDisplay();
        setupCompleteTable();
        setVisible(true);
    }

    public void init(Item item, Entity entity) {
        System.out.println(item);
        this.callingEntity = entity;
        setupDisplay(item);
        setupSingleTable();
        setVisible(true);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        backPressed = false;
        if (!visible) {
            for (int i = 0; i < InventoryComponent.MAX_SIZE ; i++) {
                slotArray.get(i).setItem(null);
            }
            for (Item item : items) {
                item.setListener(null);
            }
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
            for (int i = 0; i < InventoryComponent.MAX_SIZE; i++) {
                if (i < items.size()) {
                    slotArray.get(i).setItem(items.get(i));
                } else {
                    slotArray.get(i).setItem(null);
                }
            }
        }
    }

    private void setupDisplay() {
        //resetListeners();
        items = new ArrayList<>();

        for (int i = 0; i < InventoryComponent.MAX_SIZE; i++) {
            if (i < inventory.getItems().size()) {
                if (inventory.getItems().get(i).getClass().equals(itemType)) {
                    final Item item = inventory.getItems().get(i);
                    items.add(item);
                    item.setListener(new ClickListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            if (!backPressed) {
                                if (callingEntity instanceof GemPillar && item instanceof ColouredGem) {
                                    ((GemPillar) callingEntity).setGem((ColouredGem) item);
                                    inventory.removeItem(item);
                                }
                                backPressed = true;
                            }
                            return super.touchDown(event, x, y, pointer, button);
                        }

                        @Override
                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            if (backPressed) {
                                item.setListener(null);
                            }
                        }
                    });
                }
            }
        }
    }

    private void setupDisplay (final Item item) {
        items = new ArrayList<>();
        items.add(item);
        item.setListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!backPressed) {
                    if (callingEntity instanceof GemPillar && item instanceof ColouredGem) {
                        if (!inventory.isFull()) {
                            inventory.addItem(((GemPillar) callingEntity).takeGem());
                        } else {
                            String message = Messages.INVENTORY_FULL;
                            PlayScreen.uiManager.initTextBox(message);
                        }
                    }
                    backPressed = true;
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (backPressed) {
                    item.setListener(null);
                }
            }
        });
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