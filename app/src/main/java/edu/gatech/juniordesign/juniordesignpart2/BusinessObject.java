package edu.gatech.juniordesign.juniordesignpart2;

import java.util.ArrayList;

public class BusinessObject {
    private int iD;
    private String name;
    private String category;
    private String rating;
    private String extraDetails;
    private String aboutTheOwner;
    private ArrayList<String> reviews;
    //will need to store a profile photo here but I dont think we have any photots yet

    public BusinessObject(int iD, String name, String category, String rating, String extraDetails, String
                          aboutTheOwner, ArrayList<String> reviews) {
        this.iD = iD;
        this.name = name;
        this.category = category;
        this.rating = rating;
        this.extraDetails = extraDetails;
        this.aboutTheOwner = aboutTheOwner;
        this.reviews = reviews;
    }

    public int getID() {
        return this.iD;
    }

    public String getName() {
        return this.name;
    }

    public String getCategory() {
        return this.category;
    }

    public String getRating() {
        return this.rating;
    }

}
