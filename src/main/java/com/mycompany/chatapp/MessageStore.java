package com.mycompany.chatapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class MessageStore {

    private static final String DEFAULT_JSON_FILE = "stored_messages.json";

    private Message[] sentMessages = new Message[0];
    private Message[] disregardedMessages = new Message[0];
    private Message[] storedMessages = new Message[0];
    private String[] messageHashes = new String[0];
    private String[] messageIDs = new String[0];

    private final String jsonFilePath;

    public MessageStore() {
        this(DEFAULT_JSON_FILE);
    }

    public MessageStore(String jsonFilePath) {
        this.jsonFilePath = jsonFilePath;
    }

    // Record a message into the appropriate array(s) based on its flag
    public void recordMessage(Message msg, String flag) {
        if (msg == null) {
            return;
        }
        msg.setFlag(flag);

        messageIDs = appendString(messageIDs, msg.getMessageID());
        messageHashes = appendString(messageHashes, msg.getMessageHash());

        if ("Sent".equalsIgnoreCase(flag)) {
            sentMessages = appendMessage(sentMessages, msg);
        } else if ("Disregard".equalsIgnoreCase(flag)) {
            disregardedMessages = appendMessage(disregardedMessages, msg);
        } else if ("Stored".equalsIgnoreCase(flag)) {
            storedMessages = appendMessage(storedMessages, msg);
            saveStoredMessagesToJson();
        }
    }

    // Save storedMessages array to a JSON file
    public void saveStoredMessagesToJson() {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        for (int i = 0; i < storedMessages.length; i++) {
            Message m = storedMessages[i];
            json.append("  {\n");
            json.append("    \"messageID\": \"").append(escapeJson(m.getMessageID())).append("\",\n");
            json.append("    \"messageNumber\": ").append(m.getMessageNumber()).append(",\n");
            json.append("    \"sender\": \"").append(escapeJson(m.getSender())).append("\",\n");
            json.append("    \"recipient\": \"").append(escapeJson(m.getRecipient())).append("\",\n");
            json.append("    \"message\": \"").append(escapeJson(m.getMessage())).append("\",\n");
            json.append("    \"messageHash\": \"").append(escapeJson(m.getMessageHash())).append("\",\n");
            json.append("    \"flag\": \"Stored\"\n");
            json.append("  }");
            if (i < storedMessages.length - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFilePath))) {
            writer.write(json.toString());
        } catch (IOException e) {
            System.out.println("Error saving stored messages to JSON: " + e.getMessage());
        }
    }

    // Read JSON file into storedMessages array
    public Message[] loadStoredMessagesFromJson() {
        File file = new File(jsonFilePath);
        if (!file.exists()) {
            return storedMessages;
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
            return storedMessages;
        }

        String raw = content.toString().trim();
        if (raw.isEmpty() || raw.equals("[]")) {
            storedMessages = new Message[0];
            return storedMessages;
        }

        Message[] loaded = new Message[0];
        String[] objects = raw.substring(1, raw.length() - 1).split("\\}\\s*,\\s*\\{");

        for (int i = 0; i < objects.length; i++) {
            String obj = objects[i].replace("{", "").replace("}", "").trim();
            if (obj.isEmpty()) {
                continue;
            }

            String id = extractJsonString(obj, "messageID");
            int num = extractJsonInt(obj, "messageNumber", i + 1);
            String sender = extractJsonString(obj, "sender");
            String recipient = extractJsonString(obj, "recipient");
            String text = extractJsonString(obj, "message");
            String customHash = extractJsonString(obj, "messageHash");

            Message m = new Message(num, sender, recipient, text, "Stored");
            if (!id.isEmpty()) {
                m.setMessageID(id);
            }
            if (!customHash.isEmpty()) {
                // Keep generated unless specifically needed; generateMessageID + createMessageHash stays consistent
            }
            loaded = appendMessage(loaded, m);
            messageIDs = appendString(messageIDs, m.getMessageID());
            messageHashes = appendString(messageHashes, m.getMessageHash());
        }

        storedMessages = loaded;
        return storedMessages;
    }

    // 2a. Display sender and recipient of all stored messages
    public String displayStoredSendersAndRecipients() {
        if (storedMessages.length == 0) {
            return "No stored messages found.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessages) {
            sb.append("Sender: ").append(m.getSender())
              .append(" | Recipient: ").append(m.getRecipient()).append("\n");
        }
        return sb.toString().trim();
    }

    // 2b. Display the longest stored message
    public String displayLongestStoredMessage() {
        if (storedMessages.length == 0) {
            return "No stored messages found.";
        }
        Message longest = storedMessages[0];
        for (Message m : storedMessages) {
            if (m.getMessage() != null && longest.getMessage() != null
                    && m.getMessage().length() > longest.getMessage().length()) {
                longest = m;
            }
        }
        return longest.getMessage();
    }

    // 2c. Search for a message ID and display corresponding recipient and message
    public String searchByMessageID(String targetID) {
        if (targetID == null) {
            return "Message ID not found.";
        }
        for (Message m : allTrackedMessages()) {
            if (targetID.equals(m.getMessageID())) {
                return "Recipient: " + m.getRecipient() + " | Message: " + m.getMessage();
            }
        }
        return "Message ID not found.";
    }

    // 2d. Search for all messages sent or stored for a particular recipient
    public String searchMessagesByRecipient(String targetRecipient) {
        if (targetRecipient == null) {
            return "No messages found for recipient.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            if (targetRecipient.equals(m.getRecipient())) {
                sb.append(m.getMessage()).append("\n");
            }
        }
        for (Message m : storedMessages) {
            if (targetRecipient.equals(m.getRecipient())) {
                sb.append(m.getMessage()).append("\n");
            }
        }
        String result = sb.toString().trim();
        return result.isEmpty() ? "No messages found for recipient." : result;
    }

    // 2e. Delete a message using the message hash
    public String deleteMessageByHash(String targetHash) {
        if (targetHash == null) {
            return "Message hash not found.";
        }

        // Check storedMessages
        for (int i = 0; i < storedMessages.length; i++) {
            if (targetHash.equals(storedMessages[i].getMessageHash())) {
                String deletedText = storedMessages[i].getMessage();
                storedMessages = removeMessageAt(storedMessages, i);
                messageHashes = removeStringValue(messageHashes, targetHash);
                saveStoredMessagesToJson();
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }

        // Check sentMessages
        for (int i = 0; i < sentMessages.length; i++) {
            if (targetHash.equals(sentMessages[i].getMessageHash())) {
                String deletedText = sentMessages[i].getMessage();
                sentMessages = removeMessageAt(sentMessages, i);
                messageHashes = removeStringValue(messageHashes, targetHash);
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }

        // Check disregardedMessages
        for (int i = 0; i < disregardedMessages.length; i++) {
            if (targetHash.equals(disregardedMessages[i].getMessageHash())) {
                String deletedText = disregardedMessages[i].getMessage();
                disregardedMessages = removeMessageAt(disregardedMessages, i);
                messageHashes = removeStringValue(messageHashes, targetHash);
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }

        return "Message hash not found.";
    }

    // 2f. Display a report listing full details of stored messages (and sent summary)
    public String displayStoredMessagesReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== STORED MESSAGES REPORT ===\n");
        if (storedMessages.length == 0) {
            sb.append("No stored messages available.\n");
        } else {
            for (Message m : storedMessages) {
                sb.append("Message ID: ").append(m.getMessageID())
                  .append(" | Message Hash: ").append(m.getMessageHash())
                  .append(" | Sender: ").append(m.getSender())
                  .append(" | Recipient: ").append(m.getRecipient())
                  .append(" | Message: ").append(m.getMessage()).append("\n");
            }
        }
        return sb.toString().trim();
    }

    // Helper: combine sent and stored (and disregarded) for ID lookup
    private Message[] allTrackedMessages() {
        Message[] combined = new Message[sentMessages.length + storedMessages.length + disregardedMessages.length];
        int idx = 0;
        for (Message m : sentMessages) combined[idx++] = m;
        for (Message m : storedMessages) combined[idx++] = m;
        for (Message m : disregardedMessages) combined[idx++] = m;
        return combined;
    }

    private Message[] appendMessage(Message[] arr, Message item) {
        Message[] copy = Arrays.copyOf(arr, arr.length + 1);
        copy[arr.length] = item;
        return copy;
    }

    private String[] appendString(String[] arr, String item) {
        String[] copy = Arrays.copyOf(arr, arr.length + 1);
        copy[arr.length] = item;
        return copy;
    }

    private Message[] removeMessageAt(Message[] arr, int index) {
        Message[] result = new Message[arr.length - 1];
        for (int i = 0, j = 0; i < arr.length; i++) {
            if (i != index) {
                result[j++] = arr[i];
            }
        }
        return result;
    }

    private String[] removeStringValue(String[] arr, String target) {
        int count = 0;
        for (String s : arr) {
            if (target.equals(s)) count++;
        }
        if (count == 0) return arr;
        String[] result = new String[arr.length - count];
        int j = 0;
        for (String s : arr) {
            if (!target.equals(s)) {
                result[j++] = s;
            }
        }
        return result;
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String unescapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private String extractJsonString(String obj, String key) {
        String search = "\"" + key + "\"";
        int keyIndex = obj.indexOf(search);
        if (keyIndex == -1) return "";
        int colonIndex = obj.indexOf(":", keyIndex + search.length());
        if (colonIndex == -1) return "";
        int firstQuote = obj.indexOf("\"", colonIndex + 1);
        if (firstQuote == -1) return "";
        int secondQuote = firstQuote + 1;
        while (secondQuote < obj.length()) {
            if (obj.charAt(secondQuote) == '"' && obj.charAt(secondQuote - 1) != '\\') {
                break;
            }
            secondQuote++;
        }
        return unescapeJson(obj.substring(firstQuote + 1, secondQuote));
    }

    private int extractJsonInt(String obj, String key, int fallback) {
        String search = "\"" + key + "\"";
        int keyIndex = obj.indexOf(search);
        if (keyIndex == -1) return fallback;
        int colonIndex = obj.indexOf(":", keyIndex + search.length());
        if (colonIndex == -1) return fallback;
        String rest = obj.substring(colonIndex + 1).trim();
        StringBuilder numStr = new StringBuilder();
        for (char c : rest.toCharArray()) {
            if (Character.isDigit(c) || c == '-') {
                numStr.append(c);
            } else if (numStr.length() > 0) {
                break;
            }
        }
        try {
            return Integer.parseInt(numStr.toString());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    // Array getters for unit tests and inspection
    public Message[] getSentMessages() {
        return Arrays.copyOf(sentMessages, sentMessages.length);
    }

    public Message[] getDisregardedMessages() {
        return Arrays.copyOf(disregardedMessages, disregardedMessages.length);
    }

    public Message[] getStoredMessages() {
        return Arrays.copyOf(storedMessages, storedMessages.length);
    }

    public String[] getMessageHashes() {
        return Arrays.copyOf(messageHashes, messageHashes.length);
    }

    public String[] getMessageIDs() {
        return Arrays.copyOf(messageIDs, messageIDs.length);
    }
}