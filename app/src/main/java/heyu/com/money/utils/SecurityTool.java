package heyu.com.money.utils;

import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.facebook.crypto.Crypto;
import com.facebook.crypto.Entity;

import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by hzy on 10:46.
 */

public class SecurityTool {

    private static String TAG= "SecTool";
    int mLocalVersion = 0;
    private static byte[] RAMDOM_STRING = DigestUtils.getRandomString(64).getBytes();
    private static byte[] BUFFER_DATA = DigestUtils.getRandomString(16).getBytes();
    private static int INSERT_LENGTH = 128;

    private static Entity sEntity;
    private static Crypto sCrypto;

    private static byte[] getKeyString() {
        byte[] keyBuf = new byte[16];
        int bitTakeKey[] = {35, 35, 55, 41, 39, 31, 4, 6, 62, 15, 33, 4, 0, 19, 55, 48};
        for (int i = 0, source = 1; i < INSERT_LENGTH / 8; i++) {
            if (i > 12)
                source = i * 4;
            else if (i > 8)
                source = i * 3;
            else
                source = i * 2;
            keyBuf[i] = RAMDOM_STRING[source];
        }
        for (int i = 4; i < INSERT_LENGTH / 8; i += 2) {
            keyBuf[i] = RAMDOM_STRING[bitTakeKey[i]];
        }

        return keyBuf;
    }

    /***
     * AES解密
     * @param data
     * @param len
     * @return
     */
    public static String decryptionReceivedData(byte[] data, int len) {
        if ((len & 15) != 0) {
            Log.w(TAG, "decryptionReceivedData, data len=" + len);
            return null;
        }
        String str = null;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(getKeyString(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(BUFFER_DATA);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            byte[] dec = cipher.doFinal(data, 0, len);
            str = new String(dec, "UTF-8");
        } catch (Exception e) {
            Log.d(TAG, "decryptionReceivedData,exception occurred");
            e.printStackTrace();
        }
        return str;
    }

    /***
     * AES加密
     * @param data
     * @param len
     * @return
     */
    public static byte[] encryptData(byte[] data, int len){
        String str = null;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(getKeyString(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(BUFFER_DATA);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            byte[] dec = cipher.doFinal(data, 0, len);
            return dec;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * AES加密后传递的数据
     * @param putExtraData
     * @return 加密后的字节数组
     */
    public static byte[] encryptIntentPutExtraData(String putExtraData){

        return  SecurityTool.encryptData(putExtraData.getBytes(), putExtraData.length());
    }

    /***
     * AES解密后得到的数据
     * @param getExtraData
     * @return
     */
    public static String decryptionIntentGetExtraData(byte[] getExtraData){

        return SecurityTool.decryptionReceivedData(getExtraData, getExtraData.length);
    }

    public static boolean encryptionDataOrigin(boolean b){
        return b;
    }





    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        final String HEX = "0123456789ABCDEF";
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }


    public synchronized  static byte[] octopusStr2Byte(String keyString){
        //return keyString.getBytes(Charset.forName("utf-8"));
        return Base64.encode(keyString.getBytes(Charset.forName("utf-8")),0);
        //return keyString == null?null: Base64.encodeToString(keyString.getBytes(Charset.forName("utf-8")), 0);
    }

    public synchronized static String octopusByte2Str(byte[] key){
        return new String(key, Charset.forName("utf-8"));
        //return key == null?null:Base64.encodeToString(key, 0);
    }


    public static Entity getEntity() {
        return sEntity;
    }

    public static void setEntity(Entity entity) {
        sEntity = entity;
    }

    public static Crypto getCrypto() {
        return sCrypto;
    }

    public static void setCrypto(Crypto crypto) {
        sCrypto = crypto;
    }

    public static String cryptoStr(String boy) {
        try {
            //return Base64.encodeToString(getCrypto().encrypt(boy.getBytes(Charset.forName("utf-8")),getEntity()),0);
            //return EncryUtils.getInstance().encryptText(boy);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String ret = EncryUtils.getInstance().encryptText(boy);
                if (ret == null) {
                    return "";
                } else {
                    return ret;
                }
            }else{
                String ret = Base64.encodeToString(getCrypto().encrypt(boy.getBytes(Charset.forName("utf-8")),getEntity()),0);
                if (ret == null) {
                    return "";
                } else {
                    return ret;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String deCryptoStr(String girl) {
        try {
            //return  new String(getCrypto().decrypt(Base64.decode(girl,0),getEntity()),Charset.forName("utf-8"));
            //return EncryUtils.getInstance().decryptData(girl);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String ret = EncryUtils.getInstance().decryptData(girl);
                if (ret == null) {
                    return "";
                } else {
                    return ret;
                }
            }else{
                String ret = new String(getCrypto().decrypt(Base64.decode(girl,0),getEntity()), Charset.forName("utf-8"));
                if (ret == null) {
                    return "";
                } else {
                    return ret;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
