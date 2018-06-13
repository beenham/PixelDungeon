package net.team11.pixeldungeon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;

public class Assets {
    public static final String BACKGROUND = "backgrounds";
    public static final String BLOCKS = "blocks";
    public static final String HUD = "hud";
    public static final String ITEMS = "items";
    public static final String LEVELS = "levels";
    public static final String PLAYER = "player";
    public static final String TRAPS = "traps";
    public static final String UI_SKIN = "uiskin";
    public static final String P_FONT = "pixelFont";
    public static final String BP_FONT = "bpFont";

    private static Assets INSTANCE = new Assets();
    private static HashMap<String, TextureAtlas> textures;
    private static HashMap<String, Skin> skins;
    private static HashMap<String, BitmapFont> fonts;

    private Assets() {
        textures = new HashMap<>();
        skins = new HashMap<>();
        fonts = new HashMap<>();
        loadAssets();
    }

    private void loadAssets() {
        textures.put(BACKGROUND, new TextureAtlas(Gdx.files.internal("ui/Backgrounds.atlas")));
        textures.put(BLOCKS, new TextureAtlas(Gdx.files.internal("entities/Blocks.atlas")));
        textures.put(HUD, new TextureAtlas(Gdx.files.internal("ui/Hud.atlas")));
        textures.put(ITEMS, new TextureAtlas(Gdx.files.internal("ui/Items.atlas")));
        textures.put(LEVELS, new TextureAtlas(Gdx.files.internal("ui/Levels.atlas")));
        textures.put(PLAYER, new TextureAtlas(Gdx.files.internal("entities/Player.atlas")));
        textures.put(TRAPS, new TextureAtlas(Gdx.files.internal("entities/Traps.atlas")));

        skins.put(UI_SKIN, new Skin(Gdx.files.internal("Skins/uiskin/ui_skin.json")));

        fonts.put(P_FONT, new BitmapFont(Gdx.files.internal("fonts/PixelFont.fnt"),
                Gdx.files.internal("fonts/PixelFont.png"), false));
        fonts.put(BP_FONT, new BitmapFont(Gdx.files.internal("fonts/BulkyPixels.fnt"),
                Gdx.files.internal("fonts/BulkyPixels.png"), false));
    }

    public TextureAtlas getTextureSet(String atlas) {
        if (textures.containsKey(atlas)) {
            return textures.get(atlas);
        } else {
            throw new NullPointerException("Texture Set '" + atlas + "' does not exist.");
        }
    }

    public BitmapFont getFont(String font) {
        if (fonts.containsKey(font)) {
            return fonts.get(font);
        } else {
            throw new NullPointerException("Font '" + font + "' does not exist.");
        }
    }

    public Skin getSkin(String skin) {
        if (skins.containsKey(skin)) {
            return skins.get(skin);
        } else {
            throw new NullPointerException("Skin '" + skin + "' does not exist.");
        }
    }

    public static Assets getInstance() {
        return INSTANCE;
    }

    public static void dispose() {
        textures.remove(BLOCKS).dispose();
        textures.remove(HUD).dispose();
        textures.remove(PLAYER).dispose();
        textures.remove(TRAPS).dispose();

        fonts.remove(P_FONT).dispose();
    }
}
