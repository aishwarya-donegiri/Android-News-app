package com.example.bottomnavigationbar;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class HomeFragment extends Fragment {

//    ListView listView;
//    String[] nameList = {"a","b","c","d","e"};
//    ArrayAdapter<String> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
//        ListView lv=(ListView) view.findViewById(R.id.myListView);
//        arrayAdapter=new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,nameList);
//        lv.setAdapter(arrayAdapter);
//

        return view;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getActivity().getMenuInflater().inflate(R.menu.toolbar_search,menu);
//        MenuItem menuItem = menu.findItem(R.id.action_search);
//        SearchView searchView=(SearchView) menuItem.getActionView();
//        searchView.setQueryHint("Type here to search");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                arrayAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//
//
//        return super.onCreateOptionsMenu(menu);
//    }
}
