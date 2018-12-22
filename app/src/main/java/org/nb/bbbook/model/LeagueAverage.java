package org.nb.bbbook.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("lgav")
public class LeagueAverage {
    @Id
    private final int id;

    private final float minFourFactor;
    private final float maxFourFactor;

    private final float minNetRating;
    private final float maxNetRating;

    public LeagueAverage(
        int id,
        float minFourFactor,
        float maxFourFactor,
        float minNetRating,
        float maxNetRating
    ) {
        this.id = 0;
        this.minFourFactor = minFourFactor;
        this.maxFourFactor = maxFourFactor;
        this.minNetRating = minNetRating;
        this.maxNetRating = maxNetRating;
    }

    public int getId() {
        return id;
    }

    public float getMinFourFactor() {
        return minFourFactor;
    }

    public float getMaxFourFactor() {
        return maxFourFactor;
    }

    public float getMinNetRating() {
        return minNetRating;
    }

    public float getMaxNetRating() {
        return maxNetRating;
    }

}
