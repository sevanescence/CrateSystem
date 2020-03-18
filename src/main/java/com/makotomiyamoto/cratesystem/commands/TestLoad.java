package com.makotomiyamoto.cratesystem.commands;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.makotomiyamoto.cratesystem.CrateSystem;
import com.makotomiyamoto.cratesystem.table.ItemDrop;
import com.makotomiyamoto.cratesystem.table.LootTable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class TestLoad implements CommandExecutor {
    private CrateSystem plugin;
    public TestLoad(CrateSystem plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        File lootTable = new File(plugin.getDataFolder().getPath()
                + File.separator + "data" + File.separator + "tables"
                + File.separator + "center_table_1.yml_Alpha-THREE.json");
        Gson gson = new Gson();
        try {
            LootTable table = gson.fromJson(new JsonReader(new FileReader(lootTable)), LootTable.class);
            System.out.println(gson.toJson(table));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
}
