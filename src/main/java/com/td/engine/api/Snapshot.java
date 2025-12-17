package com.td.engine.api;

import java.util.List;

public record Snapshot(
        HUD hud,
        List<EnemyView> enemies,
        List<TowerView> towers
) {

    // Üstteki HUD bilgileri
    public record HUD(
            int lives,
            int money,
            int wave,
            boolean waveActive,
            boolean gameOver,
            boolean gameWon
    ) {}

    // UI'nın ihtiyaç duyduğu düşman bilgisi (pozisyon + can + tip + yavaşlatma)
    public record EnemyView(
            double x,
            double y,
            double hp,
            double maxHp,
            boolean slowed,
            String type
    ) {}

    // Kule bilgisi (pozisyon + tip + cooldown)
    public record TowerView(
            double x,
            double y,
            String type,
            double cooldown
    ) {}
}
