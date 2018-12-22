package org.nb.bbbook.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("user")
public class User {
    @Id String id;
    String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
