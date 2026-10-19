/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

/**
 *
 * @author Student
 */
public class Login {

    // Helper method to check username requirements
    public boolean checkUserName(String username) {
        // Username must contain '_' and be <= 5 characters
        return username.contains("_") && username.length() <= 5;
    }

    // Helper method to check password complexity
    public boolean checkPasswordComplexity(String password) {
        // Must be >= 8 chars, contain capital letter, number, and special character
        boolean hasMinLength = password.length() >= 8;
        boolean hasCapital = !password.equals(password.toLowerCase()) && password.matches(".*[A-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

        return hasMinLength && hasCapital && hasNumber && hasSpecial;
    }

    // Helper method to check South African cell phone number format
    public boolean checkCellPhoneNumber(String phone) {
        // Must start with international code +27 and follow valid digit length
        return phone.matches("^\\+27[0-9]{9}$");
    }

    // Returns formatted registration success or error message
    public String registerUser(String username, String password, String phone) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        } else if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        } else if (!checkCellPhoneNumber(phone)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        } else {
            return "Username, password, and cell phone number successfully captured.";
        }
    }

    // Verifies entered credentials against registered credentials
    public boolean loginUser(String enteredUser, String enteredPass, String storedUser, String storedPass) {
        return enteredUser.equals(storedUser) && enteredPass.equals(storedPass);
    }

    // Returns formatted login outcome message
    public String returnLoginStatus(boolean isLoggedIn, String firstName, String lastName) {
        if (isLoggedIn) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
    
