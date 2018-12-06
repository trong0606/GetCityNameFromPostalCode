package com.example.desktop.getcitynamefrompostalcode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DistrictAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<District> DistricList;

    public DistrictAdapter(Context context, int layout, List<District> districList) {
        this.context = context;
        this.layout = layout;
        DistricList = districList;
    }

    @Override
    public int getCount() {
        return DistricList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        AutoCompleteTextView txtDistrict;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null );
            holder.txtDistrict = (AutoCompleteTextView)convertView.findViewById(R.id.autotextviewhuyen);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        District district = DistricList.get(position);
        holder.txtDistrict.setText(district.getNameDs());
        Toast.makeText(context, "Ma huyen la: " + district.getCodeDS(), Toast.LENGTH_SHORT).show();

        return convertView;
    }
}
