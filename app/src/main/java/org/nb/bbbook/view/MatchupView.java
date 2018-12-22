package org.nb.bbbook.view;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.LinkedHashMap;

@JsonPropertyOrder({"samples", "visitorSpread", "homeSpread", "totalPoints", "h1Points", "q1Points", "q2Points", "q3Points", "q4points"})
public class MatchupView {
    private final int samples;
    private final float visitorSpread;
    private final float homeSpread;
    private final float q1Points;
    private final float q2Points;
    private final float q3Points;
    private final float q4Points;
    private final float totalPoints;
    private final float h1Points;
    private final FourFactorsView visitor;
    private final FourFactorsView home;

    public MatchupView(int samples, FourFactorsView visitor, FourFactorsView home) {
        this.samples = samples;
        this.visitor = visitor;
        this.home = home;
        this.visitorSpread = -1 * (visitor.getFourFactorsAdjusted() - home.getFourFactorsAdjusted());
        this.homeSpread = -1 * (home.getFourFactorsAdjusted() - visitor.getFourFactorsAdjusted());
        this.q1Points = visitor.getQ1Points() + home.getQ1Points();
        this.q2Points = visitor.getQ2Points() + home.getQ2Points();
        this.q3Points = visitor.getQ3Points() + home.getQ3Points();
        this.q4Points = visitor.getQ4Points() + home.getQ4Points();
        this.totalPoints = visitor.getTotalPoints() + home.getTotalPoints();
        this.h1Points = visitor.getQ1Points() + visitor.getQ2Points() + home.getQ1Points() + home.getQ2Points();
    }

    public int getSamples() {
        return samples;
    }

    public float getVisitorSpread() {
        return visitorSpread;
    }

    public float getHomeSpread() {
        return homeSpread;
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

    public float getH1Points() {
        return h1Points;
    }

    public FourFactorsView getVisitor() {
        return visitor;
    }

    public FourFactorsView getHome() {
        return home;
    }

    public static class MapView extends LinkedHashMap<String, MatchupView> {}

}
