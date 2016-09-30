package com.example.n.lotte_jjim.Activity.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.n.lotte_jjim.Activity.Adapter.JJimAdapter;
import com.example.n.lotte_jjim.Activity.DataObject.JJimDO;
import com.example.n.lotte_jjim.Activity.Util.HttpUtil;
import com.example.n.lotte_jjim.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JJimListActivity extends AppCompatActivity {


    private static final String TAG = "JJimListActivity";
    public static final String mPackageName = "com.example.n.lotte_jjim";
    private String mCurrentUserID;

    ArrayList<JJimDO> mJJimDO = new ArrayList<JJimDO>();

    ListView mList_JJim;
    JJimAdapter mAdapter_JJim;

    JSONArray mJJimArray;
    JSONArray mJJimDeleteArray;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jjim_list);

        Init_UI();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Init_ID();


        Set_AdapterToList();
        mJJimJSON();
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

         mAdapter_JJim = new JJimAdapter(getApplicationContext(), R.layout.list_jjim, mJJimDO);
         mList_JJim = (ListView)findViewById(R.id.mList_JJim);

        mList_JJim.setAdapter(mAdapter_JJim);
        mAdapter_JJim.setOnClickListener(new JJimAdapter.onButtonClickListener() {

            @Override
            public void onBtnDeleteJJim(JJimDO item) {
                Alert_DeleteJJim(item);
            }
        });
    }


    public void Alert_DeleteJJim(JJimDO mPosition) {

        final JJimDO m_Position = mPosition;
        AlertDialog.Builder mAlert_DeleteJJim = new AlertDialog.Builder(JJimListActivity.this);

        mAlert_DeleteJJim.setTitle("찜 알림 삭제하기")
                .setMessage("\"" + mPosition.GetJJimStoreName() + "\"점의 \"" + mPosition.GetJJimProductName() + "\"상품 입고 시 받을 푸시 알림을 취소하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mJJimDeleteArray = new JSONArray();
                        String JJimDeleteCheck=null;

                        toJSON(mJJimDeleteArray, "userID", mCurrentUserID);
                        toJSON(mJJimDeleteArray, "productID", m_Position.GetmJJimProductId());
                        toJSON(mJJimDeleteArray, "storeName", m_Position.GetJJimStoreName());

                        Log.d(TAG, m_Position.GetJJimStoreName() + m_Position.GetJJimProductName() + " 삭제 요청");

                        try {
                            HttpUtil httpUtil = new HttpUtil("Wait", "JJimDeleteRequest", mJJimDeleteArray);
                            httpUtil.start();
                            httpUtil.join();

                            JSONArray mJJimDeleteResult = httpUtil.getResponse();

                            int size = mJJimDeleteResult.length();

                            for (int i = 0; i < size; i++) {
                                JJimDeleteCheck = mJJimDeleteResult.getJSONObject(i).getString("check");
                                Log.i(TAG, "결과 :" + JJimDeleteCheck);
                            }

                            if(JJimDeleteCheck.equals("true")){
                                mAdapter_JJim.notifyDataSetChanged();
                                mJJimDO.remove(m_Position);
                                String mAlertMSG = "찜 리스트에서 삭제되었습니다.";
                                CheckState(mAlertMSG);

                                mJJimDO.remove(m_Position);
                                mAdapter_JJim.notifyDataSetChanged();


                                if (m_Position.GetJJimProductName().equals(null)) {

                                } else {
                                    Log.d(TAG, m_Position.GetJJimProductName());
                                }


                            } else if (JJimDeleteCheck.equals("false")) {
                                Toast.makeText(getApplicationContext(), "찜 실패", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.toString();
                        }
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        AlertDialog mAlertDialog = mAlert_DeleteJJim.create();
        mAlertDialog.show();
        mAdapter_JJim.notifyDataSetChanged();
    }

    public void CheckState(String mMsg) {

        AlertDialog.Builder mCheckComplete = new AlertDialog.Builder(JJimListActivity.this);
        mCheckComplete.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mCheckComplete.setMessage(mMsg);
        mCheckComplete.show();
    }


    public void Init_ID(){
        SharedPreferences setting = getSharedPreferences("MYID", MODE_PRIVATE);
        mCurrentUserID = setting.getString("ID","");
    }


    public void Init_UI() {
        setTitle("찜 목록");
        //getSupportActionBar().hide();
        ChangeTitleTextSize(16);
    }

    public void  mJJimJSON(){

        mJJimArray = new JSONArray();

        toJSON(mJJimArray, "userID", mCurrentUserID);
        Log.d(TAG, mCurrentUserID + "의 찜 조회중");

        try {
            HttpUtil httpUtil = new HttpUtil("Wait","JJimRequest",  mJJimArray);
            httpUtil.start();
            httpUtil.join();

            JSONArray mJJimResult = httpUtil.getResponse();

            int size = mJJimResult.length();
            Log.d(TAG, "SIZE :" + size);

            if(mJJimResult.getJSONObject(0).getString("productName").equals("null")) {
                CheckIsList("결과 항목이 없습니다.");
            } else {
                for (int i = 0; i < size; i++) {
                    String productID = mJJimResult.getJSONObject(i).getString("productID");
                    String productName = mJJimResult.getJSONObject(i).getString("productName");
                    String storeName = mJJimResult.getJSONObject(i).getString("storeName");

                    int tmpProductImage = getResources().getIdentifier("a" + productID, "drawable", mPackageName);
                    mJJimDO.add(new JJimDO(productID, storeName, productName, tmpProductImage));
                    Log.d(TAG, "NAME :" + mJJimDO.get(i).GetJJimProductName() + " " + mJJimDO.get(i).GetJJimStoreName());
                }
            }
        }catch (Exception e) {
            e.toString();
        }
        mList_JJim.setAdapter(mAdapter_JJim);
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


    public void CheckIsList(String mMsg) {

        AlertDialog.Builder mCheckComplete = new AlertDialog.Builder(JJimListActivity.this);
        mCheckComplete.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mCheckComplete.setMessage(mMsg);
        mCheckComplete.show();
    }
}
