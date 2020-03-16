package com.makotomiyamoto.cratesystem.table;

import com.makotomiyamoto.cratesystem.meta.SafeItemStack;
import org.bukkit.configuration.ConfigurationSection;

public class ItemDrop {
    private int weight;
    private SafeItemStack safeItemStack;
    public ItemDrop(int weight, SafeItemStack safeItemStack) {
        this.weight = weight;
        this.safeItemStack = safeItemStack;
    }
    public static ItemDrop parseFromYamlSection(ConfigurationSection itemDrops) {
        int weight = itemDrops.getInt("weight");
        // todo get safe item stack
        SafeItemStack safeItemStack = SafeItemStack.parseFromYamlSection(itemDrops.getConfigurationSection("item"));
        return new ItemDrop(weight, safeItemStack);
    }
    public int getWeight() {
        return weight;
    }
    public SafeItemStack getSafeItemStack() {
        return safeItemStack;
    }
}
