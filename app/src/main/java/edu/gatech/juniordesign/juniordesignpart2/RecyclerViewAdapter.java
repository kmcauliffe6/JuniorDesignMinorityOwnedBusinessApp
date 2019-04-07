package edu.gatech.juniordesign.juniordesignpart2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>{
    private ArrayList<BusinessListItem> itemList;
    private Context context;
    private Map<Integer, Integer> id_map;
    private RecyclerView mRecyclerView;

    public RecyclerViewAdapter(Context context, ArrayList<BusinessListItem> itemList) {
        this.itemList = itemList;
        this.id_map = new HashMap<>();
        if (itemList != null) {
            for (int i = 0; i < itemList.size(); i++) {
                this.id_map.put(i, itemList.get(i).getId());
            }
        }
        this.context = context;
    }

    /**This method creates the recycler view
     *
     * @param viewGroup view group to be used in our custom holder
     * @param i parameter for onCreateViewHolder method, not used
     * @return our custom view holder for display
     */
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_row_layout, null);
        mRecyclerView = (RecyclerView) viewGroup;
        return new RecyclerViewAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        BusinessListItem item = itemList.get(i);
        String info = item.getName();
        //Setting text view title
        customViewHolder.textTitle.setText(info);
        String subcats = item.getSubcategoriesForList().get(0);
        for (int x = 1; x < item.getSubcategoriesForList().size(); x++) {
            subcats += ", " + item.getSubcategoriesForList().get(x);
        }
        customViewHolder.textSubcats.setText(subcats);
        String rating_str = item.getRating();
        float rating_num;
        customViewHolder.textRating.setText(rating_str);
        try {
            rating_num = Float.parseFloat(rating_str);
        } catch(NumberFormatException ex) {
            rating_num = (float)0.0;
        }
        customViewHolder.stars.setRating(rating_num);
        customViewHolder.itemView.setOnClickListener(new MyOnClickListener());
    }

    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private TextView textSubcats;
        private TextView textRating;
        private RatingBar stars;

        private CustomViewHolder(View view) {
            super(view);
            this.textTitle = view.findViewById(R.id.title);
            this.textSubcats = view.findViewById(R.id.subcategories);
            this.textRating = view.findViewById(R.id.rating);
            this.stars = view.findViewById(R.id.stars);
        }
    }

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(final View view) {
            int itemPosition = mRecyclerView.getChildLayoutPosition(view);
            //TODO get business data for business selected and go to business detail page
            int business_id = id_map.get(itemPosition);
            Intent intent = new Intent (context, BusinessDetailPageActivity.class);
            DatabaseModel.getInstance().setBusiness_id(business_id);
            context.startActivity(intent);
        }

    }
}
