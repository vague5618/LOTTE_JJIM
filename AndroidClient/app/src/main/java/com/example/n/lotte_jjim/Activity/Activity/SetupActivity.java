package com.example.n.lotte_jjim.Activity.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.n.lotte_jjim.R;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SetupActivity";
    String mCurrentUserID = null;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Init_ID();


        Init_UI();
        Init_Button();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void Init_ID(){
        setting = getSharedPreferences("MYID", MODE_PRIVATE);
        mCurrentUserID = setting.getString("ID", "");
        editor= setting.edit();
    }


    public void Init_UI() {
        setTitle("설정");
        ChangeTitleTextSize(18);
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

        Button mBtn_CheckJjim = (Button) findViewById(R.id.mBtn_CheckJjim);
        Button mBtn_CheckReservation = (Button) findViewById(R.id.mBtn_CheckReservation);
        Button mBtn_SetPush = (Button) findViewById(R.id.mBtn_SetPush);
        Button mBtn_Logout = (Button) findViewById(R.id.mBtn_Logout);

        mBtn_CheckJjim.setOnClickListener(this);
        mBtn_CheckReservation.setOnClickListener(this);
        mBtn_SetPush.setOnClickListener(this);
        mBtn_Logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mBtn_CheckJjim:
                Intent mIntent_CheckJiim = new Intent(getApplicationContext(), JJimListActivity.class);
                startActivity(mIntent_CheckJiim);
                break;
            case R.id.mBtn_CheckReservation:
                Intent mIntent_CheckReservation = new Intent(getApplicationContext(),OrderListActivity.class);
                startActivity(mIntent_CheckReservation);
                break;
            case R.id.mBtn_SetPush:
                Intent mIntent_SetPush = new Intent(getApplicationContext(), PushActivity.class);
                startActivity(mIntent_SetPush);
                break;
            case R.id.mBtn_Logout:
                AlertDialog.Builder mAlert_Logout= new AlertDialog.Builder(SetupActivity.this);

                mAlert_Logout.setTitle("로그아웃")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor.clear();
                                editor.commit();
                                finish();
                                Intent mIntent_Logout = new Intent(getApplicationContext(), LoginActivity.class);
                                mIntent_Logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mIntent_Logout);

                                Log.d(TAG, setting.getBoolean("AutoLogin",false)+"");


                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });

                AlertDialog mAlertDialog = mAlert_Logout.create();
                mAlertDialog.show();
                break;
        }
    }
}
