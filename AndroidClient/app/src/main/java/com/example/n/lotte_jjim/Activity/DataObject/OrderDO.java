package com.example.n.lotte_jjim.Activity.DataObject;

public class OrderDO {

    private static final String TAG = "OrderDO";

    String mOrderProductID;
    String mOrderStoreName;
    String mOrderProductName;
    String mOrderProductPrice;
    String mOrderProductStock;
    String mOrderProductEvent;
    String mOrderInputNum;
    int mOrderProductImgId;


    public OrderDO(String mOrderProductID, String mOrderStoreName, String mOrderProductName, String mOrderProductPrice, String mOrderProductStock, String mOrderProductEvent, int mOrderProductImgId) {
       this.mOrderProductID = mOrderProductID;
        this.mOrderStoreName = mOrderStoreName;
        this.mOrderProductName = mOrderProductName;
        this.mOrderProductPrice = mOrderProductPrice;
        this.mOrderProductStock = mOrderProductStock;
        this.mOrderProductEvent = mOrderProductEvent;
        this.mOrderProductImgId = mOrderProductImgId;
        this.mOrderInputNum = "1";
    }

    public OrderDO(String mOrderProductID, String mOrderStoreName, String mOrderProductName, String mOrderInputNum, int mOrderProductImgId) {
        this.mOrderProductID = mOrderProductID;
        this.mOrderStoreName = mOrderStoreName;
        this.mOrderProductName = mOrderProductName;
        this.mOrderProductPrice = null;
        this.mOrderProductStock = null;
        this.mOrderProductEvent = null;
        this.mOrderProductImgId = mOrderProductImgId;
        this.mOrderInputNum = "1";
    }


    public void SetmOrderProductID(String mOrderProductID) {
        this.mOrderProductID = mOrderProductID;
    }

    public void SetOrderStoreName(String mOrderStoreName) {
        this.mOrderStoreName = mOrderStoreName;
    }
    public void SetOrderProductName(String mOrderProductName) {
        this.mOrderProductName = mOrderProductName;
    }
    public void SetOrderProductPrice(String mOrderProductPrice) {
        this.mOrderProductPrice = mOrderProductPrice;
    }
    public void SetOrderProductStock(String mOrderProductStock) {
        this.mOrderProductStock = mOrderProductStock;
    }
    public void SetOrderProductEvent(String mOrderProductEvent) {
        this.mOrderProductEvent = mOrderProductEvent;
    }
    public void SetOrderInputNum(String mOrderInputNum) {
        this.mOrderInputNum = mOrderInputNum;
    }
    public void SetOrderProductImgId(int mOrderProductImgId) {
        this.mOrderProductImgId = mOrderProductImgId;
    }


    public String GetOrderStoreName() {
        return mOrderStoreName;
    }
    public String GetOrderProductName() {
        return mOrderProductName;
    }
    public String GetOrderProductPrice() {
        return mOrderProductPrice;
    }
    public String GetOrderProductStock() {
        return mOrderProductStock;
    }
    public String GetOrderProductEvent() {
        return mOrderProductEvent;
    }
    public String GetOrderInputNum() {
        return mOrderInputNum;
    }
    public int GetOrderProductImgId() {
        return mOrderProductImgId;
    }
    public String GetmOrderProductID() {
        return mOrderProductID;
    }

}
