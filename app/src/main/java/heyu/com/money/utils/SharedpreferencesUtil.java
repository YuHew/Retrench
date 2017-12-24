package heyu.com.money.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by heyu on 2016/8/17.
 */
public class SharedpreferencesUtil {
    private final static String SP_NAME = "octopus";
    private static SharedPreferences sp;

    public static void saveBoolean(Context context, String key, boolean value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        sp.edit().putBoolean(key, value).commit();
    }

    public static void saveString(Context context, String key, String value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        if(value!=null && !value.equals("")) {
            String object_en = SecurityTool.cryptoStr(value);
            sp.edit().putString(key, object_en).commit();
        }else{
        sp.edit().putString(key, value).commit();
        }

    }

    public static void clear(Context context) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        sp.edit().clear().commit();
    }

    public static void clearByKey(Context context, String key) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        sp.edit().remove(key).commit();
    }

    public static void saveLong(Context context, String key, long value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        sp.edit().putLong(key, value).commit();
    }

    public static void saveInt(Context context, String key, int value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        sp.edit().putInt(key, value).commit();
    }

    public static void saveFloat(Context context, String key, float value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        sp.edit().putFloat(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        String s = sp.getString(key, defValue);
        if(s!=null && !s.equals("")) {
            return SecurityTool.deCryptoStr(s);
        }else{
            return s;
        }
    }

    public static int getInt(Context context, String key, int defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        return sp.getInt(key, defValue);
    }

    public static long getLong(Context context, String key, long defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        return sp.getLong(key, defValue);
    }

    public static float getFloat(Context context, String key, float defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        return sp.getFloat(key, defValue);
    }

    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        return sp.getBoolean(key, defValue);
    }

    public static void saveObject(Context context, String key, Object obj) {
        try {
            if (sp == null){
                if(context != null){
                    sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
                }else {
                    if(context != null){
                        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
                    }
                }
            }

            if(sp != null){
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(bos);
                os.writeObject(obj);
                String bytesToHexString = bytesToHexString(bos.toByteArray());
                if(bytesToHexString!=null && !bytesToHexString.equals("")) {
                    String object_en = SecurityTool.cryptoStr(bytesToHexString);
                    sp.edit().putString(key, object_en).commit();
                }else{
                sp.edit().putString(key, bytesToHexString).commit();
                }
                bos.close();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static Object readObject(Context context, String key) {
        try {
            if (sp == null){
                if(context != null){
                    sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
                }else {
                    if(context != null){
                        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
                    }
                }
            }

            if(sp != null){
                if (sp.contains(key)) {
                    String string = sp.getString(key, "");
                    if(!TextUtils.isEmpty(string)) {
                        byte[] stringToBytes = StringToBytes(SecurityTool.deCryptoStr(string));
                        ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                        ObjectInputStream is = new ObjectInputStream(bis);
                        Object readObject = is.readObject();
                        bis.close();
                        is.close();
                        return readObject;
                    }else{
                        return null;
                    }
                }
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch;
            char hex_char1 = hexString.charAt(i);
            int int_ch1;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch1 = (hex_char1 - 48) * 16;
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch1 = (hex_char1 - 55) * 16;
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i);
            int int_ch2;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch2 = (hex_char2 - 48);
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch2 = hex_char2 - 55;
            else
                return null;
            int_ch = int_ch1 + int_ch2;
            retData[i / 2] = (byte) int_ch;
        }
        return retData;
    }

}
