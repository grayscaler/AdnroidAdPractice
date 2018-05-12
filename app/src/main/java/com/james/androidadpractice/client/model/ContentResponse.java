package com.james.androidadpractice.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContentResponse {
    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("data")
    @Expose
    private Data data;

    public String getType() {
        return type;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        @SerializedName("payload")
        @Expose
        private List<Content> contents;

        public List<Content> getContents() {
            return contents;
        }
    }
}
