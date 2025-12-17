package com.td.ui;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;

import com.td.engine.GameEngine;
import com.td.engine.api.Snapshot;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class MainController {

    // -----------------------------------------------------
    // FXML NESNELERİ
    // -----------------------------------------------------
    @FXML private Canvas canvas;

    @FXML private Button btnStart;
    @FXML private Button btnArcher;
    @FXML private Button btnCannon;
    @FXML private Button btnIce;
    @FXML private Button btnRestart;

    @FXML private Label lLives;
    @FXML private Label lMoney;
    @FXML private Label lWave;

    // -----------------------------------------------------
    // OYUN DURUMU
    // -----------------------------------------------------
    private GraphicsContext g;
    private GameEngine eng;
    private AnimationTimer timer;

    private String towerToBuild = null;

    // -----------------------------------------------------
    // INITIALIZE
    // -----------------------------------------------------
    @FXML
    public void initialize() {
        g = canvas.getGraphicsContext2D();
        eng = new GameEngine();

        startGameLoop();

        canvas.setOnMouseClicked(this::onCanvasClicked);
    }

    private void onCanvasClicked(MouseEvent e) {
        if (towerToBuild == null) return;
        eng.placeTower(towerToBuild, (int)e.getX(), (int)e.getY());
        towerToBuild = null;
        resetTowerButtonHighlight();
    }

    // -----------------------------------------------------
    // OYUN DÖNGÜSÜ
    // -----------------------------------------------------
    private void startGameLoop() {
        timer = new AnimationTimer() {
            long last = System.nanoTime();

            @Override
            public void handle(long now) {
                double dt = (now - last) / 1e9;
                last = now;

                eng.tick(dt);
                Snapshot s = eng.getSnapshot();
                draw(s);

                if (s.hud().gameOver()) {
                    showEndDialog(true);
                    stop();
                } else if (s.hud().gameWon()) {
                    showEndDialog(false);
                    stop();
                }
            }
        };
        timer.start();
    }

    // -----------------------------------------------------
    // OYUN SONU POPUP
    // -----------------------------------------------------
    private void showEndDialog(boolean gameOver) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Oyun Bitti");
        alert.setHeaderText(null);

        if (gameOver)
            alert.setContentText("OYUN BİTTİ!\nYemek yandı 😢");
        else
            alert.setContentText("TEBRİKLER!\nYemeği kurtardınız 🥘");

        alert.show();
    }

    // -----------------------------------------------------
    // RESTART
    // -----------------------------------------------------
    @FXML
    private void onRestartGame() {
        if (timer != null) timer.stop();
        eng = new GameEngine();
        towerToBuild = null;
        resetTowerButtonHighlight();
        startGameLoop();
    }

    // -----------------------------------------------------
    // DALGA BAŞLAT
    // -----------------------------------------------------
    @FXML
    private void onStartWaveClicked() {
        eng.startNextWave();
    }

    // menüde eski isim varsa hata çıkmasın
    @FXML
    private void onStartWave() { onStartWaveClicked(); }

    // -----------------------------------------------------
    // KULE SEÇİMİ
    // -----------------------------------------------------
    @FXML private void onBuildBicak() {
        towerToBuild = "bicak";
        highlight(btnArcher);
    }

    @FXML private void onBuildOcak() {
        towerToBuild = "ocak";
        highlight(btnCannon);
    }

    @FXML private void onBuildDondurucu() {
        towerToBuild = "dondurucu";
        highlight(btnIce);
    }

    @FXML private void onBuildArcher() { onBuildBicak(); }
    @FXML private void onBuildCannon() { onBuildOcak(); }
    @FXML private void onBuildIce()    { onBuildDondurucu(); }

    // -----------------------------------------------------
    // BUTTON HİGHLIGHT
    // -----------------------------------------------------
    private void resetTowerButtonHighlight() {
        if (btnArcher != null) btnArcher.setStyle("");
        if (btnCannon != null) btnCannon.setStyle("");
        if (btnIce != null)    btnIce.setStyle("");
    }

    private void highlight(Button b) {
        resetTowerButtonHighlight();
        if (b != null)
            b.setStyle("-fx-border-color: #ff5500; -fx-border-width: 2px;");
    }

    // -----------------------------------------------------
    // DRAW LOOP
    // -----------------------------------------------------
    private void draw(Snapshot s) {

        // HUD
        lLives.setText(String.valueOf(s.hud().lives()));
        lMoney.setText(String.valueOf(s.hud().money()));
        lWave.setText(String.valueOf(s.hud().wave()));

        // Dalga / restart butonları
        if (btnStart != null) {
            btnStart.setDisable(s.hud().waveActive());
        }
        if (btnRestart != null) {
            btnRestart.setDisable(!(s.hud().gameOver() || s.hud().gameWon()));
        }



        btnArcher.setDisable(s.hud().money() < 50);
        btnCannon.setDisable(s.hud().money() < 75);
        btnIce.setDisable(s.hud().money() < 70);

        drawBackground();

        g.setStroke(Color.rgb(180,180,180));
        g.setLineWidth(2);
        g.strokeLine(240,260, canvas.getWidth()-240,260);

        // düşmanlar
        s.enemies().forEach(this::drawEnemy);

        // kuleler
        s.towers().forEach(this::drawTower);

        // oyun sonu overlay
        if (s.hud().gameOver() || s.hud().gameWon()) {
            drawOverlayText(
                    s.hud().gameOver()
                            ? "OYUN BİTTİ\nYemek yandı 😢"
                            : "TEBRİKLER\nYemeği kurtardınız 🥘"
            );
        }
    }

    // -----------------------------------------------------
    // ARKA PLAN
    // -----------------------------------------------------
    private void drawBackground() {
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        // 1. Genel arka plan – çok açık krem
        g.setFill(Color.web("#fdf6ee")); // daha sıcak pastel
        g.fillRect(0, 0, w, h);

        // 2. Oyun alanı – pastel açık ahşap
        double boardX = 240;
        double boardY = 120;
        double boardW = w - 2 * 240;
        double boardH = 300;

        g.setFill(Color.web("#f2e6d8")); // açık ahşap ton
        g.fillRoundRect(boardX, boardY, boardW, boardH, 25, 25);

        // 3. Hafif gölge (soft look)
        g.setStroke(Color.rgb(0,0,0,0.05));
        g.setLineWidth(8);
        g.strokeRoundRect(boardX, boardY, boardW, boardH, 25, 25);

        // 4. Çok nazik ahşap damar çizgileri
        g.setStroke(Color.rgb(0,0,0,0.07));
        g.setLineWidth(1);
        for (double x = boardX + 40; x < boardX + boardW; x += 40) {
            g.strokeLine(x, boardY + 10, x, boardY + boardH - 10);
        }
    }


    // -----------------------------------------------------
    // DÜŞMAN ÇİZİMİ
    // -----------------------------------------------------
    private void drawEnemy(Snapshot.EnemyView e) {
        switch (e.type()) {
            case "ZirhliPatatesDusman" -> drawPotatoEnemy(e);
            case "HavucDusman"         -> drawCarrotEnemy(e);
            case "UcanSoganDusman"     -> drawOnionEnemy(e);
            default                    -> drawSimpleEnemy(e);
        }
    }

    private void drawPotatoEnemy(Snapshot.EnemyView e) {
        double cx=e.x(), cy=e.y();
        double rw=18, rh=22;

        g.setFill(Color.SADDLEBROWN);
        g.fillOval(cx-rw/2, cy-rh/2, rw, rh);

        g.setFill(Color.SILVER);
        g.fillRect(cx-rw/2, cy-3, rw,6);

        g.setFill(Color.BEIGE);
        g.fillOval(cx-5, cy-6,4,4);
        g.fillOval(cx+1, cy-6,4,4);

        drawEnemyHpBar(e,rw);
        drawSlowRing(e,Math.max(rw,rh)/2);
    }

    private void drawCarrotEnemy(Snapshot.EnemyView e) {
        double cx=e.x(), cy=e.y();
        double size=22;

        g.setFill(Color.ORANGE);
        g.fillPolygon(
                new double[]{cx-size/2, cx+size/2, cx},
                new double[]{cy-size/2, cy-size/2, cy+size/2},
                3
        );

        g.setStroke(Color.LIMEGREEN);
        g.setLineWidth(3);
        g.strokeLine(cx,cy-size/2, cx, cy-size/2-8);

        drawEnemyHpBar(e,size);
        drawSlowRing(e,size/2);
    }

    private void drawOnionEnemy(Snapshot.EnemyView e) {
        double cx=e.x(), cy=e.y()-6;
        double r=13;

        g.setFill(Color.MEDIUMPURPLE);
        g.fillOval(cx-r,cy-r,2*r,2*r);

        g.setStroke(Color.CORNFLOWERBLUE);
        g.setLineWidth(3);
        g.strokeOval(cx-r-4, cy-4, (r+4)*2, 8);

        drawEnemyHpBar(e,r*2);
        drawSlowRing(e,r);
    }

    private void drawSimpleEnemy(Snapshot.EnemyView e) {
        double r=12;
        g.setFill(Color.WHITE);
        g.fillOval(e.x()-r, e.y()-r, 2*r,2*r);
        drawEnemyHpBar(e,r*2);
        drawSlowRing(e,r);
    }

    private void drawEnemyHpBar(Snapshot.EnemyView e, double widthRef) {
        double w=Math.max(28,widthRef), h=4;
        double px=e.x()-w/2;
        double py=e.y()-20;

        double ratio=e.hp()/Math.max(1.0,e.maxHp());

        g.setFill(Color.DARKRED);
        g.fillRect(px,py,w,h);

        g.setFill(Color.LIMEGREEN);
        g.fillRect(px,py,w*ratio,h);
    }

    private void drawSlowRing(Snapshot.EnemyView e, double r) {
        if(!e.slowed()) return;
        g.setStroke(Color.CYAN);
        g.setLineWidth(2);
        g.strokeOval(e.x()-r-4, e.y()-r-4, 2*(r+4),2*(r+4));
    }

    // -----------------------------------------------------
    // KULELER
    // -----------------------------------------------------
    private void drawTower(Snapshot.TowerView t) {
        switch (t.type()) {
            case "BicakKulesi"     -> drawKnifeTower(t);
            case "OcakKulesi"      -> drawStoveTower(t);
            case "DondurucuKulesi" -> drawFreezerTower(t);
            default                -> drawSimpleTower(t);
        }
    }

    private void drawKnifeTower(Snapshot.TowerView t) {
        double x=t.x(), y=t.y(), h=40;

        g.setFill(Color.SADDLEBROWN);
        g.fillRoundRect(x-5, y-h/2, 10, h/2,4,4);

        g.setFill(Color.SILVER);
        g.fillPolygon(
                new double[]{x-4, x+4, x},
                new double[]{y, y, y+h/2},
                3
        );
    }

    private void drawStoveTower(Snapshot.TowerView t) {
        double x=t.x(), y=t.y(), size=30;

        g.setFill(Color.DARKSLATEGRAY);
        g.fillRoundRect(x-size/2, y-size/2, size,size,6,6);

        g.setFill(Color.ORANGERED);
        g.fillOval(x-8,y-4,16,8);

        g.setStroke(Color.YELLOW);
        g.setLineWidth(1.5);
        g.strokeOval(x-8,y-4,16,8);
    }

    private void drawFreezerTower(Snapshot.TowerView t) {
        double x=t.x(), y=t.y();
        double w=26,h=36;

        g.setFill(Color.LIGHTSKYBLUE);
        g.fillRoundRect(x-w/2,y-h/2,w,h,6,6);

        g.setStroke(Color.WHITE);
        g.setLineWidth(2);
        g.strokeLine(x-w/2+4, y, x+w/2-4, y);
    }

    private void drawSimpleTower(Snapshot.TowerView t) {
        double r=12;
        g.setFill(Color.WHITE);
        g.fillRoundRect(t.x()-r, t.y()-r, 2*r,2*r,4,4);
    }

    // -----------------------------------------------------
    // OYUN SONU OVERLAY
    // -----------------------------------------------------
    private void drawOverlayText(String text){
        double w=canvas.getWidth(), h=canvas.getHeight();

        g.setFill(Color.color(0,0,0,0.35));
        g.fillRect(0,0,w,h);

        g.setFill(Color.WHITE);
        g.setFont(Font.font("System", FontWeight.BOLD, 28));
        g.setTextAlign(TextAlignment.CENTER);
        g.setTextBaseline(VPos.CENTER);
        g.fillText(text, w/2, h/2);
    }
}
