package com.example.n.lotte_jjim.Activity.Activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.n.lotte_jjim.Activity.Util.HttpUtil;
import com.example.n.lotte_jjim.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class PushActivity extends AppCompatActivity {

    private static final String TAG = "PushActivity";

    public TextView switchStatus;
    public Switch mSwitch_Push;
    public static String mCurrentUserID;

    JSONArray mPushArray;
    JSONArray mPushInitArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        Init_ID();

        Init_UI();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSwitch_Push = (Switch) findViewById(R.id.mSwitch_Push);


        mPush_Init_JSON();
        mSwitch_Push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    mPushJSON("true");

                }else{
                    mPushJSON("false");
                }
            }
        });
    }

    public void Init_ID(){
        SharedPreferences setting = getSharedPreferences("MYID", MODE_PRIVATE);
        mCurrentUserID = setting.getString("ID","");
    }

    public void Init_UI() {

        setTitle("푸시 알림 설정");
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


    public void  mPushJSON(String pushCheck){
        mPushArray = new JSONArray();
        toJSON(mPushArray, "userID", mCurrentUserID);
        toJSON(mPushArray, "check", pushCheck);
        String check1=null;



        try {
//            Log.d(TAG, "전송할 데이터 " + mPushArray.getJSONObject(0).getString("check"));

            HttpUtil httpUtil = new HttpUtil("Member","PushOptionRequest",  mPushArray);
            String ccc = mPushArray.getJSONObject(1).getString("check");
            Log.d(TAG, ccc + mCurrentUserID);

            httpUtil.start();

            httpUtil.join();

            Log.d(TAG, "데이터 전송 완료");

            JSONArray mPushResult = httpUtil.getResponse();

            int size = mPushResult.length();

            Log.d(TAG, "SIZE :" + size);


            // (2/19) rasp:  리스트 정보가 없으면 없다고 알림창

            for (int i = 0; i < size; i++) {
                check1= mPushResult.getJSONObject(i).getString("check");
//                    int productImg = mOrderDetailResult.getJSONObject(i).getInt("productImg");

            }
        }
        catch (Exception e) {
            e.toString();
        }
        Log.d(TAG, "결과 :" + check1);}






    public void  mPush_Init_JSON(){
        mPushInitArray = new JSONArray();
        toJSON(mPushInitArray, "userID", mCurrentUserID);
        String check=null;

        try {
            HttpUtil httpUtil = new HttpUtil("Member","PushRequest",  mPushInitArray);
            httpUtil.start();
            httpUtil.join();
            Log.d(TAG, "데이터 전송 완료");

            JSONArray mPushResult = httpUtil.getResponse();
            int size = mPushResult.length();
            Log.d(TAG, "SIZE :" + size);

            for (int i = 0; i < size; i++) {
                check= mPushResult.getJSONObject(i).getString("check");
            }
        }
        catch (Exception e) {
            e.toString();
        }
        Log.d(TAG, "결과 :" + check);

        if(check.equals("true")){
            mSwitch_Push.setChecked(true);
        } else if(check.equals("false")) {
            mSwitch_Push.setChecked(false);
        }
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