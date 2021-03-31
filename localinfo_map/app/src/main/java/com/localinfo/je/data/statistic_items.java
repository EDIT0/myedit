package com.localinfo.je.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class statistic_items {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("index")
        @Expose
        private Integer index;
        @SerializedName("gu")
        @Expose
        private String gu;
        @SerializedName("dong")
        @Expose
        private String dong;
        @SerializedName("1_person_hh")
        @Expose
        private Double _1_person_hh;
        @SerializedName("2_person_hh")
        @Expose
        private Double _2_person_hh;
        @SerializedName("3_person_hh")
        @Expose
        private Double _3_person_hh;
        @SerializedName("n_person_hh")
        @Expose
        private Double n_person_hh;
        @SerializedName("house")
        @Expose
        private Double house;
        @SerializedName("apt")
        @Expose
        private Double apt;
        @SerializedName("row_house")
        @Expose
        private Double row_house;
        @SerializedName("multi_house")
        @Expose
        private Double multi_house;
        @SerializedName("non_house")
        @Expose
        private Double non_house;
        @SerializedName("0-9")
        @Expose
        private Double _0_9;
        @SerializedName("10-19")
        @Expose
        private Double _10_19;
        @SerializedName("20-29")
        @Expose
        private Double _20_29;
        @SerializedName("30-39")
        @Expose
        private Double _30_39;
        @SerializedName("40-49")
        @Expose
        private Double _40_49;
        @SerializedName("50-59")
        @Expose
        private Double _50_59;
        @SerializedName("60-69")
        @Expose
        private Double _60_69;
        @SerializedName("70-")
        @Expose
        private Double _70_;
        @SerializedName("environ_house")
        @Expose
        private Double environ_house;
        @SerializedName("environ_econ")
        @Expose
        private Double environ_econ;
        @SerializedName("environ_social")
        @Expose
        private Double environ_social;
        @SerializedName("environ_educ")
        @Expose
        private Double environ_educ;
        @SerializedName("crime_per_capita_gu")
        @Expose
        private Double crime_per_capita_gu;
        @SerializedName("crime_per_capita_si")
        @Expose
        private Double crime_per_capita_si;
        @SerializedName("fire_per_capita_gu")
        @Expose
        private Double fire_per_capita_gu;
        @SerializedName("fire_per_capita_si")
        @Expose
        private Double fire_per_capita_si;
        @SerializedName("traffic_safety_gu")
        @Expose
        private Double traffic_safety_gu;
        @SerializedName("traffic_safety_si")
        @Expose
        private Double traffic_safety_si;
        @SerializedName("cctv_per_sqkm_gu")
        @Expose
        private Double cctv_per_sqkm_gu;
        @SerializedName("cctv_per_sqkm_si_mean")
        @Expose
        private Double cctv_per_sqkm_si_mean;
        @SerializedName("cctv_per_sqkm_si_median")
        @Expose
        private Double cctv_per_sqkm_si_median;

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public String getGu() {
            return gu;
        }

        public void setGu(String gu) {
            this.gu = gu;
        }

        public String getDong() {
            return dong;
        }

        public void setDong(String dong) {
            this.dong = dong;
        }

        public Double get1_person_hh() {
            return _1_person_hh;
        }

        public void set1_person_hh(Double _1_person_hh) {
            this._1_person_hh = _1_person_hh;
        }

        public Double get2_person_hh() {
            return _2_person_hh;
        }

        public void set2_person_hh(Double _2_person_hh) {
            this._2_person_hh = _2_person_hh;
        }

        public Double get3_person_hh() {
            return _3_person_hh;
        }

        public void set3_person_hh(Double _3_person_hh) {
            this._3_person_hh = _3_person_hh;
        }

        public Double getN_person_hh() {
            return n_person_hh;
        }

        public void setN_person_hh(Double n_person_hh) {
            this.n_person_hh = n_person_hh;
        }

        public Double getHouse() {
            return house;
        }

        public void setHouse(Double house) {
            this.house = house;
        }

        public Double getApt() {
            return apt;
        }

        public void setApt(Double apt) {
            this.apt = apt;
        }

        public Double getRow_house() {
            return row_house;
        }

        public void setRow_house(Double row_house) {
            this.row_house = row_house;
        }

        public Double getMulti_house() {
            return multi_house;
        }

        public void setMulti_house(Double multi_house) {
            this.multi_house = multi_house;
        }

        public Double getNon_house() {
            return non_house;
        }

        public void setNon_house(Double non_house) {
            this.non_house = non_house;
        }

        public Double get0_9() {
            return _0_9;
        }

        public void set0_9(Double _0_9) {
            this._0_9 = _0_9;
        }

        public Double get10_19() {
            return _10_19;
        }

        public void set10_19(Double _10_19) {
            this._10_19 = _10_19;
        }

        public Double get20_29() {
            return _20_29;
        }

        public void set20_29(Double _20_29) {
            this._20_29 = _20_29;
        }

        public Double get30_39() {
            return _30_39;
        }

        public void set30_39(Double _30_39) {
            this._30_39 = _30_39;
        }

        public Double get40_49() {
            return _40_49;
        }

        public void set40_49(Double _40_49) {
            this._40_49 = _40_49;
        }

        public Double get50_59() {
            return _50_59;
        }

        public void set50_59(Double _50_59) {
            this._50_59 = _50_59;
        }

        public Double get60_69() {
            return _60_69;
        }

        public void set60_69(Double _60_69) {
            this._60_69 = _60_69;
        }

        public Double get70_() {
            return _70_;
        }

        public void set70_(Double _70_) {
            this._70_ = _70_;
        }

        public Double getEnviron_house() {
            return environ_house;
        }

        public void setEnviron_house(Double environ_house) {
            this.environ_house = environ_house;
        }

        public Double getEnviron_econ() {
            return environ_econ;
        }

        public void setEnviron_econ(Double environ_econ) {
            this.environ_econ = environ_econ;
        }

        public Double getEnviron_social() {
            return environ_social;
        }

        public void setEnviron_social(Double environ_social) {
            this.environ_social = environ_social;
        }

        public Double getEnviron_educ() {
            return environ_educ;
        }

        public void setEnviron_educ(Double environ_educ) {
            this.environ_educ = environ_educ;
        }

        public Double getCrime_per_capita_gu() {
            return crime_per_capita_gu;
        }

        public void setCrime_per_capita_gu(Double crime_per_capita_gu) {
            this.crime_per_capita_gu = crime_per_capita_gu;
        }

        public Double getCrime_per_capita_si() {
            return crime_per_capita_si;
        }

        public void setCrime_per_capita_si(Double crime_per_capita_si) {
            this.crime_per_capita_si = crime_per_capita_si;
        }

        public Double getFire_per_capita_gu() {
            return fire_per_capita_gu;
        }

        public void setFire_per_capita_gu(Double fire_per_capita_gu) {
            this.fire_per_capita_gu = fire_per_capita_gu;
        }

        public Double getFire_per_capita_si() {
            return fire_per_capita_si;
        }

        public void setFire_per_capita_si(Double fire_per_capita_si) {
            this.fire_per_capita_si = fire_per_capita_si;
        }

        public Double getTraffic_safety_gu() {
            return traffic_safety_gu;
        }

        public void setTraffic_safety_gu(Double traffic_safety_gu) {
            this.traffic_safety_gu = traffic_safety_gu;
        }

        public Double getTraffic_safety_si() {
            return traffic_safety_si;
        }

        public void setTraffic_safety_si(Double traffic_safety_si) {
            this.traffic_safety_si = traffic_safety_si;
        }

        public Double getCctv_per_sqkm_gu() {
            return cctv_per_sqkm_gu;
        }

        public void setCctv_per_sqkm_gu(Double cctv_per_sqkm_gu) {
            this.cctv_per_sqkm_gu = cctv_per_sqkm_gu;
        }

        public Double getCctv_per_sqkm_si_mean() {
            return cctv_per_sqkm_si_mean;
        }

        public void setCctv_per_sqkm_si_mean(Double cctv_per_sqkm_si_mean) {
            this.cctv_per_sqkm_si_mean = cctv_per_sqkm_si_mean;
        }

        public Double getCctv_per_sqkm_si_median() {
            return cctv_per_sqkm_si_median;
        }

        public void setCctv_per_sqkm_si_median(Double cctv_per_sqkm_si_median) {
            this.cctv_per_sqkm_si_median = cctv_per_sqkm_si_median;
        }
    }
}
