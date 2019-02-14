package edu.gatech.juniordesign.juniordesignpart2;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BusinessListActivity extends AppCompatActivity {

    private static BusinessListRetrevial mAuthTask = null;
    private static DatabaseModel model;
    private ArrayList<BusinessListItem> businesses = null;
    private PrefixTree b_tree = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);
        DatabaseModel.checkInitialization();
        model = DatabaseModel.getInstance();
        //TODO: Austin needs the category to be saved on the main page this is for debug
        model.setSelectedCategory("Construction"); //Category is Construction for now
        mAuthTask = new BusinessListRetrevial();
        try {
            boolean success = mAuthTask.execute((Void) null).get();
            if (success) {
                RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                this.businesses = model.getBusinessList();
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(BusinessListActivity.this, this.businesses);
                mRecyclerView.setAdapter(adapter);
                this.b_tree = new PrefixTree();
                for (BusinessListItem b : this.businesses) {
                    b_tree.insertWord(b_tree.getRoot(), b);
                }
                SearchView sv = findViewById(R.id.businessSearchView);
                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        query = query.toUpperCase();
                        ArrayList<BusinessListItem> new_b = search(query);
                        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(BusinessListActivity.this, new_b);
                        mRecyclerView.setAdapter(adapter);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        newText = newText.toUpperCase();
                        ArrayList<BusinessListItem> new_b = search(newText);
                        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(BusinessListActivity.this, new_b);
                        mRecyclerView.setAdapter(adapter);
                        return true;
                    }
                });
            }
        } catch (Exception e) {
            Log.e("BusinessList", e.getMessage());
        }

    }

    private class TrieNode {
        char letter;
        HashMap<Character, TrieNode> links;
        BusinessListItem business;

        private TrieNode(char letter) {
            this.letter = letter;
            this.links = new HashMap<>();
            this.business = null;
        }
    }

    public class PrefixTree {
        private TrieNode root;

        public PrefixTree() {
            this.root = createTree();
        }

        private TrieNode createTree() {
            return new TrieNode('\0');
        }

        public TrieNode getRoot() {
            return this.root;
        }

        public void insertWord(TrieNode root, BusinessListItem business) {
            int l = business.name.length();
            char[] letters = business.name.toCharArray();
            TrieNode curNode = root;

            for(int i = 0; i < l; i++) {
                if (curNode.links.get(letters[i]) == null) {
                    curNode.links.put(letters[i], new TrieNode(letters[i]));
                }
                curNode = curNode.links.get(letters[i]);
            }
            curNode.business = business;
        }

        public ArrayList<BusinessListItem> findMatching(TrieNode root, String word) {
            int l = word.length();
            char[] letters = word.toCharArray();
            TrieNode curNode = root;

            for(int i = 0; i < l; i++) {
                if (curNode.links.get(letters[i]) == null) {
                    return null;
                }
                curNode = curNode.links.get(letters[i]);
            }
            if (curNode == null)
                return null;
            return getLeaves(curNode);
        }

        private ArrayList<BusinessListItem> getLeaves(TrieNode root) {
            LinkedList<TrieNode> q = new LinkedList<>();
            ArrayList<BusinessListItem> b = new ArrayList<>();
            q.add(root);

            while (!q.isEmpty()) {
                TrieNode curNode = q.pop();
                if (curNode.business != null) {
                    b.add(curNode.business);
                }
                q.addAll(curNode.links.values());
            }
            return b;
        }
    }

    private ArrayList<BusinessListItem> search(String query) {
        return this.b_tree.findMatching(this.b_tree.getRoot(), query);
    }

    private static class BusinessListRetrevial extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseModel.checkInitialization();
            DatabaseModel model = DatabaseModel.getInstance();
            return model.queryBusinessList();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

}
