package com.example.prm392_v1.data.model;
import com.google.gson.annotations.SerializedName;
public class Flashcard {
    @SerializedName("CardId")
    public int cardId;

    @SerializedName("QuizId")
    public int quizId;

    @SerializedName("Question")
    public String question;

    @SerializedName("Option1")
    public String option1;

    @SerializedName("Option2")
    public String option2;

    @SerializedName("Option3")
    public String option3;

    @SerializedName("Option4")
    public String option4;

    @SerializedName("Answer")
    public int answer;
}
