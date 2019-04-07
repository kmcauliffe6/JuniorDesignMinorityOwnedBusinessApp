package edu.gatech.juniordesign.juniordesignpart2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Rating;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewActivity extends AppCompatActivity {
    Button leave_review_button = null;
    Button cancel = null;
    TextView prompt = null;
    EditText review_title = null;
    EditText review_comments = null;
    RatingBar stars_bar = null;
    DatabaseModel model = null;

    private static ReviewActivity.ReviewSubmitter mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        prompt = findViewById(R.id.reviewSubmitPrompt);


        leave_review_button = findViewById(R.id.reviewSubmitButton);
        cancel = findViewById(R.id.reviewCancelButton);
        review_title = findViewById(R.id.reviewSubmitTitle);
        review_comments = findViewById(R.id.reviewSubmitEditText);
        stars_bar = findViewById(R.id.reviewSubmitRatingBar);
        DatabaseModel.checkInitialization();
        model = DatabaseModel.getInstance();
        ReviewActivity cur = this;


        leave_review_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = stars_bar.getRating();
                String review = review_comments.getText().toString();
                String title = review_title.getText().toString();
                Log.i("leaveReviewButton", Float.toString(rating));
                mAuthTask = new ReviewSubmitter(rating, title, review);
                try {
                    boolean success = mAuthTask.execute((Void) null).get();

                    Log.i("ReviewSubmitter", "sucess: " + success);
                    success = true;
                    if (success) {
                        Log.i("ReviewActivity", "onPostExecute Success");
                        //after registration the user is taken to the home page
                        Intent intent = new Intent(cur, BusinessDetailPageActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Context context = getApplicationContext();
                        CharSequence text = "  Error Occurred.\n Please Try Again.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                } catch (Exception e) {
                    Log.e("attemptReview", e.getMessage(), e);
                }
            }
        });

        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cur, BusinessDetailPageActivity.class);
                startActivity(intent);
            }
        });
    }

    private static class ReviewSubmitter extends AsyncTask<Void, Void, Boolean> {

        private final String review;
        private final String title;
        private final float rating;

        ReviewSubmitter(float rating, String title, String review) {
            this.review = review;
            this.rating = rating;
            this.title = title;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            boolean ret;
            if (!review.equals("")) {
                ret = model.submitReview(rating, title, review);
            } else {
                ret = model.submitReview(rating);
            }
            Log.i("ReviewSubmitter", "result: " + ret);
            return ret;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
        }

        @Override
        protected void onCancelled() {
        }
    }
}
