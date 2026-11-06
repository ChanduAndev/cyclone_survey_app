package com.apepdcl.gis_survey.utilits;


import java.util.List;

/**
 * Created by SOUBHAGYA SWAIN on 24-06-2026.
 */
public interface AddressCallback {

    void onSuccess(List<AddressModel> addresses);
    void onError(String error);
}
