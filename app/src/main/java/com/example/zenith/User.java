package com.example.zenith;

public class User {
    public String id, name, email, password, imageUri, UID;

    public User() {
    }

    public User(String id, String name, String email, String password, String imageUri, String UID) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.imageUri = imageUri;
        this.UID = UID;
    }
}
