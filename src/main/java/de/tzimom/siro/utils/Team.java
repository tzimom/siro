package de.tzimom.siro.utils;

import java.util.UUID;

public class Team {

    public static final byte MAX_NAME_LENGTH = 16;

    private String teamName;
    private UUID[] members;

    public Team(String teamName, UUID[] members) {
        this.teamName = teamName;
        this.members = members;
    }

    public String getTeamName() {
        return teamName;
    }

    public UUID[] getMembers() {
        return members;
    }

}
