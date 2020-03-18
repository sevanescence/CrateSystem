package com.makotomiyamoto.cratesystem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makotomiyamoto.cratesystem.commands.*;
import com.makotomiyamoto.cratesystem.meta.SafeLocation;
import com.makotomiyamoto.cratesystem.table.LootChestGroup;
import com.makotomiyamoto.cratesystem.table.LootTable;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class CrateSystem extends JavaPlugin {

    @SuppressWarnings("ConstantConditions")
    /* warning suppression above is for this.getCommand() */
    @Override
    public void onEnable() {
        /* simple stuff */
        this.getCommand("save").setExecutor(new Save(this));
        this.getCommand("load").setExecutor(new Load(this));
        this.getCommand("drop").setExecutor(new Drop(this));
        this.getCommand("testload").setExecutor(new TestLoad(this));
        this.getCommand("generate").setExecutor(new Generate(this));
        this.getCommand("csreload").setExecutor(new Reload(this));
        /* bunch of system stuff */
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
            File parsedLocationData = new File(this.getDataFolder().getPath() + File.separator + "data"
                    + File.separator + "chests.json");
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

        // TODO actual chest system

        File lootTableGroups = new File(this.getDataFolder().getPath()
                + File.separator + "loot-table-groups.yml");
        try {
            if (lootTableGroups.createNewFile()) {
                System.out.println(lootTableGroups.getPath() + " created!");
                BufferedWriter writer = new BufferedWriter(new FileWriter(lootTableGroups));
                writer.write(
                        "# alpha: # ONE, TWO, THREE quantity defines what loot tables will generate\n"
                        + "groups:\n    c1:\n        alpha: ONE\n        locations:\n            - '26-83-168'"
                );
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileConfiguration lootTableGroupsYaml = YamlConfiguration.loadConfiguration(lootTableGroups);
        System.out.println(lootTableGroupsYaml.getConfigurationSection("groups"));
        ConfigurationSection groups = lootTableGroupsYaml.getConfigurationSection("groups");
        ArrayList<LootChestGroup> lootChestGroups = new ArrayList<>();
        for (String groupKey : groups.getKeys(false)) {
            lootChestGroups.add(LootChestGroup.parseFromYaml(groups.getConfigurationSection(groupKey)));
        }
        System.out.println(lootChestGroups);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    new File(this.getDataFolder().getPath() + File.separator + "data"
                    + File.separator + "loot-table-groups.json")
            ));
            writer.write(gson.toJson(lootChestGroups));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // todo loot table parsing to CrateSystem/data/
        File tablesFolder = new File(this.getDataFolder().getPath() + File.separator + "tables" + File.separator);
        File target = new File(this.getDataFolder().getPath() + File.separator + "data" + File.separator);
        if (new File(target.getPath() + File.separator + "tables" + File.separator).mkdirs()) {
            System.out.println("directory for json tables created!");
        }
        File destroyDir = new File(target.getPath() + File.separator + "tables");
        File[] toDestroy = destroyDir.listFiles();
        for (File destroy : toDestroy) {
            if (destroy.delete()) System.out.println(destroy.getName() + " deleted");
        }
        File[] tables = tablesFolder.listFiles();
        LootTable currentLootTable;
        for (File table : tables) {
            //System.out.println("name = " + table.getName());
            currentLootTable = LootTable.parseFromYaml(YamlConfiguration.loadConfiguration(table));
            String compiledLootTable = gson.toJson(currentLootTable);
            //System.out.println(compiledLootTable);
            try {
                BufferedWriter writer =
                        new BufferedWriter(
                                new FileWriter(
                                        new File(target.getPath() + File.separator + "tables"
                                                + File.separator +  table.getName()
                                                + "_Alpha-" + currentLootTable.getAlpha() + ".json")));
                writer.write(compiledLootTable);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


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
