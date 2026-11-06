package com.apepdcl.gis_survey.METER_SURVEY;


/**
 * Created by SOUBHAGYA SWAIN on 15-06-2026.
 */

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class PasswordUtil {
    public static String encryptNew(String password){
        MessageDigest md;
        byte byteData[] = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byteData = md.digest();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        //System.out.println("Digest(in hex format):: " + sb.toString());
        return sb.toString();

    }

}

