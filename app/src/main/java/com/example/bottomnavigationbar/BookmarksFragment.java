package com.example.bottomnavigationbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookmarksFragment extends Fragment {
    View view;
    private Context context;
    private RecyclerView mRecyclerView;
    private BookmarkCardAdapter mNewsCardAdapter;
    private ArrayList<NewsCard> card_list;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ArrayList<NewsCard> bookmarksList;


    private void getData(){
        context=getContext();
        pref=context.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        editor=pref.edit();

        Map<String,String> allBookmarks=(Map<String,String>) pref.getAll();

        Gson gson=new Gson();




        for (Map.Entry<String,String>entry:allBookmarks.entrySet()) {
            bookmarksList.add(gson.fromJson(entry.getValue(), NewsCard.class));

        }mNewsCardAdapter=new BookmarkCardAdapter(getActivity().getApplicationContext(),bookmarksList);
        mRecyclerView.setAdapter(mNewsCardAdapter);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        bookmarksList = new ArrayList<>();
        view=inflater.inflate(R.layout.fragment_bookmarks, container, false);
        mRecyclerView=view.findViewById(R.id.cards_container);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),2));
        card_list=new ArrayList<>();
        getData();
        return view;
    }

}
