package com.example.myapplication.util;

import android.util.Log;
import okhttp3.*;
import org.json.*;

import java.io.IOException;
/**
 * LLMFacade is responsible for interacting with the Gemini language model API.
 * It prepares user queries by appending a system prompt and handles network requests
 * to generate responses from the model.
 *
 * App context: The app helps users manage vehicle and license details in a government platform.
 * This class handles all AI-based question answering logic.
 *
 * @author u8030355 Shane George Shibu
 */
public class LLMFacade {
    private static final String TAG = "QAApp";
    private static final String API_KEY = "AIzaSyDnRMJch-O94aeWRweFFjQkJWOKCAyV0Xg"; // Replace this with your actual Gemini API key
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;
    private final OkHttpClient client;

    // App-specific system prompt
    private static final String SYSTEM_PROMPT =
            "You are an assistant in a government mobile app designed to help users manage their vehicles, licenses, and profile information.\n\n"
                    + "App functionalities:\n"
                    + "- Users can view their registered vehicles by tapping 'My Vehicles' on the dashboard.\n"
                    + "- Users can view and manage their licenses via 'My Licenses'.\n"
                    + "- Users can apply for or renew a license through 'Apply / Renew License'. Required details must be submitted, and the request is approved by an admin before acceptance.\n"
                    + "- Vehicle status can show if a vehicle is stolen or requires maintenance. However, this is only visible if the vehicle owner has set the status to public.\n"
                    + "- Users can change their profile picture and update personal details by clicking the 'Profile' button.\n"
                    + "- The app's purpose is to provide a central platform for managing personal vehicle and license records securely and efficiently.\n\n"
                    + "Answer the following user question accurately and concisely:\n\nQ: ";

    public LLMFacade() {
        this.client = new OkHttpClient();
    }

    public String getAnswer(String question) {
        Log.d(TAG, "Preparing Gemini API request for: " + question);
        try {
            JSONObject message = new JSONObject();
            JSONArray parts = new JSONArray();
            JSONObject textPart = new JSONObject();

            // Combine system prompt with user question
            String prompt = SYSTEM_PROMPT + question;
            textPart.put("text", prompt);
            parts.put(textPart);
            JSONArray contents = new JSONArray();
            JSONObject content = new JSONObject();
            content.put("parts", parts);
            contents.put(content);
            message.put("contents", contents);

            RequestBody body = RequestBody.create(message.toString(), MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    Log.d(TAG, "Gemini raw response: " + responseBody);
                    return parseResponse(responseBody);
                } else {
                    Log.e(TAG, "Gemini API error: " + response.code());
                    return "Error: API call failed. HTTP " + response.code();
                }
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Gemini error: ", e);
            return "Error: " + e.getMessage();
        }
    }

    private String parseResponse(String responseBody) throws JSONException {
        JSONObject obj = new JSONObject(responseBody);
        JSONArray candidates = obj.getJSONArray("candidates");
        if (candidates.length() > 0) {
            JSONObject first = candidates.getJSONObject(0);
            JSONObject content = first.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            return parts.getJSONObject(0).getString("text");
        }
        return "No answer generated.";
    }
}
