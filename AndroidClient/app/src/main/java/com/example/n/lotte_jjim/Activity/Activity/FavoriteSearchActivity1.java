package com.example.n.lotte_jjim.Activity.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.n.lotte_jjim.R;


public class FavoriteSearchActivity1 extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "FavoriteSearchActivity1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_search1);

        Init_UI();
        Init_Button();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mBtn_Seoul:
                Intent mIntent_Seoul = new Intent(getApplicationContext(), FavoriteSearchActivity2.class);
                startActivity(mIntent_Seoul);
                break;
        }
    }


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

        Button mBtn_Seoul = (Button) findViewById(R.id.mBtn_Seoul);

        mBtn_Seoul.setOnClickListener(this);
    }
}
