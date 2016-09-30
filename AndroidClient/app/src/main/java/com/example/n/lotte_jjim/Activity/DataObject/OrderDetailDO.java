package com.example.n.lotte_jjim.Activity.DataObject;


public class OrderDetailDO {

    private static final String TAG = "OrderDetailDO";
    String mOrderProductId;
    String mOrderProductName;
    String mOrderProductPrice;
    String mOrderProductEvent;
    String mOrderProductNum;
    int mOrderProductImg;


    public OrderDetailDO(String mOrderProductId, String mOrderProductName, String mOrderProductPrice, String mOrderProductEvent, String mOrderProductNum, int mOrderProductImg) {
        this.mOrderProductId = mOrderProductId;
        this.mOrderProductName = mOrderProductName;
        this.mOrderProductPrice = mOrderProductPrice;
        this.mOrderProductEvent = mOrderProductEvent;
        this.mOrderProductNum = mOrderProductNum;
        this.mOrderProductImg = mOrderProductImg;
    }

    public void SetmOrderProductId(String mOrderProductId) {
        this.mOrderProductId = mOrderProductId;
    }

    public void SetOrderProductName(String mOrderProductName) {
        this.mOrderProductName = mOrderProductName;
    }
    public void SetOrderProductPrice(String mOrderProductPrice) {
        this.mOrderProductPrice = mOrderProductPrice;
    }
    public void SetOrderProductEvent(String mOrderProductEvent){
        this.mOrderProductEvent = mOrderProductEvent;
    }
    public void SetOrderProductNum(String mOrderProductNum) {
        this.mOrderProductNum = mOrderProductNum;
    }
    public void SetOrderProductImg(int mOrderProductImg) {
        this.mOrderProductImg = mOrderProductImg;
    }



    public String GetOrderProductName() {
        return mOrderProductName;
    }
    public String GetOrderProductPrice() {
        return mOrderProductPrice;
    }
    public String GetOrderProductEvent() {
        return mOrderProductEvent;
    }
    public String GetOrderProductNum() {
        return mOrderProductNum;
    }
    public int GetOrderProductImg() {
        return  mOrderProductImg;
    }

    public String GetmOrderProductId() {
        return mOrderProductId;
    }
}
