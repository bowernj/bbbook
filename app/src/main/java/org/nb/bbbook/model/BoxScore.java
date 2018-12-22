package org.nb.bbbook.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("box")
public class    BoxScore {
    @Id
    private final String id;

    private final String visitorTeam;
    private final String homeTeam;

    private final int visitorQ1Pts;
    private final int visitorQ2Pts;
    private final int visitorQ3Pts;
    private final int visitorQ4Pts;
    // TODO OT pts?
    private final int visitorTotalPts;

    private final int homeQ1Pts;
    private final int homeQ2Pts;
    private final int homeQ3Pts;
    private final int homeQ4Pts;
    private final int homeTotalPts;

    private final float pace;

    // Four Factors
    private final float visitorEfgPercent;
    private final float visitorTovPercent;
    private final float visitorOrbPercent;
    private final float visitorFtPerFga;
    private final float homeEfgPercent;
    private final float homeTovPercent;
    private final float homeOrbPercent;
    private final float homeFtPerFga;

    public BoxScore(String id, String visitorTeam, String homeTeam, int visitorQ1Pts, int visitorQ2Pts,
        int visitorQ3Pts,
        int visitorQ4Pts, int visitorTotalPts, int homeQ1Pts, int homeQ2Pts, int homeQ3Pts, int homeQ4Pts,
        int homeTotalPts,
        float pace,
        float visitorEfgPercent, float visitorTovPercent, float visitorOrbPercent, float visitorFtPerFga,
        float homeEfgPercent, float homeTovPercent, float homeOrbPercent, float homeFtPerFga
    ) {
        this.id = id;
        this.visitorTeam = visitorTeam;
        this.homeTeam = homeTeam;
        this.visitorQ1Pts = visitorQ1Pts;
        this.visitorQ2Pts = visitorQ2Pts;
        this.visitorQ3Pts = visitorQ3Pts;
        this.visitorQ4Pts = visitorQ4Pts;
        this.visitorTotalPts = visitorTotalPts;
        this.homeQ1Pts = homeQ1Pts;
        this.homeQ2Pts = homeQ2Pts;
        this.homeQ3Pts = homeQ3Pts;
        this.homeQ4Pts = homeQ4Pts;
        this.homeTotalPts = homeTotalPts;
        this.pace = pace;
        this.visitorEfgPercent = visitorEfgPercent;
        this.visitorTovPercent = visitorTovPercent;
        this.visitorOrbPercent = visitorOrbPercent;
        this.visitorFtPerFga = visitorFtPerFga;
        this.homeEfgPercent = homeEfgPercent;
        this.homeTovPercent = homeTovPercent;
        this.homeOrbPercent = homeOrbPercent;
        this.homeFtPerFga = homeFtPerFga;

    }


    // Total Points Excluding OT Points
    public int getVisitorLinePts() {
        return visitorQ1Pts + visitorQ2Pts + visitorQ3Pts + visitorQ4Pts;
    }

    // Total Points Excluding OT Points
    public int getHomeLinePts() {
        return homeQ1Pts + homeQ2Pts + homeQ3Pts + homeQ4Pts;
    }

    public LocalDate getDate() {
        final String dateString = this.id.substring(0, id.length() - 4);
        return LocalDate.parse(dateString, DateTimeFormatter.BASIC_ISO_DATE);
    }
    public String getId() {
        return id;
    }

    public String getVisitorTeam() {
        return visitorTeam;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public int getVisitorQ1Pts() {
        return visitorQ1Pts;
    }

    public int getVisitorQ2Pts() {
        return visitorQ2Pts;
    }

    public int getVisitorQ3Pts() {
        return visitorQ3Pts;
    }

    public int getVisitorQ4Pts() {
        return visitorQ4Pts;
    }

    public int getVisitorTotalPts() {
        return visitorTotalPts;
    }

    public int getHomeQ1Pts() {
        return homeQ1Pts;
    }

    public int getHomeQ2Pts() {
        return homeQ2Pts;
    }

    public int getHomeQ3Pts() {
        return homeQ3Pts;
    }

    public int getHomeQ4Pts() {
        return homeQ4Pts;
    }

    public int getHomeTotalPts() {
        return homeTotalPts;
    }

    public float getPace() {
        return pace;
    }

    public float getHomeEfgPercent() {
        return homeEfgPercent;
    }

    public float getHomeTovPercent() {
        return homeTovPercent;
    }

    public float getHomeOrbPercent() {
        return homeOrbPercent;
    }

    public float getHomeFtPerFga() {
        return homeFtPerFga;
    }

    public float getVisitorEfgPercent() {
        return visitorEfgPercent;
    }

    public float getVisitorTovPercent() {
        return visitorTovPercent;
    }

    public float getVisitorOrbPercent() {
        return visitorOrbPercent;
    }

    public float getVisitorFtPerFga() {
        return visitorFtPerFga;
    }

}
