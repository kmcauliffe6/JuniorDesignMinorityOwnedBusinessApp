package edu.gatech.juniordesign.juniordesignpart2;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class BusinessListItem implements Comparable<BusinessListItem> {

    int id;
    String name;
    ArrayList<String> subcategories;
    String rating;
    String[] address;

    public BusinessListItem(int id, String name, String rating, String[] address,String... subcategories)
    {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.subcategories = new ArrayList<>();
        this.address = address;

        for (String subcategory:subcategories)
        {
          this.subcategories.add(subcategory);
        }
    }

    @Override
    public int compareTo(@NonNull BusinessListItem businessListItem) {
        float dif = Float.parseFloat(this.rating) - Float.parseFloat(businessListItem.rating);
        if (dif < 0) {
            return -1;
        } else if (dif == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<String> getSubcategories() {
        return this.subcategories;
    }

    public ArrayList<String> getSubcategoriesForList() {
        //TODO fix this
        if (this.subcategories.size() < 4) {
            return this.subcategories;
        } else {
            ArrayList<String> top3 = new ArrayList<>();
            top3.add(this.subcategories.get(0));
            top3.add(this.subcategories.get(1));
            top3.add(this.subcategories.get(2));
            return top3;
        }
    }

    public String getRating() {
        return this.rating;
    }
    public String[] getAddress() {
        return address;
    }
}
