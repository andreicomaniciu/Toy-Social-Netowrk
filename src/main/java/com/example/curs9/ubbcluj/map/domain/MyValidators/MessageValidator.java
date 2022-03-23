package com.example.curs9.ubbcluj.map.domain.MyValidators;

import com.example.curs9.ubbcluj.map.domain.MyModels.Message;

public class MessageValidator extends Exception{
    public void validateMessage(Message message) throws Exception{
        String mesaj = new String("");

        if(message.getId_From().equals(null)){
            mesaj+="ID-ul expeditorului nu exista!\n";
        }

        if(message.getId_To().equals(null)){
            mesaj+="ID-ul destinatarului nu exista!\n";
        }

        if(message.getId_mesaj().equals(null)){
            mesaj+="ID-ul mesajului nu exista!\n";
        }

        if(message.getMesaj().equals(null)){
            mesaj+="Mesajul este gol!\n";
        }


    }
}
