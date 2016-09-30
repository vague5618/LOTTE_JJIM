package com.example.n.lotte_jjim.Activity.DataObject;

public class ProductSearchDO {

    private static final String TAG = "ProductSearchDO";
    String mProductId;
    String mProductName;
    String mProductPrice;
    int mProductImgId;

    public ProductSearchDO(String mProductId, String mProductName, String mProductPrice, int mProductImgId) {
        this.mProductId = mProductId;
        this.mProductName = mProductName;
        this.mProductPrice = mProductPrice;
        this.mProductImgId = mProductImgId;
    }



    public void SetProductId(String mProductId) {
        this.mProductId = mProductId;
    }

    public void SetProductName(String mProductName) {
        this.mProductName = mProductName;
    }
    public void SetProductPrice(String mProductPrice) {
        this.mProductPrice = mProductPrice;
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
    public int GetProductImgId() {
        return mProductImgId;
    }
    public String GetProductId() {
        return mProductId;
    }
}