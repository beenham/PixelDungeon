package net.team11.pixeldungeon.uicomponents;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.items.Item;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.uicomponents.inventory.InventoryUI;

public class UIManager {
    private SpriteBatch batch;
    private EntityEngine engine;
    private Player player;

    private Hud hud;
    private InventoryUI inventoryUI;
    private ItemSelector itemSelector;
    private PauseMenu pauseMenu;
    private TextBox textBox;

    public UIManager(SpriteBatch batch, EntityEngine engine, Player player) {
        this.batch = batch;
        this.engine = engine;
        this.player = player;

        this.hud = new Hud(batch);
        this.inventoryUI = new InventoryUI(player, batch);
        this.itemSelector = new ItemSelector(player, batch);
        this.pauseMenu = new PauseMenu(batch, engine);
        this.textBox = new TextBox(batch);
    }

    public void showInventory(){
        hud.setPressed(false);
        inventoryUI.setVisible(true);
    }

    public void showPauseMenu(boolean screenCall) {
        if (screenCall) {
            ScreenManager.getInstance().getScreen().pause();
        }
        hud.setPressed(false);
        pauseMenu.setVisible(true);
    }

    private void hideInventory() {
        inventoryUI.setVisible(false);
        hud.setVisible(true);
    }

    private void hidePauseMenu() {
        ScreenManager.getInstance().getScreen().resume();
        pauseMenu.setVisible(false);
        hud.setVisible(true);
    }

    public void initItemSelector(Item item, Entity entity) {
        hud.setPressed(false);
        pauseMenu.setVisible(false);
        inventoryUI.setVisible(false);
        itemSelector.init(item,entity);
    }

    public void initItemSelector(Class itemType, Entity entity) {
        hud.setPressed(false);
        pauseMenu.setVisible(false);
        inventoryUI.setVisible(false);
        itemSelector.init(itemType, entity);
    }

    public void initTextBox(String text) {
        hud.setPressed(false);
        hud.setVisible(false);
        textBox.init(text);
    }

    private void hideItemSelector() {
        itemSelector.setVisible(false);
        hud.setVisible(true);
    }

    private void hideTextBox() {
        textBox.setVisible(false);
        hud.setVisible(true);
    }

    public void update(float delta, boolean paused) {
        if (!paused) {
            if (inventoryUI.isBackPressed()) {
                hideInventory();
            } else if (itemSelector.isBackPressed()) {
                hideItemSelector();
            } else if (textBox.isBackPressed()) {
                hideTextBox();
            }
            hud.update(delta);
            inventoryUI.update();
            itemSelector.update();
            textBox.update(delta);
        }
        if (pauseMenu.isResumePressed()) {
            hidePauseMenu();
        }
    }

    public void draw() {
        if (hud.isVisible()) {
            hud.draw();
        }
        if (inventoryUI.isVisible()) {
            inventoryUI.draw();
        }
        if (itemSelector.isVisible()) {
            itemSelector.draw();
        }
        if (pauseMenu.isVisible()) {
            pauseMenu.draw();
        }
        if (textBox.isVisible()) {
            textBox.draw();
        }
    }

    public void dispose() {
        hud.dispose();
        inventoryUI.dispose();
        itemSelector.dispose();
        pauseMenu.dispose();
    }

    public Hud getHud() {
        return hud;
    }

    public InventoryUI getInventoryUI() {
        return inventoryUI;
    }

    public ItemSelector getItemSelector() {
        return itemSelector;
    }

    public PauseMenu getPauseMenu() {
        return pauseMenu;
    }
}
