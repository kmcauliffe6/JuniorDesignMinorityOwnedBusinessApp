package edu.gatech.juniordesign.juniordesignpart2;

import java.util.ArrayList;

public class BusinessObject {
    private String name;
    private String category;
    private String rating;
    private String extraDetails;
    private String aboutTheOwner;
    private ArrayList<String> reviews;
    //will need to store a profile photo here but I dont think we have any photots yet

    public BusinessObject(String name, String category, String rating, String extraDetails, String
                          aboutTheOwner, ArrayList<String> reviews) {
        this.name = name;
        this.category = category;
        this.rating = rating;
        this.extraDetails = extraDetails;
        this.aboutTheOwner = aboutTheOwner;
        this.reviews = reviews;
    }

}
