package com.test.bank.tool;

import org.junit.Test;

public class PasswordUtilsTest {

    @Test
    public void testGeneratePassword() {
        String salt = PasswordUtils.getSalt(20);
        String password = PasswordUtils.generateSecurePassword("password", salt);
        System.out.println(password);
        System.out.println(salt);
    }

}