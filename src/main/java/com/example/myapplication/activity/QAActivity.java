package com.example.myapplication.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapter.ChatAdapter;
import com.example.myapplication.model.ChatMessage;
import com.example.myapplication.util.LLMFacade;

import java.util.ArrayList;
import java.util.List;
/**
 * QAActivity is an Android activity that allows users to interact with a chatbot
 * interface for vehicle and license-related queries. It handles the UI components
 * for chat, processes user input, and updates the chat history.
 *
 * It connects with LLMFacade to retrieve answers from the Gemini model.
 * Uses RecyclerView to display chat messages.
 *
 * @author u8030355 Shane George Shibu
 */

public class QAActivity extends AppCompatActivity {
    private EditText questionInput;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private LLMFacade qaFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qaactivity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AI Chat Support");


        RecyclerView recyclerView = findViewById(R.id.chatRecyclerView);
        questionInput = findViewById(R.id.questionInput);
        Button askButton = findViewById(R.id.askButton);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);
        new android.os.Handler().postDelayed(() -> {
            chatMessages.add(new ChatMessage(
                    "Hi there! I'm your Assist AI. I can help you with any questions related to your vehicles, licenses and profile. How can I assist you today?",
                    ChatMessage.Sender.BOT));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        }, 500);
        qaFacade = new LLMFacade();

        askButton.setOnClickListener(v -> {
            String question = questionInput.getText().toString().trim();
            if (!question.isEmpty()) {
                chatMessages.add(new ChatMessage(question, ChatMessage.Sender.USER));
                chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                questionInput.setText("");
                new QueryTask().execute(question);
            }
        });
    }

    private class QueryTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return qaFacade.getAnswer(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            chatMessages.add(new ChatMessage(result, ChatMessage.Sender.BOT));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        }
    }
}
