package com.makotomiyamoto.cratesystem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makotomiyamoto.cratesystem.meta.SafeLocation;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class CrateSystem extends JavaPlugin {

    @Override
    public void onEnable() {
        if (new File(this.getDataFolder().getPath() + File.separator + "data").mkdirs()) {
            System.out.println("CrateSystem data directory created!");
        }
        System.out.println("Parsing defined chest locations...");
        // TODO get chest locations from chests.yml, parse to data/chests.json as ArrayList<Location>.

        File file = new File(this.getDataFolder().getPath() + File.separator + "chests.yml");
        try {
            if (file.createNewFile()) {
                System.out.println("chests.yml is not written in the system. Don't worry, I'll make you a template!");
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write("locations:\n    - '0-10-0'");
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileConfiguration locationsConfig = YamlConfiguration.loadConfiguration(file);
        ArrayList<Location> locations = parseFromLists(locationsConfig.getStringList("locations"));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String parse = gson.toJson(parseToSerializableLocationList(locations));
        try {
            File parsedLocationData = new File(this.getDataFolder().getPath() + File.separator + "data" + File.separator + "chests.json");
            if (parsedLocationData.createNewFile()) {
                System.out.println(parsedLocationData.getPath() + " didn't exist, so I went ahead and created it.");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(parsedLocationData));
            writer.write(parse);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO create command showing defined chest locations (for debug purposes).
    }

    private ArrayList<Location> parseFromLists(List<String> list) {
        ArrayList<Location> locations = new ArrayList<>();
        for (String i : list) {
            locations.add(parseFromString(i));
        }
        return locations;
    }

    private Location parseFromString(String i) {
        // "0-10-0" -> [0,10,0]
        // TODO let user define default world.
        String[] split = i.split("-");
        int[] l = new int[3];
        for (int j = 0; j < split.length; j++) {
            l[j] = Integer.parseInt(split[j]);
        }
        return new Location(this.getServer().getWorld("world"),l[0],l[1],l[2]);
    }

    public ArrayList<SafeLocation> parseToSerializableLocationList(ArrayList<Location> locations) {
        ArrayList<SafeLocation> a = new ArrayList<>();
        for (Location i : locations) {
            a.add(SafeLocation.parseFromBukkitLocation(i));
        }
        return a;
    }

}
