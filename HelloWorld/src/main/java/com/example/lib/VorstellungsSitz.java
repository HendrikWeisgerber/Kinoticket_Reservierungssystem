package com.example.lib;

public class VorstellungsSitz {

    private Sitz sitz;
    private boolean isBesetzt;

    public Sitz getSitz() {
        return this.sitz;
    }

    public void setSitz(Sitz sitz) {
        this.sitz = sitz;
    }

    public boolean isIsBesetzt() {
        return this.isBesetzt;
    }

    public void setIsBesetzt(boolean isBesetzt) {
        this.isBesetzt = isBesetzt;
    }

    public VorstellungsSitz(Sitz sitz, boolean isBesetzt){
        this.sitz = sitz;
        this.isBesetzt = isBesetzt;
    }
    
}
