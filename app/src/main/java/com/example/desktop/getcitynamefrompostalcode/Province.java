package com.example.desktop.getcitynamefrompostalcode;

public class Province {
    String mName;
    String mCode;

    public Province(String mName, String mCode) {
        this.mName = mName;
        this.mCode = mCode;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }
}
