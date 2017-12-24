package heyu.com.money.utils.security;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 *  使用ksyStore加密工具类
 */

public class EncryUtils {

    static  EncryUtils encryUtilsInstance;

    private KeyStore keyStore;

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    private String ALIAS = "octopus_smart";

    public static EncryUtils getInstance() {
        synchronized (EncryUtils.class) {
            if (null == encryUtilsInstance) {
                encryUtilsInstance = new EncryUtils();
            }
        }
        return encryUtilsInstance;
    }

    private EncryUtils() {
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    private SecretKey getSecretKey(final String alias)  {

        try {

            if(keyStore.containsAlias(alias)){
                KeyStore.Entry entry = keyStore.getEntry(alias, null);
                if (entry != null && entry instanceof KeyStore.SecretKeyEntry) {
                    SecretKey secretKey = ((KeyStore.SecretKeyEntry) entry).getSecretKey();
                    return secretKey;
                }
            }

            final KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .build());

            }

            return keyGenerator.generateKey();

        }catch (Exception e){

        }

        return null;
    }


    public String encryptText(final String textToEncrypt){

        SecretKey secretKey =  getSecretKey(ALIAS);

        if(secretKey == null){
            return null;
        }else{

            try {
                final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                cipher.init(Cipher.ENCRYPT_MODE,secretKey);
                byte[] encryption = cipher.doFinal(textToEncrypt.getBytes("UTF-8"));
                byte[] iv = cipher.getIV();
                return Base64.encodeToString(encryption, Base64.DEFAULT)+"--x1x1x1x1x--"+ Base64.encodeToString(iv, Base64.DEFAULT);

            }catch (Exception e){

            }
        }


        return null;
    }

    public String decryptData(final String encryptedData){


        SecretKey secretKey =  getSecretKey(ALIAS);

        if(secretKey == null){
            return null;
        }else{

            try {
                String[] encryptedStr = encryptedData.split("--x1x1x1x1x--");
                if(encryptedStr!=null && encryptedStr.length==2) {
                    byte[] encryptedDataSource = Base64.decode(encryptedStr[0], Base64.DEFAULT);
                    byte[] encryptedIV = Base64.decode(encryptedStr[1], Base64.DEFAULT);
                    final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        final GCMParameterSpec spec = new GCMParameterSpec(128, encryptedIV);
                        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
                    }else {
                        cipher.init(Cipher.DECRYPT_MODE, secretKey);
                    }

                    return new String(cipher.doFinal(encryptedDataSource), "UTF-8");
                }

            }catch (Exception e){

            }
        }


        return null;
    }
}
