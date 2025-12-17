package com.td.engine.model.towers;

import com.td.engine.model.*;
import com.td.engine.model.enemies.UcanSoganDusman;

import java.util.List;

public class OcakKulesi extends Tower {
    public OcakKulesi(double x, double y) { super(x,y,140, 1.0/3.0, 75); } // 3 sn'de 1
    private double aoe = 50;

    @Override protected void onFire(Enemy target, List<Enemy> all) {
        double base = 20;
        for (Enemy e : all) {
            if (e instanceof UcanSoganDusman) continue; // uçanı vuramaz
            double dx = e.x()-target.x(), dy = e.y()-target.y();
            if (dx*dx + dy*dy <= aoe*aoe) {
                e.take(netDamage(base, e));
            }
        }
    }
}
