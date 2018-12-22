package org.nb.bbbook.view;

import java.util.List;

public class DailySummaryView {
    private final List<GameView> games;

    public DailySummaryView(List<GameView> games) {
        this.games = games;
    }

    public List<GameView> getGames() {
        return games;
    }

}

