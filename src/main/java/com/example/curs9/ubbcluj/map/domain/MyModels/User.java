package com.example.curs9.ubbcluj.map.domain.MyModels;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<User> friends;

    /**
     * Constructor User cu email dat
     * @param id
     * @param prenume
     * @param nume
     * @param email
     */
    public User(String id, String prenume, String nume, String email) {
        this.id = id;
        this.lastName = nume;
        this.firstName = prenume;
        this.email = email;
        this.friends = new ArrayList<>();
    }

    /**
     * Constructor User cu email generat
     * @param id
     * @param prenume
     * @param nume
     */
    public User(String id, String prenume, String nume) {
        this.id = id;
        this.lastName = nume;
        this.firstName = prenume;
        this.email = generateEmail(prenume, nume);
        this.friends = new ArrayList<>();
    }

    /**
     * genereaza un email de tipul prenume.nume@map.com
     * @param prenume
     * @param nume
     * @return String, email-ul generat
     */
    private String generateEmail(String prenume, String nume) {
        String email = new String(prenume + "." + nume + "@map.com");
        email = email.replaceAll("\\s", "");

        return email;
    }

    /**
     * adauga un prieten in lista prietenilor a User-ului curent
     * @param user
     */
    public void addFriend(User user) {
        friends.add(user);
    }

    /**
     * sterge un prieten din lista prietenilor a User-ului curent
     * @param user
     */
    public void deleteFriend(User user) {
        friends.remove(user);
    }

    /**
     * verifica daca User-ul curent are user in lista prietenilor
     * @param user
     * @return true, daca user este prieten, false altfel
     */
    public boolean hasFriend(User user) {
        return friends.contains(user);
    }

    /**
     * stabilirea User-urilor identice
     * @param o
     * @return true, daca User o este identic cu obiectul curent, false altfel
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(lastName, user.lastName) && Objects.equals(firstName, user.firstName) && Objects.equals(email, user.email) && Objects.equals(friends, user.friends);
    }

    /**
     * stabileste si returneaza hash-ul User-ului curent
     * @return int, hash-ul obiectului curent
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, lastName, firstName, email, friends);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", friends=" + friends +
                '}';
    }

    /**
     * getter ID
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * setter ID
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getter lastName
     * @return String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * setter lastName
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * getter firstName
     * @return String
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * setter firstName
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * getter email
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getter prietenii
     * @return ArrayList<User>
     */
    public ArrayList<User> getFriends() {
        return friends;
    }

    /**
     * setter prietenii
     * @param friends
     */
    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }
}
