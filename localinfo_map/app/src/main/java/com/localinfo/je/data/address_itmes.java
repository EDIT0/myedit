package com.localinfo.je.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class address_itmes {

    @SerializedName("documents")
    @Expose
    private List<Document> documents = null;


    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public class Document {

        @SerializedName("address_name")
        @Expose
        private String address_name;
        @SerializedName("y")
        @Expose
        private String y;
        @SerializedName("x")
        @Expose
        private String x;
        @SerializedName("address_type")
        @Expose
        private String address_type;
        @SerializedName("address")
        @Expose
        private Address address;


        public String getAddress_name() {
            return address_name;
        }

        public void setAddress_name(String address_name) {
            this.address_name = address_name;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getAddress_type() {
            return address_type;
        }

        public void setAddress_type(String address_type) {
            this.address_type = address_type;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }


    }


    public class Address {

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
        @SerializedName("region_3depth_h_name")
        @Expose
        private String region_3depth_h_name;
        @SerializedName("h_code")
        @Expose
        private String h_code;
        @SerializedName("b_code")
        @Expose
        private String b_code;
        @SerializedName("mountain_yn")
        @Expose
        private String mountain_yn;
        @SerializedName("main_address_no")
        @Expose
        private String main_address_no;
        @SerializedName("sub_address_no")
        @Expose
        private String sub_address_no;
        @SerializedName("x")
        @Expose
        private String x;
        @SerializedName("y")
        @Expose
        private String y;

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

        public String getRegion_3depth_h_name() {
            return region_3depth_h_name;
        }

        public void setRegion_3depth_h_name(String region_3depth_h_name) {
            this.region_3depth_h_name = region_3depth_h_name;
        }

        public String getH_code() {
            return h_code;
        }

        public void setH_code(String h_code) {
            this.h_code = h_code;
        }

        public String getB_code() {
            return b_code;
        }

        public void setB_code(String b_code) {
            this.b_code = b_code;
        }

        public String getMountain_yn() {
            return mountain_yn;
        }

        public void setMountain_yn(String mountain_yn) {
            this.mountain_yn = mountain_yn;
        }

        public String getMain_address_no() {
            return main_address_no;
        }

        public void setMain_address_no(String main_address_no) {
            this.main_address_no = main_address_no;
        }

        public String getSub_address_no() {
            return sub_address_no;
        }

        public void setSub_address_no(String sub_address_no) {
            this.sub_address_no = sub_address_no;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }

    }
}
