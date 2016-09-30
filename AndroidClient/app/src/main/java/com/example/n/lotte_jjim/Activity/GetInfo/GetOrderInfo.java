package com.example.n.lotte_jjim.Activity.GetInfo;

import android.os.Parcel;
import android.os.Parcelable;


public class GetOrderInfo implements Parcelable {

    private static final String TAG = "GetOrderInfo";

    private String mStoreName;
    private String mProductId;
    private String mProductName;
    private String mProductSalesPrice;
    private String mProductEvent;
    private String mProductStock;


    public GetOrderInfo(Parcel in) {
        mStoreName = in.readString();
        mProductId = in.readString();
        mProductName = in.readString();
        mProductSalesPrice = in.readString();
        mProductEvent = in.readString();
        mProductStock = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mStoreName);
        dest.writeString(mProductId);
        dest.writeString(mProductName);
        dest.writeString(mProductSalesPrice);
        dest.writeString(mProductEvent);
        dest.writeString(mProductStock);
    }


    public static final Parcelable.Creator<GetOrderInfo> CREATOR = new Parcelable.Creator<GetOrderInfo>() {
        public GetOrderInfo createFromParcel(Parcel in) {
            return new GetOrderInfo(in);
        }

        public GetOrderInfo[] newArray(int size) {
            return new GetOrderInfo[size];
        }
    };


    public GetOrderInfo(String mStoreName, String mProductId, String mProductName, String mProductSalesPrice, String mProductEvent, String mProductStock) {
        this.mStoreName = mStoreName;
        this.mProductId = mProductId;
        this.mProductName = mProductName;
        this.mProductSalesPrice = mProductSalesPrice;
        this.mProductEvent = mProductEvent;
        this.mProductStock = mProductStock;
    }


    public void SetStoreName(String mStoreName) {
        this.mStoreName = mStoreName;
    }
    public void SetProductId(String mProductId) {
        this.mProductId = mProductId;
    }
    public void SetProductName(String mProductName) {
        this.mProductName = mProductName;
    }
    public void SetProductSalesPrice(String mProductSalesPrice) {
        this.mProductSalesPrice = mProductSalesPrice;
    }
    public void SetProductEvent(String mProductEvent) {
        this.mProductEvent = mProductEvent;
    }
    public void SetProductStock(String mProductStock) {
        this.mProductStock = mProductStock;
    }


    public String GetStoreName() {
        return mStoreName;
    }
    public String GetProductId() {
        return mProductId;
    }
    public String GetProductName() {
        return mProductName;
    }
    public String GetProductPrice() {
        return mProductSalesPrice;
    }
    public String GetProductEvent() {
        return mProductEvent;
    }
    public String GetProductStock() {
        return mProductStock;
    }
}