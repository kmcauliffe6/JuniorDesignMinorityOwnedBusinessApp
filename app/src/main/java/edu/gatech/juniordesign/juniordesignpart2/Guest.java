package edu.gatech.juniordesign.juniordesignpart2;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Guest {
    private static boolean isGuestUser;



    public Guest() {

    }

    /**
     * Setter for isGuestUser
     * @param isGuest boolean for whether an user is a guest (not logged in)
     */
    public static void setGuestUser(boolean isGuest) {
        Log.i("guest", "guest set");
        isGuestUser = isGuest;
    }

    public static boolean isGuestUser() {
        return isGuestUser;
    }


    /**
     * This method adds a guest favorite to internal storage
     * on the user's phone when the heart button is pressed
     * @param context Activity Context
     * @param businessID the id of the business to be removed
     */
    public void saveGuestFavorite(Context context, int businessID) {
        String fileName = context.getFilesDir() + "/" + "guest_favorites";
        String fileContents = Integer.toString(businessID);

        try {
            FileWriter fp = new FileWriter(fileName, true);
            fp.write(fileContents + "\n");
            fp.close();
        } catch (IOException e) {
            // Error while creating file
            Log.e("Favorite Saves For Guests", "IOException in saveGuestFavorite");
        }

    }


    /**
     * This method removes a guest favorite when the heart button is unpressed
     * @param context Activity Context
     * @param businessID the id of the business to be removed
     */
    public void removeGuestFavorite(Context context, int businessID) {
        String yourFilePath = context.getFilesDir() + "/" + "guest_favorites";
        File temp = new File(context.getFilesDir() + "/" + "temp.txt");
        File favoritesFile = new File(yourFilePath);
        try {
            FileWriter fp = new FileWriter(temp, true);
            Scanner scanner = new Scanner(favoritesFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.compareTo(Integer.toString(businessID)) == 0) {
                    //do nothing bc we don't want to add this line
                } else {
                    try {
                        fp.write(line + "\n");
                        fp.close();
                    } catch (IOException i) {
                        Log.e("Guest Saves", "IOException in removeGuestFavorite()");
                    }
                }
            }
        } catch(FileNotFoundException e) {
            Log.e("Guest Saves", "FileNotFound in removeGuestFavorite() ");
        } catch (IOException i) {
            Log.e("Guest Saves", "IOException in removeGuestFavorite()");
        }
        temp.renameTo(favoritesFile);
    }



}
