package com.example.team13.flashbackmusic;

import com.google.api.services.people.v1.model.Person;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by Luzanne on 3/13/18.
 */

public class FBMUser {
    private String ID, name, email, proxyName;
    private List<String> friendsID;

    public FBMUser() {
        ID = "";
        name = "";
        proxyName = "";
        email = "";
        friendsID = new ArrayList<>();
    }

    public FBMUser(String ID, String name, String email) {
        this.ID = ID;
        this.name = name;
        this.proxyName = createProxyName();
        this.email = email;
        friendsID = new ArrayList<>();
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProxyName() {
        return proxyName;
    }

    public List<String> getFriendsID() {
        return friendsID;
    }

    public void setConnections(List<Person> connections) {
        for(Person p : connections) {
            friendsID.add(p.getResourceName());
        }
    }

    // TODO
    private String createProxyName() {
        return "";
    }

}
