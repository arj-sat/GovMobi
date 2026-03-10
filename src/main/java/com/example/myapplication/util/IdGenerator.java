package com.example.myapplication.util;

import java.util.UUID;

/**
 * IdGenerator - Utility class for generating random alphanumeric IDs.
 * Generates 5-character IDs consisting of uppercase letters and digits.
 * Used for creating unique identifiers for various application entities - License request IDs.
 * @author  u8007015 Arjun Satish
 */
public class IdGenerator {
    public String getid() {
        return UUID.randomUUID().toString();
    }

}
