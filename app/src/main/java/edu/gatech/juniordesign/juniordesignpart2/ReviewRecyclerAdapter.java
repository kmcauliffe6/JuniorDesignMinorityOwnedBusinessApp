package edu.gatech.juniordesign.juniordesignpart2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ReviewViewHolder>{
    private ArrayList<Review> itemList;
    private Context context;
    private RecyclerView mRecyclerView;
    private static int rev_id;
    private static float rev_rating;
    private static String rev_business;
    private static UserReviewDeletion mAuthTask = null;

    public ReviewRecyclerAdapter(Context context, ArrayList<Review> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    /**This method creates the recycler view
     *
     * @param viewGroup view group to be used in our custom holder
     * @param i parameter for onCreateViewHolder method, not used
     * @return our custom view holder for display
     */
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_recycler_view_row_layout, null);
        mRecyclerView = (RecyclerView) viewGroup;
        return new ReviewRecyclerAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder customViewHolder, int i) {
        Review item = itemList.get(i);
        String title = item.getTitle();
        int id = item.getRev_id();
        rev_id = id;
        customViewHolder.textTitle.setText(title);
        float rating = item.getRating();
        rev_rating = rating;
        String business = item.getBusiness();
        rev_business = business;
        customViewHolder.stars.setRating(rating);
        String rating_str = Float.toString(rating);
        int len = rating_str.length();
        if (len > 4) {
            len = 4;
            rating_str = rating_str.substring(0, len);
        } else if (len == 3) {
            rating_str = rating_str.concat("0");
        } else if (len == 2) {
            rating_str = rating_str.concat("00");
        } else {
            rating_str = rating_str.concat(".00");
        }
        String rating_text = rating_str + " / 5.00";
        customViewHolder.textRating.setText(rating_text);
        String review = item.getReview();
        customViewHolder.textReview.setText(review);
        if (id != 0) {
            customViewHolder.delete.setVisibility(View.VISIBLE);
            customViewHolder.delete.setEnabled(true);
            customViewHolder.delete.setOnClickListener(new MyOnClickListener());
        }

    }

    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private TextView textReview;
        private TextView textRating;
        private RatingBar stars;
        private Button delete;

        private ReviewViewHolder(View view) {
            super(view);
            this.textTitle = view.findViewById(R.id.rev_title);
            this.textReview = view.findViewById(R.id.rev_review);
            this.textRating = view.findViewById(R.id.rev_rating_text);
            this.stars = view.findViewById(R.id.rev_rating);
            this.delete = view.findViewById(R.id.user_review_delete);
        }
    }

    private void doDeletion() {
        mAuthTask = new UserReviewDeletion();
        try {
            boolean success = mAuthTask.execute((Void) null).get();
        } catch (Exception e) {
            Log.e("RemoveUserReview", e.getMessage());
        }
        Intent intent = new Intent(context, ProfilePageActivity.class);
        context.startActivity(intent);
    }

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(final View view) {
            doDeletion();
        }

    }

    private static class UserReviewDeletion extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            return model.deleteUserReview(rev_id, rev_rating, rev_business);
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