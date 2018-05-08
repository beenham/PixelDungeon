package net.team11.pixeldungeon.utils;

public class RoundTo {
    public static float RoundToNearest(float number, float roundTo) {
        if (roundTo == 0) {
            return number;
        } else {
            return (Math.round(number / roundTo) * roundTo);
        }
    }
}
