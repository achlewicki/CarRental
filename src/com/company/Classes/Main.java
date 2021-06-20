/**@author Chlewicki Adam */
package com.company.Classes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/company/Views/LoginForm.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Wypożyczalnia Aut - Rent&Drive");
        Scene scene = new Scene(root, 1200, 750);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
