package com.mylab.topfm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.overwees.topfm.R;

import java.util.ArrayList;

public class TrackAdapter extends ArrayAdapter<Track> {

    private int resourceLayout;
    private Context mContext;
    // View lookup cache

    public TrackAdapter(Context context, int resource, ArrayList<Track> products) {
        super(context, resource, products);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Track track = getItem(position);

        if (track != null) {
            TextView tv_rank = (TextView) v.findViewById(R.id.textview_rank);
            TextView tv_name = (TextView) v.findViewById(R.id.textview_name);
            TextView tv_artist = (TextView) v.findViewById(R.id.textview_artist);
            TextView tv_listeners = (TextView) v.findViewById(R.id.textview_listeners);
            TextView tv_playcount = (TextView) v.findViewById(R.id.textview_playcount);
            if (tv_rank != null) {
                tv_rank.setText("Rank: " + track.getRank());
            }
            if (tv_name != null) {
                tv_name.setText("Name: " + track.getName());
            }

            if (tv_artist != null) {
                tv_artist.setText("Artist: " + track.getArtist());
            }

            if (tv_playcount != null) {
                tv_playcount.setText("Playcount: " + track.getPlaycount());
            }

            if (tv_listeners != null) {
                tv_listeners.setText("Listeners: " + track.getListeners());
            }

        }

        return v;
    }
}
