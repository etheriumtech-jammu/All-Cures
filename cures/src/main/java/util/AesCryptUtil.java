package util;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCryptUtil {
	
	public static final int GCM_TAG_LENGTH = 16;
	public byte[] IV = null;
	private Cipher encCipher;
	private Cipher decCipher;
	private SecretKey secretKey;
	
	public AesCryptUtil(String workingKey) throws Exception{
		secretKey = new SecretKeySpec(workingKey.getBytes(), 0, workingKey.getBytes().length, "AES");
		encCipher = Cipher.getInstance("AES/GCM/NoPadding");
		decCipher = Cipher.getInstance("AES/GCM/NoPadding");
    }
	
	public String encrypt(String pPlainText) {
	    try {
		   IV = new byte[12];
		   SecureRandom vRandom = new SecureRandom();
		   vRandom.nextBytes(IV);
		   encCipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV));
		   byte[] encData = encCipher.doFinal(pPlainText.getBytes());
           return asHex(IV)+asHex(encData);
	     } catch (Exception e) {
			e.printStackTrace();
			return null;
	     }
    }
	
    public String decrypt(String pEncryptedText) throws Exception{
		String encodedIV = pEncryptedText.substring(0,24);
		IV = hexToByte(encodedIV);
		pEncryptedText = pEncryptedText.substring(24, pEncryptedText.length());
		decCipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV));
		byte[] decodedBytes = hexToByte(pEncryptedText);
		byte[] decryptedData = decCipher.doFinal(decodedBytes);
		return new String(decryptedData);
    }

    public static byte[] hexToByte( String hexString){
		int len = hexString.length();
		byte[] ba = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
		   ba[i/2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i+1), 16));
		}
		return ba;
    }

    public static String asHex(byte buf[]){
		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		for(int i = 0; i < buf.length; i++){
			if((buf[i] & 0xff) < 16)
		      strbuf.append("0");
			strbuf.append(Long.toString(buf[i] & 0xff, 16));
		}
		return strbuf.toString().toUpperCase();
    }

    public static void main(String[] args) {
		String encKey="039AE11691FCF783D1539D35C6188AF9";
		String plainText="4111111111111111";
		try {
			AesCryptUtil AesGcmCryptUtil=new AesCryptUtil(encKey);
			  String cipherText=AesGcmCryptUtil.encrypt(plainText);
			  System.out.println("encrypt text AES GCM mode: " + cipherText.toUpperCase());
			  System.out.println("decrypt text AES GCM mode: " + AesGcmCryptUtil.decrypt(cipherText));
		}
		catch (Exception ex) {
			System.out.println("Exception: " + ex);      
		}
    }

}

