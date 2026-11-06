package com.apepdcl.gis_survey.estimates.models;

import java.util.List;

public class FeedersModel {
    public List<NearBy11kvModel> nearBy11kvModels;
    public List<Master11kvModel> master11kvModels;
    public List<Near33kvModel> near33kvModels;
    public List<Master33kvModel> master33kvModels;

    public List<NearBy11kvModel> getNearBy11kvModels() {
        return nearBy11kvModels;
    }

    public void setNearBy11kvModels(List<NearBy11kvModel> nearBy11kvModels) {
        this.nearBy11kvModels = nearBy11kvModels;
    }

    public List<Master11kvModel> getMaster11kvModels() {
        return master11kvModels;
    }

    public void setMaster11kvModels(List<Master11kvModel> master11kvModels) {
        this.master11kvModels = master11kvModels;
    }

    public List<Near33kvModel> getNear33kvModels() {
        return near33kvModels;
    }

    public void setNear33kvModels(List<Near33kvModel> near33kvModels) {
        this.near33kvModels = near33kvModels;
    }

    public List<Master33kvModel> getMaster33kvModels() {
        return master33kvModels;
    }

    public void setMaster33kvModels(List<Master33kvModel> master33kvModels) {
        this.master33kvModels = master33kvModels;
    }
}
