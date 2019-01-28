package edu.gatech.juniordesign.juniordesignpart2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private LayoutInflater mInflater;
    private final ArrayList<String> mCategoryList;
    //private final ListItemClickListener mOnClickListener;

    public CategoryAdapter(Context context, ArrayList<String> categories) {
        mInflater = LayoutInflater.from(context);
        this.mCategoryList = categories;
        //this.mOnClickListener = onClickListener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        String mCurrentCategory = mCategoryList.get(position);
        holder.categoryItemView.setText(mCurrentCategory);
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        public final TextView categoryItemView;
        final CategoryAdapter mAdapter;

        public CategoryViewHolder(View itemView, CategoryAdapter adapter) {
            super(itemView);
            categoryItemView = (TextView) itemView.findViewById(R.id.category);
            this.mAdapter = adapter;
            //itemView.setOnClickListener(this);
        }

        /**
        @Override
        public void onClick(View view) {
            int mPosition = getLayoutPosition();

        }
        */
    }
    /**
    public interface ListItemClickListener {
        void onItemClick(int position);
    }
     */
}
