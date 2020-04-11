package com.makotomiyamoto.cratesystem.crate;

import com.makotomiyamoto.cratesystem.meta.SafeLocation;

import java.util.ArrayList;
import java.util.List;

public class CrateReference {

    private List<String> lootTablePointers;
    private SafeLocation safeLocation;

    public CrateReference() {
        lootTablePointers = new ArrayList<>();
        safeLocation = null;
    }

    public List<String> getLootTablePointers() {
        return lootTablePointers;
    }
    public SafeLocation getSafeLocation() {
        return safeLocation;
    }

    public void setLootTablePointers(List<String> lootTablePointers) {
        this.lootTablePointers = lootTablePointers;
    }
    public void setSafeLocation(SafeLocation safeLocation) {
        this.safeLocation = safeLocation;
    }

}
