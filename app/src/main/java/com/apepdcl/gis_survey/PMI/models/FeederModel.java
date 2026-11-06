package com.apepdcl.gis_survey.PMI.models;
import com.google.gson.annotations.SerializedName;

public class FeederModel {

    @SerializedName("SEC_ID")
    private String secId;

    @SerializedName("SEC_NAME")
    private String secName;

    @SerializedName("SUB_STATION_NAME")
    private String subStationName;

    @SerializedName("SUB_STATION_ID")
    private String subStationId;

    @SerializedName("FEEDER_NAME")
    private String feederName;

    @SerializedName("FEEDER_ID")
    private String feederId;

    @SerializedName("POLES")
    private String poles;

    @SerializedName("PENDING_POLES_COUNT")
    private String pendingPolesCount;

    @SerializedName("PENDING_POLES")
    private String pendingPoles;

    @SerializedName("COMPLETED_POLES_COUNT")
    private String completedPolesCount;

    @SerializedName("COMPLETED_POLES")
    private String completedPoles;

    public String getSecId() { return secId; }

    public String getSecName() { return secName; }

    public String getSubStationName() { return subStationName; }

    public String getSubStationId() { return subStationId; }

    public String getFeederName() { return feederName; }

    public String getFeederId() { return feederId; }

    public String getPoles() { return poles; }

    public String getPendingPolesCount() { return pendingPolesCount; }

    public String getPendingPoles() { return pendingPoles; }

    public String getCompletedPolesCount() { return completedPolesCount; }

    public String getCompletedPoles() { return completedPoles; }
}