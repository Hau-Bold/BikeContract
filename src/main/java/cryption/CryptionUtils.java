package cryption;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import constants.Constants;
import contractDigitalizer.BikeContract;

/** the class CryptionUtils */
public class CryptionUtils {

	/**
	 * hex to byte[] : 16dd
	 * 
	 * @param hex
	 *            hex string
	 * @return
	 */
	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}

	/**
	 * byte[] to hex : unsigned byte
	 *
	 * @param ba
	 *            byte[]
	 * @return
	 */
	public static String byteArrayToHex(byte[] ba) {

		if (ba == null || ba.length == 0) {
			return null;
		}

		StringBuffer sb = new StringBuffer(ba.length * 2);
		String hexNumber;
		for (int x = 0; x < ba.length; x++) {
			hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

			sb.append(hexNumber.substring(hexNumber.length() - 2));
		}

		return sb.toString();
	}

	/**
	 * AES
	 *
	 * @param message
	 * @return - the encrypted
	 * @throws NoSuchPaddingException
	 *             - in case of technical error
	 * @throws NoSuchAlgorithmException
	 *             - in case of technical error
	 * @throws InvalidKeyException
	 *             - in case of technical error
	 * @throws BadPaddingException
	 *             - in case of technical error
	 * @throws IllegalBlockSizeException
	 *             - in case of technical error
	 */
	public static String encrypt(String message) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		// KeyGenerator kgen = KeyGenerator.getInstance("AES");
		// kgen.init(128);
		// use key coss2

		String key = BikeContract.access.get(Constants.ENCRYPTION);

		if (!message.equals(null)) {
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(Constants.CHARSET), "AES");

			// Instantiate the cipher
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

			byte[] encrypted = cipher.doFinal(message.getBytes());
			return byteArrayToHex(encrypted);
		} else {
			return Constants.EMPTY_STRING;
		}
	}

	/**
	 * AES
	 *
	 * @param message
	 * @return the decrypted
	 * @throws NoSuchPaddingException
	 *             - in case of technical error
	 * @throws NoSuchAlgorithmException
	 *             - in case of technical error
	 * @throws BadPaddingException
	 *             - in case of technical error
	 * @throws IllegalBlockSizeException
	 *             - in case of technical error
	 * @throws InvalidKeyException
	 *             - in case of technical error
	 */
	public static String decrypt(String encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		// KeyGenerator kgen = KeyGenerator.getInstance("AES");
		// kgen.init(128);
		// use key coss2

		String key = BikeContract.access.get(Constants.ENCRYPTION);

		if (!encrypted.equals(null)) {
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(Constants.CHARSET), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] original = cipher.doFinal(hexToByteArray(encrypted));
			return new String(original);
		} else {
			return Constants.EMPTY_STRING;
		}
	}

}
