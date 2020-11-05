package com.cloud6.pargmedia.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.cloud6.pargmedia.Result.ChanelResult;

import java.util.List;

public class ChanelResponse {

    @SerializedName("result")
    @Expose
    private List<ChanelResult> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<ChanelResult> getResult() {
        return result;
    }

    public void setResult(List<ChanelResult> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
