package com.td.engine.model.effects;

public class SlowEffect {

    private double remaining; // kalan süre
    private double factor;    // hız çarpanı (0.4 = %40 hız)

    public SlowEffect(double duration, double factor) {
        this.remaining = duration;
        this.factor = factor;
    }

    public void update(double dt) {
        remaining = Math.max(0, remaining - dt);
    }

    public boolean active() {
        return remaining > 0;
    }

    public double factor() {
        return factor;
    }
}
