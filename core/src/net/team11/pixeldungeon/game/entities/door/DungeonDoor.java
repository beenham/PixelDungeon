package net.team11.pixeldungeon.game.entities.door;

import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.game.entity.component.InteractionComponent;
import net.team11.pixeldungeon.game.entity.component.InventoryComponent;
import net.team11.pixeldungeon.game.entities.player.Player;
import net.team11.pixeldungeon.game.items.keys.DungeonKey;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.assets.Messages;

public class DungeonDoor extends Door {
    private DungeonKey doorKey;

    public DungeonDoor (String name, Rectangle bounds) {
        super(name, bounds, Type.DUNGEON, false);
        this.doorKey = new DungeonKey();
        this.addComponent(new InteractionComponent(this));
    }

    public void doInteraction(Player player){
        if (!open) {
            if (player.getComponent(InventoryComponent.class).hasItem(doorKey)) {
                setOpened(true);
                player.getComponent(InventoryComponent.class).removeItem(doorKey);
            } else {
                String message = Messages.DOOR_NEED_DUNGEON_KEY;
                PlayScreen.uiManager.initTextBox(message);
            }
        }
    }

    @Override
    public String toString(){
        return "Name: " + getName() + "\nKey Name: " + doorKey.getName() + "\nKey ID: " + doorKey.getName();
    }
}
