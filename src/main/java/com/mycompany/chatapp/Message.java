package com.mycompany.chatapp;

public class Message {

    private String messageID;
    private int messageNumber;
    private String sender;
    private String recipient;
    private String message;
    private String messageHash;
    private String flag; // "Sent", "Stored", "Disregard", or "None"

    // Keep track of total messages sent across application session
    private static int sentCount = 0;
    private static StringBuilder allSentMessages = new StringBuilder();

    // Original constructor (keeps backward compatibility)
    public Message(int messageNumber, String recipient, String message) {
        this(messageNumber, "CurrentUser", recipient, message, "None");
    }

    // Constructor with sender
    public Message(int messageNumber, String sender, String recipient, String message) {
        this(messageNumber, sender, recipient, message, "None");
    }

    // Full constructor including flag (useful for test data & loading)
    public Message(int messageNumber, String sender, String recipient, String message, String flag) {
        this.messageNumber = messageNumber;
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.flag = flag;
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
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }

    // 4. Check Message Length: must not exceed 250 characters
    public boolean checkMessageLength() {
        return message != null && message.length() <= 250;
    }

    // 5. Create Message Hash
    public String createMessageHash() {
        String firstTwoID = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;

        String safeMessage = message == null ? "" : message.trim();
        String[] words = safeMessage.isEmpty() ? new String[0] : safeMessage.split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        String combinedWords = (firstWord + lastWord).replaceAll("[^A-Za-z0-9]", "").toUpperCase();

        return firstTwoID + ":" + messageNumber + ":" + combinedWords;
    }

    // Method to handle sending/storing options and updating stats
    public String sentMessage(int choice) {
        if (choice == 1) {
            this.flag = "Sent";
            sentCount++;
            allSentMessages.append("ID: ").append(messageID)
                           .append(" | To: ").append(recipient)
                           .append(" | Msg: ").append(message).append("\n");
            return "Message successfully sent.";
        } else if (choice == 0) {
            this.flag = "Disregard";
            return "Press 0 to delete the message.";
        } else if (choice == 2) {
            this.flag = "Stored";
            sentCount++;
            allSentMessages.append("[STORED] ID: ").append(messageID)
                           .append(" | To: ").append(recipient).append("\n");
            return "Message successfully stored.";
        }
        return "Invalid selection.";
    }

    public static int returnTotalMessagess() {
        return sentCount;
    }

    public static String printMessages() {
        return allSentMessages.length() > 0 ? allSentMessages.toString() : "No messages sent yet.";
    }

    public static void resetSessionStats() {
        sentCount = 0;
        allSentMessages.setLength(0);
    }

    // Getters & Setters
    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
        this.messageHash = createMessageHash();
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}