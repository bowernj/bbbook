package org.nb.bbbook.view;

import java.util.ArrayList;
import java.util.List;
import org.nb.bbbook.model.Result;
import org.nb.bbbook.model.BoxScore;


public class ResultsView {
    private List<SimpleBoxScoreView> boxScores;
    private int roadWins = 0;
    private int roadLosses = 0;
    private int homeWins = 0;
    private int homeLosses = 0;
    private float roadWinPercentage;
    private float homeWinPercentage;

    public ResultsView(List<SimpleBoxScoreView> boxScores) {
        this.boxScores = boxScores;
    }

    public static ResultsView fromBoxScores(String team, List<BoxScore> boxScores) {
        List<SimpleBoxScoreView> list = new ArrayList<>();
        boxScores.forEach(bs -> list.add(SimpleBoxScoreView.fromBoxScore(team, bs)));
        return new ResultsView(list).calculateWinPercentage(team);
    }

    public List<SimpleBoxScoreView> getBoxScores() {
        return boxScores;
    }

    public int getRoadWins() {
        return roadWins;
    }

    public int getRoadLosses() {
        return roadLosses;
    }

    public int getHomeWins() {
        return homeWins;
    }

    public int getHomeLosses() {
        return homeLosses;
    }

    public float getRoadWinPercentage() {
        return roadWinPercentage;
    }

    public float getHomeWinPercentage() {
        return homeWinPercentage;
    }

    private ResultsView calculateWinPercentage(String team) {
        for (SimpleBoxScoreView bs : boxScores) {
            if (team.equalsIgnoreCase(bs.getVisitor())) {
                if (bs.getResult() == Result.W) {
                    this.roadWins++;
                } else {
                    this.roadLosses++;
                }
            } else {
                if (bs.getResult() == Result.W) {
                    this.homeWins++;
                } else {
                    this.homeLosses++;
                }
            }
        }

        this.roadWinPercentage = (float) roadWins / (roadLosses + roadWins);
        this.homeWinPercentage = (float) homeWins / (homeLosses + homeWins);

        return this;
    }

}
