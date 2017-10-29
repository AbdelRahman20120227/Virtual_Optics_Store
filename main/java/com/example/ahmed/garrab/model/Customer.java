package com.example.ahmed.garrab.model;

/**
 * Created by ESC on 7/2/2016.
 */
public class Customer {
    private int ID;
    private String fName;
    private String lName;
    private String password;
    private String phone;
    private String address;
    private String gender;
    private String email;

    public Customer(){

    }

    public Customer(int ID, String fName, String lName, String password, String phone, String address, String gender, String email) {
        this.ID = ID;
        this.fName = fName;
        this.lName = lName;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.email = email;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
