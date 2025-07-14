package com.example.prm392_v1.data.entity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String username;
    public String password;
    public String role;
    public String email;
    public boolean status;
    public String avatar;
    public long createAt;
    public long premiumExpiredAt;

    public User() {
    }

    public User(int id, String username, String password, String role, String email, boolean status, String avatar, long createAt, long premiumExpiredAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.status = status;
        this.avatar = avatar;
        this.createAt = createAt;
        this.premiumExpiredAt = premiumExpiredAt;
    }
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = "";
        this.status = true;
        this.avatar = "";
        this.createAt = System.currentTimeMillis();
        this.premiumExpiredAt = 0;
    }


    public boolean isPremium() {
        return premiumExpiredAt > System.currentTimeMillis();
    }

}
