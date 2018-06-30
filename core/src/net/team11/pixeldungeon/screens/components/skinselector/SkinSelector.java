package net.team11.pixeldungeon.screens.components.skinselector;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.inventory.skinselect.Skin;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionPush;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;
import net.team11.pixeldungeon.utils.inventory.InventoryUtil;
import net.team11.pixeldungeon.utils.stats.StatsUtil;

import java.util.Locale;

public class SkinSelector extends Table {
    public SkinSelector(float size) {
        setupTable(size/2);
        //setDebug(true);
    }

    private void setupTable(float size) {
        float padding = 50 * PixelDungeon.SCALAR;

        TextButton backButton = new TextButton(Messages.BACK_UPPER, Assets.getInstance().getSkin(Assets.UI_SKIN));
        backButton.getLabel().setFontScale(PixelDungeon.SCALAR);
        backButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().changeScreen(ScreenEnum.MAIN_MENU,
                        ScreenTransitionPush.init(1.5f,ScreenTransitionPush.RIGHT,Interpolation.pow2));
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        add(setupCoinSize(backButton.getHeight())).pad(padding).expandX().left();
        add(backButton).right().pad(padding);
        row();
        add(setupScrollPane(size/3)).colspan(2).expand().fillX().pad(padding,padding/2,padding,padding/2);
    }

    private ScrollPane setupScrollPane(float size) {
        InventoryUtil invenUtil = InventoryUtil.getInstance();

        Table table = new Table();
        int i = 0;
        for (Skin skin : invenUtil.getSkins()) {
            if (i > 0 && i % 3 == 0) {
                table.row();
            }
            table.add(new SkinDisplay(size,skin)).pad(size/5);
            i++;
        }
        ScrollPane scrollPane = new ScrollPane(table, Assets.getInstance().getSkin(Assets.UI_SKIN),
                "background");
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true,false);
        return scrollPane;
    }

    private Table setupCoinSize(float size) {
        Table table = new Table();
        Image coin = new Image(Assets.getInstance().getTextureSet(Assets.ITEMS)
            .findRegion(AssetName.COIN));
        Label label = new Label(
                numberToString(StatsUtil.getInstance().getGlobalStats().getCurrentCoins()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        label.setFontScale(PixelDungeon.SCALAR);

        table.add(coin).size(size).padRight(size/2);
        table.add(label);

        return table;
    }

    private String numberToString(int value) {
        if (value / 1000000 > 0) {
            return String.format(Locale.UK,
                    "%d,%03d,%03d", value / 1000000, value % 1000000 / 1000, value % 1000);
        } else if (value / 1000 > 0) {
            return String.format(Locale.UK,
                    "%d,%03d", value / 1000, value % 1000);
        } else {
            return String.format(Locale.UK,
                    "%d", value);
        }
    }
}
