package com.td.engine.core;

import com.td.engine.model.Enemy;
import com.td.engine.model.enemies.HavucDusman;
import com.td.engine.model.enemies.ZirhliPatatesDusman;
import com.td.engine.model.enemies.UcanSoganDusman;

import java.util.ArrayList;
import java.util.List;

public class WaveManager {

    // Yürüdükleri hat (MainController'da çizdiğimiz gri çizgi ile aynı hizada olsun)
    // Orada 240 kullanıyorsan bunu da 240 yap.
    private static final double PATH_Y = 240.0;

    /**
     * Belirli dalga numarası için düşman listesini üretir.
     * İsterlerdeki "1. dalga, 2. dalga" kurallarını burada sağlıyoruz.
     */
    public List<Enemy> createWave(int wave) {
        List<Enemy> list = new ArrayList<>();

        if (wave == 1) {
            // ---- 1. DALGA ----
            // 2 standart, 1 zırhlı, 1 uçan → Toplam 4 (≤ 5 şartı sağlanıyor)

            double yGround = PATH_Y;

            // Standart düşmanlar (HavucDusman = standart kabul ediyoruz)
            list.add(new HavucDusman(-60,  yGround));
            list.add(new HavucDusman(-120, yGround));

            // Zırhlı düşman
            list.add(new ZirhliPatatesDusman(-180, yGround));

            // Uçan düşman (biraz yukarıdan gitsin)
            list.add(new UcanSoganDusman(-240, yGround - 40));

        } else if (wave == 2) {
            // ---- 2. DALGA ----
            // En az 5, en fazla 10; her türden en az 1.
            // Örnek kompozisyon: 3 standart, 2 zırhlı, 1 uçan → 6 düşman.

            double yGround = PATH_Y;

            int stdCount  = 3; // standart
            int armCount  = 2; // zırhlı
            int flyCount  = 1; // uçan

            int offset = 60;

            // Standartlar
            for (int i = 0; i < stdCount; i++) {
                list.add(new HavucDusman(-offset * (i + 1), yGround));
            }

            // Zırhlılar
            for (int i = 0; i < armCount; i++) {
                list.add(new ZirhliPatatesDusman(-offset * (stdCount + i + 1), yGround));
            }

            // Uçanlar
            for (int i = 0; i < flyCount; i++) {
                list.add(new UcanSoganDusman(
                        -offset * (stdCount + armCount + i + 1),
                        yGround - 40
                ));
            }
        }

        return list;
    }
}
