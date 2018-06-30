package net.team11.pixeldungeon.game.tutorial;

import com.badlogic.gdx.math.Polygon;

import net.team11.pixeldungeon.screens.screens.PlayScreen;


public class TutorialZone {
    private Polygon zone;
    private String message;
    private boolean repeat;
    private boolean visible;

    public TutorialZone(Polygon zone, String message) {
        this.zone = zone;
        this.message = message;
        this.repeat = false;
        this.visible = false;
    }

    public Polygon getZone() {
        return zone;
    }

    public boolean isVisible() {
        return visible;
    }

    public void initZone() {
        visible = true;
        PlayScreen.uiManager.initTutorial(message);
    }

    public void exitZone() {
        visible = false;
        PlayScreen.uiManager.hideTutorial();
    }
}
