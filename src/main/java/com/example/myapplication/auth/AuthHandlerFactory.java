package com.example.myapplication.auth;

/**
 * Factory class for creating authentication handlers.
 * Returns different handler implementations based on the type of authentication.
 * Currently supports "login" and "signup".
 * For signup, name and phone must be passed as extra arguments.
 *
 * @author u7991805 - Janvi Rajendra Nandre
 */
public class AuthHandlerFactory {

    /**
     * Returns the appropriate AuthHandler based on the given type.
     *
     * @param type  Type of handler: "login" or "signup".
     * @param extra Additional parameters (e.g., name and phone for signup).
     * @return AuthHandler instance.
     * @throws IllegalArgumentException if an unknown type is provided.
     */
    public static AuthHandler getHandler(String type, String... extra) {
        if ("login".equalsIgnoreCase(type)) {
            return new LoginHandler();
        } else if ("signup".equalsIgnoreCase(type)) {
            return new SignUpHandler(extra[0], extra[1]);  // name, phone
        }
        throw new IllegalArgumentException("Unknown auth handler type");
    }
}
