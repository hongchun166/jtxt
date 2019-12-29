package com.linkb.jstx.network.result;

import com.linkb.jstx.model.Information;

import java.util.ArrayList;

public class InformationListResult extends BaseResult{

    public ArrayList<Information> dataList;

    public boolean isNotEmpty() {
        return dataList != null && !dataList.isEmpty();
    }

    public static String getFakeDate() {
        String result = "{\n" +
                "    \"code\": 200,\n" +
                "    \"dataList\": [\n" +
                "        {\n" +
                "            \"id\": 1548067949581,\n" +
                "            \"content\": \"总而言之，狭义社区其实是中间层，爱好者在狭义社区中聚集，汇集意见并向四面八方传达。\",\n" +
                "            \"title\": \"观点 | 公链治理与“社区”的几种意思\",\n" +
                "            \"textContent\": \"\",\n" +
                "            \"status\": \"1\",\n" +
                "            \"author\": \"转载\",\n" +
                "            \"timestamp\": 1548067949581\n" +
                "        }\n" +
                "    ],\n" +
                "    \"page\": {\n" +
                "        \"count\": 1,\n" +
                "        \"size\": 1,\n" +
                "        \"currentPage\": 0,\n" +
                "        \"countPage\": 1\n" +
                "    }\n" +
                "}";
        return  result;
    }
}
