package net.team11.pixeldungeon.utils;

import net.team11.pixeldungeon.PixelDungeon;

public class T11Log {
    private static final String title = "PixelDungeon";
    public static final int ERROR = 0;
    public static final int INFO = 1;
    public static final int DEBUG = 2;
    public static final int VERBOSE = 3;

    public static void error(String tag, String message) {
        tag = title + " : " + tag;
        PixelDungeon.getInstance().getAndroidInterface().debugCall(ERROR,tag,message);
    }

    public static void info(String tag, String message) {
        tag = title + " : " + tag;
        PixelDungeon.getInstance().getAndroidInterface().debugCall(INFO,tag,message);
    }

    public static void debug(String tag, String message) {
        tag = title + " : " + tag;
        PixelDungeon.getInstance().getAndroidInterface().debugCall(DEBUG,tag,message);
    }

    public static void verbose(String tag, String message) {
        tag = title + " : " + tag;
        PixelDungeon.getInstance().getAndroidInterface().debugCall(VERBOSE,tag,message);
    }
}
