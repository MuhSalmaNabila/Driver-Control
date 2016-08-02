package com.ditrol.tugasakhir.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.ditrol.tugasakhir.PetaActivity;
import com.ditrol.tugasakhir.R;

import java.util.ArrayList;

/**
 * Created by ADIK on 10/06/2016.
 */
public class ListViewAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<String[]> data;

    private static LayoutInflater inflater = null;



    public ListViewAdapter(Activity a, ArrayList<String[]> d) {
        activity = a; data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return data.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_item, null);
        TextView jam = (TextView) vi.findViewById(R.id.jam);
        TextView lat = (TextView) vi.findViewById(R.id.latitude);
        TextView lon = (TextView) vi.findViewById(R.id.longitude);
        TextView kecepatan = (TextView) vi.findViewById(R.id.kecepatan);

        String[] daftar = data.get(position);
        String jam1 = daftar[1];
        final String lati = daftar[2];
        final String loni = daftar[3];
        String kece = daftar[4];

        jam.setText(jam1);
        lat.setText(lati);
        lon.setText(loni);
        kecepatan.setText(kece);

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(activity, PetaActivity.class);
                i.putExtra("latitude",lati);
                i.putExtra("longitude",loni);
                activity.startActivity(i);
            }
        });

        return vi;
    }
}
