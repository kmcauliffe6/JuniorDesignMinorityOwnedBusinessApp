package edu.gatech.juniordesign.juniordesignpart2;

public class Review {
    private String title;
    private String review;
    private float rating;
    private String business;

    public Review(String title, String review, float rating) {
        this.title = title;
        this.review = review;
        this.rating = rating;
    }

    public Review(String title, String review, float rating, String business) {
        this.title = title;
        this.review = review;
        this.rating = rating;
        this.business = business;
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

    public String getBusiness() { return this.business; }
}
