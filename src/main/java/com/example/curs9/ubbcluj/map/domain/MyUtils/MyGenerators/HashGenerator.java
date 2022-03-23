package com.example.curs9.ubbcluj.map.domain.MyUtils.MyGenerators;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;

import static com.example.curs9.ubbcluj.map.service.NetworkService.toHex;

public class HashGenerator {

    SaltGenerator saltGenerator = new SaltGenerator();

    public ArrayList<String> generateRandomHashes (String password, ArrayList<String> randomSalts) throws NoSuchAlgorithmException, InvalidKeySpecException {
        ArrayList<String> randomHashes = new ArrayList<>();

        for (var x : randomSalts) {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), x.getBytes(), 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            randomHashes.add(toHex(hash));
        }

        return randomHashes;
    }

    public ArrayList<String> generateNRandomHashes (String password, Integer n) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return generateRandomHashes(password, saltGenerator.generateNRandomSalts(n));
    }

    public ArrayList<String> generateRandomHashAsArray (String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return generateRandomHashes(password, saltGenerator.generateNRandomSalts(1));
    }

    public String generateRandomHashAsString (String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return generateRandomHashes(password, saltGenerator.generateNRandomSalts(1)).get(0);
    }
}
