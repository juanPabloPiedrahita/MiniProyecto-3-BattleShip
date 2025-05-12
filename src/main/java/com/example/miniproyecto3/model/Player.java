package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.Ship;
import com.example.miniproyecto3.model.Board;
import java.io.Serializable;


public class Player implements Serializable{
    public String username;
    public int score;

    public Player(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
