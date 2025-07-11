package com.example.prm392_v1.data.model;
import com.google.gson.annotations.SerializedName;

public class RatingQuiz {
    @SerializedName("RatingId")
    public int ratingId;
    @SerializedName("QuizId")
    public int quizId;
    @SerializedName("UserId")
    public int userId;
    @SerializedName("Rating")
    public int rating;
    @SerializedName("Comment") // Add this
    public String comment;      // Add this
    @SerializedName("RatedAt")  // Add this (assuming the timestamp is a long)
    public long ratedAt;
}