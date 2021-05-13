package util ;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Java String Encryption Decryption
 * 
 *  
 */
public class EnDeCryptor {
    private static SecretKeySpec secretKey;
    private static byte[] key;
    

    public static void prepareSecreteKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance(Constant.SHA1);
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, Constant.CipherAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String strToEncrypt, String secret) {
        try {
        	if(secretKey == null)
        		prepareSecreteKey(secret);
            Cipher cipher = Cipher.getInstance(Constant.CipherAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }catch (Exception e) {
            Constant.log("Error while encrypting: " + e.toString(), 3);
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String strToDecrypt, String secret) {
        try {
            prepareSecreteKey(secret);
            Cipher cipher = Cipher.getInstance(Constant.CipherAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
        	Constant.log("Error while decrypting: " + e.toString(), 3);
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        final String secretKey = Constant.SECRETE;

        String originalString = Constant.PASSWORD;

        EnDeCryptor aesEncryptionDecryption = new EnDeCryptor();
        String encryptedString = aesEncryptionDecryption.encrypt(originalString, secretKey);
        String decryptedString = aesEncryptionDecryption.decrypt(encryptedString, secretKey);
    }
}