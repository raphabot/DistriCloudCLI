/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rbottino.distcloudcli;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.abstracts.ProviderAbstract;
import models.providers.DropboxProvider;
import view.ProviderViewController;

public class MainGui extends Application {
    
    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<ProviderAbstract> observableProviders = FXCollections.observableArrayList();

    public MainGui(){
        DropboxProvider dp = new DropboxProvider();
        observableProviders.add(dp);
    }
    
    public ObservableList<ProviderAbstract> getObservableProviders() {
        return observableProviders;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("DistriCloud");

        initRootLayout();

        showProvidersList();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            System.out.println(getClass().getResource("/fxml/RootLayout.fxml"));
            loader.setLocation(MainGui.class.getResource("/fxml/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showProvidersList() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainGui.class.getResource("/fxml/providersList.fxml"));
            AnchorPane providerOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(providerOverview);

            // Give the controller access to the main app.
            ProviderViewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
