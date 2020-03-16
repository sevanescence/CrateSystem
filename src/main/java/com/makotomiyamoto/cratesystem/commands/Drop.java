package com.makotomiyamoto.cratesystem.commands;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.makotomiyamoto.cratesystem.CrateSystem;
import com.makotomiyamoto.cratesystem.meta.SafeLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public final class Drop implements CommandExecutor {
    private CrateSystem plugin;
    public Drop(CrateSystem plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        ArrayList<Location> locations = new ArrayList<>();
        Gson gson = new Gson();
        SafeLocation[] parse;
        try {
            parse = gson.fromJson(new JsonReader(new FileReader(
                    new File(plugin.getDataFolder().getPath()
                            + File.separator + "data" + File.separator + "chests.json")
            )), SafeLocation[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return true;
        }
        for (SafeLocation safeLoc : parse) {
            locations.add(safeLoc.build());
        }
        for (Location loc : locations) {
            loc.getBlock().setType(Material.CHEST);
            // TODO replace with with loot table generation when done
            ((Chest) loc.getBlock().getState()).getInventory().setItem(0, new ItemStack(Material.DIAMOND));
        }
        commandSender.sendMessage("Chests placed.");
        return true;
    }
}
