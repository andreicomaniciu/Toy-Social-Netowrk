package com.example.curs9.ubbcluj.map.domain.MyModels;

import java.util.List;

/**
 * reply e null daca mesajul nou creat nu est eun raspuns, iar daca nu e null reply <- mesajul la care este raspuns
 */
public class Message {
    String id_mesaj;
    String id_From;
    String id_To;
    String mesaj;
    String data;
    String reply;
    String numeSender;
    User user_from;
    List<User> user_to;

    public List<User> getUser_to() {
        return  user_to;
    }

    public void setUser_to(List<User> user_to) {
        this.user_to = user_to;
    }

    public User getUser_from() {
        return user_from;
    }

    public void setUser_from(User user_from) {
        this.user_from = user_from;
    }

    public String getNumeSender() {
        return numeSender;
    }

    public void setNumeSender(String numeSender) {
        this.numeSender = numeSender;
    }

    public String getData() {
        return data;
    }

    /**
     * Constructor
     * @param id_mesaj id-ul mesajului
     * @param id_From id-ul celui care a trimis mesajul
     * @param id_To lista id-urilor celor catre a fost trimis mesajul
     * @param mesaj String
     */
    public Message(String id_mesaj, String id_From, String id_To, String mesaj,String data,String id_reply) {
        this.id_mesaj = id_mesaj;
        this.id_From = id_From;
        this.id_To = id_To;
        this.mesaj = mesaj;
        this.data=data;
        this.reply=id_reply;
    }

    public String getId_mesaj() {
        return id_mesaj;
    }

    public String getId_From() {
        return id_From;
    }

    public String getId_To() {
        return id_To;
    }

    public String getMesaj() {
        return mesaj;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
