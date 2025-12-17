package com.td.engine;

import com.td.engine.api.Snapshot;
import com.td.engine.core.LoggerService;
import com.td.engine.core.WaveManager;
import com.td.engine.core.GameState;
import com.td.engine.model.Enemy;
import com.td.engine.model.Tower;
import com.td.engine.model.towers.BicakKulesi;
import com.td.engine.model.towers.DondurucuKulesi;
import com.td.engine.model.towers.OcakKulesi;
import com.td.engine.log.FileLogger;   // <<< EKLENDİ

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameEngine implements com.td.engine.api.EngineFacade {

    private final GameState gs      = new GameState();
    private final WaveManager waves = new WaveManager();
    private final LoggerService log = new LoggerService();

    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Tower> towers  = new ArrayList<>();

    // dalga durumu
    private final int maxWaves = 2;
    private int  wavesStarted  = 0;
    private boolean waveActive = false;
    private boolean gameWon    = false;

    // canvas genişliği ile aynı olsun (MainController’da 980px)
    private static final double BASE_X = 980.0;

    // ---------------------------------------------------------
    // YAPICI (simülasyon başlığı)
    // ---------------------------------------------------------
    public GameEngine() {
        // Günlük dosyasını başlat
        FileLogger.header("Zorlu Geçit", gs.lives(), gs.money());
        log.log("Simülasyon başlatıldı. Başlangıç can: " +
                gs.lives() + ", para: " + gs.money());
    }

    // ---------------------------------------------------------
    // ANA OYUN DÖNGÜSÜ
    // ---------------------------------------------------------
    @Override
    public void tick(double dt) {
        // Oyun zaten bitmişse veya kazanılmışsa hiçbir şey yapma
        if (gs.isDead() || gameWon) return;

        // --------- DÜŞMANLARI GÜNCELLE ---------
        for (Enemy e : enemies) {
            e.update(dt);
        }

        // --------- ÜSSE ULAŞANLAR ---------
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            if (e.reachedBase()) {
                gs.damageBase(e.baseDamage());

                // Kısa log
                log.log("Düşman üsse ulaştı: " + e.type()
                        + " (-" + e.baseDamage() + " can)");

                // Detaylı dosya logu
                FileLogger.log(
                        "Düşman '" + enemyLabel(e) + "' üsse ulaştı. Oyuncu Canı: "
                                + gs.lives() + " (-" + e.baseDamage() + ")."
                );

                it.remove();

                // 💥 CAN SIFIRLANDIYSA: OYUN KAYBEDİLDİ
                if (gs.isDead()) {
                    waveActive = false;
                    enemies.clear();  // kalanları önemsemiyoruz

                    log.log("OYUN KAYBEDİLDİ. Üs savunması çöktü.");
                    FileLogger.log("OYUN KAYBEDİLDİ. Üs savunması çöktü.");

                    return; // Aşağıdaki dalga-bitmiş-mi / kazanma kontrolüne hiç girme
                }
            }
        }

        // --------- KULELER ---------
        for (Tower t : towers) {
            t.update(dt, enemies);
        }

        // --------- ÖLENLER (CAN 0 OLANLAR) ---------
        Iterator<Enemy> it2 = enemies.iterator();
        while (it2.hasNext()) {
            Enemy e = it2.next();
            if (!e.isAlive()) {
                gs.earn(e.reward());

                log.log("Düşman öldü: " + e.type()
                        + " (+" + e.reward() + " para)");

                FileLogger.log(
                        "Düşman '" + enemyLabel(e) + "' öldü. Ödül +" +
                                e.reward() + ". Toplam Para: " + gs.money() + "."
                );

                it2.remove();
            }
        }

        // --------- DALGA BİTTİ Mİ? (SADECE YAŞIYORSAN) ---------
        if (!gs.isDead() && waveActive && enemies.isEmpty()) {
            waveActive = false;
            if (wavesStarted >= maxWaves) {
                gameWon = true;
                log.log("SON: Tüm dalgalar temizlendi. OYUN KAZANILDI!");
                FileLogger.log("OYUN KAZANILDI. Tüm dalgalar temizlendi.");
            }
        }
    }

    // ---------------------------------------------------------
    // DALGA BAŞLATMA
    // ---------------------------------------------------------
    @Override
    public void startNextWave() {
        if (waveActive || wavesStarted >= maxWaves) return;

        wavesStarted++;
        gs.nextWave();

        // Bu dalgada çıkan düşmanları ayrı liste ile alalım
        List<Enemy> yeni = waves.createWave(gs.wave());
        enemies.addAll(yeni);
        waveActive = true;

        log.log("Dalga başladı: " + gs.wave());
        FileLogger.log("Dalga " + gs.wave() + " başladı. (Toplam Düşman: " +
                yeni.size() + ")");

        // Her düşman için detay log
        for (Enemy e : yeni) {
            FileLogger.log("Düşman '" + enemyLabel(e) + "' (Can: " +
                    e.hp() + "/" + e.maxHp() + ") haritaya girdi.");
        }
    }

    // ---------------------------------------------------------
    // KULE YERLEŞTİRME
    // ---------------------------------------------------------
    private boolean canPlaceHere(int x, int y) {
        if (x < 0 || x > BASE_X) return false;
        if (y < 0 || y > 240 - 10) return false; // üst oyun alanı
        return true;
    }

    private boolean placeTowerInternal(String type, int x, int y) {
        Tower t;

        switch (type.toLowerCase()) {
            case "bicak"      -> t = new BicakKulesi(x, y);
            case "ocak"       -> t = new OcakKulesi(x, y);
            case "dondurucu"  -> t = new DondurucuKulesi(x, y);
            default           -> { return false; }
        }

        if (!canPlaceHere(x, y))      return false;
        if (gs.money() < t.cost())    return false;

        gs.spend(t.cost());
        towers.add(t);

        log.log("Kule yerleştirildi: " + t.type() + " (" + x + "," + y + ")");
        FileLogger.log("Kullanıcı, (" + x + ", " + y + ") konumuna '" +
                towerLabel(t) + "' inşa etti. Kalan Para: " + gs.money() + ".");

        return true;
    }

    public boolean placeTower(String type, int x, int y) {
        return placeTowerInternal(type, x, y);
    }

    // ---------------------------------------------------------
    // SNAPSHOT
    // ---------------------------------------------------------
    @Override
    public Snapshot getSnapshot() {
        var hud = new Snapshot.HUD(
                gs.lives(),
                gs.money(),
                gs.wave(),
                waveActive,
                gs.isDead(),
                gameWon
        );

        var es = enemies.stream().map(e ->
                new Snapshot.EnemyView(
                        e.x(),
                        e.y(),
                        e.hp(),
                        e.maxHp(),
                        e.slowed(),
                        e.type()
                )
        ).toList();

        var ts = towers.stream().map(t ->
                new Snapshot.TowerView(
                        t.x(),
                        t.y(),
                        t.type(),
                        0.0
                )
        ).toList();

        return new Snapshot(hud, es, ts);
    }

    @Override
    public boolean isGameOver() {
        return gs.isDead();
    }

    // ---------------------------------------------------------
    // YARDIMCI LABEL METODLARI (ID benzeri)
    // ---------------------------------------------------------
    private String towerLabel(Tower t) {
        // Örn: BicakKulesi-ID3F2A
        return t.type() + "-ID" +
                Integer.toHexString(System.identityHashCode(t)).toUpperCase();
    }

    private String enemyLabel(Enemy e) {
        // Örn: ZirhliPatatesDusman-ID9AC1
        return e.type() + "-ID" +
                Integer.toHexString(System.identityHashCode(e)).toUpperCase();
    }
}
