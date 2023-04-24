package model;

import DBAccess.DBCountry;
import DBAccess.DBCustomer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Country {
    private int countryId;
    private String countryName;

    public Country(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public static ObservableList<String> getAllCountryNames(){
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        for (Country c : DBCountry.getAllCountries()){
            countryNames.add(c.getCountryName());
        }
        return countryNames;
    }
}
