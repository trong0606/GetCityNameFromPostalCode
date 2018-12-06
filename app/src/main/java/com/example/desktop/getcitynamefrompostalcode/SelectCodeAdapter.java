package com.example.desktop.getcitynamefrompostalcode;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class SelectCodeAdapter extends BaseAdapter {
    private Province[] mProvince;

    public SelectCodeAdapter(Context context) {
        this.mProvince = mProvince;
        Resources resources = context.getResources();
        String[] tentinhs = resources.getStringArray(R.array.tentinh);
        String[] matinhs = resources.getStringArray(R.array.matinh);

        mProvince = new Province[tentinhs.length];
        for (int i = 0; i< tentinhs.length;i++){
            int tentinhRes = resources.getIdentifier(tentinhs[i],"string",null);
            String tentinh = resources.getString(tentinhRes);
            int matinhRes = resources.getIdentifier(matinhs[i],"string",null);
            String matinh = resources.getString(matinhRes);
            mProvince[i] = new Province(tentinh,matinh);

        }
    }
    @Override
    public int getCount() {
        return mProvince  == null ? 0 : mProvince.length;
    }

    @Override
    public Object getItem(int position) {
        return mProvince  == null ? null: mProvince[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
        text1.setText(mProvince[position].mName);

        return convertView;
    }
}
