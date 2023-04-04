package com.example.pickupgamefinder.Handlers;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import java.security.MessageDigest;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;


public class PasswordHandler {

    public PasswordHandler()
    {

    }

    public String getHashedPassword(String password)
    {
        String hashedPassword = null;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] sha256HashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            hashedPassword = bytesToHex(sha256HashBytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashedPassword;
    }

    public boolean isPasswordValid(String password, String confirmPassword, TextView errorTV)
    {
        String errorText = "";
        int minPasswordLength = 8, upChars = 0, lowChars = 0, special = 0, digits = 0;

        for(int i = 0; i < password.length(); i++)
        {
            char c = password.charAt(i);
            if(Character.isUpperCase(c))
                upChars++;
            else if(Character.isLowerCase(c))
                lowChars++;
            else if(Character.isDigit(c))
                digits++;
            else
                special++;
        }

        if(upChars!=0 && lowChars!=0 && digits!=0 && special!=0
                && password.length() >= minPasswordLength && password.equals(confirmPassword))
        {
            if(password.length() >= 12)
            {
                if(errorTV != null) {
                    errorTV.setText("Great Password Strength");
                    errorTV.setTextColor(Color.GREEN);
                }
            }
            else
            {
                if(errorTV != null)
                {
                    errorTV.setText("Good Password Strength");
                    errorTV.setTextColor(Color.YELLOW);
                }
            }
            return true;
        }

        else
        {
            if(upChars==0)
                errorText += "\nThe Password must contain at least one uppercase character";
            if(lowChars==0)
                errorText += "\nThe Password must contain at least one lowercase character.";
            if(digits==0)
                errorText += "\nThe Password must contain at least one digit.";
            if(special==0)
                errorText += "\nThe Password must contain at least one special character.";
            if(!password.equals(confirmPassword))
                errorText += "\nPasswords Do Not Match";
            if(password.length() < minPasswordLength)
                errorText += "\nPassword must be " + minPasswordLength + " characters long";

            if(errorTV != null)
            {
                errorTV.setText(errorText);
                errorTV.setTextColor(Color.RED);
            }
            return false;

        }
    }


    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * Source: https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
     *
     * Input: Byte array
     * Output: Hexadecimal string corresponding to input (with leading zeroes)
     *
     * @author acc
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

}
