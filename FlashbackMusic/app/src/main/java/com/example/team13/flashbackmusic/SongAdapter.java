package com.example.team13.flashbackmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kazutaka & Kin on 2/9/18.
 */

public class SongAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Song> mDataSource;


    public SongAdapter(Context context, ArrayList<Song> items) {
        this.mContext = context;
        this.mDataSource = items;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position){
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rowView = mInflater.inflate(R.layout.list_item, parent, false);

        TextView titleTextView = rowView.findViewById(R.id.title);

        TextView artistTextView = rowView.findViewById(R.id.artist);

        TextView durationTextView = rowView.findViewById(R.id.info);

        Song song = (Song) getItem(position);

        titleTextView.setText(song.getTitle());
        artistTextView.setText(song.getArtist());
        // TODO: needs to be replaced with song.getDuration()
        durationTextView.setText("3:22");

        return rowView;
    }


}

