
package net.team11.pixeldungeon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.game.entities.blocks.Chest;
import net.team11.pixeldungeon.game.items.Item;
import net.team11.pixeldungeon.game.items.keys.Key;
import net.team11.pixeldungeon.inventory.skinselect.SkinList;
import net.team11.pixeldungeon.saves.SaveGame;
import net.team11.pixeldungeon.utils.inventory.InventoryUtil;
import net.team11.pixeldungeon.utils.stats.GlobalStats;
import net.team11.pixeldungeon.utils.stats.LevelStats;
import net.team11.pixeldungeon.utils.stats.StatsUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * The mother of all utils
 */
public class Util {

    private static StatsUtil statsUtil;
    private static InventoryUtil inventoryUtil;

    private static FileHandle saveLocation;

    public static SaveGame currentSave;

    public static Util getInstance(){
        return new Util();
    }

    private Util() {
        SaveGame loadedSave = loadGame();


        System.out.println("LOADED SAVE: " + loadedSave);
    }


    //////////////////////
    //  Saving/Loading  //
    //////////////////////
    public static SaveGame loadGame() {

        String playerName;
        Json json = new Json();

        //If we're signed in using google play then we have a player name
        //Use that as the directory to look for a save game
        //Otherwise use default "player"

        if (PixelDungeon.getInstance().getAndroidInterface().isSignedIn()) {
            playerName = PixelDungeon.getInstance().getAndroidInterface().getUserName();
        } else {
            playerName = "Player";
        }

        saveLocation = Gdx.files.local("saves/" + "null/" + "/" + "saveGame.json");


        //Have a save game for the currently signed in player
        if (saveLocation.exists()) {
            System.out.println("Save file exists, loading");

            SaveGame saveGame = json.fromJson(SaveGame.class, saveLocation);

            HashMap<String, LevelStats> levelStats = saveGame.getLevelStatsHashMap();
            //Check to see if any more levels have been added
            for (FileHandle file : Gdx.files.internal("stats/levels").list()) {

                if (!levelStats.containsKey(file.nameWithoutExtension())) {
                    LevelStats stats = json.fromJson(LevelStats.class, file);
                    levelStats.put(stats.getFileName(), stats);
                }
            }

            //Check to see if any more skins have been added
            SkinList skinList = json.fromJson(SkinList.class, Gdx.files.internal("shop/shopitems/skinList.json"));
            SkinList savedSkinList = saveGame.getSkinList();

            if (savedSkinList.getVersion() < skinList.getVersion()) {

                for (String skin : skinList.getSkinList()) {
                    skinList.setSkin(skin, savedSkinList.hasSkin(skin));
                }

                saveGame.setSkinList(skinList);
            }
            currentSave = saveGame;

            statsUtil = new StatsUtil(currentSave.getLevelStatsHashMap(), currentSave.getGlobalStats());

            return saveGame;
        } else {
            //Otherwise create a new empty save for them
            System.out.println("No save file exists for " + playerName + ", creating and loading new save");
            HashMap<String, LevelStats> levelStats = new HashMap<>();

            for (FileHandle file : Gdx.files.internal("stats/levels").list()) {
                LevelStats stats = json.fromJson(LevelStats.class, file);
                levelStats.put(stats.getFileName(), stats);
            }

            System.out.println(levelStats);

            GlobalStats globalStats = json.fromJson(GlobalStats.class, Gdx.files.internal("stats/globalStats.json"));

            SkinList skinList = json.fromJson(SkinList.class, Gdx.files.internal("shop/shopitems/skinList.json"));

            SaveGame saveGame = new SaveGame(levelStats, globalStats, skinList, getTimeStamp());

            saveGame(saveGame);
            statsUtil = new StatsUtil(currentSave.getLevelStatsHashMap(), currentSave.getGlobalStats());
            inventoryUtil = InventoryUtil.getInstance();
            inventoryUtil.setSkinList(saveGame.getSkinList());
            return saveGame;

        }
    }

    public static void saveGame(SaveGame saveGame) {
        Json json = new Json();
        System.out.println("Saving the game");
        saveLocation.writeString(json.toJson(saveGame), false);
        System.out.println("Finished saving");
        currentSave = saveGame;
    }

    public static void saveGame() {
        Json json = new Json();
        statsUtil.saveTimer();
        SaveGame saveGame = new SaveGame(statsUtil.getLevelStats(), statsUtil.getGlobalStats(), inventoryUtil.getSkinList(), getTimeStamp());
        saveLocation.writeString(json.toJson(saveGame), false);
        System.out.println(saveGame);
        System.out.println("SAVED GAME");
        currentSave = saveGame;
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }

    ///////////////////////
    //  Statistics Utils //
    ///////////////////////

    public static StatsUtil getStatsUtil() {
        return statsUtil;
    }

    public static void updateLevelStats(String mapName, LevelStats stats) {
        statsUtil.getLevelStats().put(mapName, stats);
    }

    public static void updateChests(Chest chest) {
        statsUtil.getCurrentStats().incrementChests();
        statsUtil.getCurrentStats().addChest(chest.getName());
        statsUtil.getGlobalStats().incrementChestsFound();
        saveGame();
    }

    //
    public static void updateKeys(Key key) {
        System.out.println("Updating key stats");
        statsUtil.getCurrentStats().incrementKeys();
        statsUtil.getGlobalStats().incrementKeysFound();
        statsUtil.getCurrentStats().addKey(key.getName());
        System.out.println(statsUtil.getCurrentStats());
        saveGame();
    }

    //
    public static void updateItems(Item item) {
        statsUtil.getCurrentStats().incrementItems();
        statsUtil.getGlobalStats().incrementItemsFound();
        statsUtil.getCurrentStats().addItem(item.getName());
        saveGame();
    }

    //
    public static void updateAttempts(String mapName) {
        statsUtil.getLevelStats(mapName).incrementAttempts();
        statsUtil.getGlobalStats().incrementAttempts();
        saveGame();
    }

    //
    public static void updateDeaths() {
        statsUtil.getCurrentStats().incrementDeaths();
        statsUtil.getGlobalStats().incrementDeaths();
        saveGame();
    }

    public static void updatePuzzleAttempts() {
        statsUtil.getGlobalStats().incrementPuzzleAttempted();
        saveGame();
    }

    public static void updatePuzzleCompleted() {
        statsUtil.getGlobalStats().incrementPuzzleCompleted();
        saveGame();
    }

    public static void saveTimer() {
        statsUtil.saveTimer();
    }

    public static void updateGlobal() {
        saveTimer();
    }

    public static void respawn(String mapName) {
        statsUtil.getCurrentStats().respawn();
        updateAttempts(mapName);
        saveGame();
    }

}