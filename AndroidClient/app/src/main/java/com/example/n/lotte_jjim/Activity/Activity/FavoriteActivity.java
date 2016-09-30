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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n.lotte_jjim.Activity.Adapter.FavoriteAdapter;
import com.example.n.lotte_jjim.Activity.DataObject.StoreDO;
import com.example.n.lotte_jjim.Activity.Util.HttpUtil;
import com.example.n.lotte_jjim.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FavoriteActivity extends AppCompatActivity {

    private static final String TAG = "FavoriteActivity";
    private String mCurrentUserID;

    ArrayList<StoreDO> mFavoriteDO = new ArrayList<StoreDO>();
    ListView mList_Favorite;
    FavoriteAdapter mAdapter_Favorite;

    JSONArray mFavoriteArray;
    JSONArray mFavoriteDeleteArray;



    public void Init_UI() {

        this.setTitle("즐겨찾는 매장");
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


    public void Set_AdapterToList() {

        mAdapter_Favorite = new FavoriteAdapter(getApplicationContext(), R.layout.list_favorite, mFavoriteDO);
        mList_Favorite = (ListView)findViewById(R.id.mList_Favorite);

        mList_Favorite.setAdapter(mAdapter_Favorite);
        mList_Favorite.setOnItemClickListener(ListViewItemClickListener);

        mAdapter_Favorite.setOnClickListener(new FavoriteAdapter.onButtonClickListener() {
            @Override
            public void onBtnDeleteFavorite(StoreDO mPosition) {
                Delete_Favorite(mPosition);
            }

            @Override
            public void onBtnShowFavoriteMap(StoreDO mPosition) {
                Intent mSelectStoreMap = new Intent(getApplicationContext(), StoreMapsActivity.class);
                mSelectStoreMap.putExtra("latitude", mPosition.GetLatitude());
                mSelectStoreMap.putExtra("longitude", mPosition.GetLongitude());
                mSelectStoreMap.putExtra("storeName", mPosition.GetStoreName());
                startActivity(mSelectStoreMap);
            }
        });
    }

    private OnItemClickListener ListViewItemClickListener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> adapterView, View clickedView, int position, long id){

            String mText_Favorite = mFavoriteDO.get(position).GetStoreName();
            String mText_FavoriteAddress = mFavoriteDO.get(position).GetStoreAddress();

            Intent mIntent_Favorite = new Intent(getApplicationContext(), ProductActivity.class);
            mIntent_Favorite.putExtra("StoreName", mText_Favorite);
            mIntent_Favorite.putExtra("address", mText_FavoriteAddress);
            mIntent_Favorite.putExtra("GuName", "");
            mIntent_Favorite.putExtra("DongName", "");
            mIntent_Favorite.putExtra("ParentActivity", TAG);
            startActivity(mIntent_Favorite);
        }
    };


    public void  mFavoriteSearchJSON(){

        mFavoriteArray = new JSONArray();
        toJSON(mFavoriteArray, "userID", mCurrentUserID);

        try {
            HttpUtil httpUtil = new HttpUtil("Favorite","FavoriteRequest",  mFavoriteArray);
            httpUtil.start();
            httpUtil.join();
            Log.d(TAG, "데이터 전송 완료");

            JSONArray mFavoriteSearchResult = httpUtil.getResponse();

            int size = mFavoriteSearchResult.length();
            Log.d(TAG, "SIZE :" + size);

            if(mFavoriteSearchResult.getJSONObject(0).getString("storeName").equals("null")) {
                CheckIsList("결과 항목이 없습니다.");
            } else {
                for (int i = 0; i < size; i++) {
                    String favoriteStoreName = mFavoriteSearchResult.getJSONObject(i).getString("storeName");
                    String favoriteAddress = mFavoriteSearchResult.getJSONObject(i).getString("storeAddress");
                    String favoriteLatitude =mFavoriteSearchResult.getJSONObject(i).getString("latitude");
                    String favoriteLongitude =mFavoriteSearchResult.getJSONObject(i).getString("longitude");
                    mFavoriteDO.add(new StoreDO(favoriteStoreName, favoriteAddress, favoriteLatitude, favoriteLongitude));
                    Log.d(TAG, "NAME :" + favoriteStoreName);
                }
            }
        }catch (Exception e) {
            e.toString();
        }
        mList_Favorite.setAdapter(mAdapter_Favorite);
    }


    public void Delete_Favorite(final StoreDO mPosition) {

        final StoreDO m_Position = mPosition;

        AlertDialog.Builder mAlert_DeleteFavorite = new AlertDialog.Builder(FavoriteActivity.this);
        mAlert_DeleteFavorite
                .setTitle("즐겨찾는 매장 삭제")
                .setMessage("\"" + mPosition.GetStoreName() + "\"을 삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String FavoriteDeleteCheck =null;
                        mFavoriteDeleteArray = new JSONArray();

                        toJSON(mFavoriteDeleteArray, "userID", mCurrentUserID);
                        toJSON(mFavoriteDeleteArray, "address", m_Position.GetStoreAddress());
                        Log.d(TAG, m_Position.GetStoreAddress());

                        try {
                            HttpUtil httpUtil = new HttpUtil("Favorite","FavoriteDeleteRequest",  mFavoriteDeleteArray);
                            httpUtil.start();
                            httpUtil.join();

                            JSONArray mFavoriteDeleteResult = httpUtil.getResponse();

                            int size = mFavoriteDeleteResult.length();

                            for (int i = 0; i < size; i++) {
                                FavoriteDeleteCheck = mFavoriteDeleteResult.getJSONObject(i).getString("check");
                                Log.i(TAG, "결과 :" + FavoriteDeleteCheck);
                            }

                            if(FavoriteDeleteCheck.equals("true")){
                                mFavoriteDO.remove(m_Position);
                                mAdapter_Favorite.notifyDataSetChanged();
                            }else if(FavoriteDeleteCheck.equals("false")){
                                Toast.makeText(getApplicationContext(),"삭제 실패",Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception e) {
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

        AlertDialog mAlertDialog = mAlert_DeleteFavorite.create();
        mAlertDialog.show();
        mAdapter_Favorite.notifyDataSetChanged();
    }


    public void CheckIsList(String mMsg) {

        AlertDialog.Builder mCheckComplete = new AlertDialog.Builder(FavoriteActivity.this);
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
        setContentView(R.layout.activity_favorite);
        Init_ID();

        Init_UI();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Set_AdapterToList();
        mFavoriteSearchJSON();
    }

    public void Init_ID(){
        SharedPreferences setting = getSharedPreferences("MYID", MODE_PRIVATE);
        mCurrentUserID = setting.getString("ID","");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){
            Intent mIntent_Home = this.getParentActivityIntent();
            mIntent_Home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mIntent_Home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent_Home);
            return true;
        }
        if (id == R.id.mBtn_AddFavorite) {
            Intent mIntent_Seoul = new Intent(getApplicationContext(), FavoriteSearchActivity1.class);
            startActivity(mIntent_Seoul);
            return true;
        }
        return super.onOptionsItemSelected(item);
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