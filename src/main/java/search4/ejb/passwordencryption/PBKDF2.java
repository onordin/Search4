package search4.ejb.passwordencryption;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
public class PBKDF2 {
    public static boolean validatePassword(String inputPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(inputPassword.toCharArray(), salt, iterations, hash.length*8);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] compareHash = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
        int diff = hash.length ^ compareHash.length;
        for (int i = 0; i < hash.length && i < compareHash.length; i++) {
            diff |= hash[i] ^ compareHash[i];
        }
        return diff == 0;
    }
    
    public static String generatePasswordHash(String password, int iterations) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64*8);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();
        return iterations+":"+toHex(salt)+":"+toHex(hash);
    }
    private static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return bytes;
    }
    private static String toHex(byte[] array) {
        BigInteger bigInteger = new BigInteger(1, array);
        String hex = bigInteger.toString(16);
        int paddingLenght = (array.length * 2) - hex.length();
        if (paddingLenght > 0) {
            return String.format("%0"+paddingLenght+"d", 0)+hex; 
        }else {
            return hex;
        }
    }
    private static byte[] getSalt() throws NoSuchProviderException, NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }
}
