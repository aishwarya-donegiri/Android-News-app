package com.example.bottomnavigationbar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    View view;
    private RecyclerView mRecyclerView;
    private NewsCardAdapter mNewsCardAdapter;
    private ArrayList<NewsCard> card_list;
    String key;




    public static SearchFragment newInstance(String key) {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void getData(String key) {
        String url = "http://reactapp4640782493.us-east-1.elasticbeanstalk.com/guardianSearch?q="+key;
        JsonArrayRequest jor=new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("check",response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject article = response.getJSONObject(i);
                                String id=article.getString("id");
                                String title=article.getString("title");
                                String imageUrl=article.getString("image");
                                String date=article.getString("date");
                                String section=article.getString("section");
                                String shareUrl=article.getString("shareUrl");

                                card_list.add(new NewsCard(id,title,imageUrl,section,date,shareUrl));
                            }

                            mNewsCardAdapter=new NewsCardAdapter(getActivity().getApplicationContext(),card_list);
                            mRecyclerView.setAdapter(mNewsCardAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ssss","hhhhhhhhhh");
                error.printStackTrace();
            }
        });

        RequestQueue queue= Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(jor);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);

        if (getArguments() != null) {
            key = getArguments().getString("key");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_cards, container, false);
        mRecyclerView=view.findViewById(R.id.cards_container);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        card_list=new ArrayList<>();
        getData(key);
        return view;

    }
}
