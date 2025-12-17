package com.td.engine.model;

import com.td.engine.model.effects.SlowEffect;

public abstract class Enemy {

    // Pozisyon
    protected double x;
    protected double y;

    // Can
    protected double hp;
    protected double maxHp;

    // Hareket ve savunma
    protected double speed;   // birim/sn
    protected int armor;      // zırh
    protected int reward;     // öldürünce verilen para
    protected int baseDamage; // üsse ulaştığında verdiği hasar

    // Yavaşlatma efekti
    protected SlowEffect slowEffect;

    // ---- Getter'lar (Snapshot ve UI için) ----
    public double x()          { return x; }
    public double y()          { return y; }
    public double hp()         { return hp; }
    public double maxHp()      { return maxHp; }
    public int armor()         { return armor; }
    public int reward()        { return reward; }
    public int baseDamage()    { return baseDamage; }

    // Hasar alma
    public void take(double amount) {
        hp = Math.max(0, hp - amount);
    }

    // Can kontrolleri
    public boolean isDead()  { return hp <= 0; }
    public boolean isAlive() { return hp > 0; }

    // Her düşman kendi update'ini yazar (polimorfizm)
    public abstract void update(double dt);

    // Tip adı (Snapshot'ta ve çizimde kullanılıyor)
    public abstract String type();

    // ---- YAVAŞLATMA SİSTEMİ ----

    /** Düşmana süreli yavaşlatma uygular. */
    public void applySlow(double duration, double factor) {
        // factor < 1.0 → yavaşlatma (0.5 = %50 hız)
        // Basit versiyon: her uygulamada önceki efekti ez.
        slowEffect = new SlowEffect(duration, factor);
    }

    /** O frame için geçerli efektif hız (yavaşlama varsa düşürülmüş). */
    public double effectiveSpeed(double dt) {
        if (slowEffect != null) {
            slowEffect.update(dt);
            if (slowEffect.active()) {
                return speed * slowEffect.factor();
            }
        }
        return speed;
    }

    /** UI'da mavi halka çizmek için durum bilgisi. */
    public boolean slowed() {
        return slowEffect != null && slowEffect.active();
    }

    // Üs kontrolü – canvas genişliği ile uyumlu (980 px)
    public boolean reachedBase() {
        return x >= 980.0;
    }

    public double distanceToBase() {
        return 980.0 - x;
    }
}
