package com.linkb.jstx.model.world_area;

import java.io.Serializable;

public class CountryBean implements Serializable {
    private String id;
   
    private String continent_id;
   
    private String code;
   
    private String name;
   
    private String full_name;
   
    private String cname;
   
    private String full_cname;
   
    private String lower_name;
   
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContinent_id() {
        return continent_id;
    }

    public void setContinent_id(String continent_id) {
        this.continent_id = continent_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getFull_cname() {
        return full_cname;
    }

    public void setFull_cname(String full_cname) {
        this.full_cname = full_cname;
    }

    public String getLower_name() {
        return lower_name;
    }

    public void setLower_name(String lower_name) {
        this.lower_name = lower_name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
