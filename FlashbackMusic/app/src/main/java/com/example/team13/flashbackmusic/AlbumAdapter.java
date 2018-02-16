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

        ViewHolder holder;
        Album album = (Album) getItem(position);

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.list_item_album,null);
            holder = new ViewHolder();
            holder.albumTitleTextView = convertView.findViewById(R.id.title);
            holder.albumArtistTextView = convertView.findViewById(R.id.artist);
            holder.numOfSongTextView = convertView.findViewById(R.id.info);

            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.albumTitleTextView.setText(album.getAlbumName());
        holder.albumArtistTextView.setText(album.getArtist());
        holder.numOfSongTextView.setText(album.getSongs().size() + " songs");

        return convertView;

    }

    static class ViewHolder{
        TextView albumTitleTextView;
        TextView albumArtistTextView;
        TextView numOfSongTextView;
    }
}
