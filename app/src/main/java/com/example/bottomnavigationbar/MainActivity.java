package com.example.bottomnavigationbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bottomnavigationbar.ui.main.SectionsPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

//    private Toolbar myToolBar;
//    private SwipeRefreshLayout mSwipeRefreshLayout;
    WeatherInfo weather_fragment;
    ArrayList<String> word_list;
    AutoSuggestAdapter autoSuggestAdapter;
//    Handler handler;
//    private static final int TRIGGER_AUTO_COMPLETE = 100;
//    private static final long AUTO_COMPLETE_DELAY = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottome_navigation);

        if(savedInstanceState==null){
            WeatherInfo weather_fragment=new WeatherInfo();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,weather_fragment).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.cards_container,new CardsFragment()).commit();

        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment=null;
                if(getSupportFragmentManager().findFragmentById(R.id.other_container) != null) {
                    getSupportFragmentManager()
                            .beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.other_container)).commit();
                }
                if(getSupportFragmentManager().findFragmentById(R.id.main_container) != null) {
                    getSupportFragmentManager()
                            .beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.main_container)).commit();
                }
                if(getSupportFragmentManager().findFragmentById(R.id.cards_container) != null) {
                    getSupportFragmentManager()
                            .beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.cards_container)).commit();
                }



                switch (menuItem.getItemId()){
                    case R.id.home:
                        WeatherInfo weather_fragment=new WeatherInfo();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,weather_fragment).commit();
                        fragment=new CardsFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.cards_container,fragment).commit();

                        break;
                    case R.id.headlines:
                        fragment=new HeadlinesFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,fragment).commit();

                        break;
                    case R.id.trending:
                        fragment=new TrendingFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.other_container,fragment).commit();


                        break;
                    case R.id.bookmarks:
                        fragment=new BookmarksFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,fragment).commit();

                        break;

                }

                return true;
            }
        });

    }

    public void getData(String keyword){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        Log.d("keyword",keyword);
        word_list = new ArrayList<String>();

        final String url = "https://api.cognitive.microsoft.com/bing/v7.0/suggestions?q="+keyword;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        //Log.d("Response", response);
                        try {
                            JSONArray suggestion_groups=response.getJSONArray("suggestionGroups");
                            JSONArray search_suggestions=suggestion_groups.getJSONObject(0).getJSONArray("searchSuggestions");
                            for (int i=0;i<search_suggestions.length();i++){
                                if(i>4){
                                    break;
                                }
                                String word=search_suggestions.getJSONObject(i).getString("displayText");
                                word_list.add(word);

                                //Log.d("word",word);
                            }
                            autoSuggestAdapter.setData(word_list);
                            autoSuggestAdapter.notifyDataSetChanged();
                        } catch ( JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Ocp-Apim-Subscription-Key","84472829b76348c89d9965280a3e8fa7");

                return params;
            }


            @Override
            public RetryPolicy getRetryPolicy() {
                return super.getRetryPolicy();
            }
        };

        queue.add(getRequest);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");



        //final String dataArr[] = {"Apple", "Amazon", "Amd", "Microsoft", "Microwave", "MicroNews", "Intel", "Intelligence"};
        //final ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line);
//       searchAutoComplete.setAdapter(newsAdapter);

        final AutoCompleteTextView autoCompleteTextView = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutoSuggestAdapter(MainActivity.this, android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(3);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Log.d("selected",autoSuggestAdapter.getObject(position).toString());
                        autoCompleteTextView.setText(autoSuggestAdapter.getObject(position));
                    }
                });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString = (String) adapterView.getItemAtPosition(itemIndex);
               autoCompleteTextView.setText("" + queryString);
               Fragment fragment=null;
                fragment=SearchFragment.newInstance(queryString);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,fragment).commit();
                if(getSupportFragmentManager().findFragmentById(R.id.cards_container) != null) {
                    getSupportFragmentManager()
                            .beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.cards_container)).commit();
                }if(getSupportFragmentManager().findFragmentById(R.id.other_container) != null) {
                    getSupportFragmentManager()
                            .beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.other_container)).commit();
                }
                //Toast.makeText(MainActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
            }
        });

//        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int
//                    count, int after) {
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//
//                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
//                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
//                        AUTO_COMPLETE_DELAY);
//            }
//
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//
//        handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                if (msg.what == TRIGGER_AUTO_COMPLETE) {
//                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
//                       getData(autoCompleteTextView.getText().toString());
//                    }
//                }
//                return false;
//            }
//        });
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Fragment fragment=null;
                    fragment=SearchFragment.newInstance(query);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container,fragment).commit();
                    if(getSupportFragmentManager().findFragmentById(R.id.cards_container) != null) {
                        getSupportFragmentManager()
                                .beginTransaction().
                                remove(getSupportFragmentManager().findFragmentById(R.id.cards_container)).commit();
                    }
                    if(getSupportFragmentManager().findFragmentById(R.id.other_container) != null) {
                        getSupportFragmentManager()
                                .beginTransaction().
                                remove(getSupportFragmentManager().findFragmentById(R.id.other_container)).commit();
                    }
//                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//                    alertDialog.setMessage("Search keyword is " + query);
//                    alertDialog.show();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    getData(newText);
    //                else{
    //                    autoSuggestAdapter.setData(new ArrayList<String>());
    //                    autoSuggestAdapter.notifyDataSetChanged();
    //                }
    //                Log.d(newText,"hey");
    //                 SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById
    //                        (androidx.appcompat.R.id.search_src_text);
    //                ArrayAdapter<String> myWordsAdapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_dropdown_item_1line,word_list);
    //                searchAutoComplete.setAdapter(myWordsAdapter);

                    return true;
                }
        });
        return super.onCreateOptionsMenu(menu);
    }




}
