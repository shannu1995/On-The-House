package com.onthehouse.details;

/**
 * Created by admin on 6/09/2017.
 */

public class Country {
    private static Country instance = null;

    private int id;
    private String name;
    private String iso_code_2;
    private String iso_code_3;
    private int address_format_id;

    public Country (){

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso_code_2() {
        return iso_code_2;
    }

    public void setIso_code_2(String iso_code_2) {
        this.iso_code_2 = iso_code_2;
    }

    public String getIso_code_3() {
        return iso_code_3;
    }

    public void setIso_code_3(String iso_code_3) {
        this.iso_code_3 = iso_code_3;
    }

    public int getAddress_format_id() {
        return address_format_id;
    }

    public void setAddress_format_id(int address_format_id) {
        this.address_format_id = address_format_id;
    }

//    public static Country getInstance() {
//        if (instance == null) {
//            instance = new Country();
//        }
//        return instance;
//
//    }
}
