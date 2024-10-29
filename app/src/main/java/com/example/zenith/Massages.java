package com.example.zenith;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Massages {
    public String id, text, ownUID, user1, user2, uri;

    public Massages() {
    }

    public Massages(String id, String text, String ownUID, String user1, String user2, String uri) {
        this.id = id;
        this.text = text;
        this.ownUID = ownUID;
        this.user1 = user1;
        this.user2 = user2;
        this.uri = uri;
    }


}
