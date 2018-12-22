package org.nb.bbbook.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("tsp")
public class TeamSplits {
    @Id
    private final String name;
    private final Splits total;
    private final Splits home;
    private final Splits road;

    public TeamSplits(String name, Splits total, Splits home, Splits road) {
        this.name = name;
        this.total = total;
        this.home = home;
        this.road = road;
    }

    public String getName() {
        return name;
    }

    public Splits getTotal() {
        return total;
    }

    public Splits getHome() {
        return home;
    }

    public Splits getRoad() {
        return road;
    }
}

