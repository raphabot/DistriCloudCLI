/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.rbottino.distcloudcli.MainGui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.abstracts.ProviderAbstract;

/**
 *
 * @author raphabot
 */
public class ProviderViewController {

    @FXML
    private TableView<ProviderAbstract> providerTable;
    @FXML
    private TableColumn<ProviderAbstract, String> firstColumn;
    @FXML
    private TableColumn<ProviderAbstract, String> secondColumn;

    @FXML
    private Label filePartsLabe;
    @FXML
    private Label secondLabel;
    @FXML
    private Label thirdLabel;
    

    // Reference to the main application.
    private MainGui mainGui;

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public ProviderViewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        firstColumn.setCellValueFactory(cellData -> cellData.getValue().getAppIdProperty());
        secondColumn.setCellValueFactory(cellData -> cellData.getValue().getAppIdProperty());
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainGui mainGui) {
        this.mainGui = mainGui;

        // Add observable list data to the table
        providerTable.setItems(mainGui.getObservableProviders());
    }
}
