package com.example.n.lotte_jjim.Activity.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.n.lotte_jjim.Activity.Adapter.SectionsPagerAdapter;
import com.example.n.lotte_jjim.Activity.DataObject.ProductDO;
import com.example.n.lotte_jjim.Activity.GetInfo.GetOrderInfo;
import com.example.n.lotte_jjim.Activity.Util.HttpUtil;
import com.example.n.lotte_jjim.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ProductActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ProductActivity";
    public static final String mPackageName = "com.example.n.lotte_jjim";

    public static String mGetIntent_StoreName;
    public static String mGetIntent_StoreAddress;
    public static String mGetIntent_ParentActivity;
    public static String mGetIntent_GuName;
    public static String mGetIntent_DongName;
    public static ArrayList<GetOrderInfo> mSelectProductList;
    public static FloatingActionButton fab;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    public static String mCurrentUserID;
    public static ArrayList<ArrayList<ProductDO>> productDOArrayList;

    public static ProgressDialog progressDialog;
    public static final int PROGRESS_DIALOG = 1001;
    public static UIHandler mHandler;
    JSONArray mOrderNumArray;
    String OrderNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Get_Intent();
        Init_ID();

        productDOArrayList = new ArrayList<ArrayList<ProductDO>>();
        mHandler = new UIHandler();


        Init_UI();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Init_TabView();
        Init_Fab();

        Thread mProduct_thread = new getProductData_Thread();
        mProduct_thread.start();

        showDialog(PROGRESS_DIALOG);
    }
    
    public void Init_ID(){
        SharedPreferences setting = getSharedPreferences("MYID", MODE_PRIVATE);
        mCurrentUserID = setting.getString("ID","");
    }

    public void Get_Intent() {

        Intent mIntent_Receive = getIntent();
        mGetIntent_StoreName = mIntent_Receive.getExtras().getString("StoreName");
        mGetIntent_StoreAddress = mIntent_Receive.getExtras().getString("address");
        mGetIntent_GuName = mIntent_Receive.getExtras().getString("GuName");
        mGetIntent_DongName = mIntent_Receive.getExtras().getString("DongName");
        mGetIntent_ParentActivity = mIntent_Receive.getExtras().getString("ParentActivity");

        mSelectProductList = new ArrayList<GetOrderInfo>();
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    public void Init_UI() {

        this.setTitle("상품 재고현황");
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


    public void Init_TabView() {
        //: tab별로 각각에 해당하는 fragment 생성
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        //: 섹션 어댑터로 뷰를 구성
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorOrange));
        tabLayout.setTabTextColors(Color.WHITE, getResources().getColor(R.color.colorOrange));
    }

    public class getProductData_Thread extends Thread {

        public void run() {
            super.run();
            Init_GetProductData();
            mHandler.sendEmptyMessage(0);
        }
    }

    public class UIHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    // 02.24_rasp TODO! 에러 자주 발생하는 부분
                    mSectionsPagerAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                    break;

                case 1:
                    mSectionsPagerAdapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }
        }
    };

    public void Init_GetProductData()
    {
        for(int i=0; i<6; i++)
        {
           productDOArrayList.add(mProductJSON(i));
        }
    }

    public void Init_Fab() {

        fab.hide();
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab:
                Intent mIntent_Fab = new Intent(getApplicationContext(), OrderActivity1.class);
                mIntent_Fab.putParcelableArrayListExtra("OrderInfo", mSelectProductList);
                mIntent_Fab.putExtra("ParentActivity", TAG);
                mIntent_Fab.putExtra("StoreName", mGetIntent_StoreName);
                mIntent_Fab.putExtra("address", mGetIntent_StoreAddress);
                mIntent_Fab.putExtra("GuName", mGetIntent_GuName);
                mIntent_Fab.putExtra("DongName", mGetIntent_DongName);
                mIntent_Fab.putExtra("ProductParent", mGetIntent_ParentActivity);
                mIntent_Fab.putExtra("ProductName", "");
                mIntent_Fab.putExtra("ProductPrice", "");
                mIntent_Fab.putExtra("ProductID", "");
                mIntent_Fab.putExtra("GpsLatitude", "");
                mIntent_Fab.putExtra("GpsLongitude", "");
                mIntent_Fab.putExtra("OrderNum", OrderNumber);

                startActivity(mIntent_Fab);
                break;
        }
    }


    public ArrayList<ProductDO> mProductJSON(int tab_num) {

        String Request_Type="";
        JSONArray mProdcutArray = new JSONArray();
        ArrayList<ProductDO> ProductDOList = new ArrayList<ProductDO>();

        switch(tab_num){
            case 0:
                Request_Type = "EventStockRequest";
                break;
            case 1:
                Request_Type = "DosirakStockRequest";
                break;
            case 2:
                Request_Type = "SnackStockRequest";
                break;
            case 3:
                Request_Type = "BeverageStockRequest";
                break;
            case 4:
                Request_Type = "CandyStockRequest";
                break;
            case 5:
                Request_Type = "LivingStockRequest";
                break;
        }

//      yena:  Request할 주소, 점포명의 데이터 수정
        toJSON(mProdcutArray, "address", mGetIntent_StoreAddress);
        toJSON(mProdcutArray, "storeName", mGetIntent_StoreName);


        try {
            HttpUtil httpUtil = new HttpUtil("Stock", Request_Type, mProdcutArray);
            httpUtil.start();
            httpUtil.join();

            JSONArray ProductResult = httpUtil.getResponse();

            int size = ProductResult.length();
            Log.d(TAG, "SIZE :" + size);

            if(ProductResult.getJSONObject(0).getString("productName").equals("null")) {
                CheckIsList("결과 항목이 없습니다.");
            } else {
                for (int i = 0; i < size; i++) {
                    String productID = ProductResult.getJSONObject(i).getString("productID");
                    String productName = ProductResult.getJSONObject(i).getString("productName");
                    String event = ProductResult.getJSONObject(i).getString("event");
                    String salesPrice = ProductResult.getJSONObject(i).getString("salesPrice");
                    String productQuantity = ProductResult.getJSONObject(i).getString("productQuantity");

                    String tmpProductRes = "a"+productID;
                    int tmpProductImage = getResources().getIdentifier(tmpProductRes, "drawable", mPackageName);
                    Log.d(TAG, "상품이미지이름: " + tmpProductRes);

//                    :rasp: 비트맵
//                    Bitmap bitmap = decodeSampledBitmapFromResource(getResources(),tmpProductImage, 100, 100);


                    ProductDOList.add(new ProductDO(productID, productName, salesPrice, productQuantity, event, tmpProductImage));
                }
            }
        } catch (Exception e) {
            e.toString();
        }
        return ProductDOList;
    }
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
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


    public Dialog onCreateDialog(int id){
        switch (id) {
            case(PROGRESS_DIALOG):
                progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("상품정보 확인 중..");
                progressDialog.setCanceledOnTouchOutside(false);
        }
        return progressDialog;
    }


    public void CheckIsList(String mMsg) {

        AlertDialog.Builder mCheckComplete = new AlertDialog.Builder(ProductActivity.this);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        int mId = item.getItemId();

        if (mId == android.R.id.home) {

            switch (mGetIntent_ParentActivity) {
                case "FavoriteActivity":
                    Intent mIntent_BackFavorite = new Intent(getApplicationContext(), FavoriteActivity.class);
                    mIntent_BackFavorite.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mIntent_BackFavorite);
                    finish();
                    break;
                case "StoreSearchActivity3":
                    Intent mIntent_BackStoreSearch = new Intent(getApplicationContext(), StoreSearchActivity3.class);
                    mIntent_BackStoreSearch.putExtra("GuName", mGetIntent_GuName);
                    mIntent_BackStoreSearch.putExtra("DongName", mGetIntent_DongName);
                    mIntent_BackStoreSearch.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mIntent_BackStoreSearch);
                    finish();
                    break;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
