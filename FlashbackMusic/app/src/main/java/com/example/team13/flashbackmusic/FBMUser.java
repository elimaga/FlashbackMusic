package com.example.team13.flashbackmusic;

import android.util.Log;

import com.google.api.services.people.v1.model.Person;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by Luzanne on 3/13/18.
 */

public class FBMUser {
    private String ID, name, proxyName;
    private Set<String> friendsID;
    private String[] proxyPrefix = {"aardvark", "baboon", "chimp", "dingo", "elephant",
        "flamingo", "giraffe", "hyena", "iguana", "jackalope", "kangaroo", "llama", "manatee",
        "narwhal", "octopus", "penguin", "quail", "raccoon", "sloth", "turtle", "upupa",
        "viper", "wombat", "xenarthra", "yak", "zebra"};

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
        Log.d("FBM", "proxy: " + proxyName);
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
                if(p.getNames().size() > 1) {
                    friendsID.add(p.getNames().get(1).getMetadata().getSource().getId());
                }
            }
        }
    }

    private String createProxyName() {
        char c = name.toLowerCase().toCharArray()[0];
        int index = ((int) c) - 97;

        return proxyPrefix[index] + ID.substring(ID.length() - 5, ID.length());
    }

}
