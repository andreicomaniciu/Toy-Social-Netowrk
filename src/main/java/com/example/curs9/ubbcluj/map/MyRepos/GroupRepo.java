package com.example.curs9.ubbcluj.map.MyRepos;

import com.example.curs9.ubbcluj.map.domain.MyModels.Group;

import java.util.ArrayList;

public abstract class GroupRepo {
    private ArrayList<Group> groups;

    public GroupRepo() {
        this.groups = new ArrayList<>();
    }

    private boolean exists(Group user) {
        for (var x : groups) {
            if (x.getId().equals(user.getId())) {
                return true;
            }
        }

        return false;
    }

    public void add(Group group){
        groups.add(group);
    }

    public Group getGroup(String ID){
        for (var x : groups) {
            if (x.getId().equals(ID)) {
                return x;
            }
        }

        return null;
    }

    public ArrayList<Group> getAll(){
        return groups;
    }

    public ArrayList<Group> getAllFromCurrent(String id){
        ArrayList<Group> grupuri=new ArrayList<>();
        for(var x:groups){
            if(x.getId().contains(id))
                grupuri.add(x);
        }
        return grupuri;
    }


}
