/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Display Welcome Header
        System.out.println("=====================================");
        System.out.println("       WELCOME TO QUICKCHAT          ");
        System.out.println("=====================================");

        System.out.print("How many messages would you wish to send? ");
        int numberOfMessages = input.nextInt();
        input.nextLine(); // Clear buffer

        // Outer loop to process the defined number of messages
        for (int i = 0; i < numberOfMessages; i++) {
            System.out.println("\n-------------------------------------");
            System.out.println("Message " + (i + 1) + " of " + numberOfMessages);
            System.out.println("-------------------------------------");

            String recipient = "";
            Message tempMessageObj;

            // 1. Recipient Cell Number Validation Loop
            do {
                System.out.print("Enter recipient cell number (with +27): ");
                recipient = input.nextLine();
                tempMessageObj = new Message(i + 1, recipient, "Placeholder");

                if (!tempMessageObj.checkRecipientCell(recipient)) {
                    System.out.println("Error: Cell phone number is incorrectly formatted or missing international code. Try again.");
                }
            } while (!tempMessageObj.checkRecipientCell(recipient));

            String messageText = "";

            // 2. Message Length Validation Loop (Max 250 characters)
            do {
                System.out.print("Enter your message (max 250 characters): ");
                messageText = input.nextLine();
                tempMessageObj = new Message(i + 1, recipient, messageText);

                if (!tempMessageObj.checkMessageLength()) {
                    System.out.println("Error: Please enter a message of less than 250 characters.");
                }
            } while (!tempMessageObj.checkMessageLength());

            // 3. Instantiate Final Message Object
            Message finalMessage = new Message(i + 1, recipient, messageText);

            System.out.println("\n[Generated Information]");
            System.out.println("Message ID:   " + finalMessage.getMessageID());
            System.out.println("Message Hash: " + finalMessage.getMessageHash());

            // 4. Action Menu Prompt
            System.out.println("\nWhat would you like to do now?");
            System.out.println("1 - Send message");
            System.out.println("0 - Disregard message");
            System.out.println("2 - Store message");
            System.out.print("Select an option: ");

            int option = input.nextInt();
            input.nextLine(); // Clear buffer

            if (option == 1) {
                System.out.println("Status: Message successfully sent!");
            } else if (option == 0) {
                System.out.println("Status: Press 0 to delete the message. Message disregarded.");
            } else if (option == 2) {
                System.out.println("Status: Message successfully stored!");
            } else {
                System.out.println("Invalid option selected.");
            }
        }

        System.out.println("\n=====================================");
        System.out.println("All messages processed successfully.");
        System.out.println("=====================================");
    }
}