package net.team11.pixeldungeon.screens.components.skinselector;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.inventory.skinselect.Skin;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.components.coin.CoinDisplay;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionPush;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;
import net.team11.pixeldungeon.utils.inventory.InventoryUtil;
import net.team11.pixeldungeon.utils.stats.StatsUtil;

import java.util.ArrayList;

public class SkinSelector extends Table {
    private SkinInfo info;
    private CoinDisplay coinDisplay;
    private ArrayList<SkinDisplay> skinDisplays;

    public SkinSelector(SkinInfo skinInfo, float size) {
        this.info = skinInfo;
        skinDisplays = new ArrayList<>();
        setupTable(size/2);
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

        add(coinDisplay = new CoinDisplay(backButton.getHeight(),
                StatsUtil.getInstance().getGlobalStats().getCurrentCoins()))
                .pad(padding).expandX().left();
        add(backButton).right().pad(padding);
        row();
        add(setupScrollPane(size/3)).colspan(2).expand().fillX().pad(padding,padding/2,padding,padding/2);
    }

    private ScrollPane setupScrollPane(float size) {
        final InventoryUtil invenUtil = InventoryUtil.getInstance();

        Table table = new Table();
        for (int i = 0 ; i < invenUtil.getSkins().size() ; i++) {
            final Skin currSkin = invenUtil.getSkins().get(i);
            if (i > 0 && i % 3 == 0) {
                table.row();
            }
            SkinDisplay skinDisplay = new SkinDisplay(size,currSkin);
            skinDisplay.addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    info.update(currSkin.getId());
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
            skinDisplays.add(skinDisplay);
            table.add(skinDisplay).pad(size/5);
        }
        ScrollPane scrollPane = new ScrollPane(table, Assets.getInstance().getSkin(Assets.UI_SKIN),
                "background");
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true,false);
        return scrollPane;
    }

    public void update() {
        getCell(coinDisplay).setActor(coinDisplay = new CoinDisplay(coinDisplay.getHeight(),
                StatsUtil.getInstance().getGlobalStats().getCurrentCoins()));

        for (SkinDisplay display : skinDisplays) {
            display.update();
        }
    }
}
