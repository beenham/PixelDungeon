package net.team11.pixeldungeon.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.team11.pixeldungeon.PixelDungeon;

import java.awt.Font;

import javax.swing.text.View;

public class Hud {
    public Stage stage;
    public OrthographicCamera guiCam;
    private Viewport viewport;
    public Rectangle wLeftBounds = new Rectangle(0,0,100,100);
    public Rectangle wRightBounds = new Rectangle(150,0,100,100);

    private Integer worldTimer;
    private float timeCount;
    private Integer score;

    public ImageButton upButton, downButton, leftButton, rightButton;

    public Hud(SpriteBatch sb) {
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT, guiCam = new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.bottom();
        table.setFillParent(true);

        upButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("badlogic.jpg"))));
        table.add(upButton).expandX().pad(5,10,5,10);

        downButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("badlogic.jpg"))));
        table.add(downButton).expandX().pad(5,10,5,10);

        rightButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("badlogic.jpg"))));
        table.add(rightButton).expandX().pad(5,10,5,10);

        leftButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("badlogic.jpg"))));
        table.add(leftButton).expandX().pad(5,10,5,10);
        System.out.println("Left Loc: " + leftButton.getX() + " : " + leftButton.getY());
        System.out.println("Left Loc: " + leftButton.getImage().getImageX() + " : " + leftButton.getImage().getImageY());
        System.out.println("Left Loc: " + leftButton.getWidth() + " : " + leftButton.getHeight());


        stage.addActor(table);
    }
}
