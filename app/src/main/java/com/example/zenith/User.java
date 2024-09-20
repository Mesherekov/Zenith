package com.example.zenith;

public class User {
    public String id, name, email, password, imageUri;

    public User() {
    }

    public User(String id, String name, String email, String password, String imageUri) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.imageUri = imageUri;
    }
}
