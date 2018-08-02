
package net.team11.pixeldungeon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import net.team11.pixeldungeon.PixelDungeon;
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
    private StatsUtil statsUtil;
    private InventoryUtil inventoryUtil;

    private FileHandle saveLocation;
    private SaveGame currentSave;

    private static Util instance;

    public static Util getInstance(){
        if (instance == null){
            return  instance = new Util();
        } else {
            return instance;
        }
    }

    private Util() {
        StatsUtil.clearLocal();
        SaveGame loadedSave = loadGame();
    }


    //////////////////////
    //  Saving/Loading  //
    //////////////////////
    public SaveGame loadGame() {
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

        saveLocation = Gdx.files.local("saves/saveGame.json");


        //Have a save game for the currently signed in player
        if (saveLocation.exists()) {
            System.out.println("Save file exists, loading");
            SaveGame saveGame = json.fromJson(SaveGame.class, saveLocation);

            HashMap<String, LevelStats> levelStats = saveGame.getLevelStatsHashMap();

            //Check to see if any more levels have been added
            for (FileHandle file : Gdx.files.internal("stats/levels").list()) {
                LevelStats internalStats = json.fromJson(LevelStats.class, file);
                if (!levelStats.containsKey(file.nameWithoutExtension())) {
                    levelStats.put(internalStats.getFileName(), internalStats);
                } else {
                    LevelStats localStats = levelStats.get(internalStats.getFileName());
                    if (internalStats.getVersionNumber() > localStats.getVersionNumber()) {
                        localStats.update(internalStats);
                    }
                }
            }

            saveGame.setLevelStatsHashMap(levelStats);

            //Check to see if any more skins have been added
            SkinList skinList = json.fromJson(SkinList.class, Gdx.files.internal("shop/shopitems/skinList.json"));
            SkinList savedSkinList = saveGame.getSkinList();

            if (savedSkinList.getVersion() < skinList.getVersion()) {

                for (String skin : skinList.getSkinList()) {
                    skinList.setSkin(skin, savedSkinList.hasSkin(skin));
                }

                saveGame.setSkinList(skinList);
            }


            statsUtil = new StatsUtil(saveGame.getLevelStatsHashMap(), saveGame.getGlobalStats());
            inventoryUtil = InventoryUtil.getInstance();
            inventoryUtil.setSkinList(saveGame.getSkinList());

            SaveGame save = new SaveGame(statsUtil.getLevelStats(), statsUtil.getGlobalStats(), inventoryUtil.getSkinList(), getTimeStamp());

            currentSave = save;

            saveGame(save);
            PixelDungeon.getInstance().getAndroidInterface().saveGame();
            return saveGame;
        } else {
            //Otherwise create a new empty save for them
            System.out.println("No save file exists for " + playerName + ", creating and loading new save");
            HashMap<String, LevelStats> levelStats = new HashMap<>();

            for (FileHandle file : Gdx.files.internal("stats/levels").list()) {
                LevelStats stats = json.fromJson(LevelStats.class, file);
                levelStats.put(stats.getFileName(), stats);
            }

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

    public void saveGame(SaveGame saveGame) {
        Json json = new Json();
        System.out.println("Saving the game");
        saveLocation.writeString(json.toJson(saveGame), false);
        System.out.println("Finished saving");
        currentSave = saveGame;
        PixelDungeon.getInstance().getAndroidInterface().saveGame();
    }

    public void saveGame() {
        Json json = new Json();
        statsUtil.saveTimer();
        SaveGame saveGame = new SaveGame(statsUtil.getLevelStats(), statsUtil.getGlobalStats(), inventoryUtil.getSkinList(), getTimeStamp());
        saveLocation.writeString(json.toJson(saveGame), false);

        currentSave = saveGame;
        PixelDungeon.getInstance().getAndroidInterface().saveGame();
    }

    private static String getTimeStamp() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }

    private void printSaveDetails(SaveGame save) {
        System.out.println("New Save Info" + save);
    }

    ///////////////////////
    //  Statistics Utils //
    ///////////////////////

    public StatsUtil getStatsUtil() {
        return this.statsUtil;
    }

}