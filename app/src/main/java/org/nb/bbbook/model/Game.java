package org.nb.bbbook.model;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("g")
public class Game implements Comparable<Game> {
    @Id
    private int id;
    private LocalDate date;
    private String start;
    private String visitorTeam;
    private int visitorPts;
    private String homeTeam;
    private int homePts;
    private String notes;

    public Game(int id, LocalDate date, String start, String visitorTeam, int visitorPts, String homeTeam, int homePts,
        String notes) {
        this.id = id;
        this.date = date;
        this.start = start;
        this.visitorTeam = visitorTeam;
        this.visitorPts = visitorPts;
        this.homeTeam = homeTeam;
        this.homePts = homePts;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStart() {
        return start;
    }

    public String getVisitorTeam() {
        return visitorTeam;
    }

    public int getVisitorPts() {
        return visitorPts;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public int getHomePts() {
        return homePts;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public int compareTo(Game o) {
        return this.getDate().compareTo(o.date);
    }
}
