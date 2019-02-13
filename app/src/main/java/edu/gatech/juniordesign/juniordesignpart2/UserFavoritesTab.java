package edu.gatech.juniordesign.juniordesignpart2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class UserFavoritesTab extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_favorites_tab, container, false);

        ListView lstItems = (ListView)view.findViewById(R.id.user_favorites);


        ArrayList<String> favorites = new ArrayList<String>();
        if (Guest.isGuestUser()) {
            String yourFilePath = getActivity().getFilesDir() + "/" + "guest_favorites";
            File favoritesFile = new File(yourFilePath);
            try {
                Scanner scanner = new Scanner(favoritesFile);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    favorites.add("Business " + line);
                }
            } catch (FileNotFoundException e) {
                Log.e("Guest Saves", "File Not Found");
            }
        } else {
            //TODO get a list of the current user's favorites here
            favorites.add("Business 1");
            favorites.add("Business 2");
        }


        ArrayAdapter<String> allItemsAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1,favorites);

        lstItems.setAdapter(allItemsAdapter);
        return view;
    }
}