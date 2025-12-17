package com.td.engine.api;

public interface EngineFacade {
    void tick(double dt);                 // saniye cinsinden
    void startNextWave();
    boolean placeTower(String type, int gx, int gy); // para & kural kontrolü
    Snapshot getSnapshot();
    boolean isGameOver();
}
