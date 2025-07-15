package com.example.prm392_v1.data.model;

import com.google.gson.annotations.SerializedName;

public class RatingQuiz {
    @SerializedName("RatingId")
    public int ratingId;
    @SerializedName("quizId") // Changed from "QuizId" to match JSON key
    public int quizId;
    @SerializedName("UserId")
    public int userId;
    @SerializedName("rating") // Changed from "Rating" to match JSON key
    public int rating;
    @SerializedName("Comment")
    public String comment;
    @SerializedName("RatedAt")
    public long ratedAt;
}