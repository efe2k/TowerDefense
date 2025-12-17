package com.td.engine.model.enemies;

import com.td.engine.model.Enemy;

public class ZirhliPatatesDusman extends Enemy {

    public ZirhliPatatesDusman() {
        x = -50;
        y = 240;
        maxHp = 80;
        hp = maxHp;
        speed = 40;
        armor = 3;
        reward = 20;
        baseDamage = 20;
    }

    // YENİ
    public ZirhliPatatesDusman(double startX, double startY) {
        this();
        this.x = startX;
        this.y = startY;
    }

    @Override
    public void update(double dt) {
        x += effectiveSpeed(dt) * dt;
    }

    @Override
    public String type() { return "ZirhliPatatesDusman"; }
}
