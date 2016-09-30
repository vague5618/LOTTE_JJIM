package com.example.n.lotte_jjim.Activity.DataObject;

public class StockDO {

    private static final String TAG = "StockDO";

    String mProductName;
    String mStoreName;
    String mStoreAddress;
    String mStoreEvent;
    String mStoreStock;
    String mProductPrice;
    String mSalesPrice;
    String mLatitude;
    String mLongitude;



    public  StockDO(String mProductName, String mStoreName, String mStoreAddress, String mStoreEvent,String mStoreStock, String mProductPrice, String mSalesPrice, String mLatitude, String mLongitude) {
        this.mProductName = mProductName;
        this.mStoreName = mStoreName;
        this.mStoreAddress = mStoreAddress;
        this.mStoreEvent = mStoreEvent;
        this.mStoreStock = mStoreStock;
        this.mProductPrice = mProductPrice;
        this.mSalesPrice = mSalesPrice;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;

    }
    public StockDO(String mStoreName, String mStoreAddress, String mStoreEvent,String mStoreStock){
        this.mStoreName = mStoreName;
        this.mStoreAddress = mStoreAddress;
        this.mStoreEvent = mStoreEvent;
        this.mStoreStock = mStoreStock;

        this.mProductPrice = null;
        this.mSalesPrice = null;
        this.mLatitude = null;
        this.mLongitude = null;
        this.mProductName = null;


    }

    public StockDO(String mProductName, String mStoreName, String mStoreAddress, String mProductPrice, String mLatitude, String mLongitude){
        this.mProductName = mProductName;
        this.mStoreName = mStoreName;
        this.mStoreAddress = mStoreAddress;
        this.mProductPrice = mProductPrice;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;

        this.mSalesPrice = null;
        this.mStoreEvent = null;
        this.mStoreStock = null;
    }




    public void SetStoreName(String mStoreName) {
        this.mStoreName = mStoreName;
    }
    public void SetStoreAddress(String mStoreAddress) {
        this.mStoreAddress = mStoreAddress;
    }
    public void SetStoreEvent(String mStoreEvent) {
        this.mStoreEvent = mStoreEvent;
    }
    public void SetStoreStock(String mStoreStock) {
        this.mStoreStock = mStoreStock;
    }
    public void SetPrice(String mProductPrice) {
        this.mProductPrice = mProductPrice;
    }
    public void SetSalesPrice(String mSalesPrice) {
        this.mSalesPrice = mSalesPrice;
    }
    public void SetLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }
    public void SetLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }
    public void SetProductName(String mProductName) {
        this.mProductName = mProductName;
    }





    public String GetStoreName() {
        return mStoreName;
    }
    public String GetStoreAddress() {
        return mStoreAddress;
    }
    public String GetStoreEvent() {
        return mStoreEvent;
    }
    public String GetStoreStock() {
        return mStoreStock;
    }
    public String GetProductPrice() {
        return mProductPrice;
    }
    public String GetSalesPrice() {
        return mSalesPrice;
    }
    public String GetLatitude() {
        return mLatitude;
    }
    public String GetLongitude() {
        return mLongitude;
    }
    public String GetProductName() {
        return mProductName;
    }
}