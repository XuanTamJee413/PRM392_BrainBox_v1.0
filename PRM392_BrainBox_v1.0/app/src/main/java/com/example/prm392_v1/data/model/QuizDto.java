package com.example.prm392_v1.data.model;

public class QuizDto {
    public int QuizId;
    public String QuizName;
    public String Description;
    public int CreatorId;
    public boolean IsPublic;
    public long CreatedAt;
    public Creator Creator;

    public static class Creator {
        public String Username;
    }
}
