package com.apepdcl.gis_survey.utilits;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ASEUtils {
    private static final String SECRET_KEY = "1234567890123456";
    private static final String INIT_VECTOR = "abcdefghijklmnop";

    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeToString(encrypted, Base64.NO_WRAP);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
