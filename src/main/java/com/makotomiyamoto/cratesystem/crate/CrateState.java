package com.makotomiyamoto.cratesystem.crate;

public class CrateState extends CrateReference {

    private int stage;

    public CrateState() {
        stage = 0;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

}
