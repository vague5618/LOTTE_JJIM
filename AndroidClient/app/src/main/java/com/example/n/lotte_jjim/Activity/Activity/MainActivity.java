package com.example.n.lotte_jjim.Activity.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.n.lotte_jjim.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    public static String mCurrentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init_ID();

        Init_UI();
        Init_Button();
    }
    public void Init_ID(){
        SharedPreferences setting = getSharedPreferences("MYID", MODE_PRIVATE);
        mCurrentUserID = setting.getString("ID","");
    }


    public void Init_UI() {

        getSupportActionBar().hide();
    }

    public void ChangeTitleTextSize(int toSize) {

        ActionBar ab = getSupportActionBar();
        TextView tv = new TextView(getApplicationContext());

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView

        tv.setLayoutParams(lp);
        tv.setText(ab.getTitle());
        tv.setTextColor(getResources().getColor(R.color.colorOrange));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, toSize);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(tv);
    }


    public void Init_Button() {

        ImageButton mBtn_AdURL = (ImageButton) findViewById(R.id.mBtn_AdURL);
        ImageButton mBtn_Setting = (ImageButton) findViewById(R.id.mBtn_Setting);
        ImageButton mBtn_Favorite = (ImageButton) findViewById(R.id.mBtn_Favorite);
        ImageButton mBtn_NearStore = (ImageButton) findViewById(R.id.mBtn_NearSearch);
        Button mBtn_Seoul = (Button) findViewById(R.id.mBtn_Seoul);

        mBtn_AdURL.setOnClickListener(this);
        mBtn_Setting.setOnClickListener(this);
        mBtn_Favorite.setOnClickListener(this);
        mBtn_NearStore.setOnClickListener(this);
        mBtn_Seoul.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mBtn_AdURL:
                String mAdURL = "http://m.7-eleven.co.kr/product/eventList.asp";
                Intent mIntent_AdURL = new Intent(Intent.ACTION_VIEW, Uri.parse(mAdURL));
                startActivity(mIntent_AdURL);
                break;
            case R.id.mBtn_Setting:
                Intent mIntent_Setting = new Intent(getApplicationContext(), SetupActivity.class);
                startActivity(mIntent_Setting);
                break;
            case R.id.mBtn_Favorite:
                Intent mIntent_Favorite = new Intent(getApplicationContext(), FavoriteActivity.class);
                startActivity(mIntent_Favorite);
                break;
            case R.id.mBtn_NearSearch:
                Intent mIntent_Near = new Intent(getApplicationContext(), NearStoreActivity.class);
                startActivity(mIntent_Near);
                break;
            case R.id.mBtn_Seoul:
                Intent mIntent_Seoul = new Intent(getApplicationContext(), StoreSearchActivity1.class);
                startActivity(mIntent_Seoul);
                break;
        }
    }
}
