package org.nb.bbbook.algo;

import org.nb.bbbook.model.TeamSplits;
import org.nb.bbbook.model.TeamStatsSeason;

public class TotalPointsModel {
    private String visitor;
    private String home;
    private float totalAvg;
    private float totalCourt;
    private int visitorORtgRank;
    private int visitorDRtgRank;
    private int visitorPaceRank;
    private int homeORtgRank;
    private int homeDRtgRank;
    private int homePaceRank;

    public TotalPointsModel(
        TeamStatsSeason vStats,
        TeamStatsSeason hStats,
        TeamSplits vSplits,
        TeamSplits hSplits
    ) {
        this.visitor = vStats.getName();
        this.home = hStats.getName();

        // Overall season totals, ignoring home / road court
        final float visitorTotalAvg =
            vSplits.getTotal().getPts() + vSplits.getTotal().getOpppts();
        final float homeTotalAvg =
            hSplits.getTotal().getPts() + hSplits.getTotal().getOpppts();
        this.totalAvg = (visitorTotalAvg + homeTotalAvg) / 2;

        // Totals using home team / road team splits.
        final float visitorTotalRoad =
            vSplits.getRoad().getPts() + vSplits.getRoad().getOpppts();
        final float homeTotalHome =
            hSplits.getHome().getPts() + hSplits.getHome().getOpppts();
        this.totalCourt = (visitorTotalRoad + homeTotalHome) / 2;

        this.visitorORtgRank = vStats.getTeamMisc().getoRtgRank();
        this.visitorDRtgRank = vStats.getTeamMisc().getdRtgRank();
        this.visitorPaceRank = vStats.getTeamMisc().getPaceRank();
        this.homeORtgRank = hStats.getTeamMisc().getoRtgRank();
        this.homeDRtgRank = hStats.getTeamMisc().getdRtgRank();
        this.homePaceRank = hStats.getTeamMisc().getPaceRank();
    }

    public String getVisitor() {
        return visitor;
    }

    public String getHome() {
        return home;
    }

    public float getTotalAvg() {
        return totalAvg;
    }

    public float getTotalCourt() {
        return totalCourt;
    }

    public int getVisitorORtgRank() {
        return visitorORtgRank;
    }

    public int getVisitorDRtgRank() {
        return visitorDRtgRank;
    }

    public int getVisitorPaceRank() {
        return visitorPaceRank;
    }

    public int getHomeORtgRank() {
        return homeORtgRank;
    }

    public int getHomeDRtgRank() {
        return homeDRtgRank;
    }

    public int getHomePaceRank() {
        return homePaceRank;
    }

}
