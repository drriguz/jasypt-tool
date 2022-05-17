package com.riguz.jasypt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass()
                .getClassLoader()
                .getResource("window.fxml")));

        Scene scene = new Scene(root, 800, 400);
        stage.setTitle("Jasypt tool");
        stage.setScene(scene);
        scene.getStylesheets().add(getClass()
                .getClassLoader()
                .getResource("window.css").toExternalForm());
        stage.getIcons().add(new Image(getClass()
                .getClassLoader()
                .getResourceAsStream("shield.png")));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}