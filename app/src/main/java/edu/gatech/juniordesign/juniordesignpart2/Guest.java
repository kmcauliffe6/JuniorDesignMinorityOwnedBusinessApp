package edu.gatech.juniordesign.juniordesignpart2;

public class Guest {
    private static boolean isGuestUser;

    public Guest() {

    }

    public static void setGuestUser(boolean isGuest) {
        isGuestUser = isGuest;
    }

    public static boolean isGuestUser() {
        return isGuestUser;
    }
}
