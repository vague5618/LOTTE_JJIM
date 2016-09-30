package com.example.n.lotte_jjim.Activity.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.n.lotte_jjim.R;


public class FavoriteSearchActivity3 extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FavoriteSearchActivity3";

    String[] mGu_Current;
    Button[] mBtn_Gu;
    String mGet_GuName;



    public void Init_UI() {

        setTitle("즐겨찾는 매장 추가");
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

        mBtn_Gu = new Button[16];
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
            }

            if(i < mGu_Current.length) {
                mBtn_Gu[i].setText(mGu_Current[i]);
            } else {
                mBtn_Gu[i].setVisibility(View.INVISIBLE);
            }

            mBtn_Gu[i].setOnClickListener(this);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_search3);
        Init_LocationArray();

        Init_UI();
        Init_Button();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onClick(View v) {

        Intent mIntent_Gu = new Intent(getApplicationContext(), FavoriteSearchActivity4.class);
        mIntent_Gu.putExtra("GuName", mGet_GuName);

        int mGu_Id = v.getId();
        for (int i = 0; i < mGu_Current.length; i++) {
            if(i == mGu_Id) {
                mIntent_Gu.putExtra("DongName", mGu_Current[i]);
                Log.d(TAG, "선택한 동 확인: " + mGu_Current[i]);
                break;
            }
        }
        startActivity(mIntent_Gu);
    }


    public void Init_LocationArray() {

        Intent mIntent_Receive = getIntent();
        mGet_GuName = mIntent_Receive.getExtras().getString("GuName");
        Log.d(TAG, mGet_GuName);

        switch (mGet_GuName) {
            case "강남구":
                mGu_Current = new String[]{
                        "개포동", "논현동", "대치동", "도곡동",
                        "삼성동", "세곡동", "수서동", "신사동",
                        "압구정동", "역삼동", "일원동", "청담동"};
                break;
            case "강동구":
                mGu_Current = new String[]{
                        "강일동", "고덕동", "길동", "둔촌동",
                        "명일동", "상일동", "성내동", "암사동",
                        "천호동"};
                break;
            case "강북구":
                mGu_Current = new String[]{
                        "미아동", "번동", "삼양동"};
                break;
            case "강서구":
                mGu_Current = new String[]{
                        "가양동", "공항동", "등촌동", "방화동",
                        "염창동", "화곡동"};
                break;
            case "관악구":
                mGu_Current = new String[]{
                        "남현동", "봉천동", "신림동"};
                break;
            case "광진구":
                mGu_Current = new String[]{
                        "광장동", "구의동", "군자동", "능동",
                        "자양동", "중곡동", "화양동"};
                break;
            case "구로구":
                mGu_Current = new String[]{
                        "가리봉동", "개봉동", "고척동", "구로동",
                        "궁동", "신도림동", "오류동"};
                break;
            case "금천구":
                mGu_Current = new String[]{
                        "가산동", "독산동", "시흥동"};
                break;
            case "노원구":
                mGu_Current = new String[]{
                        "공릉동", "상계동", "월계동", "중계동",
                        "하계동"};
                break;
            case "도봉구":
                mGu_Current = new String[]{
                        "도봉동", "방학동", "쌍문동"};
                break;
            case "동대문구":
                mGu_Current = new String[]{
                        "노량진동", "대방동", "사당동", "상도동",
                        "신대방동", "흑석동"};
                break;
            case "동작구":
                mGu_Current = new String[]{
                        "답십리동", "신설동", "용두동", "이문동",
                        "장안동", "전농동", "제기동", "회기동",
                        "휘경동"};
                break;
            case "마포구":
                mGu_Current = new String[]{
                        "공덕동", "대흥동", "도화동", "망원동",
                        "상암동", "서교동", "성산동", "신수동",
                        "아현동", "염리동", "용강동", "합정동"};
                break;
            case "서대문구":
                mGu_Current = new String[]{
                        "남가좌동", "북가좌동", "북아현동", "연희동",
                        "충정로", "홍은동", "홍제동"};
                break;
            case "서초구":
                mGu_Current = new String[]{
                        "반포동", "방배동", "서초동", "양재동",
                        "잠원동"};
                break;
            case "성동구":
                mGu_Current = new String[]{
                        "금호동", "마장동", "성수동", "송정동",
                        "옥수동", "용답동", "행당동"};
                break;
            case "성북구":
                mGu_Current = new String[]{
                        "길음동", "돈암동", "보문동", "삼선동",
                        "석관동", "성북동", "안암동", "월곡동",
                        "정릉동", "종암동"};
                break;
            case "송파구":
                mGu_Current = new String[]{
                        "가락동", "거여동", "마천동", "문정동",
                        "방이동", "삼전동", "석촌동", "송파동",
                        "오금동", "잠실동", "잠지동", "풍납동"};
                break;
            case "양천구":
                mGu_Current = new String[]{
                        "목동", "신월동", "신정동"};
                break;
            case "영등포구":
                mGu_Current = new String[]{
                        "당산동", "대림동", "도림동", "문래동",
                        "신길동", "양평동", "여의도동", "영등포동"};
                break;
            case "용산구":
                mGu_Current = new String[]{
                        "갈월동", "보광동", "서빙고동", "용문동",
                        "용산동", "원효로동", "이촌동", "이태원동",
                        "청파동", "한강로", "한남동", "효창동",
                        "후암동"};
                break;
            case "은평구":
                mGu_Current = new String[]{
                        "갈현동", "녹번동", "대조동", "불광동",
                        "수색동", "신사동", "역촌동", "응암동",
                        "증산동"};
                break;
            case "종로구":
                mGu_Current = new String[]{
                        "숭인동", "이화동", "종로", "창신동",
                        "평창동", "혜화동"};
                break;
            case "중구":
                mGu_Current = new String[]{
                        "광희동", "동화동", "명동", "소공동",
                        "신당동", "을지로동", "장충동", "중림동",
                        "필동", "회현동"};
                break;
            case "중랑구":
                mGu_Current = new String[]{
                        "망우동", "면목동", "상봉동", "신내동",
                        "중화동"};
                break;
        }
    }
}
