package com.cloud6.pargmedia.Result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChanelResult {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("channel_Category_id")
    @Expose
    private String channel_Category_id;

    @SerializedName("channel_number")
    @Expose
    private String channelNumber;

    @SerializedName("channel_name")
    @Expose
    private String channelName;

    @SerializedName("channel_url")
    @Expose
    private String channelUrl;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("social_id")
    @Expose
    private String socialId;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("register_id")
    @Expose
    private String registerId;
    @SerializedName("ios_register_id")
    @Expose
    private String iosRegisterId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("about")
    @Expose
    private String about;

    @SerializedName("channelCategoryName")
    @Expose
    private String channelCategoryName;

    public String getChannelCategoryName() {
        return channelCategoryName;
    }

    public void setChannelCategoryName(String channelCategoryName) {
        this.channelCategoryName = channelCategoryName;
    }

    public String getChannel_Category_id() {
        return channel_Category_id;
    }

    public void setChannel_Category_id(String channel_Category_id) {
        this.channel_Category_id = channel_Category_id;
    }

    public String getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(String channelNumber) {
        this.channelNumber = channelNumber;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
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

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public String getIosRegisterId() {
        return iosRegisterId;
    }

    public void setIosRegisterId(String iosRegisterId) {
        this.iosRegisterId = iosRegisterId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public ChanelResult(String id, String channel_Category_id, String channelNumber,
                        String channelName, String channelUrl, String userName, String image, String socialId,
                        String lat, String lon, String registerId, String iosRegisterId, String status, String dateTime,
                        String about, String channelCategoryName) {
        this.id = id;
        this.channel_Category_id = channel_Category_id;
        this.channelNumber = channelNumber;
        this.channelName = channelName;
        this.channelUrl = channelUrl;
        this.userName = userName;
        this.image = image;
        this.socialId = socialId;
        this.lat = lat;
        this.lon = lon;
        this.registerId = registerId;
        this.iosRegisterId = iosRegisterId;
        this.status = status;
        this.dateTime = dateTime;
        this.about = about;
        this.channelCategoryName = channelCategoryName;
    }
}
