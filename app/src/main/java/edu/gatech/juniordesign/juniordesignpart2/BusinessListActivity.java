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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BusinessListActivity extends AppCompatActivity {

    private static BusinessListRetrevial mAuthTask = null;
    private static DatabaseModel model;
    private ArrayList<BusinessListItem> businesses = null;
    private PrefixTree b_tree = null;
    private ArrayList<BusinessListItem> curSubset = null;

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
                this.curSubset = this.businesses;
                this.b_tree = new PrefixTree();
                for (BusinessListItem b : this.businesses) {
                    b_tree.insertWord(b_tree.getRoot(), b);
                }
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
        Intent intent = new Intent (this, MapsActivity.class);
        startActivity(intent);
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
