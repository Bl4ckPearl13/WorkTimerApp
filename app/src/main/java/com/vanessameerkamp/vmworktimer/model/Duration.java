package com.vanessameerkamp.vmworktimer.model;

public class Duration {

    private int duration;
    private String durationAsString;

    public Duration(int duration) {
        this.duration = duration;
        durationAsString = convert(duration);
    }

    public static String convert(int duration) {
        int seconds = duration % 60;
        int durationInMinutes = (duration - seconds) / 60;
        int minutes = durationInMinutes % 60;
        int hours = (durationInMinutes - minutes) / 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static int parseDuration(String durationAsString) {
        try {
            String[] durationAsArray = durationAsString.split(":");

            int hours = Integer.parseInt(durationAsArray[0]);
            int minutes = Integer.parseInt(durationAsArray[1]);
            int seconds = Integer.parseInt(durationAsArray[2]);

            return seconds + (minutes * 60) + (hours * 60 * 60);

        } catch (NumberFormatException e) {
            System.err.println("Falsches Eingabeformat, parseDuration() nicht möglich");
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String toString() {
        return durationAsString;
    }
}
