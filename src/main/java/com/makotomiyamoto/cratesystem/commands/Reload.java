package com.makotomiyamoto.cratesystem.commands;

import com.makotomiyamoto.cratesystem.CrateSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Reload implements CommandExecutor {
    private CrateSystem plugin;
    public Reload(CrateSystem plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        plugin.onEnable();
        return true;
    }
}
