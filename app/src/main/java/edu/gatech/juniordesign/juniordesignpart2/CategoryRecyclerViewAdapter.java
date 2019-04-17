package edu.gatech.juniordesign.juniordesignpart2;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.CustomViewHolder> {
    private ArrayList<String> itemList;
    private Context context;
    private Map<Integer, String> id_map;
    private RecyclerView mRecyclerView;

    public CategoryRecyclerViewAdapter(Context context, ArrayList<String> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.id_map = new HashMap<>();
        if (itemList != null) {
            for (int i = 0; i < itemList.size(); i++) {
                this.id_map.put(i, itemList.get(i));
            }
        }
    }
    /**This method creates the recycler view
     *
     * @param viewGroup view group to be used in our custom holder
     * @param i parameter for onCreateViewHolder method, not used
     * @return our custom view holder for display
     */
    @Override
    public CategoryRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_recycler_view_layout, null);
        mRecyclerView = (RecyclerView) viewGroup;
        return new CategoryRecyclerViewAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryRecyclerViewAdapter.CustomViewHolder customViewHolder, int i) {
        String item = itemList.get(i);
        //Setting text view title
        //customViewHolder.textTitle.setText(item);
        if (item.equals("Construction")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.construction, null));
        } else if (item.equals("Grocery")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.grocery, null));
        } else if (item.equals("Bank")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.bank, null));
        } else if (item.equals("Accounting")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.accountant, null));
        } else if (item.equals("Financial Consulting")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.financial_consulting, null));
        } else if (item.equals("Legal")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.legal, null));
        } else if (item.equals("Marketing")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.marketing, null));
        } else if (item.equals("Shopping")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.shopping, null));
        } else if (item.equals("Restaurant")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.restaurants, null));
        } else if (item.equals("Printing")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.printing, null));
        } else if (item.equals("Manufacturing")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.manufacturing, null));
        } else if (item.equals("Publisher")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.publisher, null));
        } else if (item.equals("Attorney")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.attorney, null));
        } else if (item.toLowerCase().equals("non-profit")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.non_profit, null));
        } else if (item.equals("Media")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.media, null));
        } else if (item.equals("Insurance")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.insurance, null));
        } else if (item.equals("Service")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.services, null));
        } else if (item.equals("SEE ALL")) {
            customViewHolder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.see_all, null));
        }
        customViewHolder.itemView.setOnClickListener(new CategoryRecyclerViewAdapter.MyOnClickListener());
    }

    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;

        private CustomViewHolder(View view) {
            super(view);
            this.textTitle = view.findViewById(R.id.title);
        }
    }

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(final View view) {
            int itemPosition = mRecyclerView.getChildLayoutPosition(view);
            String categoryName = id_map.get(itemPosition);
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            model.setSelectedCategory(categoryName);
            Log.i("Main", categoryName);
            Intent intent = new Intent (context, BusinessListActivity.class);
            intent.putExtra("category", categoryName);
            context.startActivity(intent);
        }

    }
}
