package com.mycompany.chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testCheckMessageLengthSuccess() {
        // Test message within 250 characters
        Message msg = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue(msg.checkMessageLength(), "Message should be valid (under 250 chars)");
    }

    @Test
    public void testCheckMessageLengthFailure() {
        // Create a string longer than 250 characters
        String longText = "A".repeat(251);
        Message msg = new Message(1, "+27718693002", longText);
        assertFalse(msg.checkMessageLength(), "Message should exceed limit and fail");
    }

    @Test
    public void testCheckRecipientCellSuccess() {
        Message msg = new Message(1, "+27718693002", "Test");
        assertTrue(msg.checkRecipientCell("+27718693002"), "Recipient cell with international code should pass");
    }

    @Test
    public void testCheckRecipientCellFailure() {
        Message msg = new Message(1, "+27718693002", "Test");
        assertFalse(msg.checkRecipientCell("08575975889"), "Recipient cell without correct formatting should fail");
    }

    @Test
    public void testCreateMessageHash() {
        // Testing with exact test structure
        Message msg = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String hash = msg.createMessageHash();
        assertNotNull(hash, "Hash should be generated");
        assertTrue(hash.contains("HITONIGHT"), "Hash should contain uppercase first/last words");
    }
}