package com.example.myapplication.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for ChatMessage model.
 * This class contains unit tests for verifying the functionality of the ChatMessage class,
 * including message content, sender type, and enum values.
 *
 * @author u7884012 - Ruoheng Feng
 */
public class ChatMessageTest {

    /**
     * Tests the constructor and getter methods with a USER sender type.
     * Verifies that the message content and sender type are correctly set and retrieved.
     */
    @Test
    public void testConstructorAndGettersWithUserSender() {
        ChatMessage message = new ChatMessage("Hello!", ChatMessage.Sender.USER);

        assertEquals("Hello!", message.getMessage());
        assertEquals(ChatMessage.Sender.USER, message.getSender());
    }

    /**
     * Tests the constructor and getter methods with a BOT sender type.
     * Verifies that the message content and sender type are correctly set and retrieved.
     */
    @Test
    public void testConstructorAndGettersWithBotSender() {
        ChatMessage message = new ChatMessage("Hi there!", ChatMessage.Sender.BOT);

        assertEquals("Hi there!", message.getMessage());
        assertEquals(ChatMessage.Sender.BOT, message.getSender());
    }

    /**
     * Tests the Sender enum values and their string representations.
     * Verifies that the enum values can be correctly converted to and from strings.
     */
    @Test
    public void testSenderEnumValues() {
        assertEquals("USER", ChatMessage.Sender.USER.name());
        assertEquals("BOT", ChatMessage.Sender.BOT.name());

        assertEquals(ChatMessage.Sender.USER, ChatMessage.Sender.valueOf("USER"));
        assertEquals(ChatMessage.Sender.BOT, ChatMessage.Sender.valueOf("BOT"));
    }
}
