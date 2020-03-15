package com.makotomiyamoto.cratesystem.table;

import com.makotomiyamoto.cratesystem.CrateSystem;
import com.makotomiyamoto.cratesystem.meta.SafeLocation;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;

public class LootChest {
    private Alpha alpha;
    private ArrayList<SafeLocation> safeLocation;
    public LootChest(Alpha alpha, ArrayList<SafeLocation> safeLocation) {
        this.alpha = alpha;
        this.safeLocation = safeLocation;
    }
    public static LootChest parseFromYaml(ConfigurationSection section) {
        Alpha alpha = Alpha.valueOf(section.getString("alpha"));
        ArrayList<SafeLocation> a = new ArrayList<>();
        for (String i : section.getStringList("locations")) {
            String[] split = i.split("-");
            
        }
        return new LootChest(Alpha.COMMON, null);
    }
    public Alpha getAlpha() {
        return alpha;
    }
    public ArrayList<SafeLocation> getSafeLocation() {
        return safeLocation;
    }
}
