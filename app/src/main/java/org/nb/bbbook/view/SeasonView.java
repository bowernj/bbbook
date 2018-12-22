package org.nb.bbbook.view;

import java.util.List;

public class SeasonView {
    List<FourFactorsView> fourFactors;

    public SeasonView(List<FourFactorsView> fourFactors) {
        this.fourFactors = fourFactors;
    }

    public List<FourFactorsView> getFourFactors() {
        return fourFactors;
    }

}
