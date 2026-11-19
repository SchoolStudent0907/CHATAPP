package com.mycompany.chatapp;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        MessageStore storage = new MessageStore();
        storage.loadStoredMessagesFromJson();

        System.out.println("=====================================");
        System.out.println("       WELCOME TO QUICKCHAT          ");
        System.out.println("=====================================");

        System.out.print("Enter your sender/username (or press Enter for default): ");
        String sender = input.nextLine().trim();
        if (sender.isEmpty()) {
            sender = "Developer";
        }

        int numberOfMessages = readInt(input, "How many messages would you wish to send? ", -1);

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
                tempMessageObj = new Message(i + 1, sender, recipient, "Placeholder");

                if (!tempMessageObj.checkRecipientCell(recipient)) {
                    System.out.println("Error: Cell phone number is incorrectly formatted or missing international code. Try again.");
                }
            } while (!tempMessageObj.checkRecipientCell(recipient));

            String messageText = "";

            // 2. Message Length Validation Loop (Max 250 characters)
            do {
                System.out.print("Enter your message (max 250 characters): ");
                messageText = input.nextLine();
                tempMessageObj = new Message(i + 1, sender, recipient, messageText);

                if (!tempMessageObj.checkMessageLength()) {
                    System.out.println("Error: Please enter a message of less than 250 characters.");
                }
            } while (!tempMessageObj.checkMessageLength());

            // 3. Instantiate Final Message Object
            Message finalMessage = new Message(i + 1, sender, recipient, messageText);

            System.out.println("\n[Generated Information]");
            System.out.println("Message ID:   " + finalMessage.getMessageID());
            System.out.println("Message Hash: " + finalMessage.getMessageHash());

            // 4. Action Menu Prompt
            System.out.println("\nWhat would you like to do now?");
            System.out.println("1 - Send message");
            System.out.println("0 - Disregard message");
            System.out.println("2 - Store message");

            int option = readInt(input, "Select an option: ", -1);

            if (option == 1) {
                System.out.println(finalMessage.sentMessage(1));
                storage.recordMessage(finalMessage, "Sent");
            } else if (option == 0) {
                System.out.println(finalMessage.sentMessage(0));
                storage.recordMessage(finalMessage, "Disregard");
            } else if (option == 2) {
                System.out.println(finalMessage.sentMessage(2));
                storage.recordMessage(finalMessage, "Stored");
            } else {
                System.out.println("Invalid option selected.");
            }
        }

        // 5. Fourth Main Menu Option: Stored Messages (a - f)
        boolean runningMenu = true;
        while (runningMenu) {
            System.out.println("\n=====================================");
            System.out.println("       MAIN MENU - STORED MESSAGES   ");
            System.out.println("=====================================");
            System.out.println("4 - Stored Messages Options:");
            System.out.println("  a. Display sender and recipient of all stored messages");
            System.out.println("  b. Display the longest stored message");
            System.out.println("  c. Search for a message ID (display recipient and message)");
            System.out.println("  d. Search all messages sent or stored for a particular recipient");
            System.out.println("  e. Delete a message using the message hash");
            System.out.println("  f. Display a report of all stored messages");
            System.out.println("0 - Exit");
            System.out.print("Enter your choice (e.g., 4a, 4b, 4c, 4d, 4e, 4f, or 0): ");

            String choice = input.nextLine().trim().toLowerCase();

            switch (choice) {
                case "4a":
                case "a":
                    System.out.println("\n" + storage.displayStoredSendersAndRecipients());
                    break;
                case "4b":
                case "b":
                    System.out.println("\nLongest Stored Message: " + storage.displayLongestStoredMessage());
                    break;
                case "4c":
                case "c":
                    System.out.print("Enter Message ID to search: ");
                    String searchId = input.nextLine().trim();
                    System.out.println("\n" + storage.searchByMessageID(searchId));
                    break;
                case "4d":
                case "d":
                    System.out.print("Enter Recipient number to search: ");
                    String searchRecipient = input.nextLine().trim();
                    System.out.println("\n" + storage.searchMessagesByRecipient(searchRecipient));
                    break;
                case "4e":
                case "e":
                    System.out.print("Enter Message Hash to delete: ");
                    String deleteHash = input.nextLine().trim();
                    System.out.println("\n" + storage.deleteMessageByHash(deleteHash));
                    break;
                case "4f":
                case "f":
                    System.out.println("\n" + storage.displayStoredMessagesReport());
                    break;
                case "0":
                case "exit":
                    runningMenu = false;
                    break;
                default:
                    System.out.println("Invalid selection. Please try again.");
            }
        }

        System.out.println("\n=====================================");
        System.out.println("Total messages sent: " + Message.returnTotalMessagess());
        System.out.println("All messages processed successfully.");
        System.out.println("=====================================");
        input.close();
    }

    private static int readInt(Scanner input, String prompt, int fallbackOnError) {
        System.out.print(prompt);
        String raw = input.nextLine().trim();
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return fallbackOnError;
        }
    }
}