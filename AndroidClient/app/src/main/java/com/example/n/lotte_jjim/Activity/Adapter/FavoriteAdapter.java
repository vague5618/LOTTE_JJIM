package com.example.n.lotte_jjim.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.n.lotte_jjim.Activity.DataObject.StoreDO;
import com.example.n.lotte_jjim.R;

import java.util.ArrayList;


public class FavoriteAdapter extends BaseAdapter {

    private static final String TAG = "FavoriteAdapter";

    Context mContext;
    int mLayout;
    ArrayList<StoreDO> mFavoriteDO;
    LayoutInflater mInflater;


    //rasp: 버튼 클릭 리스너
    public interface onButtonClickListener {
        void onBtnDeleteFavorite (StoreDO item);
        void onBtnShowFavoriteMap (StoreDO itme);
    }

    private onButtonClickListener adptCallback = null;

    public void setOnClickListener(onButtonClickListener callback) {
        adptCallback = callback;
    }
    //rasp:


    public FavoriteAdapter(Context mContext, int mLayout, ArrayList<StoreDO> mFavoriteDO) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mFavoriteDO = mFavoriteDO;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mFavoriteDO.size();
    }

    @Override
    public Object getItem(int position) {
        return mFavoriteDO.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            //: null이라면 재활용할 View가 없으므로 새로운 객체 생성
            //: view의 모양은 xml 파일로 객체를 생성, 레이아웃 파일을 객체로 부풀리는 inflater 활용
            convertView = mInflater.inflate(R.layout.list_favorite, null);
        }

        //: 리스트 뷰 번호 받아오기
        final StoreDO mStoreData = mFavoriteDO.get(position);
        if (mStoreData != null) {
            ImageButton mBtn_ShowFavoriteMap = (ImageButton)convertView.findViewById(R.id.mBtn_ShowStoreMap);
            ImageButton mBtn_DeleteFavorite = (ImageButton)convertView.findViewById(R.id.mBtn_Delete);

            mBtn_DeleteFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v ) {
                    if (adptCallback != null) {
                        adptCallback.onBtnDeleteFavorite(mStoreData);
                    }
                }
            });
            mBtn_ShowFavoriteMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v ) {
                    if (adptCallback != null) {
                        adptCallback.onBtnShowFavoriteMap(mStoreData);
                    }
                }
            });

            mBtn_DeleteFavorite.setFocusable(false);
            mBtn_ShowFavoriteMap.setFocusable(false);
        }

        TextView mStoreName = (TextView)convertView.findViewById(R.id.mText_StoreName);
        TextView mStoreAddress = (TextView)convertView.findViewById(R.id.mText_Address);

        mStoreName.setText(mFavoriteDO.get(position).GetStoreName());
        mStoreAddress.setText(mFavoriteDO.get(position).GetStoreAddress());

        return convertView;
    }
}

