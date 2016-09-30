package com.example.n.lotte_jjim.Activity.DataObject;

public class StoreDO {

    private static final String TAG = "StoreDO";

    String mStoreName;
    String mStoreAddress;
    String mLatitude;
    String mLongitude;

    public  StoreDO(String mStoreName, String mStoreAddress, String mLatitude, String mLongitude) {
        this.mStoreName = mStoreName;
        this.mStoreAddress = mStoreAddress;
        this.mLatitude=mLatitude;
        this.mLongitude=mLongitude;
    }

    public void SetStoreName(String mStoreName) {
        this.mStoreName = mStoreName;
    }
    public void SetStoreAddress(String mStoreAddress) {
        this.mStoreAddress = mStoreAddress;
    }
    public void setmLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }
    public void setmLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }


    public String GetStoreName() {
        return mStoreName;
    }
    public String GetStoreAddress() {
        return mStoreAddress;
    }
    public String GetLatitude() {
        return mLatitude;
    }
    public String GetLongitude() {
        return mLongitude;
    }
}
