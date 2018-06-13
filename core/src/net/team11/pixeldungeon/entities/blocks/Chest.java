package net.team11.pixeldungeon.entities.blocks;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entity.component.InventoryComponent;
import net.team11.pixeldungeon.items.Item;
import net.team11.pixeldungeon.items.keys.ChestKey;
import net.team11.pixeldungeon.items.Key;
import net.team11.pixeldungeon.map.Map;
import net.team11.pixeldungeon.statistics.GlobalStatistics;
import net.team11.pixeldungeon.statistics.StatisticsUtil;
import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.ChestComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.utils.Assets;
import net.team11.pixeldungeon.utils.CollisionCategory;

public class Chest extends Entity {
    private boolean opened;
    private boolean locked;
    private ChestKey chestKey;
    private Item item;

    public Chest(Rectangle bounds, boolean opened, boolean locked, String name, Item item, Map parentMap) {
        super(name, parentMap);
        this.opened = opened;
        this.locked = locked;
        this.item = item;
        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        AnimationComponent animationComponent;
        this.addComponent(new ChestComponent(this));
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0,
                (CollisionCategory.ENTITY),
                (byte)(CollisionCategory.ENTITY | CollisionCategory.PUZZLE_AREA | CollisionCategory.BOUNDARY),
                BodyDef.BodyType.StaticBody));
        this.addComponent(animationComponent = new AnimationComponent(0));
        this.addComponent(new InteractionComponent(this));
        setupAnimations(animationComponent);
    }

    private void setupAnimations(AnimationComponent animationComponent) {
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(AssetName.CHEST_CLOSED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.CHEST_OPENED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        if (opened) {
            animationComponent.setAnimation(AssetName.CHEST_OPENED);
        } else {
            animationComponent.setAnimation(AssetName.CHEST_CLOSED);
        }
    }

    public void setChestKey(ChestKey chestKey) {
        this.chestKey = chestKey;
    }

    public boolean isLocked(){
        return locked;
    }

    public void removeItem(boolean shouldRemove) {
        if (shouldRemove) {

            //Update the statistics for the level
            getParentMap().getLevelStatistics().updateChests();

            //Update the global statistics
            GlobalStatistics.updateChests();
            StatisticsUtil.writeToJson(StatisticsUtil.getGlobalStatistics(), StatisticsUtil.globalLocation);
            if (item.getClass().equals(Key.class)){
                getParentMap().getLevelStatistics().updateKeys();
                GlobalStatistics.updateKeys();
            }

            item = null;

        } else {
            opened = false;
        }
    }

    public boolean isEmpty() {
        return item == null;
    }

    public void doInteraction(Player player) {

        //Check to see if it's locked or not

        if (locked && !opened){
            System.out.println("Key is :  " + chestKey.getName());
            //Do nothing we don't have the chestKey
            System.out.println("IS LOCKED");
            //Check to see if we have the chestKey
            if (player.getComponent(InventoryComponent.class).hasItem(chestKey)){
                locked = false;
                opened = true;
                player.getComponent(InventoryComponent.class).removeItem(chestKey);
                if (!isEmpty()) {
                    removeItem(player.getComponent(InventoryComponent.class).addItem(item));
                }
                getComponent(AnimationComponent.class).setAnimation(AssetName.CHEST_OPENED);
            } else {
                System.out.println("You do not have the chestKey");
            }
        } else if (!locked && !opened) {
            opened = true;
            if (!isEmpty()) {
                removeItem(player.getComponent(InventoryComponent.class).addItem(item));
            }
            getComponent(AnimationComponent.class).setAnimation(AssetName.CHEST_OPENED);
        }
    }
}
