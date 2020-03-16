package com.makotomiyamoto.cratesystem.meta;

import com.makotomiyamoto.cratesystem.CrateSystem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class SafeItemStack {
    private Material material;
    private int amount;
    private SafeItemMeta safeItemMeta;
    public SafeItemStack(Material material, int amount) {
        this.material = material;
        this.amount = amount;
        this.safeItemMeta = new SafeItemMeta();
    }
    public static SafeItemStack parseFromYamlSection(ConfigurationSection item) {
        System.out.println(item.getCurrentPath());
        Material material = Material.valueOf(item.getString("material"));
        //int amount = item.getInt("amount");
        int amount = Math.max(item.getInt("amount"), 1);
        SafeItemStack safeItemStack = new SafeItemStack(material, amount);
        safeItemStack.setSafeItemMeta(SafeItemMeta.parseFromYamlSection(item.getConfigurationSection("meta")));
        return safeItemStack;
    }
    public static SafeItemStack parseFromBukkitItemStack(ItemStack itemStack) {
        SafeItemStack sis = new SafeItemStack(itemStack.getType(), itemStack.getAmount());
        assert itemStack.getItemMeta() != null;
        sis.setSafeItemMeta(SafeItemMeta.parseFromBukkitItemMeta(itemStack.getItemMeta()));
        return sis;
    }
    public ItemStack build(CrateSystem plugin) {
        ItemStack itemStack = new ItemStack(material, amount);
        itemStack.setItemMeta(safeItemMeta.build(plugin));
        return itemStack;
    }
    public Material getType() {
        return material;
    }
    public int getAmount() {
        return amount;
    }
    public SafeItemMeta getSafeItemMeta() {
        return safeItemMeta;
    }
    public void setSafeItemMeta(SafeItemMeta sis) {
        safeItemMeta = sis;
    }
}
