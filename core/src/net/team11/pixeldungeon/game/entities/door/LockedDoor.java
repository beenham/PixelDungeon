package net.team11.pixeldungeon.game.entities.door;

import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.game.entities.player.Player;
import net.team11.pixeldungeon.game.entity.component.InteractionComponent;
import net.team11.pixeldungeon.game.entity.component.InventoryComponent;
import net.team11.pixeldungeon.game.items.keys.DoorKey;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.assets.Messages;

public class LockedDoor extends Door {
    private DoorKey doorKey;

    public LockedDoor (String name, Rectangle bounds, boolean open) {
        super(name, bounds, Type.LOCKED, open);
        this.doorKey = new DoorKey();
        this.addComponent(new InteractionComponent(this));
    }

    public void doInteraction(Player player){
        if (!open) {
            if (player.getComponent(InventoryComponent.class).hasItem(doorKey)) {
                setOpened(true);
                player.getComponent(InventoryComponent.class).removeItem(doorKey);
                String message = Messages.DOOR_USED_KEY;
                PlayScreen.uiManager.initTextBox(message);
            } else {
                String message = Messages.DOOR_NEED_KEY;
                PlayScreen.uiManager.initTextBox(message);
            }
        }
    }

    @Override
    public String toString(){
        return "Name: " + getName() + "\nKey Name: " + doorKey.getName() + "\nKey ID: " + doorKey.getName();
    }
}
