package com.example.curs9.ubbcluj.map.domain.MyValidators;

import com.example.curs9.ubbcluj.map.domain.MyModels.User;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class UserValidator extends Exception{

    /**
     * verifica daca o seceventa corespunde unui anumit tipar
     * @param reg
     * @param secv
     * @return true, daca secventa corespunde, false altfel
     */
    private boolean regex(ArrayList<String> reg, String secv) {

        for (var x : reg) {
            if (Pattern.matches(x, secv) == true) {
                return true;
            }
        }

        return false;
    }

    /**
     * creeaza un tipar pentru nume valide si verifica daca o secventa ii corespunde
     * @param name
     * @return true, daca secventa corespunde, false altfel
     */
    private boolean isValidName(String name) {
        ArrayList<String> reg = new ArrayList<>();

        /**
         * Andrei
         */
        reg.add(new String("^[A-Z]{1,1}[a-z]*[ ]?$"));

        /**
         * Andrei Mihai
         */
        reg.add(new String("^[A-Z]{1,1}[a-z]*[ ]{1,1}[A-Z]{1,1}[a-z]*$"));

        /**
         * Andrei-Mihai
         */
        reg.add(new String("^[A-Z]{1,1}[a-z]*[-]{1,1}[A-Z]{1,1}[a-z]*$"));

        return regex(reg, name);
    }

    /**
     * creeaza un tipar pentru numere valide si verifica daca o secventa ii corespunde
     * @param numar
     * @return true, daca secventa corespunde, false altfel
     */
    private boolean isValidNumar(String numar) {
        ArrayList<String> reg = new ArrayList<>();

        /**
         * Orice numar natural >0
         */
        reg.add(new String("^[1-9]+[0-9]*$"));

        return regex(reg, numar);
    }


    /**
     * valideaza un user in functie de parametrii sai
     * @param user
     * @throws Exception, daca exista erori
     */
    public void validateUser(User user) throws Exception {

        String mesaj = new String("");

        if (user.getId().equals("")) {
            mesaj += "ID-ul nu poate fi nul!\n";
        }

        if (isValidNumar(user.getId()) == false) {
            mesaj += "ID-ul nu respecta formatul valid!\n";
        }

        if (user.getFirstName().equals("")) {
            mesaj += "Prenumele nu poate fi nul!\n";
        }

        if (user.getLastName().equals("")) {
            mesaj += "Numele nu poate fi nul!\n";
        }

        if (user.getEmail().equals("")) {
            mesaj += "Email-ul nu poate fi nul!\n";
        }

        if (isValidName(user.getFirstName()) == false) {
            mesaj += "Prenumele nu respecta formatul valid!\n";
        }

        if (isValidName(user.getLastName()) == false) {
            mesaj += "Numele nu respecta formatul valid!\n";
        }

        if (!mesaj.equals("")) {
            throw new Exception(mesaj);
        }

    }

}
