package edu.gatech.juniordesign.juniordesignpart2;

public class Review {
    private String title;
    private String review;
    private float rating;

    public Review(String title, String review, float rating) {
        this.title = title;
        this.review = review;
        this.rating = rating;
    }

    public String getTitle() {
        return this.title;
    }

    public String getReview() {
        return this.review;
    }

    public float getRating() {
        return this.rating;
    }
}
