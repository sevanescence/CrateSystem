package com.makotomiyamoto.cratesystem.table;

import com.makotomiyamoto.cratesystem.meta.SafeLocation;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;

public class LootChestGroup {
    private Alpha alpha;
    private ArrayList<SafeLocation> safeLocations;
    public LootChestGroup(Alpha alpha, ArrayList<SafeLocation> safeLocation) {
        this.alpha = alpha;
        this.safeLocations = safeLocation;
    }
    public static LootChestGroup parseFromYaml(ConfigurationSection section) {
        Alpha alpha = Alpha.valueOf(section.getString("alpha"));
        ArrayList<SafeLocation> a = new ArrayList<>();
        for (String i : section.getStringList("locations")) {
            String[] split = i.split("-");
            int[] coords = new int[3];
            for (int j = 0; j < split.length; j++) {
                coords[j] = Integer.parseInt(split[j]);
            }
            // TODO adjustable default world
            a.add(new SafeLocation("world", coords[0], coords[1], coords[2]));
        }
        return new LootChestGroup(alpha, a);
    }
    public Alpha getAlpha() {
        return alpha;
    }
    public ArrayList<SafeLocation> getSafeLocations() {
        return safeLocations;
    }
}
