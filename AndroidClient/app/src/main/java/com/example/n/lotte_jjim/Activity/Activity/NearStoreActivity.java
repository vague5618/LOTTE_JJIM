package com.example.n.lotte_jjim.Activity.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.n.lotte_jjim.Activity.Adapter.ProductSearchAdapter;
import com.example.n.lotte_jjim.Activity.DataObject.ProductSearchDO;
import com.example.n.lotte_jjim.Activity.GetInfo.GetGpsInfo;
import com.example.n.lotte_jjim.Activity.Util.HttpUtil;
import com.example.n.lotte_jjim.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class NearStoreActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NearStoreActivity";
    public static final String mPackageName = "com.example.n.lotte_jjim";

    ArrayList<ProductSearchDO> mProductSearchDO = new ArrayList<ProductSearchDO>();
    EditText mText_SearchKeyword;
    JSONArray mProductKeywordArray;
    ProductSearchAdapter mAdapter_ProductSearch;
    ListView mList_ProductSearch;

    GetGpsInfo mGpsInfo;
    String mGps_Latitude;
    String mGps_Longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_store);
        mText_SearchKeyword = (EditText)findViewById(R.id.mText_SearchKeyword);

        Init_UI();
        Init_Button();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void Init_UI() {

        setTitle("상품명으로 검색");
        ChangeTitleTextSize(16);
    }

    public void ChangeTitleTextSize(int toSize) {

        ActionBar ab = getSupportActionBar();
        TextView tv = new TextView(getApplicationContext());

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView

        tv.setLayoutParams(lp);
        tv.setText(ab.getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, toSize);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(tv);
    }

    public void Init_Button() {

        ImageButton mBtn_Search = (ImageButton)findViewById(R.id.mBtn_Search);

        mBtn_Search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mBtn_Search:
                mProductSearchDO.clear();
                Set_AdapterToList();

                if(mText_SearchKeyword.getText().length()==0) {
                    KeywordIsNull();
                } else {
                    mProductSearchJSON();
                }
                break;
        }
    }


    public void KeywordIsNull() {

        AlertDialog.Builder mAlert_DeleteFavorite = new AlertDialog.Builder(NearStoreActivity.this);
        mAlert_DeleteFavorite.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mAlert_DeleteFavorite.setMessage("검색하려는 상품명을 입력하세요");
        mAlert_DeleteFavorite.show();
    }


    public void  mProductSearchJSON() {

        mProductSearchDO.clear();
        mProductKeywordArray = new JSONArray();

        toJSON(mProductKeywordArray, "productKeyword", mText_SearchKeyword.getText().toString());
        Log.d(TAG, "NAME :" + mText_SearchKeyword.getText().toString());

        try {
            HttpUtil httpUtil = new HttpUtil("Product", "ProductKeywordRequest", mProductKeywordArray);
            httpUtil.start();
            httpUtil.join();

            JSONArray mProductKeyWordResult = httpUtil.getResponse();

            int size = mProductKeyWordResult.length();
            Log.d(TAG, "SIZE :" + size);
            Log.d(TAG, mProductKeyWordResult.getJSONObject(0).getString("productName"));

            if(mProductKeyWordResult.getJSONObject(0).getString("productName").equals("null")) {
                CheckIsList("결과 항목이 없습니다.");
            } else {
                for (int i = 0; i < size; i++) {
                    String mProductID = mProductKeyWordResult.getJSONObject(i).getString("productID");
                    String mProductName = mProductKeyWordResult.getJSONObject(i).getString("productName");
                    String mProductPrice = mProductKeyWordResult.getJSONObject(i).getString("productPrice");

                    // 02.21 rasp: TODO! 이미지 넣기 추가! 테스트 필요!
                    int mProductImage = getResources().getIdentifier("a"+mProductID, "drawable", mPackageName);
                    mProductSearchDO.add(new ProductSearchDO(mProductID, mProductName, mProductPrice, mProductImage));
                }
            }
        } catch (Exception e) {
            e.toString();
        }
    }

    JSONArray toJSON(JSONArray array, String type, String item){
        JSONObject obj = new JSONObject();
        try {
            obj.put(type, item);
            array = array.put(obj);
            return array;
        }catch(Exception e){
            return null;
        }
    }


    public void Set_AdapterToList() {
        mAdapter_ProductSearch = new ProductSearchAdapter(getApplicationContext(), R.layout.list_product_search, mProductSearchDO);
        mList_ProductSearch = (ListView) findViewById(R.id.mList_Product);

        mList_ProductSearch.setAdapter(mAdapter_ProductSearch);

        mList_ProductSearch.setOnItemClickListener(ListViewItemClickListener);
    }

    private AdapterView.OnItemClickListener ListViewItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View clickedView, int position, long id){

            if(Get_GpsInfo()) {

                String mText_ProductID = mProductSearchDO.get(position).GetProductId();
                String mText_ProductName = mProductSearchDO.get(position).GetProductName();
                String mText_Price = mProductSearchDO.get(position).GetProductPrice();

                Intent mIntent_Keyword = new Intent(getApplicationContext(), NearStockActivity.class);
                mIntent_Keyword.putExtra("ProductID", mText_ProductID);
                mIntent_Keyword.putExtra("ProductName", mText_ProductName);
                mIntent_Keyword.putExtra("ProductPrice", mText_Price);
                mIntent_Keyword.putExtra("GpsLatitude", mGps_Latitude);
                mIntent_Keyword.putExtra("GpsLongitude", mGps_Longitude);

                Log.d(TAG, mText_ProductName);
                startActivity(mIntent_Keyword);
            }
        }
    };


    public void CheckIsList(String mMsg) {

        AlertDialog.Builder mCheckComplete = new AlertDialog.Builder(NearStoreActivity.this);
        mCheckComplete.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mCheckComplete.setMessage(mMsg);
        mCheckComplete.show();
    }


    public boolean Get_GpsInfo() {

        mGpsInfo = new GetGpsInfo(NearStoreActivity.this);

        if (mGpsInfo.Get_LocationStatus()) {
            double mLatitude = mGpsInfo.Get_Latitude();
            double mLongitude = mGpsInfo.Get_Longitude();

            mGps_Latitude = String.valueOf(mLatitude);
            mGps_Longitude = String.valueOf(mLongitude);
            return  true;
        } else {

            mGpsInfo.Show_SettingsAlert();
            return false;
        }
    }
}