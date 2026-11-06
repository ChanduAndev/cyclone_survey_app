package com.apepdcl.gis_survey.utilits;


/**
 * Created by SOUBHAGYA SWAIN on 29-06-2026.
 */
public class AddressModel {
    private String address_1;
    private String address_2;
    private String locality;
    private String sub_locality;
    private String block;
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getSub_locality() {
        return sub_locality;
    }

    public void setSub_locality(String sub_locality) {
        this.sub_locality = sub_locality;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }
}
