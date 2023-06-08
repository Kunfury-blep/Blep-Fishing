package com.kunfury.blepFishing.Tournament;

public enum TournamentType {
    LARGEST,
    SMALLEST,
    EXPENSIVE,
    CHEAPEST,
    AMOUNT,
    SCORE;

    private static final TournamentType[] vals = values();

    public TournamentType next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }

    public String getDescription(){

        return switch (this) {
            case LARGEST -> "Largest Catch Wins";
            case SMALLEST -> "Smallest Catch Wins";
            case EXPENSIVE ->  "Most Valuable Fish Wins";
            case CHEAPEST ->  "Least Valuable Fish Wins";
            case AMOUNT ->  "Most Fish Caught Wins";
            case SCORE ->  "Highest Rated Fish Wins";
            default -> "";
        };
    }
}
