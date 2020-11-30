package com.example.bottomnavigationbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Hashtable;

public class BookmarkCardAdapter extends RecyclerView.Adapter<BookmarkCardAdapter.BookmarkCardViewHolder> {
    private Context context;
    private ArrayList<NewsCard> card_list;
    Dialog myDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String id;
    String title;

    public BookmarkCardAdapter(Context context,ArrayList<NewsCard> card_list){
        this.context=context;
        this.card_list=card_list;
    }

    @NonNull
    @Override
    public BookmarkCardViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.bookmark_card,parent,false);
        final BookmarkCardViewHolder viewHolder=new BookmarkCardViewHolder(v);
        context=parent.getContext();

        viewHolder.news_card_layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent expanded_article_intent=new Intent(context,ExpandedArticleActivity.class);
                expanded_article_intent.putExtra("id",card_list.get(viewHolder.getAdapterPosition()).getId());
                expanded_article_intent.putExtra("title",card_list.get(viewHolder.getAdapterPosition()).getTitle());

                context.startActivity(expanded_article_intent);
            }
        });

        viewHolder.news_card_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(pref.contains(id)) {
                    //Log.d("key present","key present");
                    editor.remove(id);
                    editor.commit();
                    BookmarksFragment frg = null;
                    //notifyDataSetChanged();
                    card_list.remove(card_list.get(viewHolder.getAdapterPosition()));
                    notifyItemRemoved(viewHolder.getAdapterPosition());
                    //Log.d("check","Removing Bookmarked");
                    Toast.makeText(context, title+" was removed from Bookmarks.", Toast.LENGTH_LONG).show();

                }

                return false;
            }
        });





        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BookmarkCardViewHolder holder, int position) {
        pref=context.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        editor=pref.edit();
        final Hashtable<String,String> card_data=new Hashtable<String,String>();
        final NewsCard current_card=card_list.get(position);
        String image_url=current_card.getImageUrl();
        title=current_card.getTitle();
        String date=current_card.getDate();
        String share_url=current_card.getShare_url();
        id=current_card.getId();
        String section=current_card.getSection();
        card_data.put("id",id);
        card_data.put("title",title);
        card_data.put("image_url",image_url);
        card_data.put("section",section);
        card_data.put("date",date);
        card_data.put("share_url",share_url);
        //final String data=card_data.toString();

        holder.title_container.setText(title);
        if(pref.contains(id)) {
            //Log.d("check","Bookmarked");
            holder.bookmark_container.setImageResource(R.drawable.bookmarked);
        }else
        {//Log.d("check","Not Bookmarked");
            holder.bookmark_container.setImageResource(R.drawable.bookmark);}

        holder.section_container.setText(date+" | "+section);
        Picasso.with(context).load(image_url).fit().centerInside().into(holder.image_container);
        //holder.bookmark_container.setImageResource(R.drawable.bookmark);
        holder.bookmark_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pref.contains(id)) {
                    //Log.d("key present","key present");
                    editor.remove(id);
                    editor.commit();
                    //Log.d("check","Removing Bookmarked");
                    holder.bookmark_container.setImageResource(R.drawable.bookmark);
                    Toast.makeText(context, title+" was removed from Bookmarks.", Toast.LENGTH_LONG).show();

                }else {
                    String stringified = (new Gson()).toJson(current_card);
                    //Log.d("TEST GSON", stringified);
                    editor.putString(id, stringified);
                    editor.commit();
                    // Log.d("check","Bookmarked");
                    holder.bookmark_container.setImageResource(R.drawable.bookmarked);
                    Toast.makeText(context, title + " was added to Bookmarks.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return card_list.size();
    }

    public class BookmarkCardViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout news_card_layout;




        public ImageView image_container;
        public TextView title_container;
        //public TextView date_container;
        public TextView section_container;
        public ImageView bookmark_container;

        public BookmarkCardViewHolder(@NonNull View itemView) {
            super(itemView);

            news_card_layout=(LinearLayout) itemView.findViewById((R.id.bookmark_card_container));

            Log.d("here","here");
            image_container = itemView.findViewById(R.id.image);
            title_container = itemView.findViewById(R.id.title);
            bookmark_container=itemView.findViewById(R.id.bookmark_container);
            section_container=itemView.findViewById(R.id.sectionAndDate);

        }
    }
}
