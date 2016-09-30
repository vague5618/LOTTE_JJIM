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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n.lotte_jjim.Activity.Adapter.OrderListAdapter;
import com.example.n.lotte_jjim.Activity.DataObject.OrderListDO;
import com.example.n.lotte_jjim.Activity.Util.HttpUtil;
import com.example.n.lotte_jjim.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class OrderListActivity extends AppCompatActivity {

    private static final String TAG = "OrderListActivity";
    public static String mCurrentUserID;

    JSONArray mOrderListArray;
    JSONArray mOrderListDeleteArray;
    ArrayList<OrderListDO> mOrderListDO = new ArrayList<OrderListDO>();
    ListView mList_OrderList;
    OrderListAdapter mAdapter_OrderList;

    String deleteCheck; //예약 내역 삭제 됬는지 체크하기 위한 변수
    OrderListDO temp_position; // DeleteJSON에서 position변수 받기 위해 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        Init_ID();

        Init_UI();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Set_AdapterToList();
        mOrderListJSON();
    }

    public void Init_ID(){
        SharedPreferences setting = getSharedPreferences("MYID", MODE_PRIVATE);
        mCurrentUserID = setting.getString("ID","");
    }


    public void Init_UI() {

        setTitle("예약 내역 확인하기");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int mId = item.getItemId();

        if (mId == android.R.id.home) {

            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Set_AdapterToList() {

        mList_OrderList = (ListView)findViewById(R.id.mList_OrderList);
        mAdapter_OrderList = new OrderListAdapter(getApplicationContext(), R.layout.list_orderlist, mOrderListDO);

        mList_OrderList.setAdapter(mAdapter_OrderList);
        mList_OrderList.setOnItemClickListener(ListViewItemClickListener);

        mAdapter_OrderList.setOnClickListener(new OrderListAdapter.onButtonClickListener() {
            @Override
            public void onBtnConfirmOrderList(OrderListDO item) {
                ShowAlertDialog(item, "상품 수령확인", "상품을 수령한 것에 동의합니다.\n확인 후 상품 재수령은 불가합니다.", 0);
            }

            @Override
            public void onBtnDeleteOrderList(OrderListDO item) {
                ShowAlertDialog(item, "예약 취소", "예약 주문 결제를 취소하시겠습니까?", 1);
            }
        });
    }


    public void ShowAlertDialog(OrderListDO mPosition, String mTitle, String mMsg, final int mCase) {

        final OrderListDO m_OrderListDO = mPosition;
        temp_position=mPosition;
        AlertDialog.Builder mAlert_CannotReservation = new AlertDialog.Builder(OrderListActivity.this);

        mAlert_CannotReservation.setTitle(mTitle)
                .setMessage(mMsg)
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mOrderListDeleteJSON(temp_position, mCase);
//                        String mSecondMsg = null;

//                        if(mCase == 0) {
//                            mSecondMsg = "상품 수령이 완료되었습니다.";
//
//                        } else if(mCase == 1) {
//                            mSecondMsg = "주문 예약이 취소되었습니다.";
//                        }
//                        CheckState(mSecondMsg);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        AlertDialog mAlertDialog = mAlert_CannotReservation.create();
        mAlertDialog.show();
    }

    public void CheckState(String mMsg) {

        AlertDialog.Builder mCheckComplete = new AlertDialog.Builder(OrderListActivity.this);
        mCheckComplete.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mCheckComplete.setMessage(mMsg);
        mCheckComplete.show();
    }


    private AdapterView.OnItemClickListener ListViewItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View clickedView, int position, long id){

            String mIntent_OrderNo = mOrderListDO.get(position).GetOrderNo();

            Intent mIntent_OrderDetail = new Intent(adapterView.getContext(), OrderDetailActivity.class);
            mIntent_OrderDetail.putExtra("OrderNo", mIntent_OrderNo);
            startActivity(mIntent_OrderDetail);
        }
    };


    public void  mOrderListJSON(){
//====TODO 아이디 진짜 로그인 아이디로 바꾸기

        mOrderListArray  = new JSONArray();
        toJSON(mOrderListArray, "userID", mCurrentUserID);
        Log.d(TAG, mCurrentUserID + "의 예약 내역 조회중");

        try {

            HttpUtil httpUtil = new HttpUtil("Ordering","OrderCompleteRequest",  mOrderListArray);

            httpUtil.start();

            httpUtil.join();

            JSONArray mOrderCompleteResult = httpUtil.getResponse();

            int size = mOrderCompleteResult.length();

            Log.d(TAG, "OrderList SIZE :" + size);

//          (2/19) rasp:  리스트 정보가 없으면 없다고 알림창
            if(mOrderCompleteResult.getJSONObject(0).getString("orderNum").equals("null")) {
                CheckIsList("예약된 내역이 없습니다.");
            } else {
                for (int i = 0; i < size; i++) {
                    String orderNum = mOrderCompleteResult.getJSONObject(i).getString("orderNum");
                    String storeName = mOrderCompleteResult.getJSONObject(i).getString("storeName");
                    String fullCost = mOrderCompleteResult.getJSONObject(i).getString("fullCost");
                    mOrderListDO.add(new OrderListDO(orderNum, storeName, fullCost));

                }
            }
        }catch (Exception e) {
            e.toString();
        }
            mAdapter_OrderList.notifyDataSetChanged();
    }

    JSONArray toJSON(JSONArray array, String type, String item){
        JSONObject obj = new JSONObject();
        try {
            obj.put(type, item);
            Log.d(TAG,"item = "+item);
            array = array.put(obj);
            return array;
        }catch(Exception e){
            return null;
        }
    }


    public void CheckIsList(String mMsg) {

        AlertDialog.Builder mCheckComplete = new AlertDialog.Builder(OrderListActivity.this);
        mCheckComplete.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mCheckComplete.setMessage(mMsg);
        mCheckComplete.show();
    }

   //수령완료, 삭제 신청 시 사용
    public void mOrderListDeleteJSON(OrderListDO position, int mCase){

        mOrderListDeleteArray  = new JSONArray();
       String RequestName = null;
        toJSON(mOrderListDeleteArray, "userID", mCurrentUserID);
        toJSON(mOrderListDeleteArray, "orderNum", position.GetOrderNo());

        Log.d(TAG, mOrderListDeleteArray + "");


        try {

            if(mCase==0){RequestName="OrderDeleteRequest";}
            else if(mCase==1){RequestName="OrderCancelRequest";}

            HttpUtil httpUtil = new HttpUtil("Ordering",RequestName,  mOrderListDeleteArray);

            httpUtil.start();

            httpUtil.join();

            JSONArray mOrderDeleteResult = httpUtil.getResponse();

            int size = mOrderDeleteResult.length();

            Log.d(TAG, "삭제 결과 전송 받음");
            Log.d(TAG, "결과" + mOrderDeleteResult);

                for (int i = 0; i < size; i++) {
                    deleteCheck = mOrderDeleteResult.getJSONObject(i).getString("check");
                }



            if(deleteCheck.equals("true")){
                String mSecondMsg = null;
                if(mCase == 0) {
                    mSecondMsg = "상품 수령이 완료되었습니다.";
                    mOrderListDO.remove(position);

                    mAdapter_OrderList.notifyDataSetChanged();

                } else if(mCase == 1) {
                    mOrderListDO.remove(position);

                    mAdapter_OrderList.notifyDataSetChanged();
                    mSecondMsg = "주문 예약이 취소되었습니다.";
                }
                CheckState(mSecondMsg);
            }else if(deleteCheck.equals("false")){
                Toast.makeText(getApplicationContext(),"트랜젝션 실패",Toast.LENGTH_LONG);
            }
            }
    catch (Exception e) {
            e.toString();
        }

    }

}