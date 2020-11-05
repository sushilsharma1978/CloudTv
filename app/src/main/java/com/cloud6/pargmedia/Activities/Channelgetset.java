package com.cloud6.pargmedia.Activities;

public class Channelgetset {
    String id,channel_name,channel_url,image,date_time,channel_number,language;


    public Channelgetset(String id, String channel_name, String channel_url, String image,
                         String date_time, String channel_number, String language) {
        this.setId(id);
        this.setChannel_name(channel_name);
        this.setChannel_url(channel_url);
        this.setImage(image);
        this.setDate_time(date_time);
        this.setChannel_number(channel_number);
        this.setLanguage(language);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getChannel_url() {
        return channel_url;
    }

    public void setChannel_url(String channel_url) {
        this.channel_url = channel_url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getChannel_number() {
        return channel_number;
    }

    public void setChannel_number(String channel_number) {
        this.channel_number = channel_number;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
