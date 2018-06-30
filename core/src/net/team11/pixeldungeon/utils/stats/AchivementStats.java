package net.team11.pixeldungeon.utils.stats;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.game.puzzles.Puzzle;
import net.team11.pixeldungeon.game.puzzles.colouredgems.ColouredGemsPuzzle;
import net.team11.pixeldungeon.game.puzzles.simonsays.SimonSays;
import net.team11.pixeldungeon.utils.crossplatform.AndroidInterface;

public class AchivementStats {
    public static void incrementAttempts() {
        PixelDungeon.getInstance().getAndroidInterface().earnNewAdventurer();
        PixelDungeon.getInstance().getAndroidInterface().earn10Attempts();
        PixelDungeon.getInstance().getAndroidInterface().earn100Attempts();
        PixelDungeon.getInstance().getAndroidInterface().earn500Attempts();
        PixelDungeon.getInstance().getAndroidInterface().earn1000Attempts();
    }

    public static void updateStats(CurrentStats currStats, LevelStats levelStats) {
        AndroidInterface androidInterface = PixelDungeon.getInstance().getAndroidInterface();

        int keyAmount = currStats.getKeysFound();
        int chestAmount = currStats.getChestsFound();

        androidInterface.earnKeyMaster(keyAmount);
        androidInterface.earnLootJunkie(chestAmount);

        String levelName = levelStats.getFileName();
        switch (levelName) {
            case "level_1_1":
                androidInterface.earnCompleteDungeon1();
                break;
            case "level_1_5":
                androidInterface.earnCompleteDungeon5();
                break;
            case "level_1_10":
                androidInterface.earnCompleteDungeon10();
                break;
            case "level_1_15":
                androidInterface.earnCompleteDungeon15();
                break;
            case "level_1_20":
                androidInterface.earnCompleteDungeon20();
                break;
            case "level_1_25":
                androidInterface.earnCompleteDungeon25();
                break;
        }
    }

    public static void completePuzzle(Puzzle puzzle) {
        AndroidInterface androidInterface = PixelDungeon.getInstance().getAndroidInterface();
        androidInterface.earnCompletePuzzle1000();

        if (puzzle instanceof SimonSays) {
            androidInterface.earnSimonSays();
        } else if (puzzle instanceof ColouredGemsPuzzle) {
            androidInterface.earnColoursAligned();
        }
    }
}
