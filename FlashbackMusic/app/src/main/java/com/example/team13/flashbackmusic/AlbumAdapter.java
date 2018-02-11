package com.example.team13.flashbackmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KM on 2/10/2018.
 */

public class AlbumAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Album> mDataSource;

    public AlbumAdapter(Context context, ArrayList<Album> items){
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
        View rowView = mInflater.inflate(R.layout.list_item_album, parent, false);

        TextView albumTitleTextView = rowView.findViewById(R.id.album_title);

        TextView albumArtistTextView = rowView.findViewById(R.id.album_artist);

        TextView numOfSongTextView = rowView.findViewById(R.id.album_numOfSong);

        Album album = (Album) getItem(position);

        albumTitleTextView.setText(album.albumTitle);
        albumArtistTextView.setText(album.artistName);
        numOfSongTextView.setText(""+ album.numOfSong);

        return rowView;
    }
}
