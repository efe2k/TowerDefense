package com.td.engine.model.towers;

import com.td.engine.model.*;
import com.td.engine.model.enemies.ZirhliPatatesDusman;

import java.util.List;

public class BicakKulesi extends Tower {
    public BicakKulesi(double x, double y) { super(x,y,150,1.0,50); } // 1/sn

    @Override protected void onFire(Enemy target, List<Enemy> all) {
        double base = 10;
        if (target instanceof ZirhliPatatesDusman) base *= 0.5; // zırhlıya ceza
        target.take(netDamage(base, target));
    }
}
