package com.example.curs9.ubbcluj.map.domain.MyUtils.MyGenerators;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import static com.example.curs9.ubbcluj.map.service.NetworkService.toHex;

public class SaltGenerator {
    public ArrayList<String> generateNRandomSalts (Integer n) throws NoSuchAlgorithmException {
        ArrayList<String> randomSalts = new ArrayList<>();

        while (n > 0) {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            randomSalts.add(toHex(salt));

            n--;
        }

        return randomSalts;
    }
}
