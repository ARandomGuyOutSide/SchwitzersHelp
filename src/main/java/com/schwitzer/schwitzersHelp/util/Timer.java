package com.schwitzer.schwitzersHelp.util;

public class Timer {

    public static int milisecondsToTicks(int milliseconds) {
        // Minecraft runs at 20 ticks per second
        // So 1000ms = 1 second = 20 ticks
        int ticks = (milliseconds * 20) / 1000;
        double seconds = milliseconds / 1000.0;
        Chat.debugMessage("Waiting for " + seconds + " seconds (" + ticks + " ticks)");
        return ticks;
    }

    public static int secondsToTicks(int seconds) {
        int ticks = seconds * 20;
        Chat.debugMessage("Waiting for " + seconds + " seconds (" + ticks + " ticks)");
        return ticks;
    }
}