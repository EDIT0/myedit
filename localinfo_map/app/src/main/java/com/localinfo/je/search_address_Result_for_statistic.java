package com.localinfo.je;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class search_address_Result_for_statistic {

    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("documents")
    @Expose
    private List<Document> documents = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public class Meta {

        @SerializedName("total_count")
        @Expose
        private Integer total_count;

        public Integer getTotal_count() {
            return total_count;
        }

        public void setTotal_count(Integer total_count) {
            this.total_count = total_count;
        }

    }

    public class Document {

        @SerializedName("region_type")
        @Expose
        private String region_type;
        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("address_name")
        @Expose
        private String address_name;
        @SerializedName("region_1depth_name")
        @Expose
        private String region_1depth_name;
        @SerializedName("region_2depth_name")
        @Expose
        private String region_2depth_name;
        @SerializedName("region_3depth_name")
        @Expose
        private String region_3depth_name;
        @SerializedName("region_4depth_name")
        @Expose
        private String region_4depth_name;
        @SerializedName("x")
        @Expose
        private Double x;
        @SerializedName("y")
        @Expose
        private Double y;

        public String getRegion_type() {
            return region_type;
        }

        public void setRegion_type(String region_type) {
            this.region_type = region_type;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getAddress_name() {
            return address_name;
        }

        public void setAddress_name(String address_name) {
            this.address_name = address_name;
        }

        public String getRegion_1depth_name() {
            return region_1depth_name;
        }

        public void setRegion_1depth_name(String region_1depth_name) {
            this.region_1depth_name = region_1depth_name;
        }

        public String getRegion_2depth_name() {
            return region_2depth_name;
        }

        public void setRegion_2depth_name(String region_2depth_name) {
            this.region_2depth_name = region_2depth_name;
        }

        public String getRegion_3depth_name() {
            return region_3depth_name;
        }

        public void setRegion_3depth_name(String region_3depth_name) {
            this.region_3depth_name = region_3depth_name;
        }

        public String getRegion_4depth_name() {
            return region_4depth_name;
        }

        public void setRegion_4depth_name(String region_4depth_name) {
            this.region_4depth_name = region_4depth_name;
        }

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }

    }
}
