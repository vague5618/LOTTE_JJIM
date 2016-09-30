package com.example.n.lotte_jjim.Activity.DataObject;

public class ProductDO {

    private static final String TAG = "ProductDO";
    String mProductId;
    String mProductName;
    String mProductPrice;
    String mProductStock;
    String mProductEvent;
    int mProductImgId;
//    Bitmap mProductImage;

    public ProductDO(String mProductId, String mProductName, String mProductPrice, String mProductStock, String mProductEvent, int mProductImgId) {
        this.mProductId = mProductId;
        this.mProductName = mProductName;
        this.mProductPrice = mProductPrice;
        this.mProductStock = mProductStock;
        this.mProductEvent = mProductEvent;
        this.mProductImgId = mProductImgId;
//        this.mProductImage = mProductImage;

    }


    public void SetmProductId(String mProductId) {
        this.mProductId = mProductId;
    }

    public void SetProductName(String mProductName) {
        this.mProductName = mProductName;
    }
    public void SetProductPrice(String mProductPrice) {
        this.mProductPrice = mProductPrice;
    }
    public void SetProductStock(String mProductStock) {
        this.mProductStock = mProductStock;
    }
    public void SetProductEvent(String mProductEvent) {
        this.mProductEvent = mProductEvent;
    }
    public void SetProductImgId(int mProductImgId) {
        this.mProductImgId = mProductImgId;
    }

    public String GetProductName() {
        return mProductName;
    }
    public String GetProductPrice() {
        return mProductPrice;
    }
    public String GetProductStock() {
        return mProductStock;
    }
    public String GetProductEvent() {
        return mProductEvent;
    }
    public String GetmProductId() {
        return mProductId;
    }
    public int GetmProductImage() {
        return mProductImgId;
    }
}