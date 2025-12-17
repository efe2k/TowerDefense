package com.td.engine.core;


public class GameState {

    private int lives  = 100;   // başlangıç can
    private int money  = 200;   // başlangıç para
    private int wave   = 0;     // 0’dan başlasın

    public int lives() { return lives; }
    public int money() { return money; }
    public int wave()  { return wave; }

    public void earn(int amount) {
        money += amount;
    }

    public void spend(int amount) {
        money = Math.max(0, money - amount);
    }

    public void damageBase(int dmg) {
        lives = Math.max(0, lives - dmg);
    }

    public void nextWave() {
        wave++;
    }

    public boolean isDead() {
        return lives <= 0;
    }
}
