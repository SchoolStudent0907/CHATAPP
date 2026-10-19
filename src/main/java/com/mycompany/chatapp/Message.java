/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to edit this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;
/**
 *
 * @author Student
 */
public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;
    private static int totalMessagesSent = 0;

    // Constructor
    public Message(int messageNumber, String recipient, String message) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    // 1. Check Message ID: must not be more than 10 characters
    public boolean checkMessageID(String id) {
        return id != null && id.length() <= 10;
    }

    // 2. Check Recipient Cell Number: must contain international code and be valid
    public boolean checkRecipientCell(String cellNumber) {
        if (cellNumber == null) {
            return false;
        }
        return cellNumber.startsWith("+") && cellNumber.length() >= 10 && cellNumber.length() <= 12;
    }

    // 3. Generate Random 10-Digit Message ID
    private String generateMessageID() {
        java.util.Random random = new java.util.Random();
        String id = "";
        for (int i = 0; i < 10; i++) {
            int digit = random.nextInt(10);
            id += digit;
        }
        return id;
    }

    // 4. Check Message Length: must not exceed 250 characters
    public boolean checkMessageLength() {
        return message != null && message.length() <= 250;
    }

    // 5. Create Message Hash
    public String createMessageHash() {
        String firstTwoID = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        
        String[] words = message.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        
        return firstTwoID + ":" + messageNumber + ":" + (firstWord + lastWord).toUpperCase();
    }

    // Getters
    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }
    
    public String getRecipient() {
        return recipient;
    }
    
    public String getMessage() {
        return message;
    }
}