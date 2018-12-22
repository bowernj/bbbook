package org.nb.bbbook.view;

import org.nb.bbbook.algo.fourfactor.TeamModel;

public class FourFactorsView implements Comparable<FourFactorsView> {
    private String teamName;
    private int gameSampleSize;
    private float overall;
    private float netRating;
    private float fourFactorsAdjusted;
    private float q1Points;
    private float q2Points;
    private float q3Points;
    private float q4Points;
    private float totalPoints;

    public FourFactorsView(
        String teamName,
        int gameSampleSize,
        float overall,
        float netRating,
        float fourFactorsAdjusted,
        float q1Points,
        float q2Points,
        float q3Points,
        float q4Points,
        float totalPoints
    ) {
        this.teamName = teamName;
        this.gameSampleSize = gameSampleSize;
        this.overall = overall;
        this.netRating = netRating;
        this.fourFactorsAdjusted = fourFactorsAdjusted;
        this.q1Points = q1Points;
        this.q2Points = q2Points;
        this.q3Points = q3Points;
        this.q4Points = q4Points;
        this.totalPoints = totalPoints;
    }

    public static FourFactorsView fromModel(TeamModel m) {
        return new FourFactorsView(
            m.getTeamName(),
            m.getGameSampleSize(),
            m.getFourFactorScore(),
            m.getNetRating(),
            m.getScaledScore(),
            m.getQ1Points(),
            m.getQ2Points(),
            m.getQ3Points(),
            m.getQ4Points(),
            m.getTotalLinePoints());
    }

    public String getTeamName() {
        return teamName;
    }

    public int getGameSampleSize() {
        return gameSampleSize;
    }

    public float getFourFactorsAdjusted() {
        return fourFactorsAdjusted;
    }

    public float getQ1Points() {
        return q1Points;
    }

    public float getQ2Points() {
        return q2Points;
    }

    public float getQ3Points() {
        return q3Points;
    }

    public float getQ4Points() {
        return q4Points;
    }

    public float getTotalPoints() {
        return totalPoints;
    }

    @Override
    public int compareTo(FourFactorsView o) {
        return Float.compare(this.overall, o.overall);
    }

}
