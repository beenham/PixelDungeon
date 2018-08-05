package net.team11.pixeldungeon.utils.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;

public class Assets {
    public static final String BACKGROUND = "backgrounds";
    public static final String BLOCKS = "blocks";
    public static final String HUD = "hud";
    public static final String ITEMS = "items";
    public static final String LEVELS = "levels";
    public static final String TRAPS = "traps";
    public static final String UI_SKIN = "uiskin";

    private static final String PLAYER_DEFAULT = "default";
    private static final String PLAYER_MUSKETEER_RED = "musketeerRed";
    private static final String PLAYER_MUSKETEER_BLUE = "musketeerBlue";
    private static final String PLAYER_MUSKETEER_GREEN = "musketeerGreen";
    private static final String PLAYER_MUSKETEER_DARK = "musketeerDark";
    public static final String PLAYER_DEATH = "deaths";

    private static Assets INSTANCE = new Assets();
    private static HashMap<String, TextureAtlas> textures;
    private static HashMap<String, TextureAtlas> playerTextures;
    private static HashMap<String, Skin> skins;

    private Assets() {
        textures = new HashMap<>();
        textures.put(BACKGROUND, new TextureAtlas(Gdx.files.internal("texturepacks/ui/Backgrounds.atlas")));
        playerTextures = new HashMap<>();
        skins = new HashMap<>();
    }

    private void loadAssets() {
        textures.put(BLOCKS, new TextureAtlas(Gdx.files.internal("texturepacks/entities/Blocks.atlas")));
        textures.put(HUD, new TextureAtlas(Gdx.files.internal("texturepacks/ui/Hud.atlas")));
        textures.put(ITEMS, new TextureAtlas(Gdx.files.internal("texturepacks/items/Items.atlas")));
        textures.put(LEVELS, new TextureAtlas(Gdx.files.internal("texturepacks/ui/Levels.atlas")));
        textures.put(TRAPS, new TextureAtlas(Gdx.files.internal("texturepacks/entities/Traps.atlas")));

        playerTextures.put(PLAYER_DEFAULT, new TextureAtlas(Gdx.files.internal("texturepacks/player/Player_Default.atlas")));
        playerTextures.put(PLAYER_MUSKETEER_RED, new TextureAtlas(Gdx.files.internal("texturepacks/player/Player_Musketeer_Red.atlas")));
        playerTextures.put(PLAYER_MUSKETEER_BLUE, new TextureAtlas(Gdx.files.internal("texturepacks/player/Player_Musketeer_Blue.atlas")));
        playerTextures.put(PLAYER_MUSKETEER_GREEN, new TextureAtlas(Gdx.files.internal("texturepacks/player/Player_Musketeer_Green.atlas")));
        playerTextures.put(PLAYER_MUSKETEER_DARK, new TextureAtlas(Gdx.files.internal("texturepacks/player/Player_Musketeer_Dark.atlas")));
        playerTextures.put(PLAYER_DEATH, new TextureAtlas(Gdx.files.internal("texturepacks/player/Player_Deaths.atlas")));

        skins.put(UI_SKIN, new Skin(Gdx.files.internal("skin/uiskin/ui_skin.json")));
    }

    public TextureAtlas getTextureSet(String atlas) {
        if (textures.containsKey(atlas)) {
            return textures.get(atlas);
        } else {
            throw new NullPointerException("Texture Set '" + atlas + "' does not exist.");
        }
    }

    public TextureAtlas getPlayerTexture(String atlas) {
        if (playerTextures.containsKey(atlas)) {
            return playerTextures.get(atlas);
        } else {
            throw new NullPointerException("Texture Set '" + atlas + "' does not exist.");
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

    public void init() {
        loadAssets();
    }

    public static void dispose() {
        textures.remove(BLOCKS).dispose();
        textures.remove(HUD).dispose();
        textures.remove(TRAPS).dispose();
    }
}