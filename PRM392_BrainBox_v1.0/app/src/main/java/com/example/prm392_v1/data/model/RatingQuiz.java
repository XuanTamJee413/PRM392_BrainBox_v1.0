package com.example.prm392_v1.data.model;

import com.google.gson.annotations.SerializedName;

public class RatingQuiz {
    @SerializedName("RatingId")
    public int ratingId;
    @SerializedName("quizId")
    public int quizId;
    @SerializedName("UserId")
    public int userId;
    @SerializedName("rating")
    public int rating;
    @SerializedName("Comment")
    public String comment;
    @SerializedName("RatedAt")
    public long ratedAt;
}