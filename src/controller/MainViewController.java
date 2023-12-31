package controller;

import DBAccess.DBAppointment;
import DBAccess.DBCustomer;
import helper.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** A Controller class for the MainView. */
public class MainViewController implements Initializable {


    /* Control elements for MainView/Appointment Tab */
    @FXML
    private ComboBox<String> byMonthCb;
    @FXML
    private ComboBox<String> byWeekCb;
    @FXML
    private ComboBox<String> contactCb;
    @FXML
    private RadioButton allAppointmentsRBtn;


    /* Appointments table on Appointments Tab */
    @FXML
    private TableView<Appointment> appointmentsTable;
    @FXML
    private TableColumn<Appointment, Integer> apptIdCol;
    @FXML
    private TableColumn<Appointment, String> apptTitleCol;
    @FXML
    private TableColumn<Appointment, String> apptDescriptionCol;
    @FXML
    private TableColumn<Appointment, String> apptLocationCol;
    @FXML
    private TableColumn<Appointment, String> apptContactCol;
    @FXML
    private TableColumn<Appointment, String> apptTypeCol;
    @FXML
    private TableColumn<Appointment, String> apptStartDateCol;
    @FXML
    private TableColumn<Appointment, String> apptStartTimeCol;
    @FXML
    private TableColumn<Appointment, String> apptEndDateCol;
    @FXML
    private TableColumn<Appointment, String> apptEndTimeCol;
    @FXML
    private TableColumn<Appointment, Integer> apptCustomerIdCol;
    @FXML
    private TableColumn<Appointment, Integer> apptUserIdCol;


    /* Customer table on Customer Tab */
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> customerIdCol;
    @FXML
    private TableColumn<Customer, String> nameCol;
    @FXML
    private TableColumn<Customer, String> addressCol;
    @FXML
    private TableColumn<Customer, String> postalCodeCol;
    @FXML
    private TableColumn<Customer, String> phoneCol;
    @FXML
    private TableColumn<Customer, Integer> divisionIdCol;


    /* Appointment table on Reports Tab */
    @FXML
    private TableView<Appointment> appointmentScheduleTable;
    @FXML
    private TableColumn<Appointment, String> contactApptEndDateCol;
    @FXML
    private TableColumn<Appointment, Integer> contactApptCustomerIdCol;
    @FXML
    private TableColumn<Appointment, Integer> contactApptIdCol;
    @FXML
    private TableColumn<Appointment, String> contactApptTitleCol;
    @FXML
    private TableColumn<Appointment, String> contactApptDescCol;
    @FXML
    private TableColumn<Appointment, String> contactApptTypeCol;
    @FXML
    private TableColumn<Appointment, String> contactApptStartTimeCol;
    @FXML
    private TableColumn<Appointment, String> contactApptStartDateCol;
    @FXML
    private TableColumn<Appointment, String> contactApptEndTimeCol;

    /* Appointments by Type table on Reports Tab */
    @FXML
    private TableView<AppointmentType> appointmentsByTypeTable;
    @FXML
    private TableColumn<AppointmentType, String> byTypeCol;
    @FXML
    private TableColumn<AppointmentType, Integer> numberByTypeCol;

    /* Appointments by Month table on Reports Tab */
    @FXML
    private TableView<AppointmentMonth> appointmentsByMonthTable;
    @FXML
    private TableColumn<AppointmentMonth, String> byMonthCol;
    @FXML
    private TableColumn<AppointmentMonth, Integer> numberByMonthCol;

    /* Appointments by Day of Week on Reports Tab */
    @FXML
    private TableView<AppointmentDay> appointmentsByDayOfWeek;
    @FXML
    private TableColumn<AppointmentDay, String> dayOfWeekCol;
    @FXML
    private TableColumn<AppointmentDay, Integer> numberByDayCol;

    @FXML
    private static Stage mainViewStage;
    @FXML
    private static Appointment selectedAppointment;
    @FXML
    private static Customer selectedCustomer;


    // Getters and Setters
    public static Stage getMainViewStage() {
        return mainViewStage;
    }

    public static Appointment getSelectedAppointment() {
        return selectedAppointment;
    }

    public static Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    Stage stage;
    Parent scene;

    /**
     * Initializes MainView.
     *
     * @param url            location information
     * @param resourceBundle resource information
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        allAppointmentsRBtn.setSelected(true);

        /* Appointments Tab */
        // Set values in Appointments Table
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        apptDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        apptContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        apptStartDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        apptEndDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        apptStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        apptEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        apptCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        apptUserIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        // Sort Appointments Table by Appointment ID
        appointmentsTable.getSortOrder().add(apptIdCol);

        // Set listeners on both Combo Boxes of Appointments Tab
        setByMonthListener();
        setByWeekListener();

        /* Customers Tab */
        // Set values in Customers Table
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));
        divisionIdCol.setCellValueFactory(new PropertyValueFactory<>("divisionId"));

        // Sort Customer Table by Customer ID
        customerTable.getSortOrder().add(customerIdCol);

        /* Reports Tab */
        contactApptIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        contactApptTitleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        contactApptDescCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        contactApptTypeCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        contactApptStartDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        contactApptEndDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        contactApptStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        contactApptEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        contactApptCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        contactCb.setItems(Contact.getAllContactNames());

        // Set up columns for Appointments By Type table
        byTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        numberByTypeCol.setCellValueFactory(new PropertyValueFactory<>("countOfType"));

        // Set up columns for  Appointments By Month table
        byMonthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        numberByMonthCol.setCellValueFactory(new PropertyValueFactory<>("CountByMonth"));


        // Initialize Appointments By Day of Week table
        dayOfWeekCol.setCellValueFactory(new PropertyValueFactory<>("dayOfWeek"));
        numberByDayCol.setCellValueFactory(new PropertyValueFactory<>("numberOfAppointmentsOnDay"));

        populateTables();


        // Alert customer to whether there is an appointment in the next 15 minutes or not
        Utility.alertUpcomingAppointments();
    }

    /**
     * Displays overlay window/stage of the AddAppointment view.
     *
     * @param actionEvent represents user click Add button under Appointments Table
     */
    public void displayAddAppointmentWindow(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddAppointment.fxml"));
            Parent root = loader.load();
            mainViewStage = new Stage();
            mainViewStage.setTitle("Add Appointment");
            mainViewStage.setScene(new Scene(root));
            mainViewStage.showAndWait();
            populateTables();
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
    }

    /**
     * Displays overlay window/stage of the UpdateAppointment view.
     *
     * @param actionEvent represents user click Update button under Appointments Table
     */
    public void displayUpdateAppointmentWindow(ActionEvent actionEvent) {
        selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (!Objects.isNull(selectedAppointment)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateAppointment.fxml"));
                Parent root = loader.load();
                mainViewStage = new Stage();
                mainViewStage.setTitle("Update Appointment");
                mainViewStage.setScene(new Scene(root));
                mainViewStage.showAndWait();
                populateTables();
            } catch (IOException e) {
                System.out.println("Caught you: " + e.getMessage());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("You must select an Appointment to Update.");
            alert.show();
        }
    }

    /**
     * Deletes Appointment object selected from the DB and tableView
     *
     * @param actionEvent represents user clicking Delete button under Appointments table
     */
    public void deleteSelectedAppointment(ActionEvent actionEvent) {
        Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
        DBAppointment.deleteAppointmentInDb(selected.getAppointmentId());
        populateTables();
    }

    /**
     * Displays overlay window/stage of the AddCustomer view.
     *
     * @param actionEvent represents user click Add button under Customers Table
     */
    public void displayAddCustomerWindow(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddCustomer.fxml"));
            Parent root = loader.load();
            mainViewStage = new Stage();
            mainViewStage.setTitle("Update Appointment");
            mainViewStage.setScene(new Scene(root));
            mainViewStage.showAndWait();
            populateTables();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays overlay window/stage of the UpdateCustomer view.
     *
     * @param actionEvent represents user click Update button under Customers Table
     */
    public void displayUpdateCustomerWindow(ActionEvent actionEvent) {
        try {
            selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateCustomer.fxml"));
            Parent root = loader.load();
            mainViewStage = new Stage();
            mainViewStage.setTitle("Update Appointment");
            mainViewStage.setScene(new Scene(root));
            mainViewStage.showAndWait();
            populateTables();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes Customer object selected from the DB and tableView
     *
     * @param actionEvent represents user clicking Delete button under Customers table
     */
    public void deleteSelectedCustomer(ActionEvent actionEvent) {

        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        DBCustomer.deleteCustomerInDb(selected.getCustomerId());
        populateTables();
    }

    /**
     * Updates Appointments tableView to show all appointments
     *
     * @param actionEvent represents user clicking "All Appointments" Radio Button
     */
    public void displayAllAppointments(ActionEvent actionEvent) {
        // Set remaining filter options to null to prevent confusion when "All Appointments" selected
        byMonthCb.setValue(null);
        byWeekCb.setValue(null);
        allAppointmentsRBtn.setSelected(true);
        populateTables();
    }

    /**
     * Updates Appointments tableView to show appointments by week selected.
     *
     * @param weekSelected week selected from byWeek combo box
     *
     * LAMBDA: This method utilizes a lambda function to define the filter criteria for the filtered() method
     * which creates a new list of appointments that meet the filter criteria. Using the lambda here is a
     * more concise way to define a function that takes one argument (the appointment)
     * and returns a boolean value indicating whether the appointment should be included in the
     * filtered list. The use of a lambda in this context makes the code more concise and readable compared to
     * using an explicit loop and function definition.
     */
    public void displayAppointmentsByWeek(String weekSelected) {
        String weekStart = weekSelected.substring(0,10);
        String weekEnd = weekSelected.substring(11,20);
        int startMonth = Integer.parseInt(weekStart.substring(0,2));
        int startDay = Integer.parseInt(weekStart.substring(3,5));
        int startYear = Integer.parseInt(weekStart.substring(6,8));
        int endDay = Integer.parseInt(weekEnd.substring(3,5));
        ObservableList<Appointment> appointments = DBAppointment.getAllAppointmentsFromDb();

        // LAMBDA: filters list of all appointments to only include those from weekSelected
        ObservableList<Appointment> filteredAppointments = appointments.filtered(a -> {
            int apptStartMonth = Integer.parseInt(a.getStartDate().substring(0,2));
            int apptStartDay = Integer.parseInt(a.getStartDate().substring(3,5));
            int apptStartYear = Integer.parseInt(a.getStartDate().substring(6,8));
            return apptStartMonth == startMonth && (apptStartDay >= startDay && apptStartDay <= endDay) && apptStartYear == startYear;
        });
        appointmentsTable.setItems(filteredAppointments);
    }

    /**
     * Updates Appointments tableView to show appointments by month selected.
     *
     * @param monthSelected month selected from byMonth combo box
     *
     * LAMBDA: This method utilizes a lambda expression to iterate over the list of appointments and
     * group them by month. Using the lambda here is a concise way to define a function that takes
     * one argument (the appointment) and performs an operation on it. In this case, the
     * operation is to extract the month value from the appointment's start date, and add
     * the appointment to the corresponding list in the `appointmentsByMonth` ArrayList. The lambda
     * expression is a more compact and expressive way to write this code compared to using an explicit
     * loop and function definition.
     */
    public void displayAppointmentsByMonth(String monthSelected) {
        ObservableList<Appointment> appointments = DBAppointment.getAllAppointmentsFromDb();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        // Create a List of ArrayLists -- one list for each month
        List<List<Appointment>> appointmentsByMonth = Stream.generate(ArrayList<Appointment>::new)
                .limit(12)
                .collect(Collectors.toList());

        // LAMBDA: Populate each month list with appointments starting in that month
        appointments.forEach(a -> {
            int monthIndex = LocalDate.parse(a.getStartDate(),formatter).getMonthValue() - 1;
            appointmentsByMonth.get(monthIndex).add(a);
        });

        // Set the Appointments table to the values of the month selected in byMonth Combo Box
        ObservableList<Appointment> appointmentsByMonthSelected = FXCollections.observableArrayList();

        // Search list of month lists to see if there are appointments in the selected month
        for (List<Appointment> appts : appointmentsByMonth) {
            if(!appts.isEmpty()) {
                String monthName = appts.get(0).getStartDate().substring(0, 2);
                if (Month.of(Integer.parseInt(monthName)).name().equals(monthSelected)) {
                    appointmentsByMonthSelected.addAll(appts);
                    break;
                }
            }else{
                // Set table to empty list if no appointments found for selected month
                appointmentsTable.setItems(FXCollections.emptyObservableList());
            }
        }
        // If non-empty list is found for selected month, setItems of Appointments table with that list
       appointmentsTable.setItems(appointmentsByMonthSelected);
    }

    /**
     * Updates and populates all tables in MainView tab pane
     *
     */
    private void populateTables() {
        appointmentsTable.setItems(DBAppointment.getAllAppointmentsFromDb());
        appointmentsByTypeTable.setItems(DBAppointment.getAppointmentsByType());
        appointmentsByMonthTable.setItems(Appointment.getAppointmentsByMonth());
        appointmentsByDayOfWeek.setItems(Appointment.getAppointmentsByDayOfWeek());
        customerTable.setItems(DBCustomer.getAllCustomersFromDb());
        setByMonthItems();
        setByWeekItems();

        // Sort Appointments Table by Appointment ID
        appointmentsTable.getSortOrder().add(apptIdCol);

        // Reset Contact combo box when tables are refreshed
        contactCb.setValue(null);

        // Reset Contact Appointment Schedule table when tables are refreshed
        appointmentScheduleTable.setItems(null);
    }

    /**
     * Sets listener on byMonth combo box to update Appointment table (on Appointment tab) with appointments for month selected.
     *
     */
    private void setByMonthListener() {
        // Set up listener for byMonth Combo Box on Reports Tab
        byMonthCb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayAppointmentsByMonth(newValue);
                allAppointmentsRBtn.setSelected(false);
                byWeekCb.setValue(null);
            }
        });
    }

    /**
     * Sets byMonth combo box items on Appointment tab.
     *
     * Will only show months for selection in combo box that have appointments.
     *
     */
    private void setByMonthItems() {
        // Find months of all Appointments
        List<String> months = new ArrayList<>();
        for (Appointment a : DBAppointment.getAllAppointmentsFromDb()) {
            String monthNumber = a.getStartDate().substring(0, 2);
            String monthName = String.valueOf(Month.of(Integer.parseInt(monthNumber)));
            months.add(monthName);
        }

        // Create observable list of unique months
        ObservableList<String> uniqueMonths = FXCollections.observableArrayList();
        for (String month : months) {
            if (!uniqueMonths.contains(month)) {
                uniqueMonths.add(month);
            }
        }
        byMonthCb.setItems(uniqueMonths);

    }

    /**
     * Sets listener on byWeek combo box to update Appointment table (on Appointment tab) with appointments for week selected.
     *
     */
    private void setByWeekListener() {

        byWeekCb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayAppointmentsByWeek(newValue);
                allAppointmentsRBtn.setSelected(false);
                byMonthCb.setValue(null);
            }
        });
    }

    /**
     * Sets byWeek combo box items on Appointment tab.
     *
     * Will only show weeks for selection in combo box that have appointments.
     *
     */
    private void setByWeekItems(){

        int year = 2023;
        List<String> dates = new ArrayList<>();
        for (Appointment a : DBAppointment.getAllAppointmentsFromDb()) {
            dates.add(a.getStartDate());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        List<ArrayList<String>> weeks = new ArrayList<>();
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        int daysToShift = (DayOfWeek.MONDAY.getValue() - firstDayOfYear.getDayOfWeek().getValue() + 7) % 7;
        for (int i = 0; i < 52; i++) {
            weeks.add(new ArrayList<>());
        }
        for (String date : dates) {
            LocalDate localDate = LocalDate.parse(date, formatter);
            int dayOfYear = localDate.getDayOfYear() - daysToShift;
            int week = dayOfYear / 7 + 1;
            weeks.get(week - 1).add(date);
        }

        // Find weeks with appointments to add to By Week combo box
        List<ArrayList<String>> weeksWithAppointments = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
            if (!weeks.get(i).isEmpty()) {
                weeksWithAppointments.add(weeks.get(i));
            }
        }

        // Combine List of Lists with weekly appointments into a single List of appointment dates (string)
        List<String> combinedList = new ArrayList<>();
        for (ArrayList<String> weekWithAppointments : weeksWithAppointments) {
            combinedList.addAll(weekWithAppointments);
        }

        ObservableList<String> dateRanges = FXCollections.observableArrayList();
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        for (String date : combinedList) {
            LocalDate localDate = LocalDate.parse(date, formatter);
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.MONDAY) {
                // Find the Monday before the date
                LocalDate mondayBefore = localDate.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
                String mondayBeforeString = mondayBefore.format(formatter2);
                // Find the Sunday after the date
                LocalDate sundayAfter = localDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
                String sundayAfterString = sundayAfter.format(formatter2);
                // Add the date range to the list
                String dateRange = mondayBeforeString + "-" + sundayAfterString;
                if(!dateRanges.contains(dateRange)){
                    dateRanges.add(dateRange);
                }
            } else {
                // Date is already a Monday, so find the Sunday after the date and add the date range to the list
                String monday = date.replace("-", "/");
                LocalDate sundayAfter = localDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
                String sundayAfterString = sundayAfter.format(formatter2);
                String dateRange = monday + "-" + sundayAfterString;
                if(!dateRanges.contains(dateRange)){
                    dateRanges.add(dateRange);
                }
            }
        }

        byWeekCb.setItems(dateRanges);
    }

    /**
     * Sets listener on Contact combo box to update Appointment Schedule table (on Reports tab) with appointments for contact selected.
     *
     */
    public void setContactCb(ActionEvent actionEvent) {
        // Set up listener for Contact Combo Box on Reports Tab
        String selectedContact = contactCb.getSelectionModel().getSelectedItem();
        ObservableList<Appointment> contactAppointments = Contact.getContactAppointments(selectedContact);

        // Populate Appointment Schedule Table based on contact selected
        appointmentScheduleTable.setItems(contactAppointments);

    }

    /**
     * Checks customer forms with user input for blank selections or text fields.
     *
     * @param customerName customer name
     * @param address customer address
     * @param postalCode customer postal code
     * @param phone customer phone number
     * @param divisionName name of division where customer lives
     * @param divisionId id of division where customer lives
     *
     */
    static boolean validateCustomerInput(String customerName, String address, String postalCode, String phone,
                                 String divisionName, int divisionId) {
        boolean customerNameEmpty = false;
        boolean addressEmpty = false;
        boolean postalCodeEmpty = false;
        boolean phoneEmpty = false;
        boolean divisionNameEmpty = false;
        boolean divisionIdEmpty = false;

        if(customerName == null){
            customerNameEmpty = true;
        }
        if(address == null){
            addressEmpty = true;
        }
        if(postalCode == null){
            postalCodeEmpty = true;
        }
        if(phone == null){
            phoneEmpty = true;
        }
        if(divisionName == null){
            divisionNameEmpty = true;
        }
        if(divisionId == -1){
            divisionIdEmpty = true;
        }

        return (!customerNameEmpty && !addressEmpty && !postalCodeEmpty && !phoneEmpty && !divisionNameEmpty
                && !divisionIdEmpty);
    }

    /**
     * Checks appointment forms with user input for blank selections or text fields.
     *
     * @param title appointment title
     * @param description appointment description
     * @param location location of appointment
     * @param type type of appointment
     * @param customerId id of customer belonging to appointment
     * @param contactId id of contact belonging to appointment
     *
     */
    static boolean validateAppointmentInput(String title, String description,
                                            String location, String type, int customerId, int contactId) {
        boolean titleEmpty = false;
        boolean descriptionEmpty = false;
        boolean locationEmpty = false;
        boolean typeEmpty = false;
        boolean customerIdEmpty = false;
        boolean contactIdEmpty = false;

        if(title.isBlank()){
            titleEmpty = true;
        }
        if(description.isBlank()){
            descriptionEmpty = true;
        }
        if(location.isBlank()){
            locationEmpty = true;
        }
        if(type == null){
            typeEmpty = true;
        }
        if(customerId == -1){
            customerIdEmpty = true;
        }
        if(contactId == -1){
            contactIdEmpty = true;
        }

        return (!titleEmpty && !descriptionEmpty && !locationEmpty && !typeEmpty && !customerIdEmpty && !contactIdEmpty);
    }


}
