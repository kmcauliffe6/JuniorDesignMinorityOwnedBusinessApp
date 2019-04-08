package edu.gatech.juniordesign.juniordesignpart2;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class UserReviewsTab extends Fragment {
    DatabaseModel model = null;
    static UserReviewRetrieval mAuthTask = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_tab, container, false);
        RecyclerView mRecyclerView = view.findViewById(R.id.review_recycler_view);
        DatabaseModel.checkInitialization();
        model = DatabaseModel.getInstance();
        if (model.getCurrentUser() == null) {
            SharedPreferences shared = getContext().getSharedPreferences("login", MODE_PRIVATE);
            User currentUser = new User(
                    shared.getString("email", ""),
                    shared.getString("firstName", ""),
                    shared.getString("lastName", ""),
                    shared.getBoolean("admin", false),
                    shared.getString("entity", ""));
            model.setCurrentUser(currentUser);
        }
        mAuthTask = new UserReviewRetrieval();
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
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private static class UserReviewRetrieval extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            return model.queryReviewListUser();
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