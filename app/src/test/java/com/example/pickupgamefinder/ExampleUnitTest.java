package com.example.pickupgamefinder;

import org.junit.Test;
import com.example.pickupgamefinder.PasswordHandler;

import static org.junit.Assert.*;

public class ExampleUnitTest {

    @Test
    public void valid_password() {
        assertTrue(new PasswordHandler().isPasswordValid("!Abc1234", "!Abc1234", null));
    }

    @Test
    public void missing_numbers() {
        assertFalse(new PasswordHandler().isPasswordValid("!Abcdefghij", "!Abcdefghij", null));
    }

    @Test
    public void missing_special_character() {
        assertFalse(new PasswordHandler().isPasswordValid("Abcdefghij", "Abcdefghij", null));
    }

    @Test
    public void non_matching_passwords() {
        assertFalse(new PasswordHandler().isPasswordValid("!Abc1234", "!Abc12345", null));
    }

    @Test
    public void missing_uppercase() {
        assertFalse(new PasswordHandler().isPasswordValid("!abc1234", "!abc1234", null));
    }

    @Test
    public void missing_lowercase() {
        assertFalse(new PasswordHandler().isPasswordValid("!ABC1234", "!ABC1234", null));
    }

    @Test
    public void not_enough_chars() {
        assertFalse(new PasswordHandler().isPasswordValid("!Abc123", "!Abc123", null));
    }

}