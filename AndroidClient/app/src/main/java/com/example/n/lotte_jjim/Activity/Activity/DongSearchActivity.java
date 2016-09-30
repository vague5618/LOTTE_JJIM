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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.n.lotte_jjim.Activity.Adapter.ProductSearchAdapter;
import com.example.n.lotte_jjim.Activity.DataObject.ProductSearchDO;
import com.example.n.lotte_jjim.Activity.Util.HttpUtil;
import com.example.n.lotte_jjim.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class DongSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DongSearchActivity";
    public static final String mPackageName = "com.example.n.lotte_jjim";

    ArrayList<ProductSearchDO> mProductSearchDO = new ArrayList<ProductSearchDO>();
    EditText mText_SearchKeyword;
    ListView mList_ProductSearch;
    ProductSearchAdapter mAdapter_ProductSearch;
    String mGetIntent_DongName;
    String mGetIntent_GuName;

    JSONArray mProductKeywordArray;



    public void Get_Intent() {

        Intent mIntent_Receive = getIntent();
        mGetIntent_GuName = mIntent_Receive.getExtras().getString("GuName");
        mGetIntent_DongName = mIntent_Receive.getExtras().getString("DongName");
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


    public void Set_AdapterToList() {

        mAdapter_ProductSearch = new ProductSearchAdapter(getApplicationContext(), R.layout.list_product_search, mProductSearchDO);
        mList_ProductSearch = (ListView) findViewById(R.id.mList_Product);

        mList_ProductSearch.setAdapter(mAdapter_ProductSearch);
        mList_ProductSearch.setOnItemClickListener(ListViewItemClickListener);
    }
    private AdapterView.OnItemClickListener ListViewItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View clickedView, int position, long id){

            String mProduct_ID = mProductSearchDO.get(position).GetProductId();
            String mText_Product = mProductSearchDO.get(position).GetProductName();
            String mText_Price = mProductSearchDO.get(position).GetProductPrice();

            Intent mIntent_Keyword = new Intent(getApplicationContext(), DongStockActivity.class);
            mIntent_Keyword.putExtra("GuName", mGetIntent_GuName);
            mIntent_Keyword.putExtra("DongName",mGetIntent_DongName);
            mIntent_Keyword.putExtra("ProductID",mProduct_ID);
            mIntent_Keyword.putExtra("ProductName",mText_Product);
            mIntent_Keyword.putExtra("ProductPrice", mText_Price);
            startActivity(mIntent_Keyword);
        }
    };


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

            if(mProductKeyWordResult.getJSONObject(0).getString("productName").equals("null")) {
                CheckIsList("결과 항목이 없습니다.");
            } else {
                for (int i = 0; i < size; i++) {
                    String mProductID = mProductKeyWordResult.getJSONObject(i).getString("productID");
                    String mProductName = mProductKeyWordResult.getJSONObject(i).getString("productName");
                    String mProductPrice = mProductKeyWordResult.getJSONObject(i).getString("productPrice");

                    // 02.21 rasp: TODO! 이미지 넣기 추가! 테스트 필요!
                    int mProductImage = getResources().getIdentifier("a"+mProductID, "drawable", mPackageName);
                    mProductSearchDO.add(new ProductSearchDO(mProductID ,mProductName, mProductPrice, mProductImage));

                    Log.d("1_ID값확인: ", mProductID);
                    Log.d("2_RES값확인: ", String.valueOf(mProductImage));
                }
            }
        } catch (Exception e) {
            e.toString();
        }
    }


    public void CheckIsList(String mMsg) {

        AlertDialog.Builder mCheckComplete = new AlertDialog.Builder(DongSearchActivity.this);
        mCheckComplete.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mCheckComplete.setMessage(mMsg);
        mCheckComplete.show();
    }


    public void KeywordIsNull() {

        AlertDialog.Builder mAlert_DeleteFavorite = new AlertDialog.Builder(DongSearchActivity.this);
        mAlert_DeleteFavorite.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mAlert_DeleteFavorite.setMessage("검색하려는 상품명을 입력하세요");
        mAlert_DeleteFavorite.show();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dong_search);
        mText_SearchKeyword = (EditText)findViewById(R.id.mText_SearchKeyword);
        Get_Intent();

        Init_UI();
        Init_Button();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int mId = item.getItemId();

        if (mId == android.R.id.home) {
            Intent mIntent_Back = new Intent(getApplicationContext(), StoreSearchActivity3.class);
            mIntent_Back.putExtra("GuName", mGetIntent_GuName);
            mIntent_Back.putExtra("DongName", mGetIntent_DongName);
            mIntent_Back.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mIntent_Back);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
