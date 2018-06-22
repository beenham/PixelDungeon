package net.team11.pixeldungeon.entities.blocks;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entity.component.InventoryComponent;
import net.team11.pixeldungeon.items.Coin;
import net.team11.pixeldungeon.items.Item;
import net.team11.pixeldungeon.items.keys.ChestKey;
import net.team11.pixeldungeon.items.keys.Key;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.assets.Messages;
import net.team11.pixeldungeon.utils.stats.CurrentStats;
import net.team11.pixeldungeon.utils.stats.StatsUtil;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.ChestComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.CollisionUtil;

import java.util.Locale;

public class Chest extends Entity {
    private boolean opened;
    private boolean locked;
    private boolean dungeonKey;
    private boolean looted;
    private ChestKey chestKey;
    private Item item;

    public Chest(Rectangle bounds, boolean opened, boolean locked, boolean dungeonKey, String name, Item item) {
        super(name);
        this.opened = opened;
        this.locked = locked;
        this.dungeonKey = dungeonKey;
        this.looted = false;
        this.item = item;
        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        AnimationComponent animationComponent;
        this.addComponent(new ChestComponent(this));
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0,
                (CollisionUtil.ENTITY),
                (byte)(CollisionUtil.ENTITY | CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));
        this.addComponent(animationComponent = new AnimationComponent(0));
        this.addComponent(new InteractionComponent(this));
        setupAnimations(animationComponent);
    }

    private void setupAnimations(AnimationComponent animationComponent) {
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(AssetName.CHEST_CLOSED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.CHEST_OPENING, textureAtlas, 1.25f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.CHEST_OPENED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.CHEST_LOOTED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.DUNGEON_CHEST_CLOSED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.DUNGEON_CHEST_OPENING, textureAtlas, 1.25f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.DUNGEON_CHEST_OPENED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.DUNGEON_CHEST_LOOTED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.LOCKED_CHEST_CLOSED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.LOCKED_CHEST_OPENING, textureAtlas, 1.25f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.LOCKED_CHEST_OPENED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.LOCKED_CHEST_LOOTED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.CHEST_EMPTY_OPENING, textureAtlas, 1.25f, Animation.PlayMode.NORMAL);
        if (dungeonKey) {
            animationComponent.setAnimation(AssetName.DUNGEON_CHEST_CLOSED);
        } else if (locked) {
            if (opened) {
                if (isEmpty()) {
                    animationComponent.setAnimation(AssetName.LOCKED_CHEST_LOOTED);
                    looted = true;
                } else {
                    animationComponent.setAnimation(AssetName.LOCKED_CHEST_OPENED);
                }
            } else {
                animationComponent.setAnimation(AssetName.LOCKED_CHEST_CLOSED);
            }
        } else {
            if (opened) {
                if (isEmpty()) {
                    animationComponent.setAnimation(AssetName.CHEST_LOOTED);
                    looted = true;
                } else {
                    animationComponent.setAnimation(AssetName.CHEST_OPENED);
                }
            } else {
                animationComponent.setAnimation(AssetName.CHEST_CLOSED);
            }
        }
    }

    public void setChestKey(ChestKey chestKey) {
        this.chestKey = chestKey;
    }

    public boolean isLocked(){
        return locked;
    }

    private void removeItem(boolean shouldRemove) {
        if (shouldRemove) {
            updateStats();
            item = null;
            looted = true;
        }
    }

    private boolean isEmpty() {
        return item == null;
    }

    public void setItem (Item item) {
        this.item = item;
    }

    public void doInteraction(Player player) {
        InventoryComponent inventory = player.getComponent(InventoryComponent.class);
        AnimationComponent animationComponent = getComponent(AnimationComponent.class);

        if (!looted) {
            if (dungeonKey) {
                if (opened) {
                    if (inventory.isFull()) {
                        String message = Messages.INVENTORY_FULL + "!\n" + Messages.CHEST_LOOT_LATER;
                        PlayScreen.uiManager.initTextBox(message);
                    } else {
                        initNotification();
                        removeItem(inventory.addItem(item));
                        animationComponent.setAnimation(AssetName.DUNGEON_CHEST_LOOTED);
                    }
                } else {
                    opened = true;
                    updateStats();
                    if (inventory.isFull()) {
                        String message = Messages.INVENTORY_FULL + "!\n" + Messages.CHEST_LOOT_LATER;
                        PlayScreen.uiManager.initTextBox(message);
                        animationComponent.setAnimation(AssetName.DUNGEON_CHEST_OPENING);
                        animationComponent.setNextAnimation(AssetName.DUNGEON_CHEST_OPENED);
                    } else {
                        initNotification();
                        removeItem(inventory.addItem(item));
                        animationComponent.setAnimation(AssetName.DUNGEON_CHEST_OPENING);
                        animationComponent.setNextAnimation(AssetName.DUNGEON_CHEST_LOOTED);
                    }
                }
            } else if (locked) {
                if (opened) {
                    if (inventory.isFull()) {
                        String message = Messages.INVENTORY_FULL + "!\n" + Messages.CHEST_LOOT_LATER;
                        PlayScreen.uiManager.initTextBox(message);
                    } else {
                        initNotification();
                        removeItem(inventory.addItem(item));
                        animationComponent.setAnimation(AssetName.LOCKED_CHEST_LOOTED);
                    }
                } else {
                    if (inventory.hasItem(chestKey)) {
                        inventory.removeItem(chestKey);
                        updateStats();
                        opened = true;
                        if (inventory.isFull()) {
                            String message = Messages.INVENTORY_FULL+ "!\n" + Messages.CHEST_LOOT_LATER;
                            PlayScreen.uiManager.initTextBox(message);
                            animationComponent.setAnimation(AssetName.LOCKED_CHEST_OPENING);
                            animationComponent.setNextAnimation(AssetName.LOCKED_CHEST_OPENED);
                        } else {
                            initNotification();
                            removeItem(inventory.addItem(item));
                            animationComponent.setAnimation(AssetName.LOCKED_CHEST_OPENING);
                            animationComponent.setNextAnimation(AssetName.LOCKED_CHEST_LOOTED);
                        }
                    } else {
                        String message = Messages.CHEST_NEED_KEY;
                        PlayScreen.uiManager.initTextBox(message);
                    }
                }
            } else {
                if (isEmpty()) {
                    updateStats();
                    looted = true;
                    String message = Messages.CHEST_IS_EMPTY;
                    PlayScreen.uiManager.initTextBox(message);
                    animationComponent.setAnimation(AssetName.CHEST_EMPTY_OPENING);
                    animationComponent.setNextAnimation(AssetName.CHEST_LOOTED);
                } else if (opened) {
                    if (inventory.isFull()) {
                        String message = Messages.INVENTORY_FULL + "!\n" + Messages.CHEST_LOOT_LATER;
                        PlayScreen.uiManager.initTextBox(message);
                    } else {
                        initNotification();
                        removeItem(inventory.addItem(item));
                        animationComponent.setAnimation(AssetName.CHEST_LOOTED);
                    }
                } else {
                    opened = true;
                    updateStats();
                    if (inventory.isFull()) {
                        String message = Messages.INVENTORY_FULL + "!\n" + Messages.CHEST_LOOT_LATER;
                        PlayScreen.uiManager.initTextBox(message);
                        animationComponent.setAnimation(AssetName.CHEST_OPENING);
                        animationComponent.setNextAnimation(AssetName.CHEST_OPENED);
                    } else {
                        initNotification();
                        removeItem(inventory.addItem(item));
                        animationComponent.setAnimation(AssetName.CHEST_OPENING);
                        animationComponent.setNextAnimation(AssetName.CHEST_LOOTED);
                    }
                }
            }
        } else {
            String message = Messages.CHEST_IS_LOOTED;
            PlayScreen.uiManager.initTextBox(message);
        }
    }

    private void initNotification() {
        String message;
        if (item.getAmount() > 1) {
            message = String.format(Locale.UK, Messages.ITEM_FIND_MULTIPLE+"!", item.getAmount(),item.getName());
        } else {
            message = String.format(Locale.UK, Messages.ITEM_FIND_ONE+"!", item.getName());
        }
        PlayScreen.uiManager.initItemReceiver(item,message);
    }

    private void updateStats() {
        CurrentStats stats = StatsUtil.getInstance().getCurrentStats();
        stats.addChest(getName());
        if (item != null) {
            if (item instanceof Key) {
                System.out.println("FOUND KEY");
                StatsUtil.getInstance().getCurrentStats().incrementKeys();
                StatsUtil.getInstance().getGlobalStats().incrementKeysFound();
                stats.addKey(item.getName());
            } else if (!(item instanceof Coin)) {
                StatsUtil.getInstance().getCurrentStats().incrementItems();
                StatsUtil.getInstance().getGlobalStats().incrementItemsFound();
                stats.addItem(item.getName());
            }
        }
        stats.incrementChests();
        StatsUtil.getInstance().getGlobalStats().incrementChestsFound();
        StatsUtil.getInstance().writeGlobalStats();
        StatsUtil.getInstance().saveTimer();
    }
}
