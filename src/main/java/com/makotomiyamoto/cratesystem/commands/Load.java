package com.makotomiyamoto.cratesystem.commands;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.makotomiyamoto.cratesystem.CrateSystem;
import com.makotomiyamoto.cratesystem.meta.SafeItemStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public final class Load implements CommandExecutor {
    private CrateSystem plugin;
    public Load(CrateSystem plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            System.out.println("Only players may execute this command.");
            return true;
        }
        Gson gson = new Gson();
        File file = new File(plugin.getDataFolder().getPath() + File.separator + "test.json");
        try {
            SafeItemStack safeItemStack = gson.fromJson(new JsonReader(new FileReader(file)), SafeItemStack.class);

            ((Player) commandSender).getInventory().addItem(safeItemStack.build(plugin));
            commandSender.sendMessage("Item loaded from " + file.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
}
