package com.apepdcl.gis_survey.estimates.sou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.models.ConductorTypeModel;
import com.apepdcl.gis_survey.estimates.models.DTRMakeMaster;
import com.apepdcl.gis_survey.estimates.models.DTR_HT_MaterialModel;
import com.apepdcl.gis_survey.estimates.models.PoleHeightModel;

import java.util.List;

public class SpinnerAdapter<T> extends ArrayAdapter<T> {

    private Context context;
    private List<T> list;

    public SpinnerAdapter(Context context, List<T> list) {
        super(context, R.layout.spinner_selected_item, list);
        this.context = context;
        this.list = list;
    }

    private String getItemText(T item) {

        if (item instanceof String) {
            return (String) item;
        }

        if (item instanceof ConductorTypeModel) {
            return ((ConductorTypeModel) item).getMATERIALPART1();
        }

        if (item instanceof PoleHeightModel) {
            return ((PoleHeightModel) item).getMATERIALPART1();
        }

        if (item instanceof DTR_HT_MaterialModel) {
            return ((DTR_HT_MaterialModel) item).getMATERAIL_DESC();
        }

        if (item instanceof DTRMakeMaster) {
            return ((DTRMakeMaster) item).getDTR_MAKE();
        }





        // Fallback
        return item.toString();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.spinner_selected_item, parent, false);
        }

        TextView tv = convertView.findViewById(android.R.id.text1);
        tv.setText(getItemText(list.get(position)));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.spinner_dropdown_item, parent, false);
        }

        TextView tv = convertView.findViewById(R.id.txtItem);
        View divider = convertView.findViewById(R.id.divider);

        tv.setText(getItemText(list.get(position)));

        divider.setVisibility(position == list.size() - 1
                ? View.GONE
                : View.VISIBLE);

        return convertView;
    }
}