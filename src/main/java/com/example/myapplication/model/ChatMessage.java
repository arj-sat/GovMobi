package com.example.myapplication.model;
/**
 * ChatMessage is a model class representing a single message in the chat conversation.
 * It distinguishes messages by sender (USER or BOT) and stores message content.
 *
 * Used in conjunction with the ChatAdapter and QAActivity for displaying chat messages.
 *
 * @author u8030355 Shane George Shibu
 */
public class ChatMessage {
    public enum Sender { USER, BOT }

    private final String message;
    private final Sender sender;

    public ChatMessage(String message, Sender sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public Sender getSender() {
        return sender;
    }
}
