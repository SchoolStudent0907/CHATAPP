/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    // Test code here...

    Login login = new Login();

    // 1. Test Username Correctly Formatted (Contains '_' and <= 5 chars)
    @Test
    public void testCheckUserNameCorrectlyFormatted() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    // 2. Test Username Incorrectly Formatted (No '_' or > 5 chars)
    @Test
    public void testCheckUserNameIncorrectlyFormatted() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    // 3. Test Password Meets Complexity Requirements
    @Test
    public void testCheckPasswordComplexitySuccess() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    // 4. Test Password Fails Complexity Requirements
    @Test
    public void testCheckPasswordComplexityFailure() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    // 5. Test Cell Phone Correctly Formatted (+27...)
    @Test
    public void testCheckCellPhoneNumberSuccess() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    // 6. Test Cell Phone Incorrectly Formatted
    @Test
    public void testCheckCellPhoneNumberFailure() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    // 7. Test Login Successful
    @Test
    public void testLoginUserSuccess() {
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!", "kyl_1", "Ch&&sec@ke99!"));
    }

    // 8. Test Login Failed
    @Test
    public void testLoginUserFailure() {
        assertFalse(login.loginUser("kyl_1", "wrongPass", "kyl_1", "Ch&&sec@ke99!"));
    }
}