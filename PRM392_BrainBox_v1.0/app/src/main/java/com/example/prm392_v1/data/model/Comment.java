package com.example.prm392_v1.data.model;

import com.example.prm392_v1.data.entity.User;

public class Comment {
    public int CommentId;
    public int DocDetailId;
    public int UserId;
    public String Content;
    public long CreatedAt;

    public DocumentDetail DocumentDetail;
    public User User;
}