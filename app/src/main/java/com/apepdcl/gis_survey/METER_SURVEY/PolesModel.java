package com.apepdcl.gis_survey.METER_SURVEY;


import java.io.Serializable;

/**
 * Created by SOUBHAGYA SWAIN on 08-06-2026.
 */
public class PolesModel implements Serializable {
    String dtr_code;
    String dtr_name;
    String pole_id;
    String pole_no;
    String pole_type;
    String pole_height;
    String pole_latitude;
    String pole_longitude;
    String lt_ckt_code;
    String POLE_IMG_AVAILABLE;

    public String getPOLE_IMG_AVAILABLE() {
        return POLE_IMG_AVAILABLE;
    }

    public void setPOLE_IMG_AVAILABLE(String POLE_IMG_AVAILABLE) {
        this.POLE_IMG_AVAILABLE = POLE_IMG_AVAILABLE;
    }

    public String getDtr_name() {
        return dtr_name;
    }

    public void setDtr_name(String dtr_name) {
        this.dtr_name = dtr_name;
    }

    public String getLt_ckt_code() {
        return lt_ckt_code;
    }

    public void setLt_ckt_code(String lt_ckt_code) {
        this.lt_ckt_code = lt_ckt_code;
    }

    public String getDtr_code() {
        return dtr_code;
    }

    public void setDtr_code(String dtr_code) {
        this.dtr_code = dtr_code;
    }

    public String getPole_id() {
        return pole_id;
    }

    public void setPole_id(String pole_id) {
        this.pole_id = pole_id;
    }

    public String getPole_no() {
        return pole_no;
    }

    public void setPole_no(String pole_no) {
        this.pole_no = pole_no;
    }

    public String getPole_type() {
        return pole_type;
    }

    public void setPole_type(String pole_type) {
        this.pole_type = pole_type;
    }



    public String getPole_height() {
        return pole_height;
    }

    public void setPole_height(String pole_height) {
        this.pole_height = pole_height;
    }

    public String getPole_latitude() {
        return pole_latitude;
    }

    public void setPole_latitude(String pole_latitude) {
        this.pole_latitude = pole_latitude;
    }

    public String getPole_longitude() {
        return pole_longitude;
    }

    public void setPole_longitude(String pole_longitude) {
        this.pole_longitude = pole_longitude;
    }
}
