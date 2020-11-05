package com.cloud6.pargmedia.Result;

public class NewChannelresult {
    String id,channel_name,channel_url,user_name,image,social_id,lat,lon,register_id,
            ios_register_id,status,date_time,about,user_id;

    public NewChannelresult(String id, String channel_name, String channel_url, String user_name,
                            String image, String social_id, String lat, String lon,
                            String register_id, String ios_register_id, String status,
                            String date_time, String about, String user_id) {
         this.setId(id);
         this.setChannel_name(channel_name);
         this.setChannel_url(channel_url);
         this.setUser_name(user_name);
         this.setImage(image);
         this.setSocial_id(social_id);
         this.setLat(lat);
         this.setLon(lon);
         this.setRegister_id(register_id);
         this.setIos_register_id(ios_register_id);
         this.setStatus(status);
         this.setDate_time(date_time);
         this.setAbout(about);
         this.setUser_id(user_id);
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getRegister_id() {
        return register_id;
    }

    public void setRegister_id(String register_id) {
        this.register_id = register_id;
    }

    public String getIos_register_id() {
        return ios_register_id;
    }

    public void setIos_register_id(String ios_register_id) {
        this.ios_register_id = ios_register_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
