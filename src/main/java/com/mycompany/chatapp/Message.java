/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Student
 */

public class Message {

    // 1. Check Message ID: must not be more than 10 characters
    public boolean checkMessageID(String messageID) {
        return messageID != null && messageID.length() <= 10;
    }

    // 2. Check Recipient Cell Number: must contain international code and be valid
    public boolean checkRecipientCell(String cellNumber) {
        // Must start with '+' or international code prefix and not exceed 10-12 characters depending on formatting rules
        if (cellNumber == null) {
            return false;
        }
        // Ensuring it has international format (e.g., starts with +) and valid length
        return cellNumber.startsWith("+") && cellNumber.length() >= 10 && cellNumber.length() <= 12;
    }
}