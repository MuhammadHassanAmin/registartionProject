package com.example.registartionproject;

public class questionModel {
    public  questionModel(){};
    public questionModel(String statement, String a, String b, String c, String d, String correctOpt) {
        this.statement = statement;
        A = a;
        B = b;
        C = c;
        D = d;
        this.correctOpt = correctOpt;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getCorrectOpt() {
        return correctOpt;
    }

    public void setCorrectOpt(String correctOpt) {
        this.correctOpt = correctOpt;
    }

    private String statement;
    private String A;
    private String B;
    private String C;
    private String D;
    private String correctOpt;

}
