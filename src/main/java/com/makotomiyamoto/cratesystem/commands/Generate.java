package com.makotomiyamoto.cratesystem.commands;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.makotomiyamoto.cratesystem.CrateSystem;
import com.makotomiyamoto.cratesystem.meta.SafeLocation;
import com.makotomiyamoto.cratesystem.table.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Generate implements CommandExecutor {
    private CrateSystem plugin;
    public Generate(CrateSystem plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("Scanning loot-table-groups.json...");
        File lootTableGroups = new File(plugin.getDataFolder().getPath()
                + File.separator + "data" + File.separator + "loot-table-groups.json");
        Gson gson = new Gson();
        LootChestGroup[] lootChestGroups;
        try {
            lootChestGroups = gson.fromJson(
                    new JsonReader(
                            new FileReader(lootTableGroups)), LootChestGroup[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return true;
        }
        assert lootChestGroups != null;
        File lootTableDirectory = new File(plugin.getDataFolder().getPath()
                + File.separator + "data" + File.separator + "tables");
        File[] lootTables = lootTableDirectory.listFiles();
        assert lootTables != null;
        HashMap<Alpha, ArrayList<LootTable>> tablesMap = new HashMap<>();
        tablesMap.put(Alpha.ONE, new ArrayList<>());
        tablesMap.put(Alpha.TWO, new ArrayList<>());
        tablesMap.put(Alpha.THREE, new ArrayList<>());
        String name;
        for (File lootTableFile : lootTables) {
            name = lootTableFile.getName();
            LootTable parsed;
            try {
                parsed = gson.fromJson(new JsonReader(new FileReader(lootTableFile)), LootTable.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return true;
            }
            assert parsed != null;
            if (name.contains("Alpha-ONE")) {
                tablesMap.get(Alpha.ONE).add(parsed);
            } else if (name.contains("Alpha-TWO")) {
                tablesMap.get(Alpha.TWO).add(parsed);
            } else {
                tablesMap.get(Alpha.THREE).add(parsed);
            }
            System.out.println("current file: " + name);
        }
        System.out.println("Alpha three length: " + tablesMap.get(Alpha.THREE).size());
        for (LootChestGroup lootChestGroup : lootChestGroups) {
            Alpha alpha = lootChestGroup.getAlpha();
            if (tablesMap.get(alpha).size() < 1)
                continue;
            int rollMax = tablesMap.get(alpha).size();
            int roll;
            int chestMax = 27;
            int slotRoll;
            LootTable table;
            for (SafeLocation safeLocation : lootChestGroup.getSafeLocations()) {
                roll = (int) (Math.random()*rollMax);
                table = tablesMap.get(alpha).get(roll);
                /* chest drop and load */
                Location location = safeLocation.build();
                location.getBlock().setType(Material.CHEST);
                /* actual loot table loading */
                int weightSum, tries;
                double dropRoll;
                for (Pool pool : table.getPools()) {
                    weightSum = pool.getWeightSum();
                    tries = pool.getTries();
                    //dropRoll = Math.random() * weightSum;
                    boolean shouldReroll;
                    while (tries > 0) {
                        shouldReroll = true;
                        slotRoll = (int) (Math.random() * chestMax);
                        for (ItemDrop itemDrop : pool.getItemDrops()) {
                            dropRoll = Math.random() * weightSum;
                            if (dropRoll < itemDrop.getWeight()) {
                                ((Chest) location.getBlock().getState()).getInventory()
                                        .setItem(slotRoll, itemDrop.getSafeItemStack().build(plugin));
                                shouldReroll = false;
                            }
                        }
                        if (!shouldReroll) tries--;
                    }
                }
            }
        }
        return true;
    }
}
