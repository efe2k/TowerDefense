package com.td.engine.core;

public final class Damage {
    private Damage() {}
    // Net = KuleHasarı * (1 - (Zırh / (Zırh + 100)))
    public static double net(double base, int armor) {
        return base * (1.0 - (armor / (armor + 100.0)));
    }
}
