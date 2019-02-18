package edu.gatech.juniordesign.juniordesignpart2;

import android.os.AsyncTask;
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

    private static DatabaseModel model;
    private static UserFavoritesTab.BusinessDetailRetrieval mAuthTask = null;
    private static BusinessObject b_o;

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
                    String businessName = getBusinessName(Integer.parseInt(line.trim()));
                    if (businessName != null) {
                        favorites.add(businessName);
                    }
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

    private String getBusinessName(int businessID) {
        DatabaseModel.checkInitialization();
        model = DatabaseModel.getInstance();

        //get the businessID of the selected business
        //set the model businessSelected to businessID
        model.setSelectedBusiness(businessID);
        mAuthTask = new UserFavoritesTab.BusinessDetailRetrieval();
        try {
            boolean success = mAuthTask.execute((Void) null).get();
            if (success) {
                b_o = model.getSelectedBusinessObject();
                return b_o.getName();
            }
        } catch (Exception e) {
            Log.e("BusinessDetails", e.getMessage());

        }
        return null;
    }

    private static class BusinessDetailRetrieval extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            return model.queryBusinessDetails();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

}