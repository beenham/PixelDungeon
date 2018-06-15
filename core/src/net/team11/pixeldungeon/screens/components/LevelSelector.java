package net.team11.pixeldungeon.screens.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.map.Map;
import net.team11.pixeldungeon.map.MapManager;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionSplit;
import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.utils.Assets;

public class LevelSelector extends Table {
    private MapManager mapManager;
    private Map currentMap;

    private Image mapPreview;

    public LevelSelector(float size) {
        mapManager = MapManager.getInstance();
        currentMap = mapManager.getFirstMap();
        setupTable(size/2);
        setBackground(new NinePatchDrawable(Assets.getInstance().getTextureSet(
                Assets.HUD).createPatch(AssetName.DARKEN_20)));
    }

    private void setupTable(float size) {
        TextButton backButton = new TextButton("BACK", Assets.getInstance().getSkin(Assets.UI_SKIN));
        backButton.getLabel().setFontScale(1f * PixelDungeon.SCALAR);
        backButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().changeScreen(ScreenEnum.MAIN_MENU,
                        ScreenTransitionSplit.init(1f, false, Interpolation.pow2));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        mapPreview = new Image(Assets.getInstance().getTextureSet(
                Assets.LEVELS).findRegion(currentMap.getMapName()));


        TextButton leftButton = new TextButton("<", Assets.getInstance().getSkin(Assets.UI_SKIN));
        leftButton.getLabel().setFontScale(PixelDungeon.SCALAR);
        leftButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                prevMap();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        TextButton rightButton = new TextButton(">", Assets.getInstance().getSkin(Assets.UI_SKIN));
        rightButton.getLabel().setFontScale(PixelDungeon.SCALAR);
        rightButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                nextMap();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        add(backButton).left().pad(50 * PixelDungeon.SCALAR,50 * PixelDungeon.SCALAR,0,0).colspan(3).expandX().row();
        add(leftButton).size(size/4).expandY();
        add(mapPreview).size(size).pad(20 * PixelDungeon.SCALAR).expandY();
        add(rightButton).size(size/4).expandY();
    }

    private void nextMap() {
        currentMap = mapManager.getNext(currentMap.getMapName());
        mapPreview.setDrawable(new SpriteDrawable(new Sprite(Assets.getInstance().getTextureSet(
                Assets.LEVELS).findRegion(currentMap.getMapName()))));
    }

    private void prevMap() {
        currentMap = mapManager.getPrevious(currentMap.getMapName());
        mapPreview.setDrawable(new SpriteDrawable(new Sprite(Assets.getInstance().getTextureSet(
                        Assets.LEVELS).findRegion(currentMap.getMapName()))));
    }

    public Map getMap() {
        return currentMap;
    }
}
