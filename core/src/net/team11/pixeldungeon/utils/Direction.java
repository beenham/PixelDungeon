package net.team11.pixeldungeon.utils;

public enum Direction {
    UP {
        @Override
        public final String toString() {
            return "up";
        }
    }, DOWN {
        @Override
        public String toString() {
            return "down";
        }
    }, LEFT {
        @Override
        public String toString() {
            return "left";
        }
    }, RIGHT {
        @Override
        public String toString() {
            return "right";
        }
    };

    public static Direction parseInput(String directionInput) {
        switch (directionInput) {
            case "up":
                return UP;
            case "down":
                return DOWN;
            case "left":
                return LEFT;
            case "right":
                return RIGHT;
                default:
                    return RIGHT;
        }
    }
}
