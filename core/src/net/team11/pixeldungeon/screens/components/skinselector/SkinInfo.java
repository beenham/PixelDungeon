package net.team11.pixeldungeon.screens.components.skinselector;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.inventory.skinselect.Skin;
import net.team11.pixeldungeon.inventory.skinselect.SkinList;
import net.team11.pixeldungeon.screens.components.coin.CoinDisplay;
import net.team11.pixeldungeon.utils.Util;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;
import net.team11.pixeldungeon.utils.inventory.InventoryUtil;
import net.team11.pixeldungeon.utils.stats.StatsUtil;

import java.util.HashMap;

public class SkinInfo extends Table {
    private HashMap<Integer,Skin> skins;
    private SkinList skinList;

    private int currentSkin;

    private Image image;
    private Label name;
    private Label description;
    private Table price;
    private TextButton button;

    public SkinInfo(float size) {
        skins = InventoryUtil.getInstance().getSkins();
        skinList = InventoryUtil.getInstance().getSkinList();
        currentSkin = skinList.getCurrentSkin();

        setupLayout(size);
        setBackground(new NinePatchDrawable(Assets.getInstance().getTextureSet(
                Assets.HUD).createPatch(AssetName.DARKEN_60)));
        setWidth(size);
    }

    private void setupLayout(float size) {
        setupImage();
        setupName();
        setupDescription();
        setupPrice();
        setupButton();

        pad(size/10);
        float width = image.getWidth() / (image.getHeight()/(size/3));
        add(image).size(width,size/3);
        row();
        add(name).expandX().center().fillX();
        row();
        add(description).expand().center().fillX();
        row();
        add(price).pad(25 * PixelDungeon.SCALAR).fillX();
        row();
        add(button);
    }

    private Image setupImage(){
        return image = new Image(skins.get(currentSkin).getImage().getDrawable());
    }

    private void setupName() {
        name = new Label(skins.get(currentSkin).getDisplayName(),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        name.setFontScale(PixelDungeon.SCALAR);
        name.setAlignment(Align.center);
        name.setWrap(true);
    }

    private void setupDescription() {
        description = new Label(skins.get(currentSkin).getDescription(),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        description.setFontScale(.75f * PixelDungeon.SCALAR);
        description.setWrap(true);
        description.setAlignment(Align.center);
    }

    private Table setupPrice() {
        System.out.println("Unlocked : " + skinList.hasSkin(skins.get(currentSkin).getName()));
        if (!skinList.hasSkin(skins.get(currentSkin).getName())) {
            if (skins.get(currentSkin).isBuyable()) {
                return price = new CoinDisplay(PixelDungeon.SCALAR * 50,skins.get(currentSkin).getCost());
            } else {
                price = new Table();
                Label unlockText = new Label(
                        skins.get(currentSkin).getUnlock(),
                        Assets.getInstance().getSkin(Assets.UI_SKIN));
                unlockText.setFontScale(0.75f * PixelDungeon.SCALAR);
                unlockText.setAlignment(Align.center);
                unlockText.setWrap(true);
                price.add(unlockText).fillX();
                return price;
            }
        } else {
            return price = new Table();
        }
    }

    private TextButton setupButton() {
        System.out.println("Unlocked : " + skinList.hasSkin(skins.get(currentSkin).getName()));

        if (skinList.hasSkin(skins.get(currentSkin).getName())) {
            if (currentSkin == skinList.getCurrentSkin()) {
                button = new TextButton(Messages.ALREADY_EQUIPPED, Assets.getInstance().getSkin(Assets.UI_SKIN));
                button.setDisabled(true);
            } else {
                button = new TextButton(Messages.EQUIP, Assets.getInstance().getSkin(Assets.UI_SKIN));
                button.addListener(new ClickListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        skinList.setCurrentSkin(currentSkin);
                        InventoryUtil.getInstance().save();
                        update(currentSkin);
                        return super.touchDown(event, x, y, pointer, button);
                    }
                });
            }
        } else {
            if (skins.get(currentSkin).isBuyable()) {
                if (Util.getInstance().getStatsUtil().getGlobalStats().getCurrentCoins() >=
                        skins.get(currentSkin).getCost()) {
                    button = new TextButton(Messages.BUY, Assets.getInstance().getSkin(Assets.UI_SKIN));
                    button.addListener(new ClickListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            skinList.unlockSkin(skins.get(currentSkin).getName());
                            skinList.setCurrentSkin(currentSkin);
//                            InventoryUtil.getInstance().save();

                            Util.getInstance().getStatsUtil().getGlobalStats()
                                    .subtractCurrentCoins(skins.get(currentSkin).getCost());
                            Util.getInstance().saveGame();
                            update(currentSkin);
                            return super.touchDown(event, x, y, pointer, button);
                        }
                    });
                } else {
                    button = new TextButton(Messages.NOT_ENOUGH, Assets.getInstance().getSkin(Assets.UI_SKIN));
                    button.setDisabled(true);
                }
            } else {
                button = new TextButton("",Assets.getInstance().getSkin(Assets.UI_SKIN));
                button.setVisible(false);
                return button;
            }
        }
        button.getLabel().setFontScale(PixelDungeon.SCALAR);
        button.setVisible(true);
        return button;
    }

    public void update(int skinID) {
        currentSkin = skinID;

        getCell(image).setActor(setupImage());
        name.setText(skins.get(currentSkin).getDisplayName());
        description.setText(skins.get(currentSkin).getDescription());
        getCell(price).setActor(setupPrice());
        getCell(button).setActor(setupButton());
    }
}
