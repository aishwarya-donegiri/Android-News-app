package com.example.bottomnavigationbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExpandedArticleActivity extends AppCompatActivity {

    String id;
    ImageView image_container;
    TextView title_container;
    TextView date_container;
    TextView section_container;
    TextView description_container;
    TextView share_container;
    String shareUrl;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    NewsCard current_card;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_article);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent expanded_article_intent = getIntent();
        id = expanded_article_intent.getStringExtra("id");
        String title_appbar = expanded_article_intent.getStringExtra("title");
        try{getSupportActionBar().setTitle(title_appbar);}catch(Exception e){Log.d("Error",e.getMessage());}
        //Log.d("id",id);
        image_container = findViewById(R.id.image_expanded);
        title_container = findViewById(R.id.title_expanded);
        date_container = findViewById(R.id.date_expanded);
        section_container = findViewById(R.id.section_expanded);
        description_container = findViewById(R.id.description_expanded);
        share_container=findViewById(R.id.share_expanded);
        getData(id);

    }


    public void getData(final String id) {
        String url = "http://reactapp4640782493.us-east-1.elasticbeanstalk.com/guardianArticle?id=" + id;
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject article) {
                        Log.d("check", article.toString());
                        try {



                            title = article.getString("title");
                            String imageUrl = article.getString("image");
                            String date = article.getString("date");
                            String section = article.getString("section");
                            String description=article.getString("desc");
                            shareUrl = article.getString("shareUrl");
                           current_card=new NewsCard(id,title,imageUrl,section,date,shareUrl);

                            title_container.setText(title);
                            Picasso.with(ExpandedArticleActivity.this).load(imageUrl).fit().centerInside().into(image_container);
                            date_container.setText(date);
                            section_container.setText(section);
                            description_container.setText(description);
                            //share_container.setText("View Full Article");
                            SpannableString content = new SpannableString("View Full Article");
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            share_container.setText(content);
                            //getActionBar().setTitle(current_card.getTitle());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ssss", "hhhhhhhhhh");
                error.printStackTrace();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(ExpandedArticleActivity.this);
        queue.add(jor);
    }


    public void setClickFullArticle(View view){
        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(shareUrl));
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_expanded_article,menu);
        pref=getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor=pref.edit();
        if(pref.contains(id)) {
            //Log.d("check","Bookmarked");
            menu.getItem(0).setIcon(R.drawable.bookmarked_scaled);
        }else
        {//Log.d("check","Not Bookmarked");
            menu.getItem(0).setIcon(R.drawable.bookmark_scaled);}

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        pref=getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor=pref.edit();

        int id=item.getItemId();
        if(id==android.R.id.home){
            this.finish();
        }
        else if(id==R.id.action_twitter){

            String url="https://twitter.com/intent/tweet?url=" + shareUrl+
                    "&hashtags=CSCI571NewsSearch";
            Intent twitter_intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(twitter_intent);

        }
        else if(id==R.id.action_bookmark){
            item.setChecked(!item.isChecked());
//            item.setIcon(item.isChecked()? R.drawable.bookmarked:R.drawable.bookmark);
            if(pref.contains(this.id)) {
                //Log.d("key present","key present");
                editor.remove(this.id);
                editor.commit();
                //Log.d("check","Removing Bookmarked");
                item.setIcon(R.drawable.bookmark_scaled);
                //Toast.makeText(this, title+" was removed from Bookmarks.", Toast.LENGTH_LONG).show();

            }else
            {
                String stringified = (new Gson()).toJson(current_card);
                //Log.d("TEST GSON", stringified);
                editor.putString(this.id,stringified);
                editor.commit();
                // Log.d("check","Bookmarked");
                item.setIcon(R.drawable.bookmarked_scaled);
            //Toast.makeText(this, title+" was added to Bookmarks.", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}


