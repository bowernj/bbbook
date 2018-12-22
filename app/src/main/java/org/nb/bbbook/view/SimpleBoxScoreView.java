package org.nb.bbbook.view;

import java.time.LocalDate;
import org.nb.bbbook.model.BoxScore;
import org.nb.bbbook.model.Result;

public class SimpleBoxScoreView {
    private LocalDate date;
    private Result result;
    private int line;
    private String visitor;
    private int[] visitorLineScore;
    private String home;
    private int[] homeLineScore;

    private SimpleBoxScoreView(
        LocalDate date,
        Result result,
        int line,
        String visitor,
        int[] visitorLineScore,
        String home,
        int[] homeLineScore
    ) {
        this.date = date;
        this.result = result;
        this.line = line;
        this.visitor = visitor;
        this.visitorLineScore = visitorLineScore;
        this.home = home;
        this.homeLineScore = homeLineScore;
    }

    public static SimpleBoxScoreView fromBoxScore(String team, BoxScore bs) {
        Result result;
        int line;
        if (bs.getVisitorTotalPts() > bs.getHomeTotalPts()) {
            if (team.equalsIgnoreCase(bs.getVisitorTeam())) {
                line = bs.getVisitorTotalPts() - bs.getHomeTotalPts();
                result = Result.W;
            } else {
                line = bs.getHomeTotalPts() - bs.getVisitorTotalPts();
                result = Result.L;
            }
        } else {
            if (team.equalsIgnoreCase(bs.getHomeTeam())) {
                line = bs.getHomeTotalPts() - bs.getVisitorTotalPts();
                result = Result.W;
            } else {
                line = bs.getVisitorTotalPts() - bs.getHomeTotalPts();
                result = Result.L;
            }
        }
        final int[] visitorLineScore =  {
            bs.getVisitorQ1Pts(),
            bs.getVisitorQ2Pts(),
            bs.getVisitorQ3Pts(),
            bs.getVisitorQ4Pts(),
            bs.getVisitorTotalPts()
        };
        final int[] homeLineScore =  {
            bs.getHomeQ1Pts(),
            bs.getHomeQ2Pts(),
            bs.getHomeQ3Pts(),
            bs.getHomeQ4Pts(),
            bs.getHomeTotalPts()
        };
        return new SimpleBoxScoreView(
            bs.getDate(),
            result,
            line,
            bs.getVisitorTeam(),
            visitorLineScore,
            bs.getHomeTeam(),
            homeLineScore);
    }

    public LocalDate getDate() {
        return date;
    }

    public Result getResult() {
        return result;
    }

    public int getLine() {
        return line;
    }

    public String getVisitor() {
        return visitor;
    }

    public int[] getVisitorLineScore() {
        return visitorLineScore;
    }

    public String getHome() {
        return home;
    }

    public int[] getHomeLineScore() {
        return homeLineScore;
    }

}
