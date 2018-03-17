package com.example.team13.flashbackmusic;

import android.graphics.ColorSpace;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Kazutaka on 2/6/18.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    SortKind sortKind = SortKind.ALBUM;
    SongTabFragment songTabFragment = null;

    enum SortKind {
        TITLE, ARTIST, ALBUM, FAVORITE
    }

    PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;

    }

    public void setSortKind(SortKind sortKind) {
        this.sortKind = sortKind;
        songTabFragment.setSortKind(sortKind);
        songTabFragment.refresh();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (songTabFragment == null) {
                    songTabFragment = new SongTabFragment();
                }
                songTabFragment.setSortKind(sortKind);
                return songTabFragment;

            case 1:

                return new AlbumTabFragment();

            default:

                return null;

        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

