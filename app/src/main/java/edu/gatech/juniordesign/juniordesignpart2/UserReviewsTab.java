package edu.gatech.juniordesign.juniordesignpart2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class UserReviewsTab extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_reviews_tab, container, false);
        ListView lstItems = (ListView)view.findViewById(R.id.user_reviews);

        //TODO get a list of the user's reviews here
        ArrayList<String> reviews = new ArrayList<String>();

        ArrayAdapter<String> allItemsAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1,reviews);

        lstItems.setAdapter(allItemsAdapter);

        return view;
    }
}