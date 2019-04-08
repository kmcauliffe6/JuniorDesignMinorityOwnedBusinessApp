package edu.gatech.juniordesign.juniordesignpart2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ReviewTab extends Fragment {

    private static DatabaseModel model;
    private static ReviewListRetrieval mAuthTask = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_tab, container, false);
        RecyclerView mRecyclerView = view.findViewById(R.id.review_recycler_view);
        DatabaseModel.checkInitialization();
        model = DatabaseModel.getInstance();
        mAuthTask = new ReviewListRetrieval();
        try {
            boolean success = mAuthTask.execute((Void) null).get();
            if (success) {
                Log.i("ReviewListRetrieval", "Yay");
            } else {
                Log.i("ReviewListRetrieval", "Boo");
            }
        } catch (Exception e) {
            Log.e("ReviewListRetrieval", e.getMessage());
        }

        //TODO get a list of the business's reviews here
        ArrayList<Review> reviews = model.getReviewsForSelected();

        ReviewRecyclerAdapter adapter = new ReviewRecyclerAdapter(getContext(), reviews);
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    private static class ReviewListRetrieval extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            return model.queryReviewList();
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

