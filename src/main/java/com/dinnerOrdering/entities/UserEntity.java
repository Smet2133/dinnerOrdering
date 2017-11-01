package com.dinnerOrdering.entities;

public class UserEntity {
    private int user_id;
    private String username;
    private String name;
    private int balance;
    private boolean autoupdate;


    public UserEntity() {
    }

    public UserEntity(int user_id, String username, String name, int balance){
        this.user_id = user_id;
        this.username = username;
        this.name = name;
        this.balance = balance;
    }

    public boolean getAutoupdate() {
        return autoupdate;
    }

    public void setAutoupdate(boolean autoupdate) {
        this.autoupdate = autoupdate;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
