package com.example.team13.flashbackmusic;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kazutaka & Kin on 2/9/18.
 */

public class SongAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Song> mDataSource;
    private boolean previewMode = false;
    private int[] colors;

    public SongAdapter(Context context, ArrayList<Song> items, String mode) {
        this.mContext = context;
        this.mDataSource = items;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.colors = new int[items.size()];

        if(mode.equals("preview")) {
            previewMode = true;
        }
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

        Song song = (Song) getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_song, null);

            holder = new ViewHolder();

            holder.titleTextView = convertView.findViewById(R.id.title);
            holder.artistTextView = convertView.findViewById(R.id.artist);
            holder.infoTextView = convertView.findViewById(R.id.info);
            holder.separatorTextView = convertView.findViewById(R.id.separator);
            holder.favoriteStatusImageButton = (FavoriteStatusImageButton) convertView.findViewById(R.id.favoriteSymbol);
            holder.listItem = convertView.findViewById(R.id.listItem);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleTextView.setText(song.getTitle());
        holder.artistTextView.setText(song.getArtist());

        if(previewMode) {
            holder.favoriteStatusImageButton.setVisibility(View.GONE);
            convertView.setBackgroundColor(colors[position]);
        } else {
            holder.favoriteStatusImageButton.setSong(song);
            holder.favoriteStatusImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FavoriteStatusImageButton button = (FavoriteStatusImageButton) view;
                    button.updateStatus(mContext);
                    button.updateImage();
                }
            });
        }


        return convertView;
    }

    static class ViewHolder
    {
        TextView titleTextView;
        TextView artistTextView;
        TextView infoTextView;
        TextView separatorTextView;
        FavoriteStatusImageButton favoriteStatusImageButton;
        RelativeLayout listItem;
    }

    public void highlightItemAt(int pos) {
        for(int i=0;i<colors.length;i++) {
            if(i==pos ) {
                colors[i]=Color.LTGRAY;
            }
            else {
                colors[i]= Color.WHITE;
            }
        }

        //refresh our list to see the change
        this.notifyDataSetChanged();
    }




}

