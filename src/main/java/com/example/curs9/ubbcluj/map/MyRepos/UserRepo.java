package com.example.curs9.ubbcluj.map.MyRepos;

import com.example.curs9.ubbcluj.map.domain.MyModels.User;
import com.example.curs9.ubbcluj.map.domain.MyValidators.UserRepoValidator;

import java.util.ArrayList;

public abstract class UserRepo {

    private ArrayList<User> users;

    /**
     * Constructor UserRepo
     */
    public UserRepo() {
        this.users = new ArrayList<>();
    }

    /**
     * verifica daca un User exista in obiectul curent
     * @param user
     * @return true, daca user exista, false altfel
     */
    private boolean exists(User user) {
        for (var x : users) {
            if (x.getId().equals(user.getId())) {
                return true;
            }
        }

        return false;
    }

    /**
     * adauga un User in obiectul curent
     * @param user
     * @throws UserRepoValidator, daca User-ul exista deja
     */
    public void add(User user) throws UserRepoValidator {
        if (exists(user)) {
            throw new UserRepoValidator("\nUtilizatorul exista deja!\n");
        }

        users.add(user);
    }

    /**
     * sterge un User din obiectul curent
     * @param ID
     * @throws UserRepoValidator, daca User-ul nu exista
     */
    public void delete(String ID) throws UserRepoValidator {
        for (var x : users) {
            if (x.getId().equals(ID)) {
                users.remove(x);
                return;
                // load
            }
        }

        throw new UserRepoValidator("\nUtilizatorul " + ID + " nu exista!\n");
    }

    /**
     * verifica daca un User exista in obiect
     * @param user
     * @return true, daca exista, false altfel
     */
    public boolean has(User user) {
        return users.contains(user);
    }

    /**
     * returneaza un User pe baza ID-ului sau
     * @param ID
     * @return user, daca s-a gasit un ID identic, null altfel
     */
    public User getUser(String ID) throws UserRepoValidator {
        for (var x : users) {
            if (x.getId().equals(ID)) {
                return x;
            }
        }

        return null;
    }

    public abstract String getSaltByEmail(String email) throws UserRepoValidator;

    public abstract String getHashByEmail(String emailCurrent) throws UserRepoValidator;

    /**
     * returneaza toti Userii memorati
     * @return ArrayList<User>
     */
    public ArrayList<User> getAll() throws UserRepoValidator {
        return users;
    }
}
