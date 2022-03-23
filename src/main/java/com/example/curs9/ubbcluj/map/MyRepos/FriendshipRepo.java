package com.example.curs9.ubbcluj.map.MyRepos;

import com.example.curs9.ubbcluj.map.domain.MyModels.Friendship;
import com.example.curs9.ubbcluj.map.domain.MyValidators.FriendshipRepoValidator;

import java.util.ArrayList;

public class FriendshipRepo {

    private ArrayList<Friendship> friendships;

    /**
     * Constructor FriendshipRepo
     */
    public FriendshipRepo() {
        this.friendships = new ArrayList<>();
    }

    /**
     * Verifica daca un Friendship exista in obiectul curent
     * @param friendship
     * @return true, daca friendship exista, false altfel
     */
    private boolean exists(Friendship friendship) {
        for (var x : friendships) {
            if ((x.getId1().equals(friendship.getId1()) && x.getId2().equals(friendship.getId2())) || (x.getId2().equals(friendship.getId1()) && x.getId1().equals(friendship.getId2()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * adauga un Friendship in obiectul curent
     * @param friendship
     * @throws FriendshipRepoValidator, daca Friendship-ul exista deja
     */
    public void add(Friendship friendship) throws FriendshipRepoValidator {
        if (exists(friendship)) {
            throw new FriendshipRepoValidator("\nPrietenia exista deja!\n");
        }

        friendships.add(friendship);
    }

    /**
     * sterge un Friendship din obiectul curent, pe baza ID-urilor userilor
     * @param ID1
     * @param ID2
     * @throws FriendshipRepoValidator, daca Friendship-ul nu exista
     */
    public void delete(String ID1, String ID2) throws FriendshipRepoValidator {
        ArrayList<Friendship> friendships1 = new ArrayList<>();
        for (var x : friendships) {
            if (!(x.hasUserByID(ID1) && x.hasUserByID(ID2))) {
                friendships1.add(x);
            }
        }

        if (friendships.size() == friendships1.size()) {
            throw new FriendshipRepoValidator("\nPrietenia dintre " + ID1 + " si " + ID2 + " nu exista!\n");
        }
        else
        {
            friendships = friendships1;
        }
    }

    /**
     * verifica daca un Friendship exista in obiectul curent
     * @param friendship
     * @return true, daca exista, false altfel
     */
    public boolean has(Friendship friendship) {
        return exists(friendship);
    }

    /**
     * returneaza toate prieteniile memorate
     * @return ArrayList<Friendship>
     */
    public ArrayList<Friendship> getAll() throws FriendshipRepoValidator {
        return friendships;
    }

    /**
     * obtine si returneaza toate prieteniile care contin un User
     * @param userID
     * @return ArrayList<Friendship>
     */
    public ArrayList<Friendship> getAllOfUserID(String userID) throws FriendshipRepoValidator {

        ArrayList<Friendship> friendshipsOfUser = new ArrayList<>();

        for (var x : friendships) {
            if (x.hasUserByID(userID)) {
                friendshipsOfUser.add(x);
            }
        }

        return friendshipsOfUser;
    }

    public void deleteFlagged(String ID) throws FriendshipRepoValidator, FriendshipRepoValidator {
        ArrayList<Friendship> friendships1 = new ArrayList<>();
        for (var x : friendships) {
            if (!(x.hasUserByID(ID))) {
                friendships1.add(x);
            }
        }

        friendships = friendships1;
    }
}
