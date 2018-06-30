package net.team11.pixeldungeon.game.uicomponents;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;

public class TutorialMessage extends Stage {
    private Label textLabel;
    private String text = Messages.WALL_UNREADABLE_TEXT;

    private boolean visible = false;

    public TutorialMessage (SpriteBatch spriteBatch) {
        super(new FitViewport(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT, new OrthographicCamera()), spriteBatch);
        setupLabel();
        addActor(setupTable());
    }

    private void setupLabel() {
        textLabel = new Label(text, Assets.getInstance().getSkin(Assets.UI_SKIN));
        textLabel.setWrap(true);
        textLabel.setAlignment(Align.center);
        textLabel.setFontScale(PixelDungeon.SCALAR * 0.75f);
    }

    private Table setupTable() {
        Table table = new Table();
        float padding = 100 * PixelDungeon.SCALAR;

        table.add(textLabel).pad(padding,padding*4,padding*2,padding*4).expand().top().fillX();
        table.setSize(PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        return table;
    }

    public void init(String text) {
        if (text != null) {
            this.text = text;
        } else {
            this.text = Messages.WALL_UNREADABLE_TEXT;
        }
        this.textLabel.setText(this.text);
        setVisible(true);
    }

    public void init(String text, float time) {
        if (text != null) {
            this.text = text;
        } else {
            this.text = Messages.WALL_UNREADABLE_TEXT;
        }
        this.textLabel.setText(this.text);
        setVisible(true);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
