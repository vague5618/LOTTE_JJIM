package com.example.n.lotte_jjim.Activity.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n.lotte_jjim.Activity.Adapter.DongAdapter;
import com.example.n.lotte_jjim.Activity.Application.MyApplication;
import com.example.n.lotte_jjim.Activity.DataObject.StoreDO;
import com.example.n.lotte_jjim.Activity.Util.HttpUtil;
import com.example.n.lotte_jjim.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class IncheonSearchActivity3 extends AppCompatActivity  implements View.OnClickListener {

    private static final String TAG = "StoreSearchActivity3";

    //:Intent로 받을 항목들
    private String mCurrentUserID;
    private String mGetIntent_DongName;

    ArrayList<StoreDO> mStoreDO = new ArrayList<StoreDO>();
    JSONArray mStoreArray;
    ListView mList_Store;
    DongAdapter mAdapter_Dong;



    public void Get_Intent() {

        Intent mIntent_Receive = getIntent();
        mGetIntent_DongName = mIntent_Receive.getExtras().getString("DongName");


    }

    public void Init_UI() {

        setTitle("지역별 매장 검색");
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

        Button mBtn_DongProductSearch = (Button) findViewById(R.id.mBtn_DongSearch);

        mBtn_DongProductSearch.setOnClickListener(this);
    }


    public void Add_NewDO() {

        //: 인천광역시 부평구 산곡동
        mStoreDO.add(new StoreDO("AAA점", "인천광역시 부평구 산곡동 AAA","",""));
        mStoreDO.add(new StoreDO("BBB점", "인천광역시 부평구 산곡동 BBB", "", ""));
        mStoreDO.add(new StoreDO("CCC점", "인천광역시 부평구 산곡동 CCC", "", ""));
        mStoreDO.add(new StoreDO("DDD점", "인천광역시 부평구 산곡동 DDD", "", ""));
    }


    public void Set_AdapterToList() {

        mAdapter_Dong = new DongAdapter(getApplicationContext(), R.layout.list_dong, mStoreDO);
        mList_Store = (ListView) findViewById(R.id.mList_Store);

        mList_Store.setAdapter(mAdapter_Dong);
        mList_Store.setOnItemClickListener(ListViewItemClickListener);

        mAdapter_Dong.setOnClickListener(new DongAdapter.onButtonClickListener() {
            @Override
            public void onBtnShowStoreMap(StoreDO item) {
                Toast.makeText(getApplicationContext(), item.GetStoreName() + "지도", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void mStoreJSON(){
        mStoreArray = new JSONArray();
        toJSON(mStoreArray, "add1","인천");
        toJSON(mStoreArray, "add2", "부평구");
        toJSON(mStoreArray, "add3", "산곡동");

        try{
            HttpUtil httpUtil = new HttpUtil("Store", "StoreSearchRequest", mStoreArray);
            httpUtil.start();
            httpUtil.join();

            JSONArray mStoreResult = httpUtil.getResponse();
            int size = mStoreResult.length();

            for(int i = 0; i <size;i++){
                String storeName = mStoreResult.getJSONObject(i).getString("storeName");
                String address =mStoreResult.getJSONObject(i).getString("address");
                String latitude =mStoreResult.getJSONObject(i).getString("latitude");
                String longitude =mStoreResult.getJSONObject(i).getString("longitude");
                mStoreDO.add(new StoreDO(storeName, address, latitude, longitude));
                Log.d(TAG, "NAME :" + storeName);
            }

        }catch(Exception e){
            e.toString();
        }
        mList_Store.setAdapter(mAdapter_Dong);
    }


    private AdapterView.OnItemClickListener ListViewItemClickListener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> adapterView, View clickedView, int position, long id){

            String mText_SelectStore = mStoreDO.get(position).GetStoreName();
            String mText_SelectStoreAddress = mStoreDO.get(position).GetStoreAddress();

            Intent mSelectStore = new Intent (getApplicationContext(), ProductActivity.class);
            mSelectStore.putExtra("StoreName", mText_SelectStore);   //rasp: 동의 여러가지 점포 중 하나를 선택할 때 그 점포 이름 넘기기
            mSelectStore.putExtra("address", mText_SelectStoreAddress );
            startActivity(mSelectStore);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incheon_search3);
        mCurrentUserID  = ((MyApplication)getApplicationContext()).GetUserID();
        Get_Intent();

        Init_UI();
        Init_Button();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Add_NewDO();
        Set_AdapterToList();
        mStoreJSON();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBtn_DongSearch:
                Intent mIntent_DongSearch = new Intent(getApplicationContext(), DongSearchActivity.class);
                mIntent_DongSearch.putExtra("DongName", mGetIntent_DongName);
                startActivity(mIntent_DongSearch);
                break;
        }
    }


    public JSONArray toJSON(JSONArray array, String type, String item){

        JSONObject obj = new JSONObject();
        try{
            obj.put(type, item);
            array = array.put(obj);
            return array;
        }
        catch(Exception e){
            return null;
        }
    }
}
