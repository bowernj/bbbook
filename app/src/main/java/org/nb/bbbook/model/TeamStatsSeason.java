package org.nb.bbbook.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("tss")
public class TeamStatsSeason {
    @Id
    private String name;
    private TeamMisc teamMisc;

    public TeamStatsSeason(String name, TeamMisc teamMisc) {
        this.name = name;
        this.teamMisc = teamMisc;
    }

    public String getName() {
        return name;
    }

    public TeamMisc getTeamMisc() {
        return teamMisc;
    }

}
