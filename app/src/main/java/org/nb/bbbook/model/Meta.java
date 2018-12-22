package org.nb.bbbook.model;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("m")
public class Meta {
    @Id
    private final int id;
    private final Instant lastUpdated;
    private final MetaStatus status;

    public Meta(int id, Instant lastUpdated, MetaStatus status) {
        this.id = 0;
        this.lastUpdated = lastUpdated;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public MetaStatus getStatus() {
        return status;
    }

    public enum MetaStatus {
        OK,
        BAD,
        REFRESHING
    }
}

