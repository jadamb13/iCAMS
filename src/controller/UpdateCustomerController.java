package controller;

import DBAccess.DBAppointment;
import DBAccess.DBCustomer;
import DBAccess.DBDivision;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Appointment;
import model.Contact;
import model.Country;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/** A Controller class for the UpdateCustomer view. */
public class UpdateCustomerController implements Initializable {

    @FXML
    private TextField customerIdTxt;
    @FXML
    private TextField nameTxt;

    @FXML
    private TextField postalCodeTxt;
    @FXML
    private TextField phoneNumberTxt;
    @FXML
    private ComboBox<String> countryCb;
    @FXML
    private ComboBox<String> divisionCb;
    @FXML
    private TextField addressTxt;


    /**
     Initializes UpdateCustomer view.

     @param url location information
     @param resourceBundle resource information
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("UpdateCustomer Initialized.");
        try {
            Customer selected = MainViewController.getSelectedCustomer();
            customerIdTxt.setText(String.valueOf(selected.getCustomerId()));
            nameTxt.setText(selected.getCustomerName());
            addressTxt.setText(selected.getCustomerAddress());
            postalCodeTxt.setText(selected.getCustomerPostalCode());
            phoneNumberTxt.setText(selected.getCustomerPhoneNumber());
            countryCb.setItems(Country.getAllCountryNames());
            String countryName = DBDivision.getCountryNameById(selected.getDivisionId());
            countryCb.setValue(countryName);

            // Populate divisionCb based on the selected country
            switch (countryName) {
                case "U.S" -> {
                    divisionCb.setItems(DBDivision.getAllUnitedStatesDivisionNames());
                    divisionCb.setValue(DBDivision.getDivisionNameById(selected.getDivisionId()));
                }
                case "UK" -> {
                    divisionCb.setItems(DBDivision.getAllUnitedKingdomDivisionNames());
                    divisionCb.setValue(DBDivision.getDivisionNameById(selected.getDivisionId()));
                }
                case "Canada" -> {
                    divisionCb.setItems(DBDivision.getAllCanadaDivisionNames());
                    divisionCb.setValue(DBDivision.getDivisionNameById(selected.getDivisionId()));
                }
            }

            // Set up listener for countryCb
            countryCb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    // Populate divisionCb based on the selected country
                    String selectedCountry = newValue.toString();
                    if (selectedCountry.equals("U.S")) {
                        divisionCb.setItems(DBDivision.getAllUnitedStatesDivisionNames());
                    } else if (selectedCountry.equals("UK")) {
                        divisionCb.setItems(DBDivision.getAllUnitedKingdomDivisionNames());
                    } else if (selectedCountry.equals("Canada")) {
                        divisionCb.setItems(DBDivision.getAllCanadaDivisionNames());
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Caught ye AddCustomerController: " + e.getMessage());
        }
    }



    /**
     Checks the user input for validity, creates a new Customer object, and updates the Customer in DB before returning
     the user to the MainView.

     @param actionEvent object containing information about the page where user clicks Save button
     //@exception IOException thrown if FXMLLoader.load() resource is Null
     //@exception SQLException thrown in case of invalid SQL statement
     */
    public void saveUpdatedCustomer(ActionEvent actionEvent) {
        try {
            // Form data
            int customerId = Integer.parseInt(customerIdTxt.getText());
            String customerName = nameTxt.getText();
            String address = addressTxt.getText();
            String postalCode = postalCodeTxt.getText();
            String phone = phoneNumberTxt.getText();
            String divisionName = divisionCb.getSelectionModel().getSelectedItem();
            int divisionId = DBDivision.getDivisionIdByName(divisionName);

            // Use insertAppointment() and data from form to insert new Appointment into DB
            DBCustomer.updateCustomerInDb(customerId, customerName, address, postalCode, phone, divisionId);
        }catch(Exception e){
            System.out.println("Caught ye: " + e.getMessage());
        }
        // Close AddAppointment view and show MainView
        MainViewController.getMainViewStage().close();
    }

    /**
     Returns user to the MainView.

     @param actionEvent ActionEvent object holding information about page where user clicks Cancel button
     @exception IOException thrown if FXMLLoader.load() resource is Null
     */
    public void displayCustomerTab(ActionEvent actionEvent) {
        MainViewController.getMainViewStage().close();
    }



}
