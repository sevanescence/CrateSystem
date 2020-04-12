package com.makotomiyamoto.cratesystem;

import com.google.gson.Gson;
import com.makotomiyamoto.cratesystem.crate.CrateReference;
import com.makotomiyamoto.cratesystem.meta.SafeItemStack;
import com.makotomiyamoto.cratesystem.meta.SafeLocation;
import com.makotomiyamoto.cratesystem.table.ItemDrop;
import com.makotomiyamoto.cratesystem.table.LootTable;
import com.makotomiyamoto.cratesystem.table.Pool;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.List;

public final class Plugin extends JavaPlugin {

    public String PATH, LOOT_TABLES, CHESTS, DATA, PREFIX, DATA_LOOT_TABLES, DATA_CHESTS;

    @Override
    public void onEnable() {

        String s = File.separator;

        // todo parse loot tables (dir: plugins/CratesSystem/tables/)
        this.saveDefaultConfig();

        PREFIX = ChatColor.translateAlternateColorCodes('&', getConfig().getString("settings.prefix"));
        PATH = getDataFolder().getPath();
        DATA = PATH + s + "data";

        if (new File(DATA).mkdir()) {
            print(DATA + " created.");
        }

        File lootTablesDir = new File(PATH + s + "loot-tables");
        if (lootTablesDir.mkdir()) {

            LOOT_TABLES = lootTablesDir.getPath();

            print("Creating loot table boilerplate...");

            InputStream iStream = getResource("template-loot-table.yml");
            try {

                byte[] buffer = new byte[iStream.available()];
                // noinspection all
                iStream.read(buffer);
                File templateLootTableTarget = new File(LOOT_TABLES + s + "template-loot-table.yml");
                OutputStream oStream = new FileOutputStream(templateLootTableTarget);
                oStream.write(buffer);
                oStream.close();

                print(templateLootTableTarget.getPath() + " created.");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        File chestsTarget = new File(PATH + s + "chests.yml");
        try {

            if (chestsTarget.createNewFile()) {
                CHESTS = chestsTarget.getPath();

                print("Creating chests.yml...");

                InputStream iStream = getResource("chests.yml");
                byte[] buffer = new byte[iStream.available()];
                // noinspection all
                iStream.read(buffer);
                OutputStream oStream = new FileOutputStream(chestsTarget);
                oStream.write(buffer);
                oStream.close();

                print(chestsTarget.getPath() + " created.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // todo parse all loot tables

        File[] lootTables = lootTablesDir.listFiles();
        assert lootTables != null;
        for (File tableFile : lootTables) {

            FileConfiguration tableConfig = YamlConfiguration.loadConfiguration(tableFile);

            LootTable table = new LootTable();
            table.setFileName(tableFile.getName().replace(".yml", ".json"));

            ConfigurationSection tableSection = tableConfig.getConfigurationSection("table");
            if (tableSection == null) {
                throw new IllegalArgumentException("table is not defined in " + tableFile.getAbsolutePath());
            }

            for (String poolKey : tableSection.getKeys(false)) {

                ConfigurationSection poolSection = tableSection.getConfigurationSection(poolKey);
                Pool pool = new Pool();

                // todo get pool
                try {
                    pool.setTries(poolSection.getInt("tries"));
                } catch (NullPointerException e) {
                    pool.setTries(1);
                }
                try {
                    pool.setMin(poolSection.getInt("min"));
                } catch (NullPointerException e) {
                    pool.setMin(1);
                }

                ConfigurationSection dropsSection = poolSection.getConfigurationSection("drops");
                if (dropsSection == null) {
                    throw new IllegalArgumentException("drops section undefined in " + tableFile.getAbsolutePath());
                }

                for (String dropKey : dropsSection.getKeys(false)) {

                    ConfigurationSection itemDropSection = dropsSection.getConfigurationSection(dropKey);

                    ItemDrop itemDrop = new ItemDrop();

                    try {
                        itemDrop.setChance((float)itemDropSection.getDouble("chance"));
                    } catch (NullPointerException e) {
                        itemDrop.setChance(1.0f);
                    }
                    try {
                        List<Integer> temp = itemDropSection.getIntegerList("range");
                        int[] ints = new int[temp.size()];
                        for (int i = 0; i < temp.size(); i++) {
                            ints[i] = temp.get(i);
                        }
                        itemDrop.setRange(ints);
                    } catch (NullPointerException e) {
                        itemDrop.setRange(new int[]{1,1});
                    }
                    ConfigurationSection itemSection = itemDropSection.getConfigurationSection("item");
                    try {
                        // todo SafeItemStack.parseFromYamlSection()
                        itemDrop.setItem(SafeItemStack.parseFromYamlSection(itemSection));
                    } catch (NullPointerException e) {
                        throw new IllegalArgumentException(
                                "item is not defined in tables." + poolKey + "." + dropKey
                                        + " in " + tableFile.getAbsolutePath());
                    }

                    pool.getItemDrops().add(itemDrop);

                }

                table.getPools().add(pool);

            }

            // todo save table to plugins/CrateSystem/data/tables/fileName
            DATA_LOOT_TABLES = DATA + s + "loot-tables";
            File dataLootTablesDir = new File(DATA_LOOT_TABLES);
            if (dataLootTablesDir.mkdir()) print(dataLootTablesDir.getPath() + " created.");
            //noinspection ConstantConditions
            for (File file : dataLootTablesDir.listFiles()) {
                // noinspection all
                file.delete();
            }

            File dataLootTableFile = new File(DATA_LOOT_TABLES + s + table.getFileName());
            try {

                if (dataLootTableFile.createNewFile())
                    print(dataLootTableFile.getPath() + " created.");

                Gson gson = new Gson();
                BufferedWriter writer = new BufferedWriter(new FileWriter(dataLootTableFile));
                writer.write(gson.toJson(table));
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // todo parse chests.yml
        assert chestsTarget.exists();
        DATA_CHESTS = DATA + s + "chests.json";
        File chestsFile = new File(DATA_CHESTS);

        try {
            if (chestsFile.createNewFile()) {
                print(DATA_CHESTS + " created.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileConfiguration chestsList = YamlConfiguration.loadConfiguration(chestsTarget);
        ConfigurationSection chests = chestsList.getConfigurationSection("chests");
        assert chests != null;
        CrateReference[] set = new CrateReference[chests.getKeys(false).size()];
        int i = 0;
        for (String chestKey : chests.getKeys(false)) {

            ConfigurationSection chestSection = chests.getConfigurationSection(chestKey);
            CrateReference crate = new CrateReference();
            try {
                crate.setLootTablePointers(chestSection.getStringList("loot-tables"));
            } catch (NullPointerException ignored) {}
            try {
                crate.setSafeLocation(SafeLocation.parseFromYamlSection(chestSection.getConfigurationSection("location")));
            } catch (NullPointerException ignored) {}

            set[i] = crate;
            i++;

        }

        Gson gson = new Gson();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(chestsFile));
            writer.write(gson.toJson(set));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        print("CrateSystem enabled!");

    }

    public void print(String message) {
        print(message, false);
    }
    public void print(String message, boolean announce) {

        if (announce) {
            // todo tell all players
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(
                        ChatColor.translateAlternateColorCodes('&',
                                PREFIX + " " + message));
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("[CrateSystem] " + message);
        }

    }
    public void sendMessage(Entity entity, String message) {
        entity.sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " " + message));
    }

}
