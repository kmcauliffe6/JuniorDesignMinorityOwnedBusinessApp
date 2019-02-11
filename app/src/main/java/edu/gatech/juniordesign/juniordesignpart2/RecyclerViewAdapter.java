package edu.gatech.juniordesign.juniordesignpart2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>{
    private List<String[]> itemList;
    private Context context;
    private RecyclerView mRecyclerView;

    public RecyclerViewAdapter(Context context, List<String[]> itemList) {
        this.itemList = itemList;
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
        String[] item = itemList.get(i);
        String info = item[0] + item[1];
        //Setting text view title
        customViewHolder.textTitle.setText(info);
        customViewHolder.textRating.setText(item[2]);
        customViewHolder.itemView.setOnClickListener(new MyOnClickListener());
    }

    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView textTitle;
        protected TextView textRating;

        public CustomViewHolder(View view) {
            super(view);
            this.textTitle = view.findViewById(R.id.title);
            this.textRating = view.findViewById(R.id.rating);
        }
    }

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(final View view) {
            int itemPosition = mRecyclerView.getChildLayoutPosition(view);
            //TODO get business data for business selected and go to business detail page
        }

    }
}
