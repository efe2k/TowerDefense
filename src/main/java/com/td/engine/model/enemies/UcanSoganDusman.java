package com.td.engine.model.enemies;

import com.td.engine.model.Enemy;

public class UcanSoganDusman extends Enemy {

    public UcanSoganDusman() {
        x = -50;
        y = 200;     // uçan olduğu için biraz yukarıda
        maxHp = 30;
        hp = maxHp;
        speed = 90;
        armor = 1;
        reward = 15;
        baseDamage = 15;
    }

    // YENİ
    public UcanSoganDusman(double startX, double startY) {
        this();
        this.x = startX;
        this.y = startY;
    }

    @Override
    public void update(double dt) {
        x += effectiveSpeed(dt) * dt;
    }

    @Override
    public String type() { return "UcanSoganDusman"; }
}
