package model;

import DBAccess.DBContact;
import DBAccess.DBCustomer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Contact {
    private int contactId;
    private String contactName;
    private String contactEmail;

    public Contact(int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    /**
     Creates list of names representing all Contact objects.

     @return contactNames list of String objects representing names of all Contacts

     */
    public static ObservableList<String> getAllContactNames(){
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        for (Contact c : DBContact.getAllContacts()){
            contactNames.add(c.getContactName());
        }
        return contactNames;
    }


    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public static int getContactIdByName(String name){
        for (Contact c : DBContact.getAllContacts()){
            if(c.contactName.equals(name)){
                return c.getContactId();
            }

        }
        return -1;
    }

    public static String getContactNameById(int id){
        for (Contact c : DBContact.getAllContacts()){
            if(c.getContactId() == id){
                return c.getContactName();
            }
        }
        return "";
    }
}
