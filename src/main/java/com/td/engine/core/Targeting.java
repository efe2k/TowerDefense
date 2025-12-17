package com.td.engine.core;

import com.td.engine.model.Enemy;
import java.util.Comparator;
import java.util.List;

// "Üsse en yakın" hedefi seç
public final class Targeting {
    private Targeting(){}
    public static Enemy nearestToBase(List<Enemy> enemies) {
        return enemies.stream()
                .filter(Enemy::isAlive)
                .min(Comparator.comparingDouble(Enemy::distanceToBase))
                .orElse(null);
    }
}
