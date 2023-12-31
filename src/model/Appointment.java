package model;

import DBAccess.DBAppointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Class representing an Appointment object */
public class Appointment {

    private int appointmentId;
    private int customerId;
    private int contactId;
    private int userId;
    private String title;
    private String description;
    private String location;
    private String contactName;
    private String type;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;

    public Appointment(int appointmentId, int customerId, int contactId, int userId, String title, String description,
                       String location, String contactName, String type, String startDate, String endDate,
                       String startTime, String endTime) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.contactId = contactId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contactName = contactName;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /* Getters and Setters */
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contactName;
    }

    public void setContact(String contactName) {
        this.contactName = contactName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDate(){
        return startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    /**
     Sets "Type" of Appointment that can be selected for new or updated Appointments.

     @return appointmentTypes list of String objects representing types of Appointments

     */
    public static ObservableList<String> getAppointmentTypes(){
        ObservableList<String> appointmentTypes = FXCollections.observableArrayList();
        appointmentTypes.add("De-Briefing");
        appointmentTypes.add("Planning Session");
        appointmentTypes.add("Strategy Session");
        appointmentTypes.add("Meet and Greet");
        appointmentTypes.add("Brainstorm Session");
        appointmentTypes.add("Performance Review");
        appointmentTypes.add("Other");
        return appointmentTypes;
    }

    /**
     Counts and groups appointments by month.

     @return appointmentsByMonth ObservableList of AppointmentMonth objects representing
     unique months and the number of appointments for each month

     */
    public static ObservableList<AppointmentMonth> getAppointmentsByMonth(){
        ObservableList<AppointmentMonth> appointmentsByMonth = FXCollections.observableArrayList();
        List<String> months = new ArrayList<>();
        for (Appointment a : DBAppointment.getAllAppointmentsFromDb()){
            String monthNumber = a.getStartDate().substring(0, 2);
            //String year = a.getStartDate().substring(7, 11);
            String monthName = String.valueOf(Month.of(Integer.parseInt(monthNumber)));
            months.add(monthName);

        }
        List<String> uniqueMonths = new ArrayList<>();
        for (String month : months) {
            if (!uniqueMonths.contains(month)) {
                uniqueMonths.add(month);
            }
        }
        List<Integer> frequencies = new ArrayList<>();
        for (String uniqueMonth : uniqueMonths) {
            frequencies.add(Collections.frequency(months, uniqueMonth));
        }
        for (int i = 0; i < uniqueMonths.size(); i++){
            AppointmentMonth am = new AppointmentMonth(uniqueMonths.get(i), frequencies.get(i));
            appointmentsByMonth.add(am);
        }

        return appointmentsByMonth;
    }

    /**
     Counts and groups appointments by day of week.

     @return appointmentsByDay ObservableList of AppointmentDay objects representing
     unique days and the number of appointments for each unique day

     */
    public static ObservableList<AppointmentDay> getAppointmentsByDayOfWeek(){
        ObservableList<AppointmentDay> appointmentsByDay = FXCollections.observableArrayList();
        List<String> days = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        for (Appointment a : DBAppointment.getAllAppointmentsFromDb()){
            LocalDate date = LocalDate.parse(a.getStartDate(), formatter);
            String dayName = date.getDayOfWeek().toString();
            days.add(dayName);

        }
        List<String> uniqueDays = new ArrayList<>();
        for (String day : days) {
            if (!uniqueDays.contains(day)) {
                uniqueDays.add(day);
            }
        }
        List<Integer> frequencies = new ArrayList<>();
        for (String uniqueDay : uniqueDays) {
            frequencies.add(Collections.frequency(days, uniqueDay));
        }
        for (int i = 0; i < uniqueDays.size(); i++){
            AppointmentDay ad = new AppointmentDay(uniqueDays.get(i), frequencies.get(i));
            appointmentsByDay.add(ad);
        }

        return appointmentsByDay;
    }

    /**
     Determines appointments times available based on user ZonedDateTime and EST business hours.

     @param selectedDate date chosen by user from startDate DatePicker
     @return timeList list of String objects representing times available for appointments

     */
    public static ObservableList<String> getAppointmentTimes(LocalDate selectedDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a"); // formatter for times added to timesList
        ObservableList<String> timesList = FXCollections.observableArrayList();

        // Get the current ZoneId, Date, and Time in the user's time zone and create ZonedDateTime
        ZoneId userZoneId = ZoneId.systemDefault();
        LocalDate userDate = LocalDate.now(userZoneId);
        LocalTime userTime = LocalTime.now(userZoneId);
        ZonedDateTime userZDT = ZonedDateTime.of(userDate, userTime, userZoneId).truncatedTo(ChronoUnit.SECONDS);

        // Get the current ZoneId, Date, and Time in the EST time zone and create ZonedDateTime
        ZoneId estZoneId = ZoneId.of("America/New_York");
        LocalDate estDate = LocalDate.now(estZoneId);
        LocalTime estTime = LocalTime.now(estZoneId);
        ZonedDateTime estZDT = ZonedDateTime.of(estDate, estTime, estZoneId).truncatedTo(ChronoUnit.SECONDS);

        /* Get difference between user's system time and EST */
        // Get the offsets for each time zone
        ZoneOffset userOffset = userZDT.getOffset();
        ZoneOffset estOffset = estZDT.getOffset();

        // Calculate the difference between the timezone offsets
        Duration offsetDifference = Duration.ofSeconds(userOffset.getTotalSeconds() - estOffset.getTotalSeconds());
        long minutesDifference = offsetDifference.toMinutes();

        // Determine user equivalent of 8am EST
        LocalTime localStartTime = LocalTime.of(8, 0).plusMinutes(minutesDifference);
        LocalTime localEndTime = LocalTime.of(22, 0).plusMinutes(minutesDifference);

        // If user selects current day (locally), timesList populated based on time of day (in EST equivalent)
        if(selectedDate.compareTo(userDate) == 0){

            // If before or equal to the equivalent of 8am EST, show full list for the day
            if(userTime.compareTo(localStartTime) <= 0){
                LocalTime time = localStartTime;

                for (int i = 0; i < 56; i++) { // 56 possible appointment times between 8am-10pm EST
                    if (time.compareTo(localEndTime) == 0) { // Exit loop when time = user equivalent of 10pm EST
                        String timeString = time.format(formatter);
                        timesList.add(timeString);
                        break;
                    }
                    // Format time for timesList, add to list, increment to next time to be added
                    String timeString = time.format(formatter);
                    timesList.add(timeString);
                    time = time.plusMinutes(15);
                }
                // Add last time of day
                String timeString = time.format(formatter);
                if(!timesList.contains(timeString)){
                    timesList.add(timeString);
                }
            }
            // If between equivalent of 8am-10pm EST, show partial list starting with next local quarter hour
            if(userTime.compareTo(localStartTime) > 0 && userTime.compareTo(localEndTime.minusMinutes(14)) < 0){
                int minutesToNextQuarterHour = 15 - userTime.getMinute() % 15;
                LocalTime time = userTime.plusMinutes(minutesToNextQuarterHour).truncatedTo(ChronoUnit.MINUTES);
                for (int i = 0; i < 56; i++) { // 56 possible appointment times between 8am-10pm EST
                    if (time.compareTo(localEndTime) == 0) { // Stop loop when time = user equivalent of 10pm EST
                        String timeString = time.format(formatter);
                        timesList.add(timeString);
                        break;
                    }
                    // Format time for timesList, add to list, increment to next time to be added
                    String timeString = time.format(formatter);
                    timesList.add(timeString);
                    time = time.plusMinutes(15);
                }
                // Add last time of day
                String timeString = time.format(formatter);
                if(!timesList.contains(timeString)){
                    timesList.add(timeString);
                }
            }
            // If after equivalent of (9:45pm)/10pm EST, show empty list
            if(userTime.compareTo(localEndTime.minusMinutes(14)) >= 0){
                timesList = FXCollections.emptyObservableList();
            }
        }

        // If user selects future date, show full list
        if (selectedDate.compareTo(userDate) > 0) {
            LocalTime time = localStartTime;

            for (int i = 0; i < 56; i++) { // 56 possible appointment times between 8am-10pm EST
                if (time.compareTo(localEndTime) == 0) { // Exit loop when time = user equivalent of 10pm EST
                    String timeString = time.format(formatter);
                    timesList.add(timeString);
                    break;
                }
                // Format time for timesList
                String timeString = time.format(formatter);
                timesList.add(timeString);
                time = time.plusMinutes(15); // add appointment times in 15 minute increments
            }
            // Format time for timesList, add to list, increment to next time to be added
            String timeString = time.format(formatter);
            if(!timesList.contains(timeString)){
                timesList.add(timeString);
            }
        }
        return timesList;
    }

}
