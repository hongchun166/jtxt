package com.linkb.jstx.model.intent;

import java.io.Serializable;

public class SearchUserParam implements Serializable {

    private int searchType;

    private String inputStr;
    private String region;
    private String industry;
    private String label;
    String gender;
    private String otherKey;

    public String getInputStr() {
        return inputStr;
    }

    public void setInputStr(String inputStr) {
        this.inputStr = inputStr;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public String getOtherKey() {
        return otherKey;
    }

    public void setOtherKey(String otherKey) {
        this.otherKey = otherKey;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
