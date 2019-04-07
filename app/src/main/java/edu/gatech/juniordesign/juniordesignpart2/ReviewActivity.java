package edu.gatech.juniordesign.juniordesignpart2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Rating;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewActivity extends AppCompatActivity {
    Button leave_review_button = null;
    Button cancel = null;
    TextView prompt = null;
    EditText review_title = null;
    EditText review_comments = null;
    RatingBar stars_bar = null;
    DatabaseModel model = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        prompt = findViewById(R.id.reviewSubmitPrompt);
        String p = "Please leave a review for " + model.getSelectedBusinessObject().getName();

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
                int rating = stars_bar.getNumStars();
                String review = review_comments.getText().toString();
                String title = review_title.getText().toString();
                if (rating == 0) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
                    builder1.setMessage("The rating must be between 1 and 5 stars.");
                    builder1.setCancelable(false);

                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else {
                    if (!review.equals("")) {
                        model.submitReview(rating, title, review);
                    } else {
                        model.submitReview(rating);
                    }
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

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            return model.getCategories();
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
