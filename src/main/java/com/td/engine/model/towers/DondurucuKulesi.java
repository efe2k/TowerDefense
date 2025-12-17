package com.td.engine.model.towers;

import com.td.engine.model.Enemy;
import com.td.engine.model.Tower;

import java.util.List;

public class DondurucuKulesi extends Tower {

    // Bu kule menzili içinde alan yavaşlatma yapar
    private static final double SLOW_RADIUS   = 80;   // hedefin etrafındaki yarıçap
    private static final double SLOW_DURATION = 1.5;  // saniye
    private static final double SLOW_FACTOR   = 0.4;  // %40 hız (yani %60 yavaşlatma)

    public DondurucuKulesi(double x, double y) {
        super(x, y, 160, 0.7, 70); // range, fireRate, cost
    }

    @Override
    protected void onFire(Enemy target, List<Enemy> all) {

        // SADECE mevcuttaki düşmanlara slow uygula,
        // LİSTEYE YENİ DÜŞMAN EKLEME, ÇIKARMA YOK.
        double r2 = SLOW_RADIUS * SLOW_RADIUS;

        for (Enemy e : all) {
            if (!e.isAlive()) continue;

            double dx = e.x() - target.x();
            double dy = e.y() - target.y();
            if (dx * dx + dy * dy <= r2) {
                // hepsi kendi Enemy örneği üzerinde çalışıyor
                e.applySlow(SLOW_DURATION, SLOW_FACTOR);
            }
        }
    }
}
