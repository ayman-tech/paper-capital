package com.example.papertrading;

public class PortfolioListData {
    private String code, price, changeNo, changePercent;
    private String buyPrice, buyQuantity;

    public PortfolioListData(String code, String price, String changeNo,
                             String changePercent, String buyPrice, String buyQuantity) {
        this.code = code;
        this.price = price;
        this.changeNo = changeNo;
        this.changePercent = changePercent;
        this.buyPrice = buyPrice;
        this.buyQuantity = buyQuantity;
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

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getBuyQuantity() {
        return buyQuantity;
    }

    public void setBuyQuantity(String buyQuantity) {
        this.buyQuantity = buyQuantity;
    }
}
