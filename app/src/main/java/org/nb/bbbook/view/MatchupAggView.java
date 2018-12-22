package org.nb.bbbook.view;

import java.util.List;

public class MatchupAggView {
    private final long visitorDaysRest;
    private final long homeDaysRest;
    private final List<MatchupView> matchup;

    public MatchupAggView(
        long visitorDaysRest,
        long homeDaysRest,
        List<MatchupView> matchup
    ) {
        this.visitorDaysRest = visitorDaysRest;
        this.homeDaysRest = homeDaysRest;
        this.matchup = matchup;
    }

    public long getVisitorDaysRest() {
        return visitorDaysRest;
    }

    public long getHomeDaysRest() {
        return homeDaysRest;
    }

    public List<MatchupView> getMatchup() {
        return matchup;
    }

}
