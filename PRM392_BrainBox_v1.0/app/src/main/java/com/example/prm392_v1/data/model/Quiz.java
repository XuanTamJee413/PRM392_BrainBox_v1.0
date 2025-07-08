package com.example.prm392_v1.data.model;
import com.google.gson.annotations.SerializedName;
public class Quiz {
    @SerializedName("QuizId")
    public int quizId;

    @SerializedName("QuizName")
    public String quizName;

    @SerializedName("Description")
    public String description;

    @SerializedName("CreatorId")
    public int creatorId;

    @SerializedName("IsPublic")
    public boolean isPublic;

    @SerializedName("CreatedAt")
    public long createdAt;
}
