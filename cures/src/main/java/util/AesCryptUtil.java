package util;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AesCryptUtil {

    private Cipher ecipher;
    private Cipher dcipher;
    private byte[] buf;

    private static final String HEXES = "0123456789ABCDEF";

    public AesCryptUtil(int keySize) {
        buf = new byte[1024];
        try {
            SecretKey key = generateKey(keySize);
            setupCrypto(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SecretKey generateKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom();
        keyGenerator.init(keySize, secureRandom);
        return keyGenerator.generateKey();
    }
   

    public AesCryptUtil(String keyString) {
        buf = new byte[1024];
        try {
            SecretKey key = new SecretKeySpec(getMD5(keyString), "AES");
            setupCrypto(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupCrypto(SecretKey key) {
        byte[] iv = new byte[16];
        for (int i = 0; i < 16; i++) {
            iv[i] = (byte) i;
        }
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ecipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void encrypt(InputStream in, OutputStream out) {
        try {
            CipherOutputStream cos = new CipherOutputStream(out, ecipher);
            int numRead;
            while ((numRead = in.read(buf)) >= 0) {
                cos.write(buf, 0, numRead);
            }
            cos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String str) {
        try {
            byte[] utf8 = str.getBytes("UTF-8");
            byte[] enc = ecipher.doFinal(utf8);
            return byteToHex(enc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void decrypt(InputStream in, OutputStream out) {
        try {
            CipherInputStream cis = new CipherInputStream(in, dcipher);
            int numRead;
            while ((numRead = cis.read(buf)) >= 0) {
                out.write(buf, 0, numRead);
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String decrypt(String str) {
        try {
            byte[] dec = dcipher.doFinal(hexToByte(str));
            return new String(dec, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(byte[] bytes) {
        try {
            byte[] dec = dcipher.doFinal(bytes);
            return new String(dec, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] getMD5(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String byteToHex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    public static byte[] hexToByte(String str) {
        int length = str.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bytes;
    }

    public static void main(String[] args) {
        String errorMessage = null;
        String result = null;
        try {
            if (args == null || args.length < 3) {
                errorMessage = "error: missing one or more arguments. Usage: AesCryptUtil key data <enc|dec>";
            } else {
                String key = args[0];
                String data = args[1];
                String action = args[2];
                if (key == null) {
                    errorMessage = "error: key cannot be null.";
                } else if (data == null) {
                    errorMessage = "error: data cannot be null.";
                } else if (action == null) {
                    errorMessage = "error: action cannot be null.";
                } else if (!action.equals("enc") && !action.equals("dec")) {
                    errorMessage = "error: invalid action. Use 'enc' for encryption or 'dec' for decryption.";
                } else {
                    AesCryptUtil aesCryptUtil = new AesCryptUtil(key);
                    if (action.equals("enc")) {
                        result = aesCryptUtil.encrypt(data);
                    } else if (action.equals("dec")) {
                        result = aesCryptUtil.decrypt(data);
                    }
                }
            }
        } catch (Exception e) {
            errorMessage = "error: " + e.getMessage();
        }

        if (errorMessage != null) {
            System.err.println(errorMessage);
        } else {
            System.out.println(result);
        }
    }
}
