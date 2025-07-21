package com.example.prm392_v1.data.model.gemini;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GeminiResponse {
    @SerializedName("candidates")
    private List<Candidate> candidates;

    public String getResponseText() {
        if (candidates != null && !candidates.isEmpty()) {
            Candidate firstCandidate = candidates.get(0);
            if (firstCandidate.content != null && firstCandidate.content.parts != null && !firstCandidate.content.parts.isEmpty()) {
                return firstCandidate.content.parts.get(0).text;
            }
        }
        return "Xin lỗi, tôi không thể xử lý yêu cầu này.";
    }

    public static class Candidate {
        @SerializedName("content")
        public Content content;
    }

    public static class Content {
        @SerializedName("parts")
        public List<Part> parts;
    }

    public static class Part {
        @SerializedName("text")
        public String text;
    }
}