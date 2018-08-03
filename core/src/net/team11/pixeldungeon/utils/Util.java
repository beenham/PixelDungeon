
package net.team11.pixeldungeon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Timer;

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
import java.util.Locale;

/**
 * The mother of all utils
 */
public class Util {
    private static final String TAG = "Util";

    private static final String SKIN_LIST_PATH = "shop/shopitems/skinList.json";
    private static final String GLOBAL_STATS_PATH = "stats/globalStats.json";
    private static final String LEVEL_STATS_PATH = "stats/levels";

    public static final String PLAYER_SAVE_PATH = "saves/player/SaveGame.json";
    public static final String LOCAL_SAVE_PATH = "saves/local/SaveGame.json";

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
        //PixelDungeon.getInstance().getAndroidInterface().deleteSave();
        loadGame();
    }

    //////////////////////
    //  Saving/Loading  //
    //////////////////////
    public void loadGame() {
        Json json = new Json();

        if (PixelDungeon.getInstance().getAndroidInterface().isSignedIn()) {
            T11Log.error(TAG,"Using player save");
            saveLocation = Gdx.files.local(PLAYER_SAVE_PATH);
        } else {
            T11Log.error(TAG,"Using local save");
            saveLocation = Gdx.files.local(LOCAL_SAVE_PATH);
        }

        //  Checking if save directory exists
        if (saveLocation.exists()) {
            T11Log.error(TAG,"Save file exists, Loading");
            currentSave = json.fromJson(SaveGame.class, saveLocation);
            HashMap<String, LevelStats> levelStats = currentSave.getLevelStatsHashMap();

            //Check to see if any more levels have been added
            for (FileHandle file : Gdx.files.internal(LEVEL_STATS_PATH).list()) {
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

            currentSave.setLevelStatsHashMap(levelStats);

            //Check to see if any more skins have been added
            SkinList skinList = json.fromJson(SkinList.class, Gdx.files.internal(SKIN_LIST_PATH));
            SkinList savedSkinList = currentSave.getSkinList();

            if (savedSkinList.getVersion() < skinList.getVersion()) {
                for (String skin : skinList.getSkinList()) {
                    skinList.setSkin(skin, savedSkinList.hasSkin(skin));
                }
                currentSave.setSkinList(skinList);
            }

            saveGame(currentSave);
            PixelDungeon.getInstance().getAndroidInterface().saveGame();
        } else {    //  Create new save directory
            T11Log.error(TAG,"No save file");

            HashMap<String, LevelStats> levelStats = new HashMap<>();
            for (FileHandle file : Gdx.files.internal(LEVEL_STATS_PATH).list()) {
                LevelStats stats = json.fromJson(LevelStats.class, file);
                levelStats.put(stats.getFileName(), stats);
            }

            GlobalStats globalStats = json.fromJson(GlobalStats.class, Gdx.files.internal(GLOBAL_STATS_PATH));
            SkinList skinList = json.fromJson(SkinList.class, Gdx.files.internal(SKIN_LIST_PATH));

            currentSave = new SaveGame(levelStats, globalStats, skinList, getTimeStamp());
            saveGame(currentSave);
        }

        inventoryUtil = InventoryUtil.getInstance();
        statsUtil = new StatsUtil(currentSave.getLevelStatsHashMap(), currentSave.getGlobalStats());
        inventoryUtil.setSkinList(currentSave.getSkinList());
    }

    public void saveGame(SaveGame saveGame) {
        Json json = new Json();
        T11Log.error(TAG,"Initiating save");
        saveLocation.writeString(json.toJson(saveGame), false);
        T11Log.error(TAG,"Saving finished");
        PixelDungeon.getInstance().getAndroidInterface().saveGame();
    }

    public void saveGame() {
        statsUtil.saveTimer();
        currentSave = new SaveGame(statsUtil.getLevelStats(), statsUtil.getGlobalStats(),
                inventoryUtil.getSkinList(), getTimeStamp());
        saveGame(currentSave);
    }

    private static String getTimeStamp() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.UK).format(new Date());
    }

    ///////////////////////
    //  Statistics Utils //
    ///////////////////////

    public StatsUtil getStatsUtil() {
        return this.statsUtil;
    }

    public void signOut() {
        saveGame();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                PixelDungeon.getInstance().getAndroidInterface().signOut();
                clearPlayerDirectory();
                loadGame();
            }
        },0.5f,0,0);
    }

    public void signIn() {
        saveLocation = Gdx.files.local(PLAYER_SAVE_PATH);
        SaveGame localSave = currentSave;
        PixelDungeon.getInstance().getAndroidInterface().loadSaveGame();=
        if (currentSave.getTotalTime() == 0) {
            T11Log.error(TAG,"CloudSave time: 0, using local dir save");
            saveGame(localSave);
            loadGame();
        } else if (localSave.getTotalTime() == 0) {
            T11Log.error(TAG,"Local time: 0, using cloud save");
            saveGame(currentSave);
            loadGame();
        } else {
            T11Log.error(TAG,"Both saves have play time. Asking user.");
        }
    }

    public static void clearLocal() {
        Gdx.files.local("").deleteDirectory();
    }

    private static void clearPlayerDirectory() {
        Gdx.files.local("saves/player").deleteDirectory();
    }

    private static void clearLocalDirectory() {
        Gdx.files.local("saves/local").deleteDirectory();
    }
}