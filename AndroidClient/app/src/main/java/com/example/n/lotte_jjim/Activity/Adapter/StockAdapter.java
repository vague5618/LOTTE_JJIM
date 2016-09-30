package com.example.n.lotte_jjim.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.n.lotte_jjim.Activity.DataObject.StockDO;
import com.example.n.lotte_jjim.R;

import java.util.ArrayList;

public class StockAdapter extends BaseAdapter {

    private static final String TAG = "StockAdapter";

    Context mContext;
    int mLayout;
    ArrayList<StockDO> mStockDO;
    LayoutInflater mInflater;


    //rasp: 버튼 클릭 리스너
    public interface onButtonClickListener {
        void onBtnJjim (StockDO item);
        void onBtnShowFavoriteMap (StockDO item);
    }

    private onButtonClickListener adptCallback = null;

    public void setOnClickListener(onButtonClickListener callback) {
        adptCallback = callback;
    }

    public StockAdapter(Context mContext, int mLayout, ArrayList<StockDO> mStockDO) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mStockDO = mStockDO;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mStockDO.size();
    }

    @Override
    public Object getItem(int position) {
        return mStockDO.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.list_dong_stock, null);
        }

        //: 리스트 뷰 번호 받아오기
        final StockDO mStockData = mStockDO.get(position);
        if (mStockData != null) {
            ImageButton mBtn_ShowFavoriteMap = (ImageButton)convertView.findViewById(R.id.mBtn_ShowStoreMap);
            ImageButton  mBtn_Jjim = (ImageButton)convertView.findViewById(R.id.mBtn_Jjim);

            mBtn_Jjim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v ) {
                    if (adptCallback != null) {
                        adptCallback.onBtnJjim(mStockData);
                    }
                }
            });
            mBtn_ShowFavoriteMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v ) {
                    if (adptCallback != null) {
                        adptCallback.onBtnShowFavoriteMap(mStockData);
                    }
                }
            });
            mBtn_Jjim.setFocusable(false);
            mBtn_ShowFavoriteMap.setFocusable(false);
        }


        TextView mStoreName = (TextView)convertView.findViewById(R.id.mText_StoreName);
        TextView mStoreAddress = (TextView)convertView.findViewById(R.id.mText_Address);
        TextView mStoreEvent = (TextView)convertView.findViewById(R.id.mText_Event);
        TextView mStoreStock = (TextView)convertView.findViewById(R.id.mText_Stock);

        mStoreName.setText(mStockDO.get(position).GetStoreName());
        mStoreAddress.setText(mStockDO.get(position).GetStoreAddress());
        mStoreEvent.setText(mStockDO.get(position).GetStoreEvent());
        mStoreStock.setText(mStockDO.get(position).GetStoreStock());

        return convertView;
    }
}