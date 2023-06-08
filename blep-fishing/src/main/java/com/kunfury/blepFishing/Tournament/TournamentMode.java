package com.kunfury.blepFishing.Tournament;

public enum TournamentMode {
    DAY,
    HOUR;

    private static final TournamentMode[] vals = values();

    public TournamentMode next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}
