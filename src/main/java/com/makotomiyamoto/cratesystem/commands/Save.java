package com.makotomiyamoto.cratesystem.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makotomiyamoto.cratesystem.CrateSystem;
import com.makotomiyamoto.cratesystem.meta.SafeItemStack;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class Save implements CommandExecutor {
    private CrateSystem plugin;
    public Save(CrateSystem plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player) || ((Player) commandSender).getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            commandSender.sendMessage("Must have an item in your hand to run this command.");
            return true;
        }
        SafeItemStack safeItemStack = SafeItemStack.parseFromBukkitItemStack(((Player) commandSender).getInventory().getItemInMainHand());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String parse = gson.toJson(safeItemStack);
        File file = new File(plugin.getDataFolder().getPath() + File.separator + "test.json");
        try {
            if (file.createNewFile()) {
                commandSender.sendMessage(file.getPath() + " created!");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(parse);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        commandSender.sendMessage("Item data saved as SafeItemStack to " + file.getPath() + "!");
        return true;
    }
}
