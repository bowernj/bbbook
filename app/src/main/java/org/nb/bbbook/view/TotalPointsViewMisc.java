package org.nb.bbbook.view;

public class TotalPointsViewMisc {
    private int oRtgRank;
    private int dRtgRank;
    private int paceRank;

    public TotalPointsViewMisc(int oRtgRank, int dRtgRank, int paceRank) {
        this.oRtgRank = oRtgRank;
        this.dRtgRank = dRtgRank;
        this.paceRank = paceRank;
    }

    public int getoRtgRank() {
        return oRtgRank;
    }

    public int getdRtgRank() {
        return dRtgRank;
    }

    public int getPaceRank() {
        return paceRank;
    }

}
