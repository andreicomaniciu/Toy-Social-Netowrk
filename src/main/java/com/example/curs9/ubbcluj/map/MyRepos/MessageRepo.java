package com.example.curs9.ubbcluj.map.MyRepos;

import com.example.curs9.ubbcluj.map.domain.MyModels.Message;
import com.example.curs9.ubbcluj.map.domain.MyValidators.MessageRepoValidator;

import java.util.ArrayList;
import java.util.List;

public class MessageRepo {
    private ArrayList<Message> messages;

    /**
     * Constructor MessageRepo
     */
    public MessageRepo() {
        this.messages=new ArrayList<>();
    }

    /**
     * verifica daca un mesaj exista in lista curenta
     * @param message
     * @return true, daca mesajul exista, false altfel
     */
    private boolean exists(Message message){
        for (var x : messages) {
            if (x.getId_mesaj().equals(message.getId_mesaj())) {
                return true;
            }
        }

        return false;
    }

    /**
     * adauga un mesaj la obiectul curent
     * @param message
     * @throws MessageRepoValidator, daca Message-ul exista deja
     */
    public void add(Message message) throws MessageRepoValidator {
        if (exists(message)) {
            throw new MessageRepoValidator("\nMesajul exista deja!\n");
        }

        messages.add(message);
    }

    /**
     * Sterge un Mesaj din obiectul curent
     * @param ID
     * @throws MessageRepoValidator, daca Mesajul nu exista
     */
    public void delete(String ID) throws MessageRepoValidator, MessageRepoValidator {
        for (var x : messages) {
            if (x.getId_mesaj().equals(ID)) {
                messages.remove(x);
                return;
                // load
            }
        }

        throw new MessageRepoValidator("\nMesajul " + ID + " nu exista!\n");
    }

    /**
     * returneaza un mesaj pe baza id-ului sau
     * @param ID
     * @return message, daca s-a gasit un ID identic, null altfel
     */
    public Message getMessage(String ID) throws MessageRepoValidator{
        for (var x : messages) {
            if (x.getId_mesaj().equals(ID)) {
                return x;
            }
        }

        return null;
    }

    /**
     * returneaza toate mesajele
     * @return
     */
    public ArrayList<Message> getAll() throws MessageRepoValidator{
        return messages;
    }

    /**
     * Returneaza o conversatie intre 2 useri
     * @return
     * @throws MessageRepoValidator
     */
    public ArrayList<Message> getConversation(List<String> ids) throws MessageRepoValidator{
        ArrayList<Message> messages1=new ArrayList<>();
        if(ids.size()==2){
            for(var x:messages){
                if((x.getId_From().equals(ids.get(0)) &&x.getId_To().equals(ids.get(1))) ||(x.getId_From().equals(ids.get(1)) && x.getId_To().equals(ids.get(0)))){
                    messages1.add(x);
                }
            }
        }
        else{
            for(var x: messages ){
                String id1=x.getId_From()+" "+x.getId_To();

                String[] s=id1.split(" ");

                List<String> list = new ArrayList<>();
                for(var y:s){
                    list.add(y);
                }
                int nrElemListaIds=ids.size();
                int nrElemCurrent=0;
                for(var z:list){
                    for(var a:ids){
                        if(z.equals(a))
                            nrElemCurrent++;
                    }
                }
                if(nrElemCurrent==nrElemListaIds)
                    messages1.add(x);
            }
        }

        return messages1;
    }

    public ArrayList<Message> getMessagesToRespond(String id1) throws MessageRepoValidator{
        ArrayList<Message> messages1=new ArrayList<>();
        for(var x: messages ){
            if(x.getId_To().equals(id1) && x.getReply().equals("fara raspuns"))
                messages1.add(x);
        }
        return messages1;
    }
}
