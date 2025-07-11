package com.example.prm392_v1.data.model;
public class RatingQuizRequest {
    public int quizId;
    public int rating;
    public String comment;
    public RatingQuizRequest(int quizId, int rating, String comment) {
        this.quizId = quizId;
        this.rating = rating;
        this.comment = comment;
    }
}