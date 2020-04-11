package com.makotomiyamoto.cratesystem.table;

import java.util.LinkedList;

public class Pool {

    private int tries, min;
    private LinkedList<ItemDrop> itemDrops;

    public Pool() {
        tries = 1;
        min = 1;
        itemDrops = new LinkedList<>();
    }

    public int getTries() {
        return tries;
    }
    public int getMin() {
        return min;
    }
    public LinkedList<ItemDrop> getItemDrops() {
        return itemDrops;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public void setItemDrops(LinkedList<ItemDrop> itemDrops) {
        this.itemDrops = itemDrops;
    }

}
