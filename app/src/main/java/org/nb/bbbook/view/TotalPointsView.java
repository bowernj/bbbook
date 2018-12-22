package org.nb.bbbook.view;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.nb.bbbook.algo.TotalPointsModel;
import org.nb.bbbook.util.Convert;

public class TotalPointsView {
    private String totalAvg;
    private String totalCourt;
    private Map<String, TotalPointsViewMisc> misc;

    public TotalPointsView(String totalAvg, String totalCourt, Map<String, TotalPointsViewMisc> misc) {
        this.totalAvg = totalAvg;
        this.totalCourt = totalCourt;
        this.misc = misc;
    }

    public static TotalPointsView fromModel(TotalPointsModel model) {

        return new TotalPointsView(
            Convert.decimalFormat().format(model.getTotalAvg()),
            Convert.decimalFormat().format(model.getTotalCourt()),
            ImmutableMap.of(
                model.getHome(),
                new TotalPointsViewMisc(
                    model.getHomeORtgRank(),
                    model.getHomeDRtgRank(),
                    model.getHomePaceRank()),
                model.getVisitor(),
                new TotalPointsViewMisc(
                    model.getVisitorORtgRank(),
                    model.getVisitorDRtgRank(),
                    model.getVisitorPaceRank())
            )
        );
    }

    public String getTotalAvg() {
        return totalAvg;
    }

    public String getTotalCourt() {
        return totalCourt;
    }

    public Map<String, TotalPointsViewMisc> getMisc() { return misc; }

}
