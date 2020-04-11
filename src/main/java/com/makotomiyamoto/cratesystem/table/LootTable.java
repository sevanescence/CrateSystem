package com.makotomiyamoto.cratesystem.table;

import java.util.LinkedList;

public class LootTable {

    private transient String fileName;
    private LinkedList<Pool> pools;

    public LootTable() {
        fileName = "none";
        pools = new LinkedList<>();
    }

    public String getFileName() {
        return fileName;
    }
    public LinkedList<Pool> getPools() {
        return pools;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public void setPools(LinkedList<Pool> pools) {
        this.pools = pools;
    }

}
