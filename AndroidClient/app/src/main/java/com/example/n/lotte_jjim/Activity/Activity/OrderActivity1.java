package com.example.n.lotte_jjim.Activity.Activity;

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

import com.example.n.lotte_jjim.Activity.Adapter.OrderAdapter;
import com.example.n.lotte_jjim.Activity.DataObject.OrderDO;
import com.example.n.lotte_jjim.Activity.GetInfo.GetOrderInfo;
import com.example.n.lotte_jjim.Activity.Util.HttpUtil;
import com.example.n.lotte_jjim.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class OrderActivity1 extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "OrderActivity1";
    public static final String mPackageName = "com.example.n.lotte_jjim";
    public static String mCurrentUserID;

    ArrayList<OrderDO> mOrderDO = new ArrayList<OrderDO>();
    ArrayList<GetOrderInfo> mSelectProductArrayList;
    OrderAdapter mAdapter_Order;
    ListView mList_Order;
    String mGetIntent_ParentActivity;
    String mGetIntent_StoreName;
    String mGetIntent_address;
    String mGetIntent_GuName;
    String mGetIntent_DongName;
    String mGetIntent_ProductParent;
    String mGetIntent_ProductPrice;
    String mGetIntent_ProductName;
    String mGetIntent_ProductID;
    String mGps_Latitude;
    String mGps_Longitude;

    JSONArray mOrderingArray;
    JSONArray mOrderArray;
    JSONArray mOrderNumArray;
    JSONArray mOrderDetailArray;

    String OrderNumber;
    String OrderCheck;
    String OrderDetailCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order1);
        Init_ID();

        Init_UI();
        Init_Button();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Get_Intent();
        Set_AdapterToList();
    }

    public void Init_ID() {
        SharedPreferences setting = getSharedPreferences("MYID", MODE_PRIVATE);
        mCurrentUserID = setting.getString("ID", "");
    }


    public void Init_UI() {
        setTitle("상품 예약하기");
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

        Button mBtn_OrderSuccess = (Button) findViewById(R.id.mBtn_OrderSuccess);
        Button mBtn_OrderCancel = (Button) findViewById(R.id.mBtn_OrderCancel);

        mBtn_OrderSuccess.setOnClickListener(this);
        mBtn_OrderCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mBtn_OrderSuccess:
                Intent mIntent_OrderSuccess = new Intent(getApplicationContext(), OrderFinishActivity.class);


                mOrderNumJSON();

                if(OrderNumber !=null) {
                    mOrderingJSON();

                    if (OrderCheck.equals("true")) {
                        mOrderDetailJSON();
                    }else{
                        Log.d(TAG, "예약 문제 발생");
                    }
                }else{
                    Log.d(TAG, "예약 번호 못 받아옴");
                }

                if(OrderDetailCheck.equals("true")){
                mIntent_OrderSuccess.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mIntent_OrderSuccess.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent_OrderSuccess);
                finish();}

                break;
            case R.id.mBtn_OrderCancel:
                finish();
                break;
        }
    }


    public void Set_AdapterToList() {

        mAdapter_Order = new OrderAdapter(getApplicationContext(), R.layout.list_order, mOrderDO);
        mList_Order = (ListView) findViewById(R.id.mList_Order);

        mList_Order.setAdapter(mAdapter_Order);
        mList_Order.setOnItemClickListener(ListViewItemClickListener);
    }

    private AdapterView.OnItemClickListener ListViewItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View clickedView, int position, long id) {

        }
    };


    public void Get_Intent() {

        //rasp: 액티비티 인텐트를 위해 필요한 변수들
        mGetIntent_ParentActivity = getIntent().getExtras().getString("ParentActivity");
        mGetIntent_StoreName = getIntent().getExtras().getString("StoreName");
        mGetIntent_address = getIntent().getExtras().getString("address");
        mGetIntent_GuName = getIntent().getExtras().getString("GuName");
        mGetIntent_DongName = getIntent().getExtras().getString("DongName");
        mGetIntent_ProductParent = getIntent().getExtras().getString("ProductParent");
        mGetIntent_ProductPrice = getIntent().getExtras().getString("ProductPrice");
        mGetIntent_ProductName = getIntent().getExtras().getString("ProductName");
        mGetIntent_ProductID = getIntent().getExtras().getString("ProductID");
        mGps_Latitude = getIntent().getExtras().getString("GpsLatitude");
        mGps_Longitude = getIntent().getExtras().getString("GpsLongitude");

        mSelectProductArrayList = getIntent().getParcelableArrayListExtra("OrderInfo");


        for (int i = 0; i < mSelectProductArrayList.size(); i++) {

            GetOrderInfo tmpOrderInfo = mSelectProductArrayList.get(i);
            int tmpProductImage = getResources().getIdentifier("a" + tmpOrderInfo.GetProductId(), "drawable", mPackageName);

            mOrderDO.add(new OrderDO(tmpOrderInfo.GetProductId(), tmpOrderInfo.GetStoreName(), tmpOrderInfo.GetProductName(),
                    tmpOrderInfo.GetProductPrice(), tmpOrderInfo.GetProductStock(), tmpOrderInfo.GetProductEvent(), tmpProductImage));
        }


    }



    //오더넘버 받는 요청


    public void mOrderNumJSON(){
        mOrderNumArray = new JSONArray();
        OrderNumber = null;
        toJSON(mOrderNumArray, "userID", mCurrentUserID);

        try {
            HttpUtil httpUtil = new HttpUtil("Ordering","OrderNumRequest",  mOrderNumArray);
            httpUtil.start();
            httpUtil.join();
            Log.d(TAG, "데이터 전송 완료");

            JSONArray mOrderNumResult = httpUtil.getResponse();

            int size = mOrderNumResult.length();
            Log.d(TAG, size + "개의 데이터 수신 완료");
            for (int i = 0; i < size; i++) {
                OrderNumber = Integer.toString(mOrderNumResult.getJSONObject(i).getInt("orderNumber"));
                Log.i(TAG, "결과 :" + OrderNumber);
            }
        }catch (Exception e) {
            e.toString();
        }
    }

    //오더넘버와 함께 주문 정보 넘김
    public void mOrderingJSON(){
        mOrderArray = new JSONArray();
        OrderCheck=null;
       Log.d(TAG, OrderNumber+ mCurrentUserID);
        toJSON(mOrderArray, "orderNumber", OrderNumber);
        toJSON(mOrderArray, "userID", mCurrentUserID);
        toJSON(mOrderArray, "storeName", mOrderDO.get(0).GetOrderStoreName());
        toJSON(mOrderArray, "cost",  Cost());



        try {
            HttpUtil httpUtil = new HttpUtil("Ordering","OrderRequest",  mOrderArray);
            httpUtil.start();
            httpUtil.join();
            Log.d(TAG, "데이터 전송 완료");

            JSONArray mOrderResult = httpUtil.getResponse();

            int size = mOrderResult.length();
            Log.d(TAG, size + "개의 데이터 수신 완료");
            for (int i = 0; i < size; i++) {
                OrderCheck = mOrderResult.getJSONObject(i).getString("check");
                Log.i(TAG, "결과 :" + OrderCheck);
            }
        }catch (Exception e) {
            e.toString();
        }


    }




//총 가격 계산

    String Cost(){
        int size = mOrderDO.size();
        int  fullCost_tmp = 0;
        for(int i = 0; i< size ; i++){
            fullCost_tmp = fullCost_tmp + Integer.valueOf(mOrderDO.get(i).GetOrderProductPrice()) *Integer.valueOf(mOrderDO.get(i).GetOrderInputNum()) ;}
        String fullCost = String.valueOf(fullCost_tmp);
        Log.d(TAG, "총 가격 :" + fullCost);
        return fullCost;

    }



    public void mOrderDetailJSON(){


        mOrderDetailArray = new JSONArray();
        OrderDetailCheck=null;
        int size = mOrderDO.size();

            try {

                for (int i = 0; i < size; i++) {
                    JSONObject oneitem_obj = new JSONObject();
                    oneitem_obj.put("orderNumber",OrderNumber );
                    oneitem_obj.put("storeName", mOrderDO.get(i).GetOrderStoreName());
                    oneitem_obj.put("productName", mOrderDO.get(i).GetOrderProductName());
                    oneitem_obj.put("orderingQuantity", mOrderDO.get(i).GetOrderInputNum());
                    oneitem_obj.put("salesPrice", mOrderDO.get(i).GetOrderProductPrice());

                    mOrderDetailArray = mOrderDetailArray.put(oneitem_obj);
                }
                Log.d(TAG, mOrderDetailArray+"");
                HttpUtil httpUtil = new HttpUtil("OrderDetail","OrderDetailRequest",  mOrderDetailArray);
                httpUtil.start();
                httpUtil.join();
                Log.d(TAG, "데이터 전송 완료");

                JSONArray mOrderDetailResult = httpUtil.getResponse();

                int size_detail = mOrderDetailResult.length();
                Log.d(TAG, size_detail + "개의 데이터 수신 완료");
                for (int i = 0; i < size; i++) {
                    OrderDetailCheck = mOrderDetailResult.getJSONObject(i).getString("check");
                    Log.i(TAG, "결과 :" + OrderDetailCheck);


            } }catch (Exception e) {

            }




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
    public boolean onOptionsItemSelected(MenuItem item) {

        int mId = item.getItemId();

        if (mId == android.R.id.home) {

            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}





