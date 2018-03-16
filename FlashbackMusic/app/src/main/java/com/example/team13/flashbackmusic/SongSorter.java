package com.example.team13.flashbackmusic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kazutaka on 3/16/18.
 */

public class SongSorter {

    static ArrayList<Song> sortByTitle(ArrayList<Song> songs) {
        if(songs.size() == 1) {
            return songs;
        } else {
            ArrayList<Song> sorted = new ArrayList<>();
            int lastIndex = songs.size()-1;
            ArrayList<Song> leftAry = sortByTitle(new ArrayList<Song>(songs.subList(0,lastIndex/2)));
            ArrayList<Song> rightAry = sortByTitle(new ArrayList<Song>(songs.subList(lastIndex/2,lastIndex)));
            Song left = leftAry.remove(0);
            Song right = rightAry.remove(0);
            while(leftAry.size() > 0 && rightAry.size() > 0) {
                if (left.getTitle().compareToIgnoreCase(right.getTitle()) < 0) {
                    sorted.add(left);
                    left = leftAry.remove(0);
                } else {
                    sorted.add(right);
                    right = rightAry.remove(0);
                }
            }
            sorted.addAll(leftAry);
            sorted.addAll(rightAry);
            return sorted;
        }
    }

    static ArrayList<Song> sortByArtist(ArrayList<Song> songs) {
        if(songs.size() == 1) {
            return songs;
        } else {
            ArrayList<Song> sorted = new ArrayList<>();
            int lastIndex = songs.size()-1;
            ArrayList<Song> leftAry = sortByArtist(new ArrayList<Song>(songs.subList(0,lastIndex/2)));
            ArrayList<Song> rightAry = sortByArtist(new ArrayList<Song>(songs.subList(lastIndex/2,lastIndex)));
            Song left = leftAry.remove(0);
            Song right = rightAry.remove(0);
            while(leftAry.size() > 0 && rightAry.size() > 0) {
                if (left.getArtist().compareToIgnoreCase(right.getArtist()) < 0) {
                    sorted.add(left);
                    left = leftAry.remove(0);
                } else {
                    sorted.add(right);
                    right = rightAry.remove(0);
                }
            }
            sorted.addAll(leftAry);
            sorted.addAll(rightAry);
            return sorted;
        }
    }

//    static ArrayList<Song> sortByAlbum(ArrayList<Album> albums) {
//        if(albums.size() == 1) {
//            return albums;
//        } else {
//            ArrayList<Song> sorted = new ArrayList<>();
//            int lastIndex = albums.size() -1;
//            ArrayList<Album> leftAry = sortByAlbum(new ArrayList<Album>(albums.subList(0, lastIndex/2)));
//
//        }
//    }
//
//    static ArrayList<Song> extractFavorites(ArrayList<Song> songs) {
//
//    }
}
