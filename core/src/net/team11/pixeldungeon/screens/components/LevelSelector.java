package net.team11.pixeldungeon.screens.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.map.Map;
import net.team11.pixeldungeon.map.MapManager;
import net.team11.pixeldungeon.utils.Assets;

public class LevelSelector extends Table {
    private MapManager mapManager;
    private Map currentMap;

    private Image mapPreview;
    private Button leftButton, rightButton;

    public LevelSelector(float size) {
        setDebug(true);

        mapManager = MapManager.getInstance();
        currentMap = mapManager.getFirstMap();
        mapPreview = new Image(Assets.getInstance().getTextureSet(
                Assets.LEVELS).findRegion(currentMap.getMapName()));

        leftButton = new TextButton("<",Assets.getInstance().getSkin(Assets.UI_SKIN));
        leftButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                prevMap();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        rightButton = new TextButton(">",Assets.getInstance().getSkin(Assets.UI_SKIN));
        rightButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                nextMap();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        setupTable(size/2);
    }

    private void setupTable(float size) {
        add(leftButton).size(size/4);
        add(mapPreview).size(size).pad(20 * PixelDungeon.SCALAR);
        add(rightButton).size(size/4);
        pack();
    }

    public void nextMap() {
        currentMap = mapManager.getNext(currentMap.getMapName());
        mapPreview.setDrawable(new SpriteDrawable(new Sprite(Assets.getInstance().getTextureSet(
                Assets.LEVELS).findRegion(currentMap.getMapName()))));
    }

    public void prevMap() {
        currentMap = mapManager.getPrevious(currentMap.getMapName());
        mapPreview.setDrawable(new SpriteDrawable(new Sprite(Assets.getInstance().getTextureSet(
                        Assets.LEVELS).findRegion(currentMap.getMapName()))));
    }

    public Map getMap() {
        return currentMap;
    }
}
