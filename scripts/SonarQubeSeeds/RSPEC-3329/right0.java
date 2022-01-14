
public class MyCbcClass {

  SecureRandom random = new SecureRandom();

  public String applyCBC(String strKey, String plainText) {
    byte[] bytesIV = new byte[16];
    random.nextBytes(bytesIV);

    /* KEY + IV setting */
    IvParameterSpec iv = new IvParameterSpec(bytesIV);
    SecretKeySpec skeySpec = new SecretKeySpec(strKey.getBytes("UTF-8"), "AES");

    /* Ciphering */
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv); // Compliant
    byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
    return DatatypeConverter.printBase64Binary(bytesIV)
            + ";" + DatatypeConverter.printBase64Binary(encryptedBytes);
  }
}
