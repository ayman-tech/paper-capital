package com.example.papertrading;

public class StockListData {
    private String code, price, changeNo, changePercent;

    public StockListData(String code, String price, String changeNo, String changePercent) {
        this.code = code;
        this.price = price;
        this.changeNo = changeNo;
        this.changePercent = changePercent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChangeNo() {
        return changeNo;
    }

    public void setChangeNo(String changeNo) {
        this.changeNo = changeNo;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }
}
