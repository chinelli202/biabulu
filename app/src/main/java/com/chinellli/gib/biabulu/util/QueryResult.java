package com.chinellli.gib.biabulu.util;

public class QueryResult<T> {

    private T object;

    private String returnMesssage;
    private boolean returnStatusPositive;

    /*public QueryResult(T object, String returnMesssage, boolean returnStatusPositive) {
        this.object = object;
        this.returnMesssage = returnMesssage;
        this.returnStatusPositive = returnStatusPositive;
    }

    public QueryResult(String returnMesssage, boolean returnStatusPositive) {
        this.returnMesssage = returnMesssage;
        this.returnStatusPositive = returnStatusPositive;
    }*/

    public T getObject() {
        return object;
    }



    public void setObject(T object) {
        this.object = object;
    }

    public String getReturnMesssage() {
        return returnMesssage;
    }

    public void setReturnMesssage(String returnMesssage) {
        this.returnMesssage = returnMesssage;
    }

    public boolean isReturnStatusPositive() {
        return returnStatusPositive;
    }

    public void setReturnStatusPositive(boolean returnStatusPositive) {
        this.returnStatusPositive = returnStatusPositive;
    }
}
