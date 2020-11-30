package com.example.bottomnavigationbar;

public class NewsCard {
    private String id;
    private String title;
    private String image_url;
    private String section;
    private String date;
    private String share_url;

    public NewsCard(String id, String title, String image_url, String section, String date, String share_url){
        this.id=id;
        this.title=title;
        this.image_url=image_url;
        this.section=section;
        this.date=date;
        this.share_url=share_url;
    }

    public String getImageUrl(){
        return image_url;
    }

    public String getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getDate(){
        return date;
    }

    public String getShare_url(){
        return share_url;
    }

    public String getSection(){
        return section;
    }
}
