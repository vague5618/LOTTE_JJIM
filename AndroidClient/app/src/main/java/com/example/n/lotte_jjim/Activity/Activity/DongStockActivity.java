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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n.lotte_jjim.Activity.Adapter.StockAdapter;
import com.example.n.lotte_jjim.Activity.DataObject.StockDO;
import com.example.n.lotte_jjim.Activity.GetInfo.GetOrderInfo;
import com.example.n.lotte_jjim.Activity.Util.HttpUtil;
import com.example.n.lotte_jjim.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class DongStockActivity extends AppCompatActivity {

    private static final String TAG = "DongStockActivity";
    public static final String mPackageName = "com.example.n.lotte_jjim";
    public static String  mCurrentUserID;

    private StockDO temp_position;

    ArrayList<StockDO> mStockDO = new ArrayList<StockDO>();
    ImageView mImage_CurrentProductImg;
    TextView mText_OriginPrice;
    TextView mText_CurrentProduct;
    ListView mList_Stock;
    StockAdapter mAdapter_Stock;

    String mGet_ProductPrice;
    String mGetIntent_GuName;
    String mGetIntent_DongName;
    String mGetIntent_ProductID;
    String mGetIntent_ProductName;
    ArrayList <GetOrderInfo> mPushIntent_Order;

    JSONArray mDongStockArray;
    JSONArray mJJimAddArray;



    public void Get_Intent() {

        Intent mIntent_Receive = getIntent();
        mGet_ProductPrice = mIntent_Receive.getExtras().getString("ProductPrice");
        mGetIntent_ProductName = mIntent_Receive.getExtras().getString("ProductName");
        mGetIntent_GuName = mIntent_Receive.getExtras().getString("GuName");
        mGetIntent_DongName = mIntent_Receive.getExtras().getString("DongName");
        mGetIntent_ProductID = mIntent_Receive.getExtras().getString("ProductID");
    }


    public void Init_UI() {

        setTitle("동별 상품 검색");
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


    public void findView(){

        mImage_CurrentProductImg = (ImageView) findViewById(R.id.mImage_CurrentProductImg);
        mText_OriginPrice= (TextView) findViewById(R.id.mText_OriginPrice);
        mText_CurrentProduct = (TextView)findViewById(R.id.mText_CurrentProduct);
    }


    public void Set_AdapterToList() {

        mAdapter_Stock = new StockAdapter(getApplicationContext(), R.layout.list_dong_stock, mStockDO);
        mList_Stock = (ListView)findViewById(R.id.mList_DongStore);

        mList_Stock.setAdapter(mAdapter_Stock);
        mList_Stock.setOnItemClickListener(ListViewItemClickListener);

        mAdapter_Stock.setOnClickListener(new StockAdapter.onButtonClickListener() {
            @Override
            public void onBtnJjim(StockDO item) {

                int mStock_SelectStore = Integer.parseInt(item.GetStoreStock());
                if (mStock_SelectStore != 0) {
                    JJimImpossible();
                } else {
                    JJimPossible(item);
                }
            }

            @Override
            public void onBtnShowFavoriteMap(StockDO item) {
                Toast.makeText(getApplicationContext(), item.GetStoreName() + "지도", Toast.LENGTH_SHORT).show();
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

            mPushIntent_Order = new ArrayList<GetOrderInfo>();

            String tmp_StoreName = mStockDO.get(position).GetStoreName();
            String tmp_ProductPrice = mStockDO.get(position).GetProductPrice();
            String tmp_StoreEvent = mStockDO.get(position).GetStoreEvent();
            String tmp_StoreStock = mStockDO.get(position).GetStoreStock();

            int mStock_SelectStoreInt = Integer.parseInt(tmp_StoreStock);

            if (mStock_SelectStoreInt == 0) {
                String mMsg = "재고가 없는 상품입니다.";
                CheckState(mMsg);
            } else {
                mPushIntent_Order.add(new GetOrderInfo(tmp_StoreName, mGetIntent_ProductID, mGetIntent_ProductName, tmp_ProductPrice, tmp_StoreEvent, tmp_StoreStock));

                Intent mIntent_SelectStore = new Intent(getApplicationContext(), OrderActivity1.class);
                mIntent_SelectStore.putParcelableArrayListExtra("OrderInfo", mPushIntent_Order);
                mIntent_SelectStore.putExtra("ParentActivity", TAG);

                mIntent_SelectStore.putExtra("StoreName", "");
                mIntent_SelectStore.putExtra("address", "");
                mIntent_SelectStore.putExtra("GuName", mGetIntent_GuName);
                mIntent_SelectStore.putExtra("DongName", mGetIntent_DongName);
                mIntent_SelectStore.putExtra("ProductParent", "");
                mIntent_SelectStore.putExtra("ProductName", mGetIntent_ProductName);
                mIntent_SelectStore.putExtra("ProductPrice", mGet_ProductPrice);
                mIntent_SelectStore.putExtra("ProductID", mGetIntent_ProductID);
                mIntent_SelectStore.putExtra("GpsLatitude", "");
                mIntent_SelectStore.putExtra("GpsLongitude", "");
                startActivity(mIntent_SelectStore);
            }
        }
    };


    public void  mDongStockJSON() {

        mDongStockArray = new JSONArray();

        toJSON( mDongStockArray, "productName", mGetIntent_ProductName);
        toJSON( mDongStockArray, "add1", "서울");
        toJSON(mDongStockArray, "add2", mGetIntent_GuName);
        toJSON(mDongStockArray, "add3", mGetIntent_DongName);

        try {
            HttpUtil httpUtil = new HttpUtil("Stock","DongStockRequest", mDongStockArray);
            httpUtil.start();
            httpUtil.join();

            JSONArray mDongStockResult = httpUtil.getResponse();

            int size = mDongStockResult.length();
             Log.d(TAG, "SIZE :" + size);

            mImage_CurrentProductImg.setImageResource(getResources().getIdentifier("a" + mGetIntent_ProductID, "drawable", mPackageName));
            mText_CurrentProduct.setText(mGetIntent_ProductName);
            mText_OriginPrice.setText(mGet_ProductPrice);

            if(mDongStockResult.getJSONObject(0).getString("productName").equals("null")) {
                CheckIsList("결과 항목이 없습니다.");
            } else {
                for (int i = 0; i < size; i++) {
                    String storeName = mDongStockResult.getJSONObject(i).getString("storeName");
                    String address = mDongStockResult.getJSONObject(i).getString("address");
                    String productStock = mDongStockResult.getJSONObject(i).getString("productStock");
                    String eventText = mDongStockResult.getJSONObject(i).getString("eventText");
                    String productName = mDongStockResult.getJSONObject(i).getString("productName");
                    String productPrice = mDongStockResult.getJSONObject(i).getString("price");
                    String salesPrice = mDongStockResult.getJSONObject(i).getString("salesPrice");
                    String latitude = mDongStockResult.getJSONObject(i).getString("latitude");
                    String longitude = mDongStockResult.getJSONObject(i).getString("longitude");

                    mStockDO.add(new StockDO(productName, storeName, address, eventText, productStock, productPrice, salesPrice, latitude, longitude));
                    Log.d(TAG, "NAME :" + storeName);
                }
            }
        }catch (Exception e) {
            e.toString();
        }
        mAdapter_Stock.notifyDataSetChanged();
    }


    public void  mJJimAddJSON(StockDO position) {

        Log.d(TAG, mCurrentUserID + position.GetStoreAddress() + position.GetProductName());
        String JJimAddCheck = null;

        if (mCurrentUserID != null) {
            mJJimAddArray = new JSONArray();
            toJSON(mJJimAddArray, "userID",mCurrentUserID);
            toJSON(mJJimAddArray, "productName", position.GetProductName());
            toJSON(mJJimAddArray, "address", position.GetStoreAddress() );
        }

        try {
            HttpUtil httpUtil = new HttpUtil("Wait", "JJimAddRequest", mJJimAddArray);
            Log.d(TAG, "데이터 전송 완료");
            httpUtil.start();
            httpUtil.join();

            JSONArray JJimAddResult = httpUtil.getResponse();
            int size = JJimAddResult.length();

            for (int i = 0; i < size; i++) {
                JJimAddCheck = JJimAddResult.getJSONObject(i).getString("check");
                Log.i(TAG, "결과 :" + JJimAddCheck);
            }
            if (JJimAddCheck.equals("true")) {
                String mMsg = "찜 푸시 리스트에 추가되었습니다.";
                CheckState(mMsg);
            }

        } catch (Exception e) {
            e.toString();
        }
    }


    public void JJimImpossible() {

        AlertDialog.Builder mAlert_JJimImpossible= new AlertDialog.Builder(DongStockActivity.this);

        mAlert_JJimImpossible.setTitle("찜 불가 상품")
                .setMessage("점포에 상품이 남아있습니다.\n재고가 없을 경우에 찜 푸시를 받을 수 있습니다." )
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        mAlert_JJimImpossible.show();
    }


    public void JJimPossible(StockDO mPosition) {
        temp_position = mPosition;
        AlertDialog.Builder mAlert_CannotReservation = new AlertDialog.Builder(DongStockActivity.this);

        mAlert_CannotReservation.setTitle("찜 푸시 받기")
                .setMessage("상품 입고시 찜 푸시를 받으시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mJJimAddJSON(temp_position);
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

        AlertDialog.Builder mCheckComplete = new AlertDialog.Builder(DongStockActivity.this);
        mCheckComplete.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mCheckComplete.setMessage(mMsg);
        mCheckComplete.show();
    }


    public void CheckIsList(String mMsg) {

        AlertDialog.Builder mCheckComplete = new AlertDialog.Builder(DongStockActivity.this);
        mCheckComplete.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mCheckComplete.setMessage(mMsg);
        mCheckComplete.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dong_stock);
        Init_ID();
        Get_Intent();

        findView();
        Init_UI();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Set_AdapterToList();
        mDongStockJSON();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int mId = item.getItemId();

        if (mId == android.R.id.home) {
            Intent mIntent_Gu = new Intent(getApplicationContext(), DongSearchActivity.class);
            mIntent_Gu.putExtra("GuName", mGetIntent_GuName);
            mIntent_Gu.putExtra("DongName", mGetIntent_DongName);
            mIntent_Gu.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mIntent_Gu);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void Init_ID(){
        SharedPreferences setting = getSharedPreferences("MYID", MODE_PRIVATE);
        mCurrentUserID = setting.getString("ID","");
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
}
