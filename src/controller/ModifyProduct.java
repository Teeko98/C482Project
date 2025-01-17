package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The modify product view controller
 * Allows the user to modify a selected product object from the inventory
 *
 * @author Alberto Sosa
 */

public class ModifyProduct implements Initializable {

    // Product fields
    public TextField idTextField;
    public TextField nameTextField;
    public TextField inventoryTextField;
    public TextField priceTextField;
    public TextField minTextField;
    public TextField maxTextField;
    // All parts table view
    public TableView allPartsTableView;
    public TableColumn allPartsIdColumn;
    public TableColumn allPartsNameColumn;
    public TableColumn allPartsInventoryLevelColumn;
    public TableColumn allPartsCostPerUnitColumn;
    public TextField partsSearchBarTextField;
    // Associated parts table view
    public TableView associatedPartsTableView;
    public TableColumn associatedPartsIdColumn;
    public TableColumn associatedPartsNameColumn;
    public TableColumn associatedPartsInventoryLevelColumn;
    public TableColumn associatedPartsCostPerUnitColumn;
    public Product tempProduct;


    /**
     * Initializes the product data with the passed in product.
     * Sets all input fields to that of the passed in product.
     *
     * @param product the product object to be modified
     */
    public void initializeModifyProductData(Product product) {
        idTextField.setText(Integer.toString(product.getId()));
        nameTextField.setText(product.getName());
        inventoryTextField.setText(Integer.toString(product.getStock()));
        priceTextField.setText(Double.toString(product.getPrice()));
        minTextField.setText(Integer.toString(product.getMin()));
        maxTextField.setText(Integer.toString(product.getMax()));
        tempProduct = product;
        associatedPartsTableView.setItems(tempProduct.getAllAssociatedParts());
    }


    /**
     * Method to return to the main view
     */
    public void returnToMainForm(ActionEvent event) throws IOException {
        Parent addPartParent = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Scene addPartScene = new Scene(addPartParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(addPartScene);
        window.show();
    }


    /**
     * Called when the enter key is pressed in the all parts search bar field.
     * Searches the inventory for part objects with either a matching part id or part name.
     */
    public void partsSearchBarEnterPushed() {
        if (partsSearchBarTextField.getText().equals("")){
            allPartsTableView.setItems(Inventory.getAllParts());
            return;
        }
        try {
            ObservableList<Part> partIdSearchSubList = FXCollections.observableArrayList();
            partIdSearchSubList.add(Inventory.lookupPart(Integer.parseInt(partsSearchBarTextField.getText())));
            allPartsTableView.setItems(partIdSearchSubList);
        } catch (NumberFormatException e) {
            allPartsTableView.setItems(Inventory.lookupPart(partsSearchBarTextField.getText()));
        }
    }

    /**
     * Called when the add associated part button is pressed.
     * Adds an associated part object to the product being modified.
     * Allows a part to only be associated to a product once and alerts the user if it is added more than once.
     */
    public void addAssociatedPart() {
        if (!tempProduct.getAllAssociatedParts().contains((Part) allPartsTableView.getSelectionModel().getSelectedItem())){
            tempProduct.addAssociatedPart((Part) allPartsTableView.getSelectionModel().getSelectedItem());
        } else {
            System.out.println("Error: Part is already associated with this product.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: Please select a valid part to add");
            alert.setHeaderText("Error: Please select a valid part to add");
            alert.setContentText("Part is already associated with this product.");
            alert.showAndWait();
            return;
        }

    }

    /**
     * Called when the remove associated part button is pressed.
     * Removes an associated part object from the product object being modified.
     * Prompts the user to confirm the removal of the part object.
     */
    public void removeAssociatedPart() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Remove Associated Part");
        alert.setHeaderText("Confirm Remove Associated Part");
        alert.setContentText("Are you sure you want to remove this associated part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            tempProduct.deleteAssociatedPart((Part) associatedPartsTableView.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Called when the cancel button is pressed.
     * Returns the user to the main view.
     */
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        returnToMainForm(event);
    }

    /**
     * Called when the save product button is pressed.
     * Ensures all input data is of the appropriate data type.
     * Updates the passed in product object from the inventory with the new input data.
     * Returns to the main view.
     */
    public void saveProductButtonPushed(ActionEvent event) throws IOException {


        //Creates base variables for user input
        String inputName = nameTextField.getText();
        double inputPrice = 0;
        int inputStock = 0;
        int inputMin = 0;
        int inputMax = 0;

        //Checks if all input is valid
        if (inputName.equals("")) {
            System.out.println("Invalid Input: No part name was given.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Input in Name Field");
            alert.setContentText("Please enter a valid part ID.");
            alert.showAndWait();
            return;
        }
        try {
            inputStock = Integer.parseInt(inventoryTextField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Input: \"" + inventoryTextField.getText() + "\" in inventory field. Integer expected.\"");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Input in Inventory Field");
            alert.setContentText("Please enter a valid part inventory amount.");
            alert.showAndWait();
            return;
        }
        try {
            inputPrice = Double.parseDouble(priceTextField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Input: \"" + priceTextField.getText() + "\" in price field. Double expected.\"");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Input in Price Field");
            alert.setContentText("Please enter a valid part price.");
            alert.showAndWait();
            return;
        }
        try {
            inputMin = Integer.parseInt(minTextField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Input: \"" + minTextField.getText() + "\" in min field. Integer expected.\"");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Input in Min Field");
            alert.setContentText("Please enter a valid part minimum.");
            alert.showAndWait();
            return;
        }
        try {
            inputMax = Integer.parseInt(maxTextField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Input: \"" + maxTextField.getText() + "\" in max field. Integer expected.\"");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Input in Max Field");
            alert.setContentText("Please enter a valid part maximum.");
            alert.showAndWait();
            return;
        }
        if (inputMin > inputMax) {
            System.out.println("Invalid Input: Inventory minimum " + inputMin + " is greater than inventory maximum " + inputMax);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Minimum must be less than maximum.");
            alert.showAndWait();
            return;
        }
        if (inputStock < inputMin) {
            System.out.println("Invalid Input: Inventory stock " + inputStock + " is less than the minimum required: " + inputMin);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Inventory must be greater than minimum.");
            alert.showAndWait();
            return;
        }
        if (inputStock > inputMax) {
            System.out.println("Invalid Input: Inventory stock " + inputStock + " is greater than the maximum allowed: " + inputMin);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Inventory must be less than maximum.");
            alert.showAndWait();
            return;
        }

        //If all input is valid, product is updated
        Product tempNewProduct = new Product(tempProduct.getId(), inputName, inputPrice, inputStock, inputMin, inputMax);
        for (int i = 0; i < tempProduct.getAllAssociatedParts().size(); i++) {
            tempNewProduct.addAssociatedPart(tempProduct.getAllAssociatedParts().get(i));
        }
        Inventory.updateProduct(Inventory.getAllProducts().indexOf(tempProduct), tempNewProduct);

        returnToMainForm(event);
    }


    /**
     * Initializes the modify product view
     * Creates table views for both the all parts table and associated parts table
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add Product Form Initialized");
        idTextField.setText(Integer.toString(Inventory.getNextProductId()));


        //Initialize parts table
        allPartsTableView.setItems(Inventory.getAllParts());
        allPartsIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsCostPerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


        //Initialize associated parts table
        associatedPartsIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartsInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartsCostPerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

}