package com.makotomiyamoto.cratesystem.commands;

import com.makotomiyamoto.cratesystem.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.io.File;
import java.io.IOException;

public final class Generate implements CommandExecutor {

    private Plugin plugin;

    public Generate(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        plugin.sendMessage((Entity) commandSender, "&7Generating loot chests...");

        // todo check if data/state.json exists, if not then create it and do drop stuff

        return true;

    }

}
