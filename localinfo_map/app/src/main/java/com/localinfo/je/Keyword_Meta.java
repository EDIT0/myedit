package com.localinfo.je;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Keyword_Meta {

    @Expose
    private Integer pageable_count;
    @SerializedName("total_count")
    @Expose
    private Integer total_count;
    @SerializedName("is_end")
    @Expose
    private Boolean is_end;

    public Integer getPageable_count() {
        return pageable_count;
    }

    public void setPageable_count(Integer pageable_count) {
        this.pageable_count = pageable_count;
    }

    public Integer getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Integer total_count) {
        this.total_count = total_count;
    }

    public Boolean getIs_end() {
        return is_end;
    }

    public void setIs_end(Boolean is_end) {
        this.is_end = is_end;
    }

}
