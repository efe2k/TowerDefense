package com.td.engine.model;

import com.td.engine.core.Damage;
import com.td.engine.core.Targeting;

import java.util.List;

public abstract class Tower {
    // <-- EKSİK OLAN ALANLAR
    protected double x, y;

    protected double range;
    protected double fireRate;   // atış/saniye
    protected double cooldown;   // kalan süre
    protected int cost;

    // Getter'lar (UI için)
    public double x(){ return x; }
    public double y(){ return y; }

    protected Tower(double x, double y, double range, double fireRate, int cost){
        this.x = x;
        this.y = y;
        this.range = range;
        this.fireRate = fireRate;
        this.cost = cost;
    }

    public int cost(){ return cost; }
    public String type(){ return getClass().getSimpleName(); }

    public void update(double dt, List<Enemy> enemies){
        cooldown = Math.max(0, cooldown - dt);
        if (cooldown > 0) return;

        Enemy target = selectTarget(enemies);
        if (target == null) return;

        onFire(target, enemies);
        cooldown = 1.0 / fireRate;
    }

    protected Enemy selectTarget(List<Enemy> enemies){
        // Varsayılan: menzildekiler arasından üss'e en yakın olan
        return Targeting.nearestToBase(
                enemies.stream().filter(this::inRange).toList()
        );
    }

    protected boolean inRange(Enemy e){
        double dx = (e.x() - x), dy = (e.y() - y);
        return (dx*dx + dy*dy) <= (range*range);
    }

    protected double netDamage(double base, Enemy e){
        return Damage.net(base, e.armor());
    }

    protected abstract void onFire(Enemy target, List<Enemy> all);
}
