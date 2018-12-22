package org.nb.bbbook.algo.fourfactor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.nb.bbbook.model.BoxScore;
import org.nb.bbbook.model.LeagueAverage;
import org.nb.bbbook.model.TeamStatsSeason;

public class TeamModel {
    private String teamName;
    private int gameSampleSize;
    private float offTotal;
    private float defTotal;
    private float fourFactorScore;
    private float netRating;
    private float scaledScore;

    private float q1Points;
    private float q2Points;
    private float q3Points;
    private float q4Points;
    private float totalLinePoints;

    private static float efgCoef = 0.54f;
    private static float tovCoef = 0.22f;
    private static float rebCoef = 0.15f;
    private static float ftFgCoef = 0.1f;

    public TeamModel(
        String teamName,
        float offTotal,
        float defTotal,
        float fourFactorScore,
        float netRating,
        int gameSampleSize
    ) {
        this.teamName = teamName;
        this.offTotal = offTotal;
        this.defTotal = defTotal;
        this.fourFactorScore = fourFactorScore;
        this.netRating = netRating;
        this.gameSampleSize = gameSampleSize;
    }

    public static TeamModel fromTeamStatsSeason(TeamStatsSeason t) {
        final String teamName = t.getName();
        // Offensive Four Factors
        final float efg = (t.getTeamMisc().getEfgPercentage() * 100) * efgCoef;
        final float tov = -t.getTeamMisc().getTovPercent() * tovCoef;
        final float orb = t.getTeamMisc().getOrbPercent() * rebCoef;
        final float ftFga = (t.getTeamMisc().getFtPerFga() * 100) * ftFgCoef;
        final float offTotal = efg + tov + orb + ftFga;
        // Defensive Four Factors
        final float oppEfg = (t.getTeamMisc().getOppEfgPercent() * 100) * efgCoef;
        final float oppTov = -t.getTeamMisc().getOppTovPercent() * tovCoef;
        final float oppOrb = (100 - t.getTeamMisc().getDrbPercent()) * rebCoef;
        final float oppFtFga = (t.getTeamMisc().getOppFtPerfga() * 100) * ftFgCoef;
        final float defTotal = oppEfg + oppTov + + oppOrb + oppFtFga;
        final float overallScore = offTotal - defTotal;
        // Net Rating
        final float netRating = t.getTeamMisc().getORtg() - t.getTeamMisc().getDRtg();

        return new TeamModel(teamName, offTotal, defTotal, overallScore, netRating, 0);
    }

    public static Optional<TeamModel> fromBoxScore(String team, BoxScore bs) {
        if (!bs.getVisitorTeam().equals(team) && !bs.getHomeTeam().equals(team)) {
            return Optional.empty();
        }

        // Visitor Team Four Factors
        float vEfg = bs.getVisitorEfgPercent() * 100 * efgCoef;
        float vTov = -bs.getVisitorTovPercent() * tovCoef;
        float vOrb = bs.getVisitorOrbPercent() * rebCoef;
        float vFtFga = bs.getVisitorFtPerFga() * 100 * ftFgCoef;
        // Home Team Four Factors
        float hEfg = bs.getHomeEfgPercent() * 100 * efgCoef;
        float hTov = -bs.getHomeTovPercent() * tovCoef;
        float hOrb = bs.getHomeOrbPercent() * rebCoef;
        float hFtFga = bs.getHomeFtPerFga() * 100 * ftFgCoef;

        if (bs.getVisitorTeam().equals(team)) {
            final String teamName = bs.getVisitorTeam();
            final float offTotal = vEfg + vTov + vOrb + vFtFga;
            final float defTotal = hEfg + hTov + hOrb + hFtFga;
            final float overallScore = offTotal - defTotal;
            final float netRating = bs.getVisitorTotalPts() - bs.getHomeTotalPts();

            final TeamModel model = new TeamModel(
                teamName,
                offTotal,
                defTotal,
                overallScore,
                netRating,
                1
            );
            model.setLineScore(
                bs.getVisitorQ1Pts(),
                bs.getVisitorQ2Pts(),
                bs.getVisitorQ3Pts(),
                bs.getVisitorQ4Pts());

            return Optional.of(model);

        } else if (bs.getHomeTeam().equals(team)) {
            final String teamName = bs.getHomeTeam();
            final float offTotal = hEfg + hTov + hOrb + hFtFga;
            final float defTotal = vEfg + vTov + vOrb + vFtFga;
            final float overallScore = offTotal - defTotal;
            final float netRating = bs.getHomeTotalPts() - bs.getVisitorTotalPts();

            final TeamModel model = new TeamModel(
                teamName,
                offTotal,
                defTotal,
                overallScore,
                netRating,
                1);
            model.setLineScore(
                bs.getHomeQ1Pts(),
                bs.getHomeQ2Pts(),
                bs.getHomeQ3Pts(),
                bs.getHomeQ4Pts());

            return Optional.of(model);
        }

        return Optional.empty();
    }

    /**
     * Take a number of box scores to create a averaged Four Factors model.
     */
    public static TeamModel fromBoxScores(String team, List<BoxScore> boxScores) {
        List<Optional<TeamModel>> models = new ArrayList<>();
        for (BoxScore b : boxScores) {
            models.add(fromBoxScore(team, b));
        }

        int size = 0;
        float offTotal = 0;
        float defTotal = 0;
        float overallScore = 0;
        float netRating = 0;
        float q1Points = 0;
        float q2Points = 0;
        float q3Points = 0;
        float q4Points = 0;
        for (Optional<TeamModel> m : models) {
            if (m.isPresent()) {
                size++;
                offTotal += m.get().getOffTotal();
                defTotal += m.get().getDefTotal();
                overallScore += m.get().getFourFactorScore();
                netRating += m.get().getNetRating();
                q1Points += m.get().getQ1Points();
                q2Points += m.get().getQ2Points();
                q3Points += m.get().getQ3Points();
                q4Points += m.get().getQ4Points();
            }
        }

        final TeamModel model = new TeamModel(
            team,
            (offTotal / size),
            (defTotal / size),
            (overallScore / size),
            (netRating / size),
            size
        );
        model.setLineScore(
            (q1Points/size),
            (q2Points/size),
            (q3Points/size),
            (q4Points/size));

        return model;
    }

    /**
     * Simple scaling algorithm using League min/max Net Rating as the bounds
     */
    public void scaleScore(LeagueAverage la) {
        // x' = (b - a) * (x - min)/(max-min) + a
        this.scaledScore =
            (la.getMaxNetRating() - la.getMinNetRating())
            * (getFourFactorScore() - la.getMinFourFactor())/(la.getMaxFourFactor() - la.getMinFourFactor())
            + la.getMinNetRating();
    }

    public void setLineScore(
        float q1Points,
        float q2Points,
        float q3Points,
        float q4Points
    ) {
        this.q1Points = q1Points;
        this.q2Points = q2Points;
        this.q3Points = q3Points;
        this.q4Points = q4Points;
        this.totalLinePoints = q1Points + q2Points + q3Points + q4Points;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getGameSampleSize() {
        return gameSampleSize;
    }

    public float getOffTotal() {
        return offTotal;
    }

    public float getDefTotal() {
        return defTotal;
    }

    public float getFourFactorScore() {
        return fourFactorScore;
    }

    public float getNetRating() {
        return netRating;
    }

    public float getScaledScore() {
        return scaledScore;
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

    public float getTotalLinePoints() {
        return totalLinePoints;
    }
}
