package edu.gatech.juniordesign.juniordesignpart2;

import java.util.ArrayList;

public class BusinessListItem {

    int id;
    String name;
    ArrayList<String> subcategories;
    String rating;

    public BusinessListItem(int id, String name, String rating, String... subcategories)
    {
        this.id = id;
        this.name = name;
        this.rating = rating;

        for (String subcategory:subcategories)
        {
          this.subcategories.add(subcategory);
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
        return this.subcategories;
    }

    public String getRating() {
        return this.rating;
    }
}
