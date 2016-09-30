package com.example.n.lotte_jjim.Activity.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.n.lotte_jjim.R;

public class OrderFinishActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "OrderFinishActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_finish);

        Init_UI();
        Init_Button();
    }

    public void Init_UI() {

        setTitle("예약 완료");
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
        Button mBtn_Check = (Button) findViewById(R.id.mBtn_Check);
        Button mBtn_CheckList = (Button) findViewById(R.id.mBtn_CheckList);

        mBtn_Check.setOnClickListener(this);
        mBtn_CheckList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mBtn_Check:
                Intent mIntent_OrderSuccess = new Intent(getApplicationContext(), MainActivity.class);
                mIntent_OrderSuccess.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mIntent_OrderSuccess.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent_OrderSuccess);
                finish();
                break;
            case R.id.mBtn_CheckList:
                Intent mIntent_CheckList = new Intent(getApplicationContext(), OrderListActivity.class);
                startActivity(mIntent_CheckList);
                break;
        }
    }


}
