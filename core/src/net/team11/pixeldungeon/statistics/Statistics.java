package net.team11.pixeldungeon.statistics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Class to handle the serialization and deserialization of LevelStatistics files/objects
 * Currently serializing to a .ser file, may change to JSON/XML later
 */
public class Statistics {

    /**
     * Static method to parse a .json file from local storage specified by the in parameter
     * @param in : Path to the .json file
     * @return : Returns a level statistics object with the values of the .json file
     */
    public static LevelStatistics parseStatsFromFile(String in){
        Gson gson = new Gson();
        try{
            LevelStatistics levelStatistics1 = gson.fromJson(new FileReader(in), LevelStatistics.class);
            System.out.println("Read json file successfully");
            System.out.println("Current Statistics: \n" + levelStatistics1.toString());
            return levelStatistics1;

        } catch (IOException ex){
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Method to create a new .json file with the values of the levelStatistics object
     * @param levelStatistics : Object to change into .json
     * @param in : File path of the .json file
     */
    public static void createNewJson(Object levelStatistics, String in){
        try (Writer writer = new FileWriter(in)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(levelStatistics, writer);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
