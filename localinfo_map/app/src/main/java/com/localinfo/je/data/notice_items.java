package com.localinfo.je.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class notice_items {

    @SerializedName("result")
    @Expose
    private ArrayList<items> items = null;

    public ArrayList<items> getItems() {
        return items;
    }

    public void setItems(ArrayList<items> items) {
        this.items = items;
    }

    public class items {
        String title;
        String content;
        String date;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

}
