package com.example.desktop.getcitynamefrompostalcode;

public class District {
    String nameDs;
    String codeDS;

    public String getNameDs() {
        return nameDs;
    }

    public void setNameDs(String nameDs) {
        this.nameDs = nameDs;
    }

    public String getCodeDS() {
        return codeDS;
    }

    public void setCodeDS(String codeDS) {
        this.codeDS = codeDS;
    }

    public District(String nameDs, String codeDS) {
        this.nameDs = nameDs;
        this.codeDS = codeDS;
    }
}
