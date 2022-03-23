package com.example.curs9.ubbcluj.map.domain.MyModels;

import java.util.List;

public class Group {
    String id_grup;
    List<String> id;
    List<User> useri;
    String tip;
    String nume;


    public Group(List<String> id, String id_grup) {
        this.id_grup=id_grup;
        this.id = id;
        if(id.size()==2)
            this.tip="i";
        else
            this.tip="g";
    }

    public String getTip() {
        return tip;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getId_grup() {
        return id_grup;
    }

    public List<String> getId() {
        return id;
    }

    public void setUseri(List<User> useri) {
        this.useri = useri;
    }

    public List<User> getUseri() {
        return useri;
    }
}
