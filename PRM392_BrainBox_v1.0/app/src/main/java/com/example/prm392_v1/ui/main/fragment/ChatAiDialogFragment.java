package com.example.prm392_v1.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.gemini.GeminiRequest;
import com.example.prm392_v1.data.model.gemini.GeminiResponse;
import com.example.prm392_v1.data.model.gemini.Message;
import com.example.prm392_v1.data.network.GeminiApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.ChatAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAiDialogFragment extends DialogFragment {

    private RecyclerView rvChat;
    private EditText etMessage;
    private ImageButton btnSend;
    private ProgressBar progressBar;
    private ChatAdapter chatAdapter;
    private List<Message> messageList = new ArrayList<>();
    private GeminiApiService geminiApiService;
    private final String API_KEY = "AIzaSyCiTLq7zVklDWLMdQa-cI-m7e2imAUJaXM";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_ai_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvChat = view.findViewById(R.id.rvChat);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSend);
        progressBar = view.findViewById(R.id.progressBar);
        geminiApiService = RetrofitClient.getGeminiApiService();

        setupRecyclerView();

        btnSend.setOnClickListener(v -> sendMessage());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void setupRecyclerView() {
        chatAdapter = new ChatAdapter(messageList);
        rvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChat.setAdapter(chatAdapter);
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();
        if (messageText.isEmpty()) return;

        addMessage(messageText, true);
        etMessage.setText("");
        progressBar.setVisibility(View.VISIBLE);

        GeminiRequest.Part part = new GeminiRequest.Part(messageText);
        GeminiRequest.Content content = new GeminiRequest.Content(Collections.singletonList(part));
        GeminiRequest request = new GeminiRequest(Collections.singletonList(content));

        geminiApiService.generateContent(request, API_KEY).enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    addMessage(response.body().getResponseText(), false);
                } else {
                    addMessage("Lỗi: " + response.code(), false);
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                addMessage("Lỗi kết nối: " + t.getMessage(), false);
            }
        });
    }

    private void addMessage(String text, boolean isFromUser) {
        messageList.add(new Message(text, isFromUser));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        rvChat.scrollToPosition(messageList.size() - 1);
    }
}