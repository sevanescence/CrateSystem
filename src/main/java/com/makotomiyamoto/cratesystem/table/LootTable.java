package com.makotomiyamoto.cratesystem.table;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

// ex table_1-ALPHA_ONE.json
public class LootTable {
    private transient Alpha alpha;
    private ArrayList<Pool> pools;
    public LootTable(Alpha alpha, ArrayList<Pool> pools) {
        this.alpha = alpha;
        this.pools = pools;
    }
    public static LootTable parseFromYaml(FileConfiguration table) {
        Alpha alpha = Alpha.valueOf(table.getString("alpha"));
        ArrayList<Pool> pools = new ArrayList<>();
        ConfigurationSection tableSection = table.getConfigurationSection("table");
        assert tableSection != null;
        // TODO get pools
        ConfigurationSection poolSection;
        for (String pool : tableSection.getKeys(false)) {
            poolSection = tableSection.getConfigurationSection(pool);
            assert poolSection != null;
            pools.add(Pool.parseFromYamlSection(poolSection));
        }
        return new LootTable(alpha, pools);
    }
    public Alpha getAlpha() {
        return alpha;
    }
    public ArrayList<Pool> getPools() {
        return pools;
    }
}
