package com.example.pickupgamefinder;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.charset.StandardCharsets;

public class PasswordHandler {

    public PasswordHandler()
    {

    }

    public String getHashedPassword(String password)
    {
        return password;
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

        if(upChars!=0 && lowChars!=0 && digits!=0 && special!=0 && password.length() >= minPasswordLength)
        {
            if(password.length() >= 12)
            {
                errorTV.setText("Great Password Strength");
                errorTV.setTextColor(Color.GREEN);
                System.out.println("\nThe Strength of Password is Strong.");
            }
            else
            {
                errorTV.setText("Good Password Strength");
                errorTV.setTextColor(Color.YELLOW);
                System.out.println("\nThe Strength of Password is Medium.");
            }
            return true;
        }

        else
        {
            errorTV.setTextColor(Color.RED);
            if(upChars==0)
                errorText += "\nThe Password must contain at least one uppercase character";
            if(lowChars==0)
                errorText += "\nThe Password must contain at least one lowercase character.";
            if(digits==0)
                errorText += "\nThe Password must contain at least one digit.";
            if(special==0)
                errorText += "\nThe Password must contain at least one special character.";
            if(password.equals(confirmPassword))
                errorText += "\nPasswords Do Not Match";
            if(password.length() < minPasswordLength)
                errorText += "\nPassword must be " + minPasswordLength + " characters long";
            errorTV.setText(errorText);
            return false;

        }
    }

}
