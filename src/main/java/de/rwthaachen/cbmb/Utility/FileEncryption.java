package de.rwthaachen.cbmb.Utility;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class FileEncryption {
    final int AES_Key_Size = 256;
    Cipher pkCipher, aesCipher;
    SecretKeySpec aeskeySpec;
    byte[] aesKey;

    public FileEncryption() throws GeneralSecurityException, DecoderException {
        pkCipher = Cipher.getInstance("RSA");
        aesCipher = Cipher.getInstance("AES");
    }

    public byte[] encryptImage(InputStream is) throws IOException, GeneralSecurityException{
        makeKey();
        aesCipher.init(Cipher.ENCRYPT_MODE, aeskeySpec);
        byte[] bytes = IOUtils.toByteArray(is);
        return aesCipher.doFinal(bytes);
    }

    public CipherInputStream decryptImage(InputStream is) throws IOException, GeneralSecurityException{
        aesCipher.init(Cipher.DECRYPT_MODE, aeskeySpec);
        CipherInputStream decryptedImage = new CipherInputStream(is, aesCipher);
        return  decryptedImage;
    }

    public void makeKey() throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(AES_Key_Size);
        SecretKey key = kgen.generateKey();
        aesKey = key.getEncoded();
        aeskeySpec = new SecretKeySpec(aesKey, "AES");
    }

    public byte[] encryptKey() throws IOException, GeneralSecurityException{
        File publicKeyFile1 = new File("/home/arahman/Documents/img/public1.der");
        File publicKeyFile2 = new File("/home/arahman/Documents/img/public2.der");

        byte[] encryptedKey = rsaEncrypt(publicKeyFile1, aesKey);
        byte[] finalEncryptedKey = rsaEncrypt(publicKeyFile2, encryptedKey);

        return finalEncryptedKey;
    }

    public byte[] rsaEncrypt(File publicKey, byte[] key) throws IOException, GeneralSecurityException {
        byte[] encodedKey = new byte[(int)publicKey.length()];
        FileInputStream fis = new FileInputStream(publicKey);
        fis.read(encodedKey);

        // create public key
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pk = kf.generatePublic(publicKeySpec);

        int size = key.length;
        byte[] buffer = new byte[245];
        byte[] encryptedData;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int i = 245;
        for(i = 245; i<size; i+=245){
            System.arraycopy(key, i-245, buffer, 0, 245);
            pkCipher.init(Cipher.ENCRYPT_MODE, pk);
            encryptedData = pkCipher.doFinal(buffer);
            outputStream.write(encryptedData);
        }

        int lastBytes = size - (i-245);
        if(lastBytes>0) {
            byte[] lastBuffer = new byte[lastBytes];
            System.arraycopy(key, i-245, lastBuffer, 0, lastBytes);
            pkCipher.init(Cipher.ENCRYPT_MODE, pk);
            encryptedData = pkCipher.doFinal(lastBuffer);
            outputStream.write(encryptedData);
        }
        fis.close();
        return outputStream.toByteArray();
    }

    public void decryptKey(byte[] key, InputStream privateKeyFile1, InputStream privateKeyFile2) throws GeneralSecurityException, IOException {
        byte[] privateKey1 = IOUtils.toByteArray(privateKeyFile1);
        byte[] privateKey2 = IOUtils.toByteArray(privateKeyFile2);
        byte[] aesKey = rsaDecrypt(rsaDecrypt(key, privateKey2), privateKey1);
        aeskeySpec = new SecretKeySpec(aesKey, "AES");
    }

    public byte[] rsaDecrypt(byte[] key, byte[] privateKeyFile) throws GeneralSecurityException, IOException{
        byte[] buffer = new byte[256];
        int size  = key.length;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyFile);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey pk = kf.generatePrivate(privateKeySpec);

        for(int i = 256; i<=size; i+=256){
            System.arraycopy(key, i-256, buffer, 0, 256);
            pkCipher.init(Cipher.DECRYPT_MODE, pk);
            byte[] encryptedData = pkCipher.doFinal(buffer);
            outputStream.write(encryptedData);
        }
        return outputStream.toByteArray();
    }

}
