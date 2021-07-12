package com;

import Controllers.MainWindow.LayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    public static final EngineInter eng = new Engine();
    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/Controllers/MainWindow/Layout.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane root = fxmlLoader.load(url.openStream());
        LayoutController mainController = fxmlLoader.getController();
        mainController.setPrimaryStage(primaryStage);
        mainController.BindChoiceBoxes();

       /*
        LoadLogic logic = new LoadLogic(mainController);
        mainController.setLoadLogic(logic);
       */

        Scene scene = new Scene(root, 1000, 750);
        primaryStage.setScene(scene);

        primaryStage.show();
        mainController.LightMode();


    }

    public static void main(String[] args) {
        launch(Main.class);
    }
}
