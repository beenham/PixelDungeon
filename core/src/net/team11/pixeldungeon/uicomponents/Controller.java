package net.team11.pixeldungeon.uicomponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.utils.AssetName;

public class Controller {
    private Viewport viewport;
    private Stage stage;
    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private boolean interactPressed, pausePressed;

    public Controller(SpriteBatch batch){
        OrthographicCamera cam = new OrthographicCamera();
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

        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("ui/Hud.atlas"));
        Image texture = new Image(textureAtlas.findRegion(AssetName.UP_BUTTON));
        float width = texture.getWidth()*14, height = texture.getHeight()*14;

        Image upImg = new Image(textureAtlas.findRegion(AssetName.UP_BUTTON));
        upImg.setSize(width, height);
        upImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return upPressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        Image downImg = new Image(textureAtlas.findRegion(AssetName.DOWN_BUTTON));
        downImg.setSize(width, height);
        downImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return downPressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
            }
        });

        Image rightImg = new Image(textureAtlas.findRegion(AssetName.RIGHT_BUTTON));
        rightImg.setSize(width, height);
        rightImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return rightPressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        Image leftImg = new Image(textureAtlas.findRegion(AssetName.LEFT_BUTTON));
        leftImg.setSize(width, height);
        leftImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return leftPressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });

        Image interactImg = new Image(textureAtlas.findRegion(AssetName.INTERACT_BUTTON));
        interactImg.setSize(width,height);
        interactImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return interactPressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                interactPressed = false;
            }
        });

        Image pauseImg = new Image(textureAtlas.findRegion(AssetName.PAUSE_BUTTON));
        pauseImg.setSize(width,height);
        pauseImg.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return pausePressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                pausePressed = false;
            }
        });

        //
        //  INTERACTION TABLE
        //
        Table interactionTable = new Table();
        interactionTable.setPosition(PixelDungeon.V_WIDTH,0);
        interactionTable.right().padRight(width/4).bottom().padBottom(height/4);
        interactionTable.add(interactImg).size(width,height);
        interactionTable.row();
        interactionTable.setDebug(true);

        Table pauseTable = new Table();
        pauseTable.setPosition(PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        pauseTable.right().padRight(width/4).top().padTop(height/4);
        pauseTable.add(pauseImg).size(width,height);
        pauseTable.setDebug(true);


        Table controllerTable = new Table();
        controllerTable.left().padLeft(width/4).bottom().padBottom(height/4);
        controllerTable.add();
        controllerTable.add(upImg).size(upImg.getWidth(), upImg.getHeight());
        controllerTable.add();
        controllerTable.row().pad(5, 5, 5, 5);
        controllerTable.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        controllerTable.add();
        controllerTable.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());
        controllerTable.row().padBottom(5);
        controllerTable.add();
        controllerTable.add(downImg).size(downImg.getWidth(), downImg.getHeight());
        controllerTable.add();
        controllerTable.setDebug(true);


        stage.addActor(controllerTable);
        stage.addActor(interactionTable);
        stage.addActor(pauseTable);
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

    public boolean isInteractPressed() {
        return interactPressed;
    }

    public boolean isPausePressed() {
        return pausePressed;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }
}