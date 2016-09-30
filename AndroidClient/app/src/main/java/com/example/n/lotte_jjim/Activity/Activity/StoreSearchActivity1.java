package com.example.n.lotte_jjim.Activity.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.n.lotte_jjim.R;


public class StoreSearchActivity1 extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "StoreSearchActivity1";

    String[] mSi_Seoul;
    Button[] mBtn_Gu;



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

        LinearLayout mLinearLayout1 = (LinearLayout) findViewById(R.id.mLinearLayout1);
        LinearLayout mLinearLayout2 = (LinearLayout) findViewById(R.id.mLinearLayout2);
        LinearLayout mLinearLayout3 = (LinearLayout) findViewById(R.id.mLinearLayout3);
        LinearLayout mLinearLayout4 = (LinearLayout) findViewById(R.id.mLinearLayout4);
        LinearLayout mLinearLayout5 = (LinearLayout) findViewById(R.id.mLinearLayout5);
        LinearLayout mLinearLayout6 = (LinearLayout) findViewById(R.id.mLinearLayout6);
        LinearLayout mLinearLayout7 = (LinearLayout) findViewById(R.id.mLinearLayout7);


        mBtn_Gu = new Button[28];
        for (int i = 0; i < mBtn_Gu.length; i++) {

            mBtn_Gu[i] = new Button(this);
            mBtn_Gu[i].setId(i);

            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1);

            int mSize_Margin = Math.round(2 * getResources().getDisplayMetrics().density);
            mLayoutParams.setMargins(mSize_Margin, mSize_Margin, mSize_Margin, mSize_Margin);

            if (i < 4) {
                mLinearLayout1.addView(mBtn_Gu[i], mLayoutParams);
            } else if (i < 8) {
                mLinearLayout2.addView(mBtn_Gu[i], mLayoutParams);
            } else if (i < 12) {
                mLinearLayout3.addView(mBtn_Gu[i], mLayoutParams);
            } else if (i < 16 ) {
                mLinearLayout4.addView(mBtn_Gu[i], mLayoutParams);
            } else if (i < 20) {
                mLinearLayout5.addView(mBtn_Gu[i], mLayoutParams);
            } else if (i < 24) {
                mLinearLayout6.addView(mBtn_Gu[i], mLayoutParams);
            } else if (i < 28) {
                mLinearLayout7.addView(mBtn_Gu[i], mLayoutParams);
            }

            if(i < mSi_Seoul.length) {
                mBtn_Gu[i].setText(mSi_Seoul[i]);
            } else {
                mBtn_Gu[i].setVisibility(View.INVISIBLE);
            }

            mBtn_Gu[i].setOnClickListener(this);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_search1);
        Init_LocationArray();

        Init_UI();
        Init_Button();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onClick(View v) {


        Intent mIntent_Gu = new Intent(getApplicationContext(), StoreSearchActivity2.class);

        int mGu_Id = v.getId();
        for (int i = 0; i < mSi_Seoul.length; i++) {
            if(i == mGu_Id) {
                mIntent_Gu.putExtra("GuName", mSi_Seoul[i]);
                Log.d(TAG, "선택한 구 확인: " + mSi_Seoul[i]);
                break;
            }
        }
        startActivity(mIntent_Gu);
    }


    public void Init_LocationArray() {

        mSi_Seoul =  new String[] {
                "강남구", "강동구", "강북구", "강서구",
                "관악구", "광진구", "구로구", "금천구",
                "노원구", "도봉구", "동대문구", "동작구",
                "마포구", "서대문구", "서초구", "성동구",
                "성북구", "송파구", "양천구", "영등포구",
                "용산구", "은평구", "종로구", "중구",
                "중랑구"
        };
    }
}
