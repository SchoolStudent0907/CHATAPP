/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login loginApp = new Login();

        System.out.println("=== USER REGISTRATION ===");
        
        // 1. Capture User Details
        System.out.print("Enter First Name: ");
        String firstName = input.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = input.nextLine();

        System.out.print("Enter Username: ");
        String username = input.nextLine();

        System.out.print("Enter Password: ");
        String password = input.nextLine();

        System.out.print("Enter Cell Phone Number (+27...): ");
        String phone = input.nextLine();

        // 2. Validate and Register
        String registrationStatus = loginApp.registerUser(username, password, phone);
        System.out.println("\n" + registrationStatus);

        // Only proceed to login if registration was fully successful
        if (registrationStatus.equals("Username, password, and cell phone number successfully captured.")) {
            
            System.out.println("\n=== USER LOGIN ===");
            System.out.print("Enter Username: ");
            String loginUser = input.nextLine();

            System.out.print("Enter Password: ");
            String loginPass = input.nextLine();

            // 3. Verify Login Credentials
            boolean isAuthenticated = loginApp.loginUser(loginUser, loginPass, username, password);
            
            // 4. Output Final Login Message
            String loginMessage = loginApp.returnLoginStatus(isAuthenticated, firstName, lastName);
            System.out.println("\n" + loginMessage);
        } else {
            System.out.println("\nRegistration failed. Please restart and follow the formatting rules.");
        }

        input.close();
    }
}
