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

    public static void setGuestUser(boolean isGuest) {
        Log.i("guest", "guest set");
        isGuestUser = isGuest;
    }

    public static boolean isGuestUser() {
        return isGuestUser;
    }

    //cache guest favorites on their device
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



    //remove a business from cached favorites
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
