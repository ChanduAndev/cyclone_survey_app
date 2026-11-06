package com.apepdcl.gis_survey.module_11KV.models;

public class ShowMapModel {
    private double lat;
    private double lon;
    String pole;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPole() {
        return pole;
    }

    public void setPole(String pole) {
        this.pole = pole;
    }
}
