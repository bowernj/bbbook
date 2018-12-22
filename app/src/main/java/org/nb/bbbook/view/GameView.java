package org.nb.bbbook.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class GameView implements Comparable<GameView>{
    private String date;
    private String start;
    private ZonedDateTime startTime;
    private String visitor;
    private String home;

    private static final DateTimeFormatter dateFormat =
        DateTimeFormatter.ofPattern("EEE d MMM yyyy");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm");

    private GameView(String date, String start, ZonedDateTime startTime, String visitor, String home) {
        this.date = date;
        this.start = start;
        this.startTime = startTime;
        this.visitor = visitor;
        this.home = home;
    }

    public String getDate() {
        return date;
    }

    public String getStart() {
        return start;
    }

    public String getVisitor() {
        return visitor;
    }

    public String getHome() {
        return home;
    }

    @Override
    public int compareTo(GameView o) {
        return this.startTime.compareTo(o.startTime);
    }

    public static class Builder {
        private String date;
        private String start;
        private ZonedDateTime startTime;
        private String visitor;
        private String home;
        private TotalPointsView totalPoints;

        public Builder(LocalDate date, String start, String visitor, String home) {
            this.date = date.format(dateFormat);
            this.start = start;
            try {
                this.startTime = timeFormat.parse(start.replace("p",""))
                    .toInstant().atZone(ZoneId.of("America/New_York"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.visitor = visitor;
            this.home = home;
        }

        public GameView build() {
            return new GameView(
                date,
                start,
                startTime,
                visitor,
                home);
        }

        public Builder setTotalPointsView(TotalPointsView t) {
            this.totalPoints = t;
            return this;
        }
    }

}
