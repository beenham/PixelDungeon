package net.team11.pixeldungeon.uicomponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;

public class TextBox extends Stage {
    private ShapeRenderer shapeRenderer;
    private Label textLabel;
    private String text = Messages.WALL_UNREADABLE_TEXT;

    private float timer = 0;
    private int time = 0;
    private boolean visible = false;
    private boolean backPressed = false;

    public TextBox (SpriteBatch spriteBatch) {
        super(new FitViewport(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT, new OrthographicCamera()), spriteBatch);
        shapeRenderer = new ShapeRenderer();
        setupLabel();
        addActor(setupTable());
    }

    private void setupLabel() {
        textLabel = new Label(text, Assets.getInstance().getSkin(Assets.UI_SKIN), "textBox");
        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return backPressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                backPressed = false;
            }
        });
        textLabel.setWrap(true);
        textLabel.setAlignment(Align.center);
    }

    private Table setupTable() {
        Table table = new Table();
        float padding = 100 * PixelDungeon.SCALAR;

        table.add(textLabel).pad(padding*2,padding*3,padding*2,padding*3).expand().bottom().fillX();
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

    public void setVisible(boolean visible) {
        this.visible = visible;
        this.backPressed = false;
        timer = 0;
        time = 0;
        if (visible) {
            Gdx.input.setInputProcessor(this);
        }
    }

    public boolean isVisible() {
        return this.visible;
    }

    public boolean isBackPressed() {
        return backPressed;
    }

    public void update(float delta) {
        if (visible) {
            timer += delta;
            if (timer >= 0.5) {
                timer = 0;
                String string = text;
                switch (time) {
                    case 0:
                        this.textLabel.setText(string);
                        time++;
                        break;
                    case 1:
                        string += ".";
                        this.textLabel.setText(string);
                        time++;
                        break;
                    case 2:
                        string += "..";
                        this.textLabel.setText(string);
                        time++;
                        break;
                    case 3:
                        string += "...";
                        this.textLabel.setText(string);
                        time = 0;
                        break;
                }
            }
        }
    }

    @Override
    public void draw() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0,0,0,0.20f));
        shapeRenderer.rect(0,0,PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        super.draw();
    }
}
