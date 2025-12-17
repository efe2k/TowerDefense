package com.td;

import com.td.engine.GameEngine;
import com.td.engine.api.EngineFacade;

public class ConsoleRunner {
    public static void main(String[] args) throws Exception {
        EngineFacade eng = new GameEngine();

        eng.placeTower("archer", 100, 200);
        eng.placeTower("cannon", 200, 200);
        eng.placeTower("ice",    300, 200);

        eng.startNextWave();

        double t=0;
        while (!eng.isGameOver() && t < 60) {  // 60 sn simülasyon
            eng.tick(0.016);                   // ~60 FPS
            t += 0.016;
            if (Math.abs(t-10.0) < 1e-6) eng.startNextWave(); // 10.sn'de 2. dalga
        }
        System.out.println("Simülasyon bitti. savunma_gunlugu.txt dosyasını kontrol et.");
    }
}
