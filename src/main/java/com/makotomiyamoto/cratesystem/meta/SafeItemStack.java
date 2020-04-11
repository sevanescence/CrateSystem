package com.makotomiyamoto.cratesystem.meta;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class SafeItemStack {

    private Material material;
    private int amount;

    public SafeItemStack(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public static SafeItemStack parseFromYamlSection(ConfigurationSection itemSection) {

        SafeItemStack safeItemStack = new SafeItemStack(Material.BEDROCK, 1);

        try {
            safeItemStack.material = Material.valueOf(itemSection.getString("type")
                    .replaceAll(" ", "_").toUpperCase());
        } catch (NullPointerException ignored) {}
        try {
            safeItemStack.amount = itemSection.getInt("amount");
        } catch (NullPointerException ignored) {}

        return safeItemStack;

    }

    public Material getType() {
        return material;
    }
    public int getAmount() {
        return amount;
    }

    public void setType(Material material) {
        this.material = material;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ItemStack build() {

        // noinspection all
        ItemStack itemStack = new ItemStack(material, amount);

        return itemStack;

    }

}
