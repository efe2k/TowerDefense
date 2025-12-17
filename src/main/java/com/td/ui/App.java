package com.td.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showMenu();   // önce ana menü
    }

    public static void showMenu() {
        try {
            FXMLLoader fxml = new FXMLLoader(App.class.getResource("/menu-view.fxml"));
            Scene scene = new Scene(fxml.load(), 980, 640);
            primaryStage.setTitle("Tower Defense (MVP)");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showGame() {
        try {
            FXMLLoader fxml = new FXMLLoader(App.class.getResource("/main-view.fxml"));
            Scene scene = new Scene(fxml.load(), 980, 640);
            primaryStage.setTitle("Tower Defense (MVP)");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
