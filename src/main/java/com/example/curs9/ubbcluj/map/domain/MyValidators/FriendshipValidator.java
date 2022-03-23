package com.example.curs9.ubbcluj.map.domain.MyValidators;

import com.example.curs9.ubbcluj.map.domain.MyModels.Friendship;

public class FriendshipValidator extends Exception{

    /**
     * valideaza o prietenie in functie de ID
     * @param friendship
     * @throws Exception, daca exista erori
     */
    public void validateFriendship(Friendship friendship) throws Exception {

        String mesaj = new String("");

        if (friendship.getId1().equals(null)) {
            mesaj += "ID-ul #1 nu exista!\n";
        }

        if (friendship.getId2().equals(null)) {
            mesaj += "ID-ul #2 nu exista!\n";
        }

        /*if (friendship.getUser1().equals(null)) {
            mesaj += "Utilizatorul #1 nu exista!\n";
        }

        if (friendship.getUser2().equals(null)) {
            mesaj += "Utilizatorul#2 nu exista!\n";
        }*/

        if (friendship.getId1().equals(friendship.getId2())) {
            mesaj += "Fa-ti alti prieteni!\n";
        }

        if (!mesaj.equals("")) {
            throw new Exception(mesaj);
        }

    }

}
