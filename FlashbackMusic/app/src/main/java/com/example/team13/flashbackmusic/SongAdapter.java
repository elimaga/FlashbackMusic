package com.example.team13.flashbackmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
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
        View rowView = mInflater.inflate(R.layout.list_item_song, parent, false);

        TextView titleTextView = rowView.findViewById(R.id.title);

        TextView artistTextView = rowView.findViewById(R.id.artist);

        TextView durationTextView = rowView.findViewById(R.id.info);

        Song song = (Song) getItem(position);

        titleTextView.setText(song.title);
        artistTextView.setText(song.artist);
        durationTextView.setText(song.duration);

        return rowView;
    }


}

