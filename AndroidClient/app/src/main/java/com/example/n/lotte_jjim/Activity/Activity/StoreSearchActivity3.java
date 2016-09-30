package com.example.n.lotte_jjim.Activity.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.n.lotte_jjim.Activity.Adapter.DongAdapter;
import com.example.n.lotte_jjim.Activity.DataObject.StoreDO;
import com.example.n.lotte_jjim.Activity.Util.HttpUtil;
import com.example.n.lotte_jjim.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StoreSearchActivity3 extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "StoreSearchActivity3";

    private String mCurrentUserID;
    String mGet_GuName;
    String mGet_DongName;

    ListView mList_Store;
    DongAdapter mAdapter_Dong;

    ArrayList<StoreDO> mStoreDO = new ArrayList<StoreDO>();
    JSONArray mStoreArray = new JSONArray();



    public void Init_ID(){
        SharedPreferences setting = getSharedPreferences("MYID", MODE_PRIVATE);
        mCurrentUserID = setting.getString("ID","");
    }


    public void Get_Intent() {

        Intent mIntent_Receive = getIntent();
        mGet_GuName = mIntent_Receive.getExtras().getString("GuName");
        Log.d(TAG,"getIntent GU = "+mGet_GuName);
        mGet_DongName = mIntent_Receive.getExtras().getString("DongName");
        Log.d(TAG,"getIntent DONG = "+mGet_DongName);
    }


    public void Init_UI() {

        setTitle("지역별 매장 검색");
        ChangeTitleTextSize(16);

        TextView mText_Location = (TextView) findViewById(R.id.mText_Location);
        mText_Location.setText("▼ \""+ mGet_GuName +">" +mGet_DongName+"\"에 위치한 매장");
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

        Button mBtn_DongProductSearch = (Button) findViewById(R.id.mBtn_DongSearch);

        mBtn_DongProductSearch.setOnClickListener(this);
    }


    public void mStoreJSON() {

        toJSON(mStoreArray,"add1", "서울");
        toJSON(mStoreArray, "add2", mGet_GuName);
        toJSON(mStoreArray, "add3", mGet_DongName);

        try
        {
            HttpUtil httpUtil = new HttpUtil("Store","StoreSearchRequest",mStoreArray);
            httpUtil.start();
            httpUtil.join();

            JSONArray mStoreResult = httpUtil.getResponse();
            int size = mStoreResult.length();
            Log.d(TAG, "SIZE :" + size);

            if(mStoreResult.getJSONObject(0).getString("storeName").equals("null")) {
                CheckIsList("결과 항목이 없습니다.");
            } else {
                for (int i = 0; i < size; i++) {

                    String storeName = mStoreResult.getJSONObject(i).getString("storeName");
                    String address = mStoreResult.getJSONObject(i).getString("address");
                    String latitude = mStoreResult.getJSONObject(i).getString("latitude");
                    String longitude = mStoreResult.getJSONObject(i).getString("longitude");
                    mStoreDO.add(new StoreDO(storeName, address, latitude, longitude));
                    Log.d(TAG, "NAME :" + storeName + latitude + longitude);
                }
            }
        }catch (Exception e) {
            e.toString();
        }

        mList_Store.setAdapter(mAdapter_Dong);
    }


    public void Set_AdapterToList() {

        mAdapter_Dong = new DongAdapter(getApplicationContext(), R.layout.list_dong, mStoreDO);
        mList_Store = (ListView) findViewById(R.id.mList_Store);

        mList_Store.setAdapter(mAdapter_Dong);
        mList_Store.setOnItemClickListener(ListViewItemClickListener);

        mAdapter_Dong.setOnClickListener(new DongAdapter.onButtonClickListener() {
            @Override
            public void onBtnShowStoreMap(StoreDO item) {

                Intent mSelectStoreMap = new Intent(getApplicationContext(), StoreMapsActivity.class);
                mSelectStoreMap.putExtra("latitude", item.GetLatitude());
                mSelectStoreMap.putExtra("longitude", item.GetLongitude());
                mSelectStoreMap.putExtra("storeName", item.GetStoreName());
                startActivity(mSelectStoreMap);
            }
        });
    }


    private AdapterView.OnItemClickListener ListViewItemClickListener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> adapterView, View clickedView, int position, long id){

            String mText_SelectStore = mStoreDO.get(position).GetStoreName();
            String mText_SelectStoreAddress = mStoreDO.get(position).GetStoreAddress();

            Intent mSelectStore = new Intent (getApplicationContext(), ProductActivity.class);
            mSelectStore.putExtra("StoreName", mText_SelectStore);
            mSelectStore.putExtra("address", mText_SelectStoreAddress );
            mSelectStore.putExtra("GuName", mGet_GuName);
            mSelectStore.putExtra("DongName", mGet_DongName);
            mSelectStore.putExtra("ParentActivity", TAG);
            startActivity(mSelectStore);
        }
    };


    public void CheckIsList(String mMsg) {

        AlertDialog.Builder mCheckComplete = new AlertDialog.Builder(StoreSearchActivity3.this);
        mCheckComplete.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mCheckComplete.setMessage(mMsg);
        mCheckComplete.show();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_search3);
        Get_Intent();
        Init_ID();

        Init_UI();
        Init_Button();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Set_AdapterToList();
        mStoreJSON();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBtn_DongSearch:
                Intent mIntent_DongSearch = new Intent(getApplicationContext(), DongSearchActivity.class);
                mIntent_DongSearch.putExtra("GuName", mGet_GuName);
                mIntent_DongSearch.putExtra("DongName", mGet_DongName);
                startActivity(mIntent_DongSearch);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        getIntent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int mId = item.getItemId();

        if (mId == android.R.id.home) {
            Intent mIntent_Gu = new Intent(getApplicationContext(), StoreSearchActivity2.class);
            mIntent_Gu.putExtra("GuName", mGet_GuName);
            mIntent_Gu.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mIntent_Gu);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
