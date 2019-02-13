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
        this.subcategories = new ArrayList<>();

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
}
