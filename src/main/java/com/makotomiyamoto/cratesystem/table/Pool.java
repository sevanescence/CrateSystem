package com.makotomiyamoto.cratesystem.table;

import com.makotomiyamoto.cratesystem.meta.SafeItemStack;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;

public class Pool {
    private int tries;
    private ArrayList<ItemDrop> itemDrops;
    public Pool(int tries, ArrayList<ItemDrop> itemDrops) {
        this.tries = tries;
        this.itemDrops = itemDrops;
    }
    public static Pool parseFromYamlSection(ConfigurationSection tableSection) {
        int tries = tableSection.getInt("tries");
        ArrayList<ItemDrop> itemDrops = new ArrayList<>();
        ConfigurationSection loot = tableSection.getConfigurationSection("loot");
        assert loot != null;
        // TODO get itemDrops
        ConfigurationSection itemDropSection;
        for (String currentItemDrop : loot.getKeys(false)) {
            itemDropSection = loot.getConfigurationSection(currentItemDrop);
            assert itemDropSection!= null;
            itemDrops.add(ItemDrop.parseFromYamlSection(itemDropSection));
        }
        return new Pool(tries, itemDrops);
    }
    public int getTries() {
        return tries;
    }
    public ArrayList<ItemDrop> getItemDrops() {
        return itemDrops;
    }
}
