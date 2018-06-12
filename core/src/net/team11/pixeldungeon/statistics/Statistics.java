package net.team11.pixeldungeon.statistics;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Writer;

/**
 * Class to handle the reading and writing of the different statistics to/from files in internal storage
 * Current statistics directory is pixeldungeon/files/
 */
public class Statistics {

    private static GlobalStatistics globalStatistics;
    public static  String globalLocation = Gdx.files.getLocalStoragePath() + "Global.json";

    /**
     * Static method to parse a .json file from local storage specified by the in parameter
     * @param in : Path to the .json file
     * @return : Returns a level statistics object with the values of the .json file
     */
    public static LevelStatistics parseStatsFromFile(String in){
        Gson gson = new Gson();

        try{
            LevelStatistics levelStatistics = gson.fromJson(new FileReader(in), LevelStatistics.class);
            System.out.println("Read " + in + "successfully");
            System.out.println(levelStatistics.getLevel() + " Statistics: \n" + levelStatistics.toString());
            return levelStatistics;

        } catch (IOException ex){
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Method to create a new .json file with the values of the levelStatistics object
     * @param statistics : Object to change into .json
     * @param in : File path of the .json file
     */
    public static void createNewJson(Object statistics, String in){
        System.out.println("Creating new JSON File");

        try (Writer writer = new FileWriter(in)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(statistics, writer);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Method to write an object to a .json file
     * Used to update statistics data when level is finished
     * @param statistics : The statistics object to write
     * @param in : Path of the file to write to
     */
    public static void writeToJson(Object statistics, String in){
        Gson gson = new Gson();
        File file = new File(in);

        try{
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(statistics));
            writer.close();
            System.out.println("Finished writing to " + in);
            parseGlobalStatistics();
            System.out.println(globalStatistics);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Method used to parse the global statistics from the Global.json file
     */
    public static void parseGlobalStatistics(){
        System.out.println("PARSING GLOBAL STATS");

        Gson gson = new Gson();
        File globalFile = new File(globalLocation);

        if (globalFile.exists()){
            System.out.println("Global file exists");
            try{
                globalStatistics = gson.fromJson(new FileReader(globalLocation), GlobalStatistics.class);
                System.out.println("Read " + globalLocation + " successfully");
                System.out.println("\t" + globalStatistics.toString());
            } catch (IOException ex){
                ex.printStackTrace();
            }
        } else {
            System.out.println("Global file does not exist creating");
            globalStatistics = new GlobalStatistics();
            createNewJson(globalStatistics, globalLocation);
        }
    }

    public static GlobalStatistics getGlobalStatistics() {
        return globalStatistics;
    }
}
