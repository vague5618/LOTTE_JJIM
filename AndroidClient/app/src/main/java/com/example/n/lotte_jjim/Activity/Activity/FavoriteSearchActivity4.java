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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n.lotte_jjim.Activity.Adapter.StoreAdapter;
import com.example.n.lotte_jjim.Activity.DataObject.StoreDO;
import com.example.n.lotte_jjim.Activity.Util.HttpUtil;
import com.example.n.lotte_jjim.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FavoriteSearchActivity4 extends AppCompatActivity {

    private static final String TAG = "FavoriteSearchActivity4";
    private String mCurrentUserID;

    ArrayList<StoreDO> mStoreDO = new ArrayList<StoreDO>();
    ListView mList_Store;
    StoreAdapter mAdapter_Store;
    String mGet_GuName;
    String mGet_DongName;

    JSONArray mFavoriteArray = new JSONArray();
    JSONArray mFavoriteAddArray = new JSONArray();



    public void Init_ID() {
        SharedPreferences setting = getSharedPreferences("MYID", MODE_PRIVATE);
        mCurrentUserID = setting.getString("ID", "");
    }


    public void Get_Intent() {

        Intent mIntent_Receive = getIntent();
        mGet_GuName = mIntent_Receive.getExtras().getString("GuName");
        mGet_DongName = mIntent_Receive.getExtras().getString("DongName");
    }


    public void Init_UI() {

        setTitle("즐겨찾는 매장 추가");
        ChangeTitleTextSize(16);

        TextView mText_Location = (TextView) findViewById(R.id.mText_Location);
        mText_Location.setText("▼ \"" + mGet_GuName + ">" + mGet_DongName + "\"에 위치한 매장");
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


    public void mStoreJSON() {

        toJSON(mFavoriteArray, "add1", "서울");
        toJSON(mFavoriteArray, "add2", mGet_GuName);
        toJSON(mFavoriteArray, "add3", mGet_DongName);

        try
        {
            HttpUtil httpUtil = new HttpUtil("Store","StoreSearchRequest", mFavoriteArray);
            httpUtil.start();
            httpUtil.join();

            JSONArray mStoreResult = httpUtil.getResponse();
            int size = mStoreResult.length();

            Log.d(TAG, "SIZE :" + size);
            Log.d(TAG, "resultObject" + mStoreResult.getJSONObject(0));

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

        mList_Store.setAdapter(mAdapter_Store);
    }


    public void Set_AdapterToList() {

        mAdapter_Store = new StoreAdapter(getApplicationContext(), R.layout.list_store, mStoreDO);
        mList_Store = (ListView) findViewById(R.id.mList_Store);

        mList_Store.setAdapter(mAdapter_Store);

        mAdapter_Store.setOnClickListener(new StoreAdapter.onButtonClickListener() {
            @Override
            public void onBtnAddStore(StoreDO mPosition) {
                Alert_AddFavorite(mPosition);
            }

            @Override
            public void onBtnShowStoreMap(StoreDO mPosition) {
                Intent mSelectStoreMap = new Intent(getApplicationContext(), StoreMapsActivity.class);
                mSelectStoreMap.putExtra("latitude", mPosition.GetLatitude());
                mSelectStoreMap.putExtra("longitude", mPosition.GetLongitude());
                mSelectStoreMap.putExtra("storeName", mPosition.GetStoreName());
                startActivity(mSelectStoreMap);
            }
        });
    }


    public void Alert_AddFavorite(StoreDO mPosition) {

        final StoreDO m_Position = mPosition;
        AlertDialog.Builder mAlert_AddFavorite= new AlertDialog.Builder(FavoriteSearchActivity4.this);

        mAlert_AddFavorite.setTitle("즐겨찾는 매장 추가")
                .setMessage("즐겨찾는 매장에 \""+ mPosition.GetStoreName() + "\"을 추가하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String FavoriteAddCheck=null;

                        toJSON(mFavoriteAddArray, "userID", mCurrentUserID);
                        toJSON(mFavoriteAddArray, "address", m_Position.GetStoreAddress());
                        toJSON(mFavoriteAddArray, "storeName", m_Position.GetStoreName());
                        Log.d(TAG, "리스트 추가 완료" + m_Position.GetStoreName());

                        try {
                            HttpUtil httpUtil = new HttpUtil("Favorite","FavoriteAddRequest",  mFavoriteAddArray);
                            httpUtil.start();
                            httpUtil.join();
                            Log.d(TAG, "데이터 전송 완료");

                            JSONArray mFavoriteAddResult = httpUtil.getResponse();

                            int size = mFavoriteAddResult.length();

                            for (int i = 0; i < size; i++) {
                                FavoriteAddCheck = mFavoriteAddResult.getJSONObject(i).getString("check");
                                Log.i(TAG, "결과 :" + FavoriteAddCheck);
                            }

                            if(FavoriteAddCheck.equals("true")){
                                Toast.makeText(getApplicationContext(), m_Position.GetStoreName() + "이 즐겨찾기에 추가 되었습니다.", Toast.LENGTH_LONG).show();
                            } else  if(FavoriteAddCheck.equals("false")) {
                                Toast.makeText(getApplicationContext(), "즐겨찾기 등록 실패", Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e) {
                            e.toString();
                        }

                        Intent mIntent_AddFavorite = new Intent(getApplicationContext(), FavoriteActivity.class);
                        mIntent_AddFavorite.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mIntent_AddFavorite.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mIntent_AddFavorite);
                        finish();
                    }})
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        AlertDialog mAlertDialog = mAlert_AddFavorite.create();
        mAlertDialog.show();
    }


    public void CheckIsList(String mMsg) {

        AlertDialog.Builder mCheckComplete = new AlertDialog.Builder(FavoriteSearchActivity4.this);
        mCheckComplete.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mCheckComplete.setMessage(mMsg);
        mCheckComplete.show();
    }



    JSONArray toJSON(JSONArray array, String type, String item) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(type, item);
            array = array.put(obj);
            return array;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_search4);
        Get_Intent();
        Init_ID();

        Init_UI();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Set_AdapterToList();
        mStoreJSON();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int mId = item.getItemId();

        if (mId == android.R.id.home) {
            Intent mIntent_Gu = new Intent(getApplicationContext(), FavoriteSearchActivity3.class);
            mIntent_Gu.putExtra("GuName", mGet_GuName);
            mIntent_Gu.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mIntent_Gu);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
