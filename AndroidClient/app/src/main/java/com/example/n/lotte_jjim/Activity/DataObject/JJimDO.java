package com.example.n.lotte_jjim.Activity.DataObject;


public class JJimDO {

    private static final String TAG = "JJimDO";
//상품 아이디 추가
    String mJJimProductId;
    String mJJimStoreName;
    String mJJimProductName;
    int mJJimProductImgId;

    public JJimDO(String mJJimProductId, String mJJimStoreName, String mJJimProductName, int mJJimProductImgId) {
        this.mJJimProductId = mJJimProductId;
        this.mJJimStoreName = mJJimStoreName;
        this.mJJimProductName = mJJimProductName;
        this.mJJimProductImgId = mJJimProductImgId;
    }

    public void SetmJJimProductId(String mJJimProductId) {
        this.mJJimProductId = mJJimProductId;
    }

    public void SetJJimStoreName(String mJJimStoreName) {
        this.mJJimStoreName = mJJimStoreName;
    }
    public void SetJJimProductName(String mJJimProductName) {
        this.mJJimProductName = mJJimProductName;
    }
    public void SetJJimProductImgId(int mJJimProductImgId) {
        this.mJJimProductImgId = mJJimProductImgId;
    }

    public String GetmJJimProductId() {
        return mJJimProductId;
    }
    public String GetJJimStoreName() {
        return mJJimStoreName;
    }
    public String GetJJimProductName() {
        return mJJimProductName;
    }
    public int GetJJimProductImgId() {
        return mJJimProductImgId;
    }


}
