package net.team11.pixeldungeon.entities.mirrors;

import net.team11.pixeldungeon.utils.Direction;

public class MirrorUtil {



    public static Direction parseDirection(String direction){

        String dir = direction.toLowerCase();

        switch (dir){
            case "up":
                return Direction.UP;

            case "down":
                return Direction.DOWN;

            case "left":
                return Direction.LEFT;

            case "right":
                return Direction.RIGHT;

            default:
                System.out.println("Unknown direction as parameter, setting to right");
                return Direction.RIGHT;


        }
    }
}
