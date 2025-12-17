package com.td.engine.model.enemies;

import com.td.engine.model.Enemy;

public class HavucDusman extends Enemy {

    public HavucDusman() {
        // Eski başlangıç değerlerin neyse aynen bırak
        x = -50;
        y = 240;
        maxHp = 40;
        hp = maxHp;
        speed = 60;
        armor = 0;
        reward = 10;
        baseDamage = 10;
    }

    // YENİ: Başlangıç konumu verilebilen constructor
    public HavucDusman(double startX, double startY) {
        this();            // üstteki constructor'ı çağır → hp, speed vs ayarlansın
        this.x = startX;   // sadece konumu override et
        this.y = startY;
    }

    @Override
    public void update(double dt) {
        x += effectiveSpeed(dt) * dt;
    }

    @Override
    public String type() { return "HavucDusman"; }
}
