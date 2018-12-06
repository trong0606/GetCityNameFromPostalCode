package com.example.desktop.getcitynamefrompostalcode;

public class Ward {
    String nameWrd;
    String codeWrd;

    public Ward(String nameWrd, String codeWrd) {
        this.nameWrd = nameWrd;
        this.codeWrd = codeWrd;
    }

    public String getNameWrd() {
        return nameWrd;
    }

    public void setNameWrd(String nameWrd) {
        this.nameWrd = nameWrd;
    }

    public String getCodeWrd() {
        return codeWrd;
    }

    public void setCodeWrd(String codeWrd) {
        this.codeWrd = codeWrd;
    }
}
