package edu.gatech.juniordesign.juniordesignpart2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class BusinessListActivity extends AppCompatActivity {

    private static BusinessListRetrieval mAuthTask = null;
    private static DatabaseModel model;
    private ArrayList<BusinessListItem> businesses = null;
    private PrefixTree b_tree = null;
    private PrefixTree sub_tree = null;
    private ArrayList<BusinessListItem> curSubset = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);
        DatabaseModel.checkInitialization();
        model = DatabaseModel.getInstance();
        mAuthTask = new BusinessListRetrieval();
        try {
            boolean success = mAuthTask.execute((Void) null).get();
            if (success) {
                RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                this.businesses = model.getBusinessList();
                this.curSubset = this.businesses;
                this.b_tree = new PrefixTree();
                this.sub_tree = new PrefixTree();
                for (BusinessListItem b : this.businesses) {
                    b_tree.insertWord(b_tree.getRoot(), b);
                }
                /*
                for (BusinessListItem b : this.businesses) {
                    sub_tree.insertSubCat(sub_tree.getRoot(), b);
                }
                */
                this.businesses = sortByRating();
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(BusinessListActivity.this, this.businesses);
                mRecyclerView.setAdapter(adapter);

                Spinner spinner = findViewById(R.id.filterSpinner);
                List<String> list = new ArrayList<String>();
                list.add("FILTER");
                list.add("Top Rated");
                list.add("See All A-Z");
                list.add("See On Map");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if (position == 1) {
                            sortByRating();
                        } else if (position == 2) {
                            sortByAlphabetical();
                        } else if (position == 3) {
                            setUpMapView(businesses);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // do nothing
                    }

                });

                SearchView sv = findViewById(R.id.businessSearchView);
                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        query = query.toUpperCase();
                        ArrayList<BusinessListItem> new_b = search(query);
                        //ArrayList<BusinessListItem> sub_b = subSearch(query);
                        //new_b.addAll(sub_b);
                        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(BusinessListActivity.this, new_b);
                        mRecyclerView.setAdapter(adapter);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        newText = newText.toUpperCase();
                        ArrayList<BusinessListItem> new_b = search(newText);
                        //ArrayList<BusinessListItem> sub_b = subSearch(newText);
                        //new_b.addAll(sub_b);
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

    public void goToSettingsPageActivity(View view) {
        Intent intent = new Intent(this, SettingsPageActivity.class);
        startActivity(intent);
    }

    public void goToProfilePageActivity(View view) {
        Intent intent = new Intent(this, ProfilePageActivity.class);
        startActivity(intent);
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // set up ActionBar with settings and profile icons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settingsButton) {
            goToSettingsPageActivity(getWindow().getDecorView().getRootView());
        }

        if (id == R.id.profilePicButton) {
            goToProfilePageActivity(getWindow().getDecorView().getRootView());
        }
        return super.onOptionsItemSelected(item);
    }

    public class TrieNode {
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

        /**
         * Creating a tree and inserting the subcategory's for the business into the tree
         *
         * @param root Root of the prefix tree being used
         * @param business Name of the business being added
         */
        public void insertSubCat(TrieNode root, BusinessListItem business) {
            ArrayList<String> subcats = business.subcategories;

            while (!subcats.isEmpty()) {
                int length = subcats.get(0).length();
                char[] letters = subcats.get(0).toCharArray();
                TrieNode curNode = root;

                for(int i = 0; i < length; i++) {
                    if (curNode.links.get(letters[i]) == null) {
                        curNode.links.put(letters[i], new TrieNode(letters[i]));
                    }
                    curNode = curNode.links.get(letters[i]);
                }
                curNode.business = business;

                subcats.remove(0);
            }
        }

        /**
         * Searching through the tree for the subcategories
         *
         * @param root Root of the tree being searched
         * @param word String being used as the search criteria
         * @return BusinessListItems that match the search criteria
         */
        public ArrayList<BusinessListItem> findSubs(TrieNode root, String word) {
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
        ArrayList<BusinessListItem> list = this.b_tree.findMatching(this.b_tree.getRoot(), query);
        this.curSubset = list;
        return list;
    }

    private ArrayList<BusinessListItem> subSearch(String query) {
        ArrayList<BusinessListItem> list = this.sub_tree.findSubs(this.sub_tree.getRoot(), query);
        this.curSubset = list;
        return list;
    }

    private ArrayList<BusinessListItem> sortByRating() {
        PriorityQueue<BusinessListItem> pQ = new PriorityQueue<>();
        pQ.addAll(this.curSubset);
        ArrayList<BusinessListItem> ordered = new ArrayList<>();
        while (!pQ.isEmpty()) {
            ordered.add(pQ.remove());
        }
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(BusinessListActivity.this, ordered);
        mRecyclerView.setAdapter(adapter);
        return ordered;
    }

    private ArrayList<BusinessListItem> sortByAlphabetical() {
        ArrayList<BusinessListItem> sortedAlpha = alphaHelper(this.b_tree.getRoot());
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(BusinessListActivity.this, sortedAlpha);
        mRecyclerView.setAdapter(adapter);
        return sortedAlpha;
    }

    private ArrayList<BusinessListItem> alphaHelper(TrieNode root) {
        ArrayList<BusinessListItem> ret = new ArrayList<>();
        if(root.business != null) {
            ret.add(root.business);
        }
        for (TrieNode n : root.links.values()) {
            ret.addAll(alphaHelper(n));
        }
        return ret;
    }

    private void setUpMapView(ArrayList<BusinessListItem> businesses) {
        String[] addresses = new String[businesses.size()];
        int i = 0;
        for (BusinessListItem b : businesses){
            if (b.getAddress() != null && b.getAddress()[0] != null) {
                String[] address = b.getAddress();
                String addr = "";
                for (String s : address) {
                    addr = addr + " " + s;
                }
                addresses[i] = addr;
            }
            i++;
        }
        if (addresses.length == 0) {
            Toast.makeText(getApplicationContext(), "Not Enough Businesses To View On A Map", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent (this, MapsActivity.class);
            model.setAddresses(addresses);
            startActivity(intent);
        }
    }

    private static class BusinessListRetrieval extends AsyncTask<Void, Void, Boolean> {

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
