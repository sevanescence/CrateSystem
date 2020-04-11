package com.makotomiyamoto.cratesystem.table;

import com.makotomiyamoto.cratesystem.meta.SafeItemStack;
import org.bukkit.Material;

public class ItemDrop {

    private float chance;
    private int[] range;
    private SafeItemStack safeItemStack;

    public ItemDrop() {
        chance = 1;
        range = new int[]{1, 1};
        safeItemStack = new SafeItemStack(Material.BEDROCK, 1);
    }

    public float getChance() {
        return chance;
    }
    public int[] getRange() {
        return range;
    }
    public SafeItemStack getItem() {
        return safeItemStack;
    }

    public void setChance(float chance) {
        this.chance = chance;
    }
    public void setRange(int[] range) {
        this.range = range.clone();
    }
    public void setItem(SafeItemStack safeItemStack) {
        this.safeItemStack = safeItemStack;
    }

}
