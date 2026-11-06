package com.apepdcl.gis_survey.PMI;

import com.apepdcl.gis_survey.PMI.models.DefectsModel;

public interface OnImageActionListener {
    void onCaptureClick(int position, DefectsModel defect, String groupName);
    void onRetakeClick(int position, DefectsModel defect, String groupName);
}
