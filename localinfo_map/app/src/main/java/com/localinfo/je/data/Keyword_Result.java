package com.localinfo.je.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Keyword_Result {
    @SerializedName("meta")
    @Expose
    private Keyword_Meta meta;
    @SerializedName("documents")
    @Expose
    private List<Keyword_Items> documents = null;

    public Keyword_Meta getMeta() {
        return meta;
    }

    public void setMeta(Keyword_Meta meta) {
        this.meta = meta;
    }

    public List<Keyword_Items> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Keyword_Items> documents) {
        this.documents = documents;
    }
}
