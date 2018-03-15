package com.example.team13.flashbackmusic;

import android.util.Log;

import com.google.api.services.people.v1.model.Person;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by Luzanne on 3/13/18.
 */

public class FBMUser {
    private String ID, name, proxyName;
    private Set<String> friendsID;

    public FBMUser() {
        ID = "";
        name = "";
        proxyName = "";
        friendsID = new HashSet<>();
    }

    public FBMUser(String ID, String name) {
        this.ID = ID;
        this.name = name;
        this.proxyName = createProxyName();
        friendsID = new HashSet<>();
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getProxyName() {
        return proxyName;
    }

    public void setFriendsID(Set<String> friendsID) {
        this.friendsID = friendsID;
    }

    public Set<String> getFriendsID() {
        return friendsID;
    }

    public boolean checkIfFriend(String friendID) {
        return friendsID.contains(friendID);
    }

    public void setConnections(List<Person> connections) {
        if(connections != null) {
            for (Person p : connections) {
                friendsID.add(p.getResourceName());
                Log.d("FBMUser", "name: " + p.getNames().get(0).getDisplayName() +
                        "; friend id: " + p.getResourceName());
            }
        }
    }

    // TODO
    private String createProxyName() {
        return "";
    }

}
