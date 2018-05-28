package net.team11.pixeldungeon.entities.door;

import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.entity.component.InventoryComponent;
import net.team11.pixeldungeon.items.keys.DoorKey;

public class LockedDoor extends Door {
    private DoorKey doorKey;

    public LockedDoor (String name, Rectangle bounds, boolean open, String keyName) {
        super(name, bounds, Type.LOCKED, open);
        this.doorKey = new DoorKey(keyName);
        this.addComponent(new InteractionComponent(this));
    }

    public void doInteraction(Player player){
        //System.out.println("ATTEMPTING UNLOCK");
        if (!player.getComponent(InventoryComponent.class).getItems().isEmpty()) {
//            System.out.println("--KeyID--> " + doorKey.getName()+" --");
//            System.out.println(player.getComponent(InventoryComponent.class).hasItem(doorKey.getName(), Key.class));
            if (player.getComponent(InventoryComponent.class).hasItem(doorKey)){
                setOpened(true);
//                System.out.println("Attempting Remove");
                player.getComponent(InventoryComponent.class).removeItem(doorKey);
//                GhostItem ghostItem = new GhostItem("");
//                System.out.println("Ghost add: " + player.getComponent(InventoryComponent.class).addItem(ghostItem));
                //System.out.println("Ghost remove: " + player.getComponent(InventoryComponent.class).removeItem(ghostItem));

            }
        }

    }

    @Override
    public String toString(){
        return "Name: " + getName() + "\nKey Name: " + doorKey.getName() + "\nKey ID: " + doorKey.getName();
    }
}
