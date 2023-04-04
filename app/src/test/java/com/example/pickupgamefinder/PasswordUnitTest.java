package com.example.pickupgamefinder;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.pickupgamefinder.Handlers.PasswordHandler;

public class PasswordUnitTest {

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
        assertFalse(new PasswordHandler().isPasswordValid("Abc12345", "Abc12345", null));
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