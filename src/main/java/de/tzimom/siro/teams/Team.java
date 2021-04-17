package de.tzimom.siro.teams;

import java.util.UUID;

public class Team {

    public static final byte MAX_NAME_LENGTH = 4;

    private String teamName;
    private UUID member1;
    private UUID member2;

    public Team(String teamName, UUID member1, UUID member2) {
        this.teamName = teamName;
        this.member1 = member1;
        this.member2 = member2;
    }

}
