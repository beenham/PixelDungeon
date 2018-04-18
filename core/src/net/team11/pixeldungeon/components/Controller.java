package net.team11.pixeldungeon.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.team11.pixeldungeon.PixelDungeon;

public class Controller {
    Viewport viewport;
    Stage stage;
    boolean upPressed, downPressed, leftPressed, rightPressed;
    OrthographicCamera cam;

    public Controller(SpriteBatch batch){
        cam = new OrthographicCamera();
        viewport = new FitViewport(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT, cam);
        stage = new Stage(viewport, batch);

        stage.addListener(new InputListener(){

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch(keycode){
                    case Input.Keys.UP:
                        upPressed = true;
                        break;
                    case Input.Keys.DOWN:
                        downPressed = true;
                        break;
                    case Input.Keys.LEFT:
                        leftPressed = true;
                        break;
                    case Input.Keys.RIGHT:
                        rightPressed = true;
                        break;
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                switch(keycode){
                    case Input.Keys.UP:
                        upPressed = false;
                        break;
                    case Input.Keys.DOWN:
                        downPressed = false;
                        break;
                    case Input.Keys.LEFT:
                        leftPressed = false;
                        break;
                    case Input.Keys.RIGHT:
                        rightPressed = false;
                        break;
                }
                return true;
            }
        });

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.left().bottom();
        Texture texture = new Texture("flatDark25.png");
        int width = texture.getWidth()*3, height = texture.getHeight()*3;
        Image upImg = new Image(new Texture("flatDark25.png"));
        upImg.setSize(width, height);
        upImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("UP PRESSED");
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        Image downImg = new Image(new Texture("flatDark26.png"));
        downImg.setSize(width, height);
        downImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("DOWN PRESSED");
                downPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
            }
        });

        Image rightImg = new Image(new Texture("flatDark24.png"));
        rightImg.setSize(width, height);
        rightImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("RIGHT PRESSED");
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        Image leftImg = new Image(new Texture("flatDark23.png"));
        leftImg.setSize(width, height);
        leftImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("LEFT PRESSED");
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });

        table.add();
        table.add(upImg).size(upImg.getWidth(), upImg.getHeight());
        table.add();
        table.row().pad(5, 5, 5, 5);
        table.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        table.add();
        table.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());
        table.row().padBottom(5);
        table.add();
        table.add(downImg).size(downImg.getWidth(), downImg.getHeight());
        table.add();
        table.setDebug(true);

        stage.addActor(table);
    }

    public void draw(){
        stage.draw();
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }
}