package com.example.n.lotte_jjim.Activity.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.n.lotte_jjim.Activity.Adapter.OrderDetailAdapter;
import com.example.n.lotte_jjim.Activity.DataObject.OrderDetailDO;
import com.example.n.lotte_jjim.Activity.Util.HttpUtil;
import com.example.n.lotte_jjim.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {

    private static final String TAG = "OrderDetailActivity";
    public static final String mPackageName = "com.example.n.lotte_jjim";
    public static String mCurrentUserID;
    private String mGetIntent_OrderNum;

    ArrayList<OrderDetailDO> mOrderDetailDO = new ArrayList<OrderDetailDO>();
    ListView mList_OrderDetail;
    OrderDetailAdapter mAdapter_OrderDetail;


    JSONArray mOrderDetailArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Init_ID();
        Get_Intent();
        Init_UI();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Set_AdapterToList();
        mOrderDetailJSON();
    }

    public void Init_ID(){
        SharedPreferences setting = getSharedPreferences("MYID", MODE_PRIVATE);
        mCurrentUserID = setting.getString("ID","");
    }


    public void Init_UI() {

        setTitle("예약 세부내용 확인");
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


    public void Set_AdapterToList() {

        mList_OrderDetail = (ListView)findViewById(R.id.mList_OrderDetail);
        mAdapter_OrderDetail = new OrderDetailAdapter(getApplicationContext(), R.layout.list_orderdetail, mOrderDetailDO);

        mList_OrderDetail.setAdapter(mAdapter_OrderDetail);
    }





    public void  mOrderDetailJSON() {

        mOrderDetailArray = new JSONArray();
        toJSON(mOrderDetailArray, "orderNum", mGetIntent_OrderNum);

        try {
            HttpUtil httpUtil = new HttpUtil("OrderDetail","OrderDetailCompleteRequest",  mOrderDetailArray);
            httpUtil.start();
            httpUtil.join();
            Log.d(TAG, "데이터 전송 완료");

            JSONArray mOrderDetailResult = httpUtil.getResponse();

            Log.d(TAG, " "+ mOrderDetailResult.getJSONObject(0));
            int size = mOrderDetailResult.length();
            Log.d(TAG, "결과 :" + mOrderDetailResult);


            for (int i = 0; i < size; i++) {

                String mEvent = mOrderDetailResult.getJSONObject(i).getString("eventText");
                String mSalesPrice = mOrderDetailResult.getJSONObject(i).getString("salesPrice");
                String mOrderQuantity = mOrderDetailResult.getJSONObject(i).getString("orderQuantity");
                String mProductID = mOrderDetailResult.getJSONObject(i).getString("productID");
                String mProductName = mOrderDetailResult.getJSONObject(i).getString("productName");

                int tmpProductImage = getResources().getIdentifier("a" + mProductID, "drawable", mPackageName);
                mOrderDetailDO.add(new OrderDetailDO(mProductID, mProductName, mSalesPrice, mEvent, mOrderQuantity, tmpProductImage));
            }
        }
        catch (Exception e) {
            e.toString();
        }

        mAdapter_OrderDetail.notifyDataSetChanged();
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


    public void Get_Intent() {

        Intent mIntent_Receive = getIntent();
        mGetIntent_OrderNum = mIntent_Receive.getExtras().getString("OrderNo");
        Log.d(TAG,mGetIntent_OrderNum);
    }
}
