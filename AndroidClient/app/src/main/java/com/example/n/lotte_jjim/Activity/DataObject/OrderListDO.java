package com.example.n.lotte_jjim.Activity.DataObject;


public class OrderListDO {
    private static final String TAG = "OrderListDO";

    String mOrderNo;
    String mOrderStoreName;
    String mOrderSalesPrice;


    public OrderListDO(String mOrderNo, String mOrderStoreName, String mOrderSalesPrice) {
        this.mOrderNo = mOrderNo;
        this.mOrderStoreName = mOrderStoreName;
        this.mOrderSalesPrice = mOrderSalesPrice;
    }


    public void SetOrderNo(String mOrderNo) {
        this.mOrderNo = mOrderNo;
    }
    public void SetOrderStoreName(String mOrderStoreName) {
        this.mOrderStoreName = mOrderStoreName;
    }
    public void SetOrderSalesPrice(String mOrderSalesPrice) {
        this.mOrderSalesPrice = mOrderSalesPrice;
    }


    public String GetOrderNo() {
        return mOrderNo;
    }
    public String GetOrderStoreName() {
        return mOrderStoreName;
    }
    public String GetOrderSalesPrice() {
        return mOrderSalesPrice;
    }

}



