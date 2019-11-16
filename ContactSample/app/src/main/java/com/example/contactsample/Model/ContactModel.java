package com.example.contactsample.Model;

import java.util.Hashtable;

public class ContactModel  {

    public static final String TABLE_NAME = "contacts";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_WEBSITE = "website";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_COMPANY = "company";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_USERNAME + " TEXT,"
                    + COLUMN_EMAIL + " TEXT,"
                    + COLUMN_PHONE + " TEXT,"
                    + COLUMN_ADDRESS + " TEXT,"
                    + COLUMN_WEBSITE + " TEXT,"
                    + COLUMN_COMPANY + " TEXT"
                    + ")";

    private int id;

    private String name;

    private String username;
    private String email;
    private Address address;
    private String phone;
    private String website;
    private Company company;
    private int img;

    private String addressString, companyString;

    public ContactModel() {
    }

    public ContactModel(int id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public ContactModel(int id, String name, String username,
                        String email, Hashtable addressData, String phone, String website, Hashtable companyData) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;

        this.address = getAddressData(addressData);
        this.phone = phone;
        this.company = getCompany(companyData);
        this.website = website;
    }

    public ContactModel(int id, String name, String username,
                        String email, String addressData, String phone, String website, String companyData) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;

        this.addressString = addressData;
        this.phone = phone;
        this.companyString = companyData;
        this.website = website;
    }

    Address getAddressData(Hashtable ht) {
        try {
            if (ht != null) {
                String street = ht.get("street").toString();
                String suite = ht.get("suite").toString();
                String city = ht.get("city").toString();
                String zipcode = ht.get("zipcode").toString();
                Hashtable geoData = (Hashtable) ht.get("geo");
                String lat = (String) geoData.get("lat");
                String lng = (String) geoData.get("lng");
                Geo geo = new Geo(lat, lng);
                return new Address(street, suite, city, zipcode, geo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    Company getCompany(Hashtable ht) {
        try {
            if (ht != null) {
                String name = ht.get("name").toString();
                String catchPhrase = ht.get("catchPhrase").toString();
                String bs = ht.get("bs").toString();

                return new Company(name, catchPhrase, bs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public String getAddressString() {
        return this.addressString;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public Company getCompany() {
        return company;
    }

    public int getImg() {
        return img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setAddressString(String address) {
        this.addressString = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
