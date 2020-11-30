package com.example.bottomnavigationbar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class CardsFragment extends Fragment {
    View view;
    private RecyclerView mRecyclerView;
    private NewsCardAdapter mNewsCardAdapter;
    private ArrayList<NewsCard> card_list;
    private RequestQueue mRequestQueue;


    private void getData() {
        String url = "http://reactapp4640782493.us-east-1.elasticbeanstalk.com/home/topHeadlines";
        JsonArrayRequest jor=new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("check",response.toString());
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_cards, container, false);
        mRecyclerView=view.findViewById(R.id.cards_container);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        card_list=new ArrayList<>();
        getData();

        return view;
    }
}
