package com.example.bottomnavigationbar;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bottomnavigationbar.ui.main.PageViewModel;
import com.example.bottomnavigationbar.ui.main.PlaceholderFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SectionFragment extends Fragment {

    private static final String ARG_SECTION_NAME = "section_name";
    View view;
    private RecyclerView mRecyclerView;
    private NewsCardAdapter mNewsCardAdapter;
    private ArrayList<NewsCard> card_list;
    String section;



    public static SectionFragment newInstance(String section) {
        SectionFragment fragment = new SectionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SECTION_NAME, section);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void getData() {
        String url = "http://reactapp4640782493.us-east-1.elasticbeanstalk.com/tabs/guardian/"+section;
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
            section = getArguments().getString(ARG_SECTION_NAME);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_section, container, false);
        mRecyclerView=view.findViewById(R.id.cards_container);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        card_list=new ArrayList<>();
        getData();
        return view;

    }
}
