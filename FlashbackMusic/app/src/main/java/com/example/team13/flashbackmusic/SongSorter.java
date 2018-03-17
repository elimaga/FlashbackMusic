package com.example.team13.flashbackmusic;

import android.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kazutaka on 3/16/18.
 */

public class SongSorter {

    static ArrayList<Song> sortByTitle(ArrayList<Song> songs) {
        if(songs.size() <= 1) {
            return songs;
        } else {
            ArrayList<Song> sorted = new ArrayList<>();
            int size = songs.size();
            ArrayList<Song> leftAry = sortByTitle(new ArrayList<Song>(songs.subList(0,size/2)));
            ArrayList<Song> rightAry = sortByTitle(new ArrayList<Song>(songs.subList(size/2,size)));
            while(leftAry.size() > 0 && rightAry.size() > 0) {
                if (leftAry.get(0).getTitle().compareToIgnoreCase(rightAry.get(0).getTitle()) < 0) {
                    sorted.add(leftAry.remove(0));
                } else {
                    sorted.add(rightAry.remove(0));
                }
            }
            sorted.addAll(leftAry);
            sorted.addAll(rightAry);
            return sorted;
        }
    }

    static ArrayList<Song> sortByArtist(ArrayList<Song> songs) {
        if(songs.size() <= 1) {
            return songs;
        } else {
            ArrayList<Song> sorted = new ArrayList<>();
            int size = songs.size();
            ArrayList<Song> leftAry = sortByArtist(new ArrayList<Song>(songs.subList(0,size/2)));
            ArrayList<Song> rightAry = sortByArtist(new ArrayList<Song>(songs.subList(size/2,size)));
            while(leftAry.size() > 0 && rightAry.size() > 0) {
                if (leftAry.get(0).getArtist().compareToIgnoreCase(rightAry.get(0).getArtist()) < 0) {
                    sorted.add(leftAry.remove(0));
                } else {
                    sorted.add(rightAry.remove(0));
                }
            }
            sorted.addAll(leftAry);
            sorted.addAll(rightAry);
            return sorted;
        }
    }

    static ArrayList<Song> sortByAlbum(ArrayList<Album> albums) {
        ArrayList<Album> sortedAlbums = sortAlbums(albums);
        ArrayList<Song> songs = new ArrayList<>();
        for (Album album : sortedAlbums) {
            songs.addAll(album.getSongs());
        }
        return songs;
    }

    private static ArrayList<Album> sortAlbums(ArrayList<Album> albums) {
        if(albums.size() <= 1) {
            return albums;
        } else {
            ArrayList<Album> sorted = new ArrayList<>();
            int size = albums.size();
            ArrayList<Album> leftAry = sortAlbums(new ArrayList<Album>(albums.subList(0, size/2)));
            ArrayList<Album> rightAry = sortAlbums(new ArrayList<Album>(albums.subList(size/2, size)));
            while(leftAry.size() > 0 && rightAry.size() > 0) {
                if (leftAry.get(0).getAlbumName().compareToIgnoreCase(rightAry.get(0).getAlbumName()) < 0) {
                    sorted.add(leftAry.remove(0));
                } else {
                    sorted.add(rightAry.remove(0));
                }
            }
            sorted.addAll(leftAry);
            sorted.addAll(rightAry);
            return sorted;
        }
    }

    static ArrayList<Song> extractFavorites(ArrayList<Song> songs) {
        ArrayList<Song> favorited = new ArrayList<>();
        ArrayList<Song> rest = new ArrayList<>();
        for (Song song : songs) {
            if (song.getFavoriteStatus() == Song.FavoriteStatus.LIKED) {
                favorited.add(song);
            } else {
                rest.add(song);
            }
        }
        favorited.addAll(rest);
        return favorited;
    }
}
