package com.example.enactushack;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowAdapter(Context context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.custom_info_window, null);

        TextView date = view.findViewById(R.id.date);
        TextView moment = view.findViewById(R.id.moment);
        ImageView img = view.findViewById(R.id.picture);

        new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd MMMM yyyy");
        date.setText(formatter.format(new Date()));

        moment.setText(marker.getTitle());

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        int imageId = context.getResources().getIdentifier(infoWindowData.getImage().toLowerCase(),
                "drawable", context.getPackageName());
        img.setImageResource(imageId);


        return view;
    }
}
