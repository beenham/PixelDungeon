package net.team11.pixeldungeon.items;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.UUID;

public class Item {
    private ClickListener listener = new ClickListener();

    protected String name;
    protected int amount = 0;
    private boolean interactive;
    private boolean dungeonOnly;
    protected Image image;
    private UUID uuid;

    protected Item(String name, boolean dungeonOnly){
        this.name = name;
        this.dungeonOnly = dungeonOnly;
        this.uuid = UUID.randomUUID();
        this.interactive = false;
    }

    public Item(Item item) {
        this.name = item.name;
        this.image = item.image;
        this.amount = item.amount;
        this.interactive = item.interactive;
        this.dungeonOnly = item.dungeonOnly;
        this.image = item.image;
        this.uuid = item.uuid;
    }

    public Image getIcon() {
        return image;
    }

    public String getName() {
        return this.name;
    }

    public boolean isDungeonOnly() {
        return dungeonOnly;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isInteractive() {
        return interactive;
    }

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
        image.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (Item.this.listener != null) {
                    return Item.this.listener.touchDown(event,x,y,pointer,button);
                } else {
                    return super.touchDown(event,x,y,pointer,button);
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (Item.this.listener != null) {
                    Item.this.listener.touchUp(event, x, y, pointer, button);
                } else {
                    super.touchUp(event, x, y, pointer, button);
                }
            }
        });
    }

    @Override
    public String toString() {
        return name + " : " + uuid;
    }
}
