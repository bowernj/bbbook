package org.nb.bbbook.model;

public class TeamMiscStats {
    private final int rank;
    private final String team;
    private final float avgAge;
    private final int wins;
    private final int losses;
    private final int pythagWins;
    private final int pythagLosses;
    private final float avgMargin;
    private final float strengthOfSchedule;
    private final float simpleRatingSystem;
    private final float oRtg;
    private final float dRtg;
    private final float pace;
    private final float ftRate;
    private final float threepaRate;
    private final float trueShooting;
    private final float efgPercentage;
    private final float tovPercent;
    private final float orbPercent;
    private final float ftPerFga;
    private final float oppEfgPercent;
    private final float oppTovPercent;
    private final float drbPercent;
    private final float oppFtPerfga;
    private final String arena;
    private final long attend;
    private final long attendPerGame;

    public TeamMiscStats(
        String rank,
        String team,
        String avgAge,
        String wins,
        String losses,
        String pythagWins,
        String pythagLosses,
        String avgMargin,
        String strengthOfSchedule,
        String simpleRatingSystem,
        String oRtg,
        String dRtg,
        String pace,
        String ftRate,
        String threepaRate,
        String trueShooting,
        String efgPercentage,
        String tovPercent,
        String orbPercent,
        String ftPerFga,
        String oppEfgPercent,
        String oppTovPercent,
        String drbPercent,
        String oppFtPerfga,
        String arena,
        String attend,
        String attendPerGame
    ) {
        this.rank = Integer.parseInt(rank);
        this.team = team;
        this.avgAge = Float.parseFloat(avgAge);
        this.wins = Integer.parseInt(wins);
        this.losses = Integer.parseInt(losses);
        this.pythagWins = Integer.parseInt(pythagWins);
        this.pythagLosses = Integer.parseInt(pythagLosses);
        this.avgMargin = Float.parseFloat(avgMargin);
        this.strengthOfSchedule = Float.parseFloat(strengthOfSchedule);
        this.simpleRatingSystem = Float.parseFloat(simpleRatingSystem);
        this.oRtg = Float.parseFloat(oRtg);
        this.dRtg = Float.parseFloat(dRtg);
        this.pace = Float.parseFloat(pace);
        this.ftRate = Float.parseFloat(ftRate);
        this.threepaRate = Float.parseFloat(threepaRate);
        this.trueShooting = Float.parseFloat(trueShooting);
        this.efgPercentage = Float.parseFloat(efgPercentage);
        this.tovPercent = Float.parseFloat(tovPercent);
        this.orbPercent = Float.parseFloat(orbPercent);
        this.ftPerFga = Float.parseFloat(ftPerFga);
        this.oppEfgPercent = Float.parseFloat(oppEfgPercent);
        this.oppTovPercent = Float.parseFloat(oppTovPercent);
        this.drbPercent = Float.parseFloat(drbPercent);
        this.oppFtPerfga = Float.parseFloat(oppFtPerfga);
        this.arena = arena;
        this.attend = Long.parseLong(attend);
        this.attendPerGame = Long.parseLong(attendPerGame);
    }


}
