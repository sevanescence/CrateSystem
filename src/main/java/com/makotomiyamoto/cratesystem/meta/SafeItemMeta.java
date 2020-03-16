package com.makotomiyamoto.cratesystem.meta;

import com.makotomiyamoto.cratesystem.CrateSystem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SafeItemMeta {
    private transient CrateSystem plugin;
    private String name;
    private List<String> lore;
    private Map<String, Integer> safeEnchantments = new HashMap<>();
    public SafeItemMeta() {

    }

    public SafeItemMeta(String name, List<String> lore, Map<Enchantment, Integer> enchantments) {
        this.name = name;
        this.lore = lore;
        safeEnchantments = new HashMap<>();
        for (Enchantment e : enchantments.keySet()) {
            safeEnchantments.put(e.toString(), enchantments.get(e));
        }
    }
    public static SafeItemMeta parseFromYamlSection(ConfigurationSection meta) {
        if (meta == null) { return new SafeItemMeta(); }
        SafeItemMeta safeItemMeta = new SafeItemMeta();
        String name = meta.getString("name");
        if (name != null) {
            safeItemMeta.setName(name.replaceAll("&", "§"));
        }
        List<String> lore = meta.getStringList("lore");
        if (lore.size() > 0) {
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, lore.get(i).replaceAll("&", "§"));
            }
            safeItemMeta.setLore(lore);
        }
        ConfigurationSection enchantmentSection = meta.getConfigurationSection("enchants");
        if (enchantmentSection != null) {
            HashMap<String, Integer> safeEnchantments = new HashMap<>();
            for (String enchantment : enchantmentSection.getKeys(false)) {
                safeEnchantments.put(enchantment, enchantmentSection.getInt(enchantment));
            }
            safeItemMeta.setSafeEnchantments(safeEnchantments);
        }
        return safeItemMeta;
    }
    public static SafeItemMeta parseFromBukkitItemMeta(ItemMeta itemMeta) {
        assert itemMeta != null;
        return new SafeItemMeta(
                itemMeta.getDisplayName(),
                itemMeta.getLore(),
                itemMeta.getEnchants()
        );
    }
    public ItemMeta build(CrateSystem plugin) {
        ItemMeta meta = new ItemStack(Material.IRON_SWORD).getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(lore);
        for (String e : safeEnchantments.keySet()) {
            String n = e.replaceAll(".*:(.*),.*", "$1");
            Enchantment en = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(n));
            assert en != null;
            meta.addEnchant(en, safeEnchantments.get(e), true);
        }
        return meta;
    }
    public String getName() {
        return name;
    }
    public List<String> getLore() {
        return lore;
    }
    public Map<String, Integer> getSafeEnchantments() {
        return safeEnchantments;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLore(List<String> lore) {
        this.lore = lore;
    }
    public void setSafeEnchantments(HashMap<String, Integer> safeEnchantments) {
        for (String i : safeEnchantments.keySet()) {
            Enchantment en = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(i));
            System.out.println(en);
            assert en != null;
            this.safeEnchantments.put(en.toString(), safeEnchantments.get(i));
        }
    }
}
