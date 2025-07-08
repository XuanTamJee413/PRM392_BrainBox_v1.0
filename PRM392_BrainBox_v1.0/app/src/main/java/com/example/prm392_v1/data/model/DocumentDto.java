package com.example.prm392_v1.data.model;

public class DocumentDto {
    public int DocId;
    public String Title;
    public String Content;
    public boolean IsPublic;
    public int Views;
    public long CreatedAt;
    public Author Author;

    public static class Author {
        public String Username;
    }
}
