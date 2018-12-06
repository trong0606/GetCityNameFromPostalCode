package com.example.desktop.getcitynamefrompostalcode;

import java.util.List;

public class JsonObjectList {
    private List<Object> data;

    public JsonObjectList(List<Object> data) {
        this.data = data;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
